package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.Administrador;
import boletamaster.Evento;

public class administradoorTest {
	
	    private Administrador admin;

	    @BeforeEach
	    void setUp() {
	        admin = new Administrador("admin", "secret");
	        // Inicializamos mapas (la clase no lo hace en el constructor)
	        admin.mapa_clientes = new HashMap<>();
	        admin.setMapa_eventos(new HashMap<>());
	        admin.setMapa_venues(new HashMap<>());
	        admin.setMapa_solicitudes(new HashMap<>());
	        admin.mapa_venues_sugeridos = new HashMap<>();
	    }

	    @AfterEach
	    void tearDown() {
	        admin = null;
	    }

	    // --------------------------------------------------------------------
	    // Coherencia básica de getters/setters de mapas
	    // --------------------------------------------------------------------
	    @Test
	    void testSettersYGettersDeMapas() {
	        HashMap<String, Evento> me = new HashMap<>();
	        HashMap<String, String> ms = new HashMap<>();

	        admin.setMapa_eventos(me);
	        admin.setMapa_solicitudes(ms);

	        assertSame(me, admin.getMapa_eventos(), "El mapa de eventos no se almacenó correctamente");
	        assertSame(ms, admin.getMapa_solicitudes(), "El mapa de solicitudes no se almacenó correctamente");
	        assertNotNull(admin.getMapa_venues(), "El mapa de venues debe estar inicializado en setUp()");
	        assertNotNull(admin.mapa_venues_sugeridos, "El mapa de venues sugeridos debe estar inicializado en setUp()");
	        assertNotNull(admin.mapa_clientes, "El mapa de clientes debe estar inicializado en setUp()");
	    }

	    // --------------------------------------------------------------------
	    // fijar_sobrecargo_evento: solo requiere Evento (disponible)
	    // --------------------------------------------------------------------
	    @Test
	    void testFijarSobrecargoEvento() {
	        Evento e = new Evento("E-1", new java.util.Date(0L), "Concierto", "PROGRAMADO");
	        assertEquals(0.0, e.getSobrecargo(), 1e-9, "Sobrecargo inicial debería ser 0.0");

	        admin.fijar_sobrecargo_evento(e, 0.20);
	        assertEquals(0.20, e.getSobrecargo(), 1e-9, "No se fijó correctamente el sobrecargo");
	    }

	    // --------------------------------------------------------------------
	    // aprobar_cancelar_evento: cambia estado en Evento almacenado en el mapa
	    // --------------------------------------------------------------------
	    @Test
	    void testAprobarCancelarEvento() {
	        Evento e = new Evento("RockFest", new java.util.Date(0L), "Música", "PROGRAMADO");
	        admin.getMapa_eventos().put("RockFest", e);

	        admin.aprobar_cancelar_evento("RockFest");
	        assertEquals(Administrador.CANCELADO, e.getEstadoEvento(),
	                "El estado del evento debe pasar a CANCELADO");
	    }

	    // --------------------------------------------------------------------
	    // cancelar_evento: idem con otra ruta (método directo)
	    // --------------------------------------------------------------------
	    @Test
	    void testCancelarEvento() {
	        Evento e = new Evento("TechConf", new java.util.Date(0L), "Conferencia", "CONFIRMADO");
	        admin.getMapa_eventos().put("TechConf", e);

	        admin.cancelar_evento("TechConf");
	        assertEquals(Administrador.CANCELADO, e.getEstadoEvento(),
	                "El estado del evento debe pasar a CANCELADO");
	    }


	    @Test
	    void testCrearVenue_InsertaEnMapa() {
	        assertEquals(0, admin.getMapa_venues().size(), "El mapa de venues debe iniciar vacío");

	        admin.crearVenue("Coliseo", "Bogotá", 12000, "Sin pólvora");

	        assertTrue(admin.getMapa_venues().containsKey("Coliseo"),
	                "El venue creado debe quedar en el mapa con la llave = nombre");
	        assertNotNull(admin.getMapa_venues().get("Coliseo"), "El Venue creado no debería ser null");
	    }

	    @Test
	    void testAprobarVenue_MueveASistema() {
	        // Requiere que exista la clase Venue con setAprobado(true)
	        boletamaster.Venue sugerido = new boletamaster.Venue(
	                "Teatro", "Teatro", "Bogotá", 5000, "N/A", false);

	        admin.mapa_venues_sugeridos.put("Teatro", sugerido);

	        admin.aprobarVenue("Teatro");

	        assertSame(sugerido, admin.getMapa_venues().get("Teatro"),
	                "El venue aprobado debe quedar en el mapa principal");
	        assertTrue(sugerido.isAprobado(), "El venue debe quedar marcado como aprobado");
	    }

	    @Test
	    void testFijarCobroEmisionImpresion_AplicaCuotaFijaATodosLosTiquetes() {
	        // Implementar cuando tengas:
	        // - Evento con getVenues() poblado
	        // - Venue con getLocalidades()
	        // - Localidad con getTiquetes()
	        // - Tiquete con setCuota_fija(double)
	        // La idea: armar objetos reales, agregar al Evento y verificar luego
	        // que cada Tiquete tenga la cuota fija establecida.
	    }

	   
	}

