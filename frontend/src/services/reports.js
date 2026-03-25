import httpClient from "../http-common";


// Obtener reporte de vueltas/tiempo para un rango de fechas
const getLapsTimeReport = (startDate, endDate) => {
  return httpClient.get("/reports/laps-time", {
    params: {
      startDate: startDate,
      endDate: endDate
    }
  });
};
// Obtener reporte por tamaño de grupo para un rango de fechas
const getGroupSizeReport = (startDate, endDate) => {
  return httpClient.get("/reports/group-size", {
    params: {
      startDate: startDate,
      endDate: endDate
    }
  });
};

export default {
  getLapsTimeReport,
  getGroupSizeReport
};