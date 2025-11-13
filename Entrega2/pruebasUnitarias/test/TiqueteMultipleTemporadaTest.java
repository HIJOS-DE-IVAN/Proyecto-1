package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.MultiEvento;
import boletamaster.TiqueteMultipleTemporada;


	
	class TestTiqueteMultipleTemporadaSinMockito {

	    private TiqueteMultipleTemporada temporada;
	    private MultiEvento multiEvento;
	    private Date fechaBase;

	    @BeforeEach
	    void setUp() {
	        fechaBase = new Date(1_700_000_000_000L);
	        multiEvento = new MultiEvento();

	        temporada = new TiqueteMultipleTemporada(
	            120_000,   // precio
	            0.08,      // cargo_porcentual_servicio
	            3_000.0,   // cuota_fija
	            fechaBase, // fecha
	            19,        // hora
	            "TMP-001", // identificador
	            true,      // esTransferible
	            multiEvento // multiEvento
	        );
	    }

	    @AfterEach
	    void tearDown() {
	        temporada = null;
	        multiEvento = null;
	        fechaBase = null;
	    }
	    // Constructor: inicializa todos los campos
	    @Test
	    void testConstructor_InicializaCamposCorrectamente() {
	        assertEquals(120_000, temporada.getPrecio(), "Precio incorrecto");
	        assertEquals(0.08, temporada.getCargo_porcentual_servicio(), 1e-9, "Cargo porcentual incorrecto");
	        assertEquals(3_000.0, temporada.getCuota_fija(), 1e-9, "Cuota fija incorrecta");
	        assertEquals(fechaBase, temporada.getFecha(), "Fecha incorrecta");
	        assertEquals(19, temporada.getHora(), "Hora incorrecta");
	        assertEquals("TMP-001", temporada.getIdentificador(), "Identificador incorrecto");
	        assertTrue(temporada.isEsTransferible(), "esTransferible debería ser true");
	        assertSame(multiEvento, temporada.getMultiEvento(), "multiEvento no se asignó correctamente");
	    }
	    // Setters numéricos y de campos simples
	    @Test
	    void testSetPrecio_ModificaValor() {
	        temporada.setPrecio(150_000);
	        assertEquals(150_000, temporada.getPrecio(), "No se actualizó el precio");
	    }

	    @Test
	    void testSetCargoPorcentual_ModificaValor() {
	        temporada.setCargo_porcentual_servicio(0.15);
	        assertEquals(0.15, temporada.getCargo_porcentual_servicio(), 1e-9, "No se actualizó el cargo porcentual");
	    }

	    @Test
	    void testSetCuotaFija_ModificaValor() {
	        temporada.setCuota_fija(7_500.0);
	        assertEquals(7_500.0, temporada.getCuota_fija(), 1e-9, "No se actualizó la cuota fija");
	    }

	    @Test
	    void testSetFecha_ModificaValor() {
	        Date nueva = new Date(1_800_000_000_000L);
	        temporada.setFecha(nueva);
	        assertEquals(nueva, temporada.getFecha(), "No se actualizó la fecha");
	    }

	    @Test
	    void testSetHora_ModificaValor() {
	        temporada.setHora(21);
	        assertEquals(21, temporada.getHora(), "No se actualizó la hora");
	    }

	    @Test
	    void testSetIdentificador_ModificaValor() {
	        temporada.setIdentificador("TMP-XYZ");
	        assertEquals("TMP-XYZ", temporada.getIdentificador(), "No se actualizó el identificador");
	    }

	    @Test
	    void testSetEsTransferible_ModificaValor() {
	        temporada.setEsTransferible(false);
	        assertFalse(temporada.isEsTransferible(), "No se actualizó esTransferible");
	    }
	    // MultiEvento: referencia y null
	    @Test
	    void testSetMultiEvento_AsignaReferencia() {
	        MultiEvento otro = new MultiEvento();
	        temporada.setMultiEvento(otro);
	        assertSame(otro, temporada.getMultiEvento(),
	                "setMultiEvento debe asignar la referencia recibida");
	    }

	    @Test
	    void testSetMultiEventoNull_Permitido() {
	        temporada.setMultiEvento(null);
	        assertNull(temporada.getMultiEvento(),
	                "Según la implementación actual, multiEvento puede quedar null");
	    }
	}



