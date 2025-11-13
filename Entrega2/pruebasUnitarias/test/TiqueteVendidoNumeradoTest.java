package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.Cliente;
import boletamaster.Localidad;
import boletamaster.TiqueteVendidoNumerado;

public class TiqueteVendidoNumeradoTest {
		private TiqueteVendidoNumerado tiqueteNum;
	    private Cliente cliente;
	    private Localidad localidad;
	    private Date fechaBase;

	    @BeforeEach
	    void setUp() {
	        fechaBase = new Date(1_700_000_000_000L);
	        cliente   = new Cliente("userNum", "pwd123", 80_000.0);
	        localidad = new Localidad("LOC-NUM", "Platea Numerada", true, 70_000.0, 50);

	        tiqueteNum = new TiqueteVendidoNumerado(
	            0.12,          // sobrecargo_porcentual_servicio
	            3_000.0,       // cuota_fija
	            "TN-001",      // identificador
	            fechaBase,     // fecha
	            21,            // hora
	            localidad,     // localidad
	            75_000.0,      // precio
	            true,          // esTransferible
	            false,         // estaVendido
	            cliente,       // cliente
	            42             // numero_asiento
	        );
	    }

	    @AfterEach
	    void tearDown() {
	        tiqueteNum = null;
	        cliente = null;
	        localidad = null;
	        fechaBase = null;
	    }
	    // Constructor: hereda bien de Tiquete y asigna numero_asiento
	    @Test
	    void testConstructor_InicializaCamposHeredadosYNumeroAsiento() {
	        // Campos heredados de Tiquete
	        assertEquals(0.12, tiqueteNum.getSobrecargo_porcentual_servicio(), 1e-9, "Sobrecargo incorrecto");
	        assertEquals(3_000.0, tiqueteNum.getCuota_fija(), 1e-9, "Cuota fija incorrecta");
	        assertEquals("TN-001", tiqueteNum.getIdentificador(), "Identificador incorrecto");
	        assertEquals(fechaBase, tiqueteNum.getFecha(), "Fecha incorrecta");
	        assertEquals(21, tiqueteNum.getHora(), "Hora incorrecta");
	        assertEquals(75_000.0, tiqueteNum.getPrecio(), 1e-9, "Precio incorrecto");
	        assertTrue(tiqueteNum.isEsTransferible(), "esTransferible debería ser true");
	        assertFalse(tiqueteNum.isEstaVendido(), "estaVendido debería ser false");
	        assertSame(cliente, tiqueteNum.getCliente(), "Cliente no se asignó correctamente");
	        assertSame(localidad, tiqueteNum.getLocalidad(), "Localidad no se asignó correctamente");

	        // Campo propio de TiqueteVendidoNumerado
	        assertEquals(42, tiqueteNum.getNumero_asiento(), "Número de asiento incorrecto");
	    }
	    // Setter específico de numero_asiento
	    @Test
	    void testSetNumeroAsiento_ModificaValor() {
	        tiqueteNum.setNumero_asiento(99);
	        assertEquals(99, tiqueteNum.getNumero_asiento(), "No se actualizó el número de asiento correctamente");
	    }
	}
