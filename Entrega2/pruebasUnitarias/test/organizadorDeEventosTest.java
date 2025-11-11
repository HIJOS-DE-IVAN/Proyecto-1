package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import boletamaster.*; 


public class organizadorDeEventosTest {

	    private Administrador admin;
	    private OrganizadorDeEventos org;

	    private Evento evNoNumerado;
	    private Evento evNumerado;

	    private static final String EV_GEN   = "ConciertoX";
	    private static final String EV_PLATE = "TeatroY";

	    @BeforeEach
	    void setUp() {
	        // Admin
	        admin = new Administrador("admin","pwd");
	        admin.mapa_clientes         = new HashMap<>();
	        admin.setMapa_eventos(new HashMap<>());
	        admin.setMapa_venues(new HashMap<>());
	        admin.setMapa_solicitudes(new HashMap<>());
	        admin.mapa_venues_sugeridos = new HashMap<>();

	        // Organizador
	        org = new OrganizadorDeEventos("org","123");
	        org.admin = admin;

	        //  Evento NO numerado 
	        evNoNumerado = new Evento(EV_GEN, new Date(0L), "Música", "PROGRAMADO");
	        Venue v1 = new Venue("V1","V1","Bogotá",10_000,"-", true);
	        Localidad locGeneral = new Localidad("L-GEN","General", false, 20_000.0, 3);
	        // Tiquetes de la localidad (uno disponible, otro vendido)
	        locGeneral.setTiquetes(new ArrayList<>(List.of(
	            new Tiquete(0.0,0.0,"T-G-1", new Date(), 0, locGeneral, 18_000.0, true, false, null),
	            new Tiquete(0.0,0.0,"T-G-2", new Date(), 0, locGeneral, 19_000.0, true, true , null)
	        )));
	        v1.setLocalidades(new ArrayList<>(List.of(locGeneral)));
	        evNoNumerado.setVenues(new ArrayList<>(List.of(v1)));

	        //  Evento numerado
	        evNumerado = new Evento(EV_PLATE, new Date(0L), "Teatro", "PROGRAMADO");
	        Venue v2 = new Venue("V2","V2","Bogotá",5_000,"-", true);
	        Localidad platea = new Localidad("L-PLA","Platea", true, 30_000.0, 10);
	        platea.setTiquetes(new ArrayList<>(List.of(
	            new TiqueteVendidoNumerado(0.0,0.0,"T-N-7", new Date(), 0, platea, 30_000.0, true, false, null, 7),
	            new TiqueteVendidoNumerado(0.0,0.0,"T-N-8", new Date(), 0, platea, 28_000.0, true, true , null, 8)
	        )));
	        v2.setLocalidades(new ArrayList<>(List.of(platea)));
	        evNumerado.setVenues(new ArrayList<>(List.of(v2)));

	        // Registrar en el organizador
	        org.getEventos().put(EV_GEN, evNoNumerado);
	        org.getEventos().put(EV_PLATE, evNumerado);

	        // También en admin si hace falta
	        admin.getMapa_eventos().put(EV_GEN, evNoNumerado);
	        admin.getMapa_eventos().put(EV_PLATE, evNumerado);
	    }

	    @AfterEach
	    void tearDown() {
	        admin = null;
	        org = null;
	        evNoNumerado = null;
	        evNumerado = null;
	    }

	    // Constructor / getters / setters básicos

	    @Test
	    void testConstructorInicializaEstructuras() {
	        OrganizadorDeEventos tmp = new OrganizadorDeEventos("x","y");
	        assertNotNull(tmp.getEventos(), "mapa_eventos debe inicializarse");
	        assertNotNull(tmp.getTiquetes(), "tiquetes debe inicializarse");
	        assertNotNull(tmp.mapa_venues, "mapa_venues debe inicializarse");
	    }

	    @Test
	    void testGetSetEventosYGetSetTiquetes() {
	        HashMap<String, Evento> nuevo = new HashMap<>();
	        org.setEventos(nuevo);
	        assertSame(nuevo, org.getEventos(), "setEventos debe asignar la referencia");

	        ArrayList<Tiquete> ts = new ArrayList<>();
	        org.setTiquetes(ts);
	        assertSame(ts, org.getTiquetes(), "setTiquetes debe asignar la referencia");
	    }


	    // crear_localidad_venue

	    @Test
	    void testCrearLocalidadVenue_AgregaALaLista() {
	        Venue v = new Venue("ID","Coliseo","Bogotá",10000,"-", true);
	        v.setLocalidades(new ArrayList<>());
	        org.mapa_venues.put("Coliseo", v);

	        org.crear_localidad_venue("Coliseo", "Preferencial", true, 50_000.0, 5);

	        assertEquals(1, v.getLocalidades().size(), "Debe agregar exactamente una localidad");
	        Localidad loc = v.getLocalidades().get(0);
	        assertEquals("Preferencial", loc.getNombre(), "Nombre de localidad incorrecto");
	        assertTrue(loc.isNumerada(), "Debe ser numerada");
	        assertEquals(50_000.0, loc.getPrecioBase(), 1e-9, "Precio base incorrecto");
	        assertEquals(5, loc.getCapacidad(), "Capacidad incorrecta");
	    }


	    // crear_evento (documenta comportamiento actual: NO guarda en mapa_eventos)

	    @Test
	    void testCrearEvento_NoLoGuardaEnMapaEventos_SegunImplementacionActual() {
	        int sizeAntes = org.getEventos().size();

	        // Prepara venue para asociarlo
	        Venue v = new Venue("ID2","Teatro","Bogotá",5000,"-", true);
	        v.setLocalidades(new ArrayList<>());
	        org.mapa_venues.put("Teatro", v);

	        org.crear_evento("E-NEW", new Date(), "Tipo", "OK", "Teatro");

	        assertEquals(sizeAntes, org.getEventos().size(),
	            "Según la implementación actual, crear_evento NO agrega el evento al mapa del organizador");
	        // (Sugerencia futura: insertar en mapa_eventos o devolver el Evento creado).
	    }

	    // --------------------------------------------------------------------
	    // crear_tiquetes_evento
	    // --------------------------------------------------------------------
	    @Test
	    void testCrearTiquetesEvento_RespetaCapacidad() {
	        // Evento con una localidad capacidad=3
	        Evento e = new Evento("E-CAP", new Date(), "X", "OK");
	        Venue v = new Venue("ID3","Auditorio","Bogotá",2000,"-", true);
	        Localidad l = new Localidad("L","General", false, 10_000.0, 3);
	        l.setTiquetes(new ArrayList<>());
	        v.setLocalidades(new ArrayList<>(List.of(l)));
	        e.setVenues(new ArrayList<>(List.of(v)));
	        org.getEventos().put("E-CAP", e);

	        // Pedimos 5; deberían agregarse como máximo 3 (capacidad)
	        org.crear_tiquetes_evento("E-CAP", 5, "NO_USE");

	        assertEquals(3, l.getTiquetes().size(),
	            "Debe crear tiquetes hasta la capacidad de la localidad");
	        // Nota: el método actual crea el *mismo objeto* repetido; si cambias, ajusta la prueba.
	    }

	    // --------------------------------------------------------------------
	    // crear_oferta_localidad: aplica descuento porcentual
	    // --------------------------------------------------------------------
	    @Test
	    void testCrearOfertaLocalidad_AplicaDescuentoAPrecios() {
	        // Localidad con 2 tiquetes con precio 100 y 80
	        Evento e = new Evento("E-OFF", new Date(), "X", "OK");
	        Venue v = new Venue("ID4","Arena","Bogotá",2000,"-", true);
	        Localidad l = new Localidad("L2","Baja", false, 10_000.0, 5);
	        l.setTiquetes(new ArrayList<>(List.of(
	            new Tiquete(0.0,0.0,"A", new Date(), 0, l, 100.0, true, false, null),
	            new Tiquete(0.0,0.0,"B", new Date(), 0, l,  80.0, true, false, null)
	        )));
	        v.setLocalidades(new ArrayList<>(List.of(l)));
	        e.setVenues(new ArrayList<>(List.of(v)));
	        org.getEventos().put("E-OFF", e);

	        org.crear_oferta_localidad("E-OFF", "Baja", 0.25); // -25%

	        assertEquals(75.0, l.getTiquetes().get(0).getPrecio(), 1e-9);
	        assertEquals(60.0, l.getTiquetes().get(1).getPrecio(), 1e-9);
	    }

	    // --------------------------------------------------------------------
	    // crear_tiquetes_multiples_temporada: hoy NPE (documenta bug)
	    // --------------------------------------------------------------------
	    @Test
	    void testCrearTiquetesMultiplesTemporada_ProvocaNPE_SegunCodigoActual() {
	        ArrayList<String> lista = new ArrayList<>(List.of(EV_GEN));
	        assertThrows(NullPointerException.class,
	            () -> org.crear_tiquetes_multiples_temporada(lista),
	            "El código actual usa 'eventos' sin inicializar; debería corregirse (por ejemplo, eventos = new ArrayList<>())");
	    }

	    // --------------------------------------------------------------------
	    // sugerencias al admin
	    // --------------------------------------------------------------------
	    @Test
	    void testSugerirVenue_SeRegistraEnAdminSugeridos() {
	        org.sugerir_venue("Nuevo", "Bogotá", 1000, "N/A");
	        assertTrue(admin.mapa_venues_sugeridos.containsKey("Nuevo"),
	            "El venue sugerido debe quedar en el mapa del admin");
	        assertFalse(admin.mapa_venues_sugeridos.get("Nuevo").isAprobado(),
	            "Debe quedar como NO aprobado inicialmente");
	    }

	    @Test
	    void testSugerirCancelarEvento_SeRegistraSolicitud() {
	        org.sugerir_cancelar_evento(EV_GEN, "Motivo X");
	        assertEquals("Motivo X", admin.getMapa_solicitudes().get(EV_GEN),
	            "La solicitud debe quedar registrada en el admin");
	    }

	    // --------------------------------------------------------------------
	    // comprar_tiquete_no_numerado y numerado (organizador agrega a su lista)
	    // --------------------------------------------------------------------
	    @Test
	    void testComprarNoNumerado_CompraYMarcaVendido() {
	        int antes = org.getTiquetes().size();
	        Localidad gen = evNoNumerado.getVenues().get(0).getLocalidades().get(0);
	        int libresAntes = (int) gen.getTiquetes().stream().filter(t -> !t.isEstaVendido()).count();

	        org.comprar_tiquete_no_numerado(EV_GEN, "General");

	        assertEquals(antes + 1, org.getTiquetes().size(), "Debe agregarse a la lista del organizador");
	        long libresDespues = gen.getTiquetes().stream().filter(t -> !t.isEstaVendido()).count();
	        assertEquals(libresAntes - 1, libresDespues, "Debe disminuir la cantidad de no vendidos en la localidad");
	        assertNull(org.getTiquetes().get(org.getTiquetes().size()-1).getCliente(),
	            "El organizador NO es Cliente; el campo cliente queda null");
	    }

	    @Test
	    void testComprarNoNumerado_ErroresEventoOLocalidad() {
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_tiquete_no_numerado("NO_EXISTE", "General"));
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_tiquete_no_numerado(EV_GEN, "VIP"));
	    }

	    @Test
	    void testComprarNumerado_CompraAsientoEspecifico() {
	        int antes = org.getTiquetes().size();
	        org.comprar_tiquete_numerado(EV_PLATE, "Platea", 7);

	        assertEquals(antes + 1, org.getTiquetes().size(), "Debe agregarse a la lista del organizador");
	        var t = org.getTiquetes().get(org.getTiquetes().size()-1);
	        assertTrue(t instanceof TiqueteVendidoNumerado, "Debe ser numerado");
	        assertTrue(t.isEstaVendido(), "Debe quedar vendido");
	        assertEquals(7, ((TiqueteVendidoNumerado) t).getNumero_asiento(), "Asiento incorrecto");
	        assertNull(t.getCliente(), "Organizador no es Cliente; queda null");
	    }

	    @Test
	    void testComprarNumerado_ErroresEventoLocalidadAsiento() {
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_tiquete_numerado("NO_EXISTE", "Platea", 7));
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_tiquete_numerado(EV_PLATE, "VIP", 7));
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_tiquete_numerado(EV_PLATE, "Platea", 8)); // ya vendido
	    }

	    // --------------------------------------------------------------------
	    // comprar_palco / paquetes múltiples
	    // --------------------------------------------------------------------
	    @Test
	    void testComprarPalco_CreaCantidadDeTiquetesEnLocalidadYEnOrganizador() {
	        Localidad platea = evNumerado.getVenues().get(0).getLocalidades().get(0);
	        int cOrgAntes = org.getTiquetes().size();
	        int cLocAntes = platea.getTiquetes().size();

	        org.comprar_palco(EV_PLATE, "Platea", 3);

	        assertEquals(cOrgAntes + 3, org.getTiquetes().size(), "Debe sumar 3 en organizador");
	        assertEquals(cLocAntes + 3, platea.getTiquetes().size(), "Debe sumar 3 en localidad");
	        assertTrue(org.getTiquetes().subList(cOrgAntes, cOrgAntes+3).stream().allMatch(Tiquete::isEstaVendido),
	            "Los nuevos tiquetes vienen vendidos=true");
	    }

	    @Test
	    void testComprarPalco_Errores() {
	        assertThrows(IllegalArgumentException.class,
	            () -> org.comprar_palco(EV_PLATE, "Platea", 0));
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_palco("NO_EXISTE", "Platea", 1));
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_palco(EV_GEN, "General", 1), // no numerada
	            "Debe fallar cuando la localidad no es numerada");
	    }

	    @Test
	    void testPaqueteTemporadaYDeluxe() {
	        Localidad platea = evNumerado.getVenues().get(0).getLocalidades().get(0);
	        int cOrgAntes = org.getTiquetes().size();
	        int cLocAntes = platea.getTiquetes().size();

	        org.comprar_paquete_multiple_temporada(List.of(EV_PLATE), "Platea", 2);
	        org.comprar_paquete_multiple_deluxe(List.of(EV_PLATE), "Platea", 2);

	        assertEquals(cOrgAntes + 4, org.getTiquetes().size(), "2+2 tiquetes en organizador");
	        assertEquals(cLocAntes + 4, platea.getTiquetes().size(), "2+2 tiquetes en localidad");
	    }

	    @Test
	    void testPaquetes_Errores() {
	        assertThrows(IllegalArgumentException.class,
	            () -> org.comprar_paquete_multiple_temporada(Collections.emptyList(), "Platea", 1));
	        assertThrows(IllegalArgumentException.class,
	            () -> org.comprar_paquete_multiple_deluxe(Collections.emptyList(), "Platea", 1));
	        assertThrows(IllegalArgumentException.class,
	            () -> org.comprar_paquete_multiple_temporada(List.of(EV_PLATE), "Platea", 0));
	        assertThrows(IllegalArgumentException.class,
	            () -> org.comprar_paquete_multiple_deluxe(List.of(EV_PLATE), "Platea", -2));
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_paquete_multiple_deluxe(List.of("NO_EXISTE"), "Platea", 1));
	        assertThrows(IllegalStateException.class,
	            () -> org.comprar_paquete_multiple_deluxe(List.of(EV_PLATE), "VIP", 1));
	    }
	}


