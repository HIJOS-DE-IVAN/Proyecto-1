package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.*;

import boletamaster.Administrador;
import boletamaster.Cliente;
import boletamaster.Evento;
import boletamaster.Localidad;
import boletamaster.Tiquete;
import boletamaster.TiqueteVendidoNumerado;
import boletamaster.Venue;


public class ClienteTest {
	
	

	    private Administrador admin;
	    private Cliente cliente;

	    private Evento eventoConcierto;   // con localidad no numerada
	    private Evento eventoTeatro;      // con localidad numerada
	    private static final String EV_CONCIERTO = "ConciertoX";
	    private static final String EV_TEATRO    = "TeatroY";

	    @BeforeEach
	    void setUp() {
	        admin = new Administrador("admin", "pwd");
	        admin.mapa_clientes       = new HashMap<>();
	        admin.setMapa_eventos(new HashMap<>());
	        admin.setMapa_venues(new HashMap<>());
	        admin.setMapa_solicitudes(new HashMap<>());
	        admin.mapa_venues_sugeridos = new HashMap<>();

	        cliente = new Cliente("cata", "123", 50_000.0);
	        cliente.admin = admin;
	        cliente.setMapa_eventos(new HashMap<>());

	        //Evento con localidad NO numerada
	        eventoConcierto = new Evento(EV_CONCIERTO, new Date(0L), "Música", "PROGRAMADO");
	        var v1 = new Venue("V1","V1","Bogotá",10_000,"-", true);
	        var locGeneral = new Localidad("General", false, 20_000.0, new ArrayList<>());
	        // Tiquetes en "General": algunos vendidos/no vendidos y con distintos precios
	        locGeneral.getTiquetes().add(new Tiquete(0.0,0.0,"T-G-1", new Date(), 0, locGeneral, 18_000.0, true, false, null));
	        locGeneral.getTiquetes().add(new Tiquete(0.0,0.0,"T-G-2", new Date(), 0, locGeneral, 25_000.0, true, false, null)); // más caro
	        locGeneral.getTiquetes().add(new Tiquete(0.0,0.0,"T-G-3", new Date(), 0, locGeneral, 19_999.0, true, true , null)); // ya vendido
	        v1.setLocalidades(new ArrayList<>(List.of(locGeneral)));
	        eventoConcierto.setVenues(new ArrayList<>(List.of(v1)));

	        // Evento con localidad numerad 
	        eventoTeatro = new Evento(EV_TEATRO, new Date(0L), "Teatro", "PROGRAMADO");
	        var v2 = new Venue("V2","V2","Bogotá",5_000,"-", true);
	        var locPlatea = new Localidad("Platea", true, 30_000.0, new ArrayList<>());
	        // Tiquetes numerados: asiento 7 disponible, asiento 8 vendido
	        locPlatea.getTiquetes().add(new TiqueteVendidoNumerado(0.0,0.0,"T-N-7", new Date(), 0, locPlatea, 30_000.0, true, false, null, 7));
	        locPlatea.getTiquetes().add(new TiqueteVendidoNumerado(0.0,0.0,"T-N-8", new Date(), 0, locPlatea, 28_000.0, true, true , null, 8));
	        v2.setLocalidades(new ArrayList<>(List.of(locPlatea)));
	        eventoTeatro.setVenues(new ArrayList<>(List.of(v2)));

	        // Registrar eventos en admin y en el cliente
	        admin.getMapa_eventos().put(EV_CONCIERTO, eventoConcierto);
	        admin.getMapa_eventos().put(EV_TEATRO, eventoTeatro);
	        cliente.getMapa_eventos().put(EV_CONCIERTO, eventoConcierto);
	        cliente.getMapa_eventos().put(EV_TEATRO, eventoTeatro);

	        // Registrar cliente en el admin
	        admin.mapa_clientes.put("cata", cliente);
	    }

	    @AfterEach
	    void tearDown() {
	        admin = null;
	        cliente = null;
	        eventoConcierto = null;
	        eventoTeatro = null;
	    }


	    // Saldo y getters/setters básicos

	    @Test
	    void testSaldoDisponibleYSetter() {
	        assertEquals(50_000.0, cliente.consultar_saldo_disponible(), 1e-9, "Saldo inicial incorrecto");
	        cliente.setSaldoPlataforma(12_345.0);
	        assertEquals(12_345.0, cliente.consultar_saldo_disponible(), 1e-9, "No se actualizó el saldo");
	    }

	    @Test
	    void testGetSetTiquetes() {
	        assertNotNull(cliente.getTiquetes(), "La lista de tiquetes debe iniciar no nula");
	        assertTrue(cliente.getTiquetes().isEmpty(), "La lista de tiquetes debe iniciar vacía");

	        var lista = new ArrayList<Tiquete>();
	        lista.add(new Tiquete(0.0,0.0,"X", new Date(), 0, null, 10.0, true, false, null));
	        cliente.setTiquetes(lista);
	        assertSame(lista, cliente.getTiquetes(), "setTiquetes debe asignar la referencia recibida");
	        assertEquals(1, cliente.getTiquetes().size(), "No se reflejó el contenido esperado");
	    }

	    @Test
	    void testGetSetMapaEventos() {
	        var map = new HashMap<String, Evento>();
	        cliente.setMapa_eventos(map);
	        assertSame(map, cliente.getMapa_eventos(), "No se almacenó correctamente el mapa de eventos");
	    }

	    // --------------------------------------------------------------------
	    // solicitar_reembolso: escribe en el mapa de solicitudes del admin
	    // --------------------------------------------------------------------
	    @Test
	    void testSolicitarReembolso_SeRegistraEnAdmin() {
	        assertEquals(0, admin.getMapa_solicitudes().size(), "Debe iniciar vacío");
	        cliente.solicitar_reembolso(EV_CONCIERTO, "Lluvia excesiva");
	        assertEquals(1, admin.getMapa_solicitudes().size(), "Debe haberse agregado una solicitud");
	        assertEquals("Lluvia excesiva", admin.getMapa_solicitudes().get(EV_CONCIERTO), "Motivo no coincide");
	    }


	    // comprar_tiquete_no_numerado

	    @Test
	    void testComprarTiqueteNoNumerado_CompraExitosamente() {
	        double saldoAntes = cliente.consultar_saldo_disponible();
	        cliente.comprar_tiquete_no_numerado(EV_CONCIERTO, "General");

	        // Debe tomar el PRIMER tiquete disponible y asequible (18_000.0), no vendido
	        assertEquals(1, cliente.getTiquetes().size(), "Debe haber 1 tiquete comprado");
	        Tiquete t = cliente.getTiquetes().get(0);
	        assertTrue(t.isEstaVendido(), "El tiquete debe quedar marcado como vendido");
	        assertSame(cliente, t.getCliente(), "El tiquete debe registrar al cliente comprador");
	        assertEquals(saldoAntes - t.getPrecio(), cliente.consultar_saldo_disponible(), 1e-9, "Saldo no disminuyó correctamente");
	    }

	    @Test
	    void testComprarTiqueteNoNumerado_NoCompraSiSaldoInsuficiente() {
	        cliente.setSaldoPlataforma(10_000.0); //menor que el precio más barato (18_000)
	        cliente.comprar_tiquete_no_numerado(EV_CONCIERTO, "General");
	        assertTrue(cliente.getTiquetes().isEmpty(), "No debería comprar con saldo insuficiente");
	    }

	    @Test
	    void testComprarTiqueteNoNumerado_LocalidadInexistente_NoCompra() {
	        cliente.comprar_tiquete_no_numerado(EV_CONCIERTO, "VIP"); // no existe
	        assertTrue(cliente.getTiquetes().isEmpty(), "No debería comprar si la localidad no existe");
	    }


	    // comprar_tiquete_numerado

	    @Test
	    void testComprarTiqueteNumerado_CompraPorNumeroDeAsiento() {
	        double saldoAntes = cliente.consultar_saldo_disponible();

	        cliente.comprar_tiquete_numerado(EV_TEATRO, "Platea", 7); // está disponible

	        assertEquals(1, cliente.getTiquetes().size(), "Debe haber 1 tiquete comprado");
	        Tiquete t = cliente.getTiquetes().get(0);
	        assertTrue(t instanceof TiqueteVendidoNumerado, "Debe ser un tiquete numerado");
	        assertTrue(t.isEstaVendido(), "Debe quedar vendido");
	        assertEquals(saldoAntes - t.getPrecio(), cliente.consultar_saldo_disponible(), 1e-9, "Saldo no disminuyó correctamente");
	        assertEquals(7, ((TiqueteVendidoNumerado) t).getNumero_asiento(), "Asiento no coincide");
	    }

	    @Test
	    void testComprarTiqueteNumerado_NoCompraSiAsientoNoDisponible() {
	        cliente.comprar_tiquete_numerado(EV_TEATRO, "Platea", 8); // 8 está vendido
	        assertTrue(cliente.getTiquetes().isEmpty(), "No debería comprar si el asiento ya está vendido");
	    }

	    // --------------------------------------------------------------------
	    // comprar_palco
	    // --------------------------------------------------------------------
	    @Test
	    void testComprarPalco_CreaYTomaCantidadDeTiquetes() {
	        // Usamos el evento numerado (Platea es numerada)
	        int cantidad = 3;
	        int sizeAntesCliente = cliente.getTiquetes().size();
	        Localidad platea = eventoTeatro.getVenues().get(0).getLocalidades().get(0);
	        int sizeAntesLocalidad = platea.getTiquetes().size();

	        cliente.comprar_palco(EV_TEATRO, "Platea", cantidad);

	        assertEquals(sizeAntesCliente + cantidad, cliente.getTiquetes().size(), "No se agregaron todos los tiquetes al cliente");
	        assertEquals(sizeAntesLocalidad + cantidad, platea.getTiquetes().size(), "No se agregaron todos los tiquetes a la localidad");
	        // Los tiquetes nuevos vienen vendidos=true según tu implementación
	        long vendidosNuevos = cliente.getTiquetes().stream().filter(Tiquete::isEstaVendido).count();
	        assertTrue(vendidosNuevos >= cantidad, "Los tiquetes de palco deberían venir vendidos");
	    }

	    @Test
	    void testComprarPalco_FallaSiLocalidadNoEsNumerada() {
	        IllegalStateException ex = assertThrows(IllegalStateException.class,
	            () -> cliente.comprar_palco(EV_CONCIERTO, "General", 2),
	            "Debe fallar: General no es numerada"
	        );
	        assertTrue(ex.getMessage().contains("no es numerada"), "Mensaje de error esperado");
	    }

	    @Test
	    void testComprarPalco_CantidadInvalida() {
	        assertThrows(IllegalArgumentException.class,
	            () -> cliente.comprar_palco(EV_TEATRO, "Platea", 0),
	            "Debe fallar si cantidad <= 0");
	    }


	    // Paquetes (temporada y deluxe)

	    @Test
	    void testComprarPaqueteMultipleTemporada() {
	        int sizeAntes = cliente.getTiquetes().size();
	        Localidad platea = eventoTeatro.getVenues().get(0).getLocalidades().get(0);
	        int sizeAntesLoc = platea.getTiquetes().size();

	        cliente.comprar_paquete_multiple_temporada(List.of(EV_TEATRO), "Platea", 2);

	        assertEquals(sizeAntes + 2, cliente.getTiquetes().size(), "No se agregaron 2 tiquetes al cliente");
	        assertEquals(sizeAntesLoc + 2, platea.getTiquetes().size(), "No se agregaron 2 tiquetes a la localidad");
	    }

	    @Test
	    void testComprarPaqueteMultipleDeluxe() {
	        int sizeAntes = cliente.getTiquetes().size();
	        Localidad platea = eventoTeatro.getVenues().get(0).getLocalidades().get(0);
	        int sizeAntesLoc = platea.getTiquetes().size();

	        cliente.comprar_paquete_multiple_deluxe(List.of(EV_TEATRO), "Platea", 2);

	        assertEquals(sizeAntes + 2, cliente.getTiquetes().size(), "No se agregaron 2 tiquetes al cliente");
	        assertEquals(sizeAntesLoc + 2, platea.getTiquetes().size(), "No se agregaron 2 tiquetes a la localidad");
	    }

	    @Test
	    void testPaquetes_ListaVaciaDebeFallar() {
	        assertThrows(IllegalArgumentException.class,
	            () -> cliente.comprar_paquete_multiple_temporada(Collections.emptyList(), "Platea", 1),
	            "Debe fallar si no se indican eventos");
	        assertThrows(IllegalArgumentException.class,
	            () -> cliente.comprar_paquete_multiple_deluxe(Collections.emptyList(), "Platea", 1),
	            "Debe fallar si no se indican eventos");
	    }

	    @Test
	    void testPaquetes_CantidadInvalida() {
	        assertThrows(IllegalArgumentException.class,
	            () -> cliente.comprar_paquete_multiple_temporada(List.of(EV_TEATRO), "Platea", -3),
	            "Debe fallar si cantidad <= 0");
	        assertThrows(IllegalArgumentException.class,
	            () -> cliente.comprar_paquete_multiple_deluxe(List.of(EV_TEATRO), "Platea", 0),
	            "Debe fallar si cantidad <= 0");
	    }

	    @Test
	    void testErroresDeBusqueda_EventoOLocalidad() {
	        // Removemos el evento para forzar error
	        cliente.getMapa_eventos().remove(EV_TEATRO);
	        IllegalStateException ex1 = assertThrows(IllegalStateException.class,
	            () -> cliente.comprar_paquete_multiple_deluxe(List.of(EV_TEATRO), "Platea", 1),
	            "Debe fallar si el evento no está cargado");
	        assertTrue(ex1.getMessage().contains("Evento no encontrado"), "Mensaje esperado");

	        // Restauramos evento, pero buscamos localidad inexistente
	        cliente.getMapa_eventos().put(EV_TEATRO, eventoTeatro);
	        IllegalStateException ex2 = assertThrows(IllegalStateException.class,
	            () -> cliente.comprar_paquete_multiple_deluxe(List.of(EV_TEATRO), "VIP", 1),
	            "Debe fallar si la localidad no existe");
	        assertTrue(ex2.getMessage().contains("Localidad no encontrada"), "Mensaje esperado");
	    }
	}

	

