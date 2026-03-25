package com.isidora.ms5.Services;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import com.isidora.ms5.Models.FrequencyDscEntity;
import com.isidora.ms5.Models.GroupSizeDscEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;



import java.util.ArrayList;
import java.util.List;
import com.isidora.ms5.Entities.CustomerEntity;
import com.isidora.ms5.Entities.ReservationEntity;
import com.isidora.ms5.Models.TariffEntity;
import com.isidora.ms5.Repositories.CustomerRepository;
import com.isidora.ms5.Repositories.ReservationRepository;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RestTemplate restTemplate;


    //URL ms1 tarifas
    public List<TariffEntity> getAllTariffs() {
        List<TariffEntity> tariffs = restTemplate.getForObject("http://ms1/api/tariffs/all", List.class);
        return tariffs;
    }
    public TariffEntity getTariffEntityByMinutes(@RequestParam Integer minutes){
        TariffEntity tariff = restTemplate.getForObject("http://ms1/api/tariffs/byMinute?minutes="+minutes, TariffEntity.class);
        return tariff;
    }
    public TariffEntity getTariffEntityByDate(@RequestParam LocalDate date, @RequestParam Integer laps){
        TariffEntity tariff = restTemplate.getForObject("http://ms1/api/tariffs/byDate?date="+date+"&laps="+laps, TariffEntity.class);
        return tariff;
    }

    public TariffEntity getTariffEntityByLaps(@RequestParam Integer laps){
        TariffEntity tariff = restTemplate.getForObject("http://ms1/api/tariffs/byLaps?laps="+laps, TariffEntity.class);
        return tariff;
    }
    //URL ms2 groups dscs
    public List<GroupSizeDscEntity> getAllGroupSizeDscs() {
        List<GroupSizeDscEntity> groupsDscs = restTemplate.getForObject("http://ms2/api/groups/all", List.class);
        return groupsDscs;
    }
    public GroupSizeDscEntity getGroupSizeDscEntityByGroupSize(@RequestParam Integer size){
        GroupSizeDscEntity groupSizeDsc = restTemplate.getForObject("http://ms2/api/groups/bySize?size="+size, GroupSizeDscEntity.class);
        return groupSizeDsc;
    }
    //URL ms3 frequency dscs
    public List<FrequencyDscEntity> getAllFrequencyDscs() {
        List<FrequencyDscEntity> frequencyDscs = restTemplate.getForObject("http://ms3/api/frequency/all", List.class);
        return frequencyDscs;
    }
    public Double getPercentageDiscountByFrequency(@RequestParam Integer frequency){
        Double frequencyDsc = restTemplate.getForObject("http://ms3/api/frequents/frequency?frequency="+frequency, Double.class);
        return frequencyDsc;
    }
    //URL ms4 tarifas especiales y cumpleaños
    public List<Double> getBirthdayDsc(
            LocalDate reservationDate, List<LocalDate> birthdays) {
        String url = "http://ms4/api/special/dsc?reservationDate=" +
                reservationDate.toString() + "&birthdays=" +
                String.join(",", birthdays.stream().map(LocalDate::toString).toList());
        return restTemplate.postForObject(url, null, List.class);

    }




    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public ReservationEntity createReservationWithPricing(LocalDate date, Time startTime, Integer laps,
                                              Integer groupSize, List<CustomerEntity> customers) {
        // Calcular tiempo fin de la reserva
        TariffEntity tariff = getTariffEntityByDate(date,laps);
        Integer durationTime = tariff.getTotal_duration();
        LocalTime localTime = startTime.toLocalTime().plusMinutes(durationTime);
        Time endTime = Time.valueOf(localTime);


        
        // guardar a los clientes
        customers.forEach(customer -> {
            customer.setVisitDate(date);
            if (customer.getId_customer() == null) {
                customerRepository.save(customer);
            }
        });

        // obtener precio base de la tarifa
        Integer basePrice = tariff.getPrice();

        // obtener el descuento por grupo
        Double groupDsc = getGroupSizeDscEntityByGroupSize(groupSize).getDiscountPercentage();
        List<Double> dsc1 = new ArrayList<>();
        for (int i = 0; i < customers.size(); i++) {
            dsc1.add(groupDsc);
        }
        List<Double> dsc2 = new ArrayList<>();
        for (CustomerEntity customer : customers) {
            String rut = customer.getRut();
            Integer frequency = customerService.getFrequencyByRut(rut);
            Double percentage = getPercentageDiscountByFrequency(frequency);
            dsc2.add(percentage);
        }
        //Comparacion de descuentos
        List<Double> finalPrices = new ArrayList<>();
        List<Double> finalDsc = new ArrayList<>();
        List<LocalDate> birthdays = new ArrayList<>();
        for (CustomerEntity customer : customers) {
            birthdays.add(customer.getBirthdate());
        }
        List<Double> dsc3 = getBirthdayDsc(date,birthdays );
        for (int j = 0; j < customers.size(); j++) {
            // Comparar los tres tipos de descuentos
            if (dsc1.get(j) >= dsc2.get(j) && dsc1.get(j) >= dsc3.get(j)) {
                // dsc1 es el mayor
                finalDsc.add(dsc1.get(j));
                finalPrices.add(basePrice-(basePrice*(dsc1.get(j)/100.0)));
            } else if (dsc2.get(j) >= dsc1.get(j) && dsc2.get(j) >= dsc3.get(j)) {
                // dsc2 es el mayor
                finalDsc.add(dsc2.get(j));
                finalPrices.add(basePrice-(basePrice*(dsc2.get(j)/100.0)));
            } else {
                // dsc3 es el mayor
                finalDsc.add(dsc3.get(j));
                finalPrices.add(basePrice-(basePrice*(dsc3.get(j)/100.0)));
            }
        }

        // Sumar el total de la reserva
        Double total = finalPrices.stream().mapToDouble(Double::doubleValue).sum();




        // Crear reserva
        ReservationEntity reservation = new ReservationEntity();
        reservation.setDate(date);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setGroupSize(groupSize);
        reservation.setBaseTariff(basePrice);
        reservation.setCustomers(customers);
        for (CustomerEntity c : customers) {
            c.setReservation(reservation);
        }
        reservation.setTotalAmount(total);
        reservation.setIndividualDscs(finalDsc);
        reservation.setIndividualPrices(finalPrices);
        ReservationEntity reservationSaved = reservationRepository.save(reservation);

        return reservationSaved;
    }
    public ReservationEntity getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Autowired
    private JavaMailSender emailSender;

    public byte[] generateGroupReservationPDF(ReservationEntity reservation) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER.rotate());
        PdfWriter.getInstance(document, baos);

        document.open();

        // Título principal
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Detalles de la Reserva", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Crear tabla
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        // Establecer anchos relativos de columnas
        float[] columnWidths = {1f, 1f, 1.5f, 2f, 1f, 1.5f};
        table.setWidths(columnWidths);

        // Estilo para encabezados
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        BaseColor headerColor = new BaseColor(240, 240, 240);

        // Encabezados de la tabla
        addHeaderCell(table, "Fecha Reserva", headerFont, headerColor);
        addHeaderCell(table, "Hora Reserva", headerFont, headerColor);
        addHeaderCell(table, "Nombre Cliente", headerFont, headerColor);
        addHeaderCell(table, "Correo", headerFont, headerColor);
        addHeaderCell(table, "Descuento", headerFont, headerColor);
        addHeaderCell(table, "Precio Individual", headerFont, headerColor);

        // Añadir datos de clientes
        List<CustomerEntity> customers = reservation.getCustomers();
        List<Double> discounts = reservation.getIndividualDscs();
        List<Double> prices = reservation.getIndividualPrices();

        for (int i = 0; i < customers.size(); i++) {
            CustomerEntity customer = customers.get(i);
            double discount = i < discounts.size() ? discounts.get(i) : 0.0;
            double price = i < prices.size() ? prices.get(i) : 0.0;

            table.addCell(reservation.getDate().toString());
            table.addCell(reservation.getStartTime().toString());
            table.addCell(customer.getName() + " " + customer.getLastname());
            table.addCell(customer.getEmail());

            PdfPCell discountCell = new PdfPCell(new Phrase("- " + String.format("%.2f", discount) + " %"));
            discountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(discountCell);

            PdfPCell priceCell = new PdfPCell(new Phrase("$ " + String.format("%.2f", price)));
            priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(priceCell);
        }

        // Calcular IVA (19%)
        double iva = (reservation.getTotalAmount() * 19) / 100.0;

        // Añadir fila de IVA y total
        PdfPCell emptyCell = new PdfPCell(new Phrase(""));
        emptyCell.setBorder(PdfPCell.NO_BORDER);

        // Fila IVA
        for (int i = 0; i < 3; i++) {
            table.addCell(emptyCell);
        }

        PdfPCell ivaLabelCell = new PdfPCell(new Phrase("IVA", headerFont));
        ivaLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        ivaLabelCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(ivaLabelCell);

        PdfPCell ivaPercentCell = new PdfPCell(new Phrase("+ 19%"));
        ivaPercentCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(ivaPercentCell);

        PdfPCell ivaValueCell = new PdfPCell(new Phrase("$ " + String.format("%.2f", iva)));
        ivaValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(ivaValueCell);

        // Fila total
        for (int i = 0; i < 3; i++) {
            table.addCell(emptyCell);
        }

        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(totalLabelCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase("$ " + String.format("%.2f", reservation.getTotalAmount())));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalValueCell.setColspan(2);
        table.addCell(totalValueCell);

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("¡Gracias por tu reserva! Te esperamos."));

        document.close();
        return baos.toByteArray();
    }

    private void addHeaderCell(PdfPTable table, String text, Font font, BaseColor color) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(color);
        cell.setPadding(8);
        table.addCell(cell);
    }

    //enviar mail de confirmacion
    @Transactional
    public void sendMailConfirmation(Long reservationId) {
        ReservationEntity reservation = getReservationById(reservationId);

        if (reservation == null) {
            throw new RuntimeException("Reserva no encontrada");
        }

        List<CustomerEntity> customers = reservation.getCustomers();

        if (customers != null && !customers.isEmpty()) {
            try {
                // Generar PDF con todos los clientes
                byte[] pdfBytes = generateGroupReservationPDF(reservation);

                // Enviar el PDF a cada cliente
                for (CustomerEntity customer : customers) {
                    if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                        try {
                            // Crear mensaje
                            MimeMessage message = emailSender.createMimeMessage();
                            MimeMessageHelper helper = new MimeMessageHelper(message, true);
                            helper.setTo(customer.getEmail());
                            helper.setSubject("Confirmación de tu reserva");

                            // Contenido del email
                            String content = String.format(
                                    "Hola %s,\n\n" +
                                            "Adjunto encontrarás los detalles de tu reserva para el %s.\n\n" +
                                            "¡Gracias por reservar con nosotros!",
                                    customer.getName(),
                                    reservation.getDate()
                            );

                            helper.setText(content);

                            // Adjuntar el PDF
                            helper.addAttachment("reserva_" + reservation.getId_reservation() + ".pdf",
                                    new ByteArrayResource(pdfBytes));

                            // Enviar el correo
                            emailSender.send(message);
                            logger.info("Correo con PDF enviado a: " + customer.getEmail());
                        } catch (Exception e) {
                            logger.error("Error al enviar correo a " + customer.getEmail() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error al generar el PDF: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            logger.warn("La reserva no tiene clientes asociados");
        }
    }

}