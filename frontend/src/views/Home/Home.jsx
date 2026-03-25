import React from 'react';
import { Box, Typography, Paper, Grid } from '@mui/material';
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import PeopleIcon from '@mui/icons-material/People';
import BarChartIcon from '@mui/icons-material/BarChart';
import PaymentIcon from '@mui/icons-material/Payment';
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';
/*
#F4F5F5 blanco
#77B8B9 celeste
#CA5833 rojo
#C98F51 naranja
#3A3A4B gris oscuro

*/

const Home = () => {
  return (
    <Box sx={{ 
      p: 4, 
      textAlign: 'center', 
      maxWidth: '1200px', 
      mx: 'auto',
      mt: 2
    }}>
      {/* Bienvenida */}
      <Box sx={{ 
        mb: 6, 
        p: 4, 
        backgroundColor: '#F4F5F5', 
        borderRadius: 2
      }}>
        <Typography variant="h3" sx={{ 
          color: '#3A3A4B', 
          mb: 2,
          fontWeight: 'bold' 
        }}>
          ¡Bienvenido a KartingRM!
        </Typography>

        <Typography variant="h4" sx={{ 
          color: '#77B8B9', 
          fontWeight: 'bold',
          mb: 3
        }}>
          El Mejor Karting de la Región Metropolitana
        </Typography>

        <Typography variant="h6" sx={{ 
          color: '#666', 
          mb: 2
        }}>
          Vive la experiencia única de velocidad y adrenalina
        </Typography>

        <Box sx={{ display: 'flex', justifyContent: 'center', mb: 2 }}>
          <EmojiEventsIcon sx={{ color: '#C98F51', fontSize: 40, mr: 1 }} />
          <Typography variant="body1" sx={{ fontWeight: 'bold', color: '#555' }}>
            Pistas de nivel internacional • Equipamiento de primera • Diversión garantizada
          </Typography>
          <EmojiEventsIcon sx={{ color: '#C98F51', fontSize: 40, ml: 1 }} />
        </Box>
      </Box>

      <Grid container spacing={4} justifyContent="center">
        {/* Tarjeta 1 */}
        <Grid gridsize={{xs:12,sm:6,md:4}}>
          <Paper elevation={3} sx={{ 
            p: 4, 
            display: 'flex', 
            flexDirection: 'column', 
            alignItems: 'center',
            height: '200px'
            
          }}>
            <DirectionsCarIcon sx={{ fontSize: 60, color: '#77B8B9', mb: 2 }} />
            <Typography variant="h6">Opciones y precios</Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Verifica las tarifas del karting
            </Typography>
          </Paper>
        </Grid>

        {/* Tarjeta 2 */}
        <Grid gridsize={{xs:12,sm:6,md:4}}>
          <Paper elevation={3} sx={{ 
            p: 4, 
            display: 'flex', 
            flexDirection: 'column', 
            alignItems: 'center',
            height: '200px'
          }}>
            <CalendarTodayIcon sx={{ fontSize: 60, color: '#77B8B9', mb: 2 }} />
            <Typography variant="h6">Reservaciones</Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Gestiona horarios y reservas
            </Typography>
          </Paper>
        </Grid>

        

        {/* Tarjeta 3 */}
        <Grid gridsize={{xs:12,sm:6,md:4}}>
          <Paper elevation={3} sx={{ 
            p: 4, 
            display: 'flex', 
            flexDirection: 'column', 
            alignItems: 'center',
            height: '200px'
          }}>
            <PaymentIcon sx={{ fontSize: 60, color: '#77B8B9', mb: 2 }} />
            <Typography variant="h6">Pagos</Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Gestiona cobros y facturación
            </Typography>
          </Paper>
        </Grid>

        {/* Tarjeta 4 */}
        <Grid gridsize={{xs:12,sm:6,md:4}}>
          <Paper elevation={3} sx={{ 
            p: 4, 
            display: 'flex', 
            flexDirection: 'column', 
            alignItems: 'center',
            height: '200px'
          }}>
            <BarChartIcon sx={{ fontSize: 60, color: '#77B8B9', mb: 2 }} />
            <Typography variant="h6">Reportes</Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              Estadísticas e informes de ingresos
            </Typography>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  )
}

export default Home
