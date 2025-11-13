package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.MultiEvento;
import boletamaster.TiqueteMultipleDeluxe;
public class TiqueteMultipleDeluxeTest {
	private TiqueteMultipleDeluxe deluxe;
    private MultiEvento multiEvento;
    private Date fechaBase;

    @BeforeEach
    void setUp() {
        fechaBase = new Date(1_700_000_000_000L);
        multiEvento = new MultiEvento();

        deluxe = new TiqueteMultipleDeluxe(
            150_000,   // precio
            0.10,      // cargo_porcentual_servicio
            5_000.0,   // cuota_fija
            fechaBase, // fecha
            20,        // hora
            "D-001",   // identificador
            "Camiseta edición limitada", // mercancia_extra
            true,      // esTransferible
            multiEvento // multiEvento
        );
    }

    @AfterEach
    void tearDown() {
        deluxe = null;
        multiEvento = null;
        fechaBase = null;
    }
    // Constructor: inicializa todos los campos

    @Test
    void testConstructor_InicializaCamposCorrectamente() {
        assertEquals(150_000, deluxe.getPrecio(), "Precio incorrecto");
        assertEquals(0.10, deluxe.getCargo_porcentual_servicio(), 1e-9, "Cargo porcentual incorrecto");
        assertEquals(5_000.0, deluxe.getCuota_fija(), 1e-9, "Cuota fija incorrecta");
        assertEquals(fechaBase, deluxe.getFecha(), "Fecha incorrecta");
        assertEquals(20, deluxe.getHora(), "Hora incorrecta");
        assertEquals("D-001", deluxe.getIdentificador(), "Identificador incorrecto");
        assertEquals("Camiseta edición limitada", deluxe.getMercancia_extra(), "Mercancía extra incorrecta");
        assertTrue(deluxe.isEsTransferible(), "esTransferible debería ser true");
        assertSame(multiEvento, deluxe.getMultiEvento(), "multiEvento no se asignó correctamente");
    }
    // Setters numéricos y de campos simples

    @Test
    void testSetPrecio_ModificaValor() {
        deluxe.setPrecio(200_000);
        assertEquals(200_000, deluxe.getPrecio(), "No se actualizó el precio");
    }

    @Test
    void testSetCargoPorcentual_ModificaValor() {
        deluxe.setCargo_porcentual_servicio(0.25);
        assertEquals(0.25, deluxe.getCargo_porcentual_servicio(), 1e-9, "No se actualizó el cargo porcentual");
    }

    @Test
    void testSetCuotaFija_ModificaValor() {
        deluxe.setCuota_fija(10_000.0);
        assertEquals(10_000.0, deluxe.getCuota_fija(), 1e-9, "No se actualizó la cuota fija");
    }

    @Test
    void testSetFecha_ModificaValor() {
        Date nueva = new Date(1_800_000_000_000L);
        deluxe.setFecha(nueva);
        assertEquals(nueva, deluxe.getFecha(), "No se actualizó la fecha");
    }

    @Test
    void testSetHora_ModificaValor() {
        deluxe.setHora(18);
        assertEquals(18, deluxe.getHora(), "No se actualizó la hora");
    }

    @Test
    void testSetIdentificador_ModificaValor() {
        deluxe.setIdentificador("D-XYZ");
        assertEquals("D-XYZ", deluxe.getIdentificador(), "No se actualizó el identificador");
    }

    @Test
    void testSetMercanciaExtra_ModificaValor() {
        deluxe.setMercancia_extra("Poster firmado");
        assertEquals("Poster firmado", deluxe.getMercancia_extra(), "No se actualizó la mercancía extra");
    }

    @Test
    void testSetEsTransferible_ModificaValor() {
        deluxe.setEsTransferible(false);
        assertFalse(deluxe.isEsTransferible(), "No se actualizó esTransferible");
    }
    // MultiEvento: referencia y null
    @Test
    void testSetMultiEvento_AsignaReferencia() {
        MultiEvento otro = new MultiEvento();
        deluxe.setMultiEvento(otro);
        assertSame(otro, deluxe.getMultiEvento(),
                "setMultiEvento debe asignar la referencia recibida");
    }

    @Test
    void testSetMultiEventoNull_Permitido() {
        deluxe.setMultiEvento(null);
        assertNull(deluxe.getMultiEvento(),
                "Según la implementación actual, multiEvento puede quedar null");
    }


}
