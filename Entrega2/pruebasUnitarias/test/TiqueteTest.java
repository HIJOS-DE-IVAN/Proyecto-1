package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.Cliente;
import boletamaster.Localidad;
import boletamaster.Tiquete;
import boletamaster.TiqueteMultiplesPalco;

public class TiqueteTest {
	    private Tiquete tiquete;
	    private Cliente cliente;
	    private Localidad localidad;
	    private Date fechaBase;

	    @BeforeEach
	    void setUp() {
	        fechaBase = new Date(1_700_000_000_000L);
	        cliente   = new Cliente("user1", "pwd", 100_000.0);
	        localidad = new Localidad("LOC-1", "Platea", true, 50_000.0, 100);

	        tiquete = new Tiquete(
	            0.15,           // sobrecargo_porcentual_servicio
	            2_000.0,        // cuota_fija
	            "T-001",        // identificador
	            fechaBase,      // fecha
	            20,             // hora
	            localidad,      // localidad
	            60_000.0,       // precio
	            true,           // esTransferible
	            false,          // estaVendido
	            cliente         // cliente
	        );
	    }

	    @AfterEach
	    void tearDown() {
	        tiquete = null;
	        cliente = null;
	        localidad = null;
	        fechaBase = null;
	    }

	   
	    // Constructor: inicializa correctamente los campos
	
	    @Test
	    void testConstructor_InicializaCamposCorrectamente() {
	        assertEquals(0.15, tiquete.getSobrecargo_porcentual_servicio(), 1e-9, "Sobrecargo incorrecto");
	        assertEquals(2_000.0, tiquete.getCuota_fija(), 1e-9, "Cuota fija incorrecta");
	        assertEquals("T-001", tiquete.getIdentificador(), "Identificador incorrecto");
	        assertEquals(fechaBase, tiquete.getFecha(), "Fecha incorrecta");
	        assertEquals(20, tiquete.getHora(), "Hora incorrecta");
	        assertEquals(60_000.0, tiquete.getPrecio(), 1e-9, "Precio incorrecto");
	        assertTrue(tiquete.isEsTransferible(), "esTransferible debería ser true");
	        assertFalse(tiquete.isEstaVendido(), "estaVendido debería ser false");
	        assertSame(cliente, tiquete.getCliente(), "Cliente no se asignó correctamente");
	        assertSame(localidad, tiquete.getLocalidad(), "Localidad no se asignó correctamente");

	        assertNotNull(tiquete.getTiquetesMultiplesPalco(), "La lista de tiquetesMultiplesPalco no debe ser null");
	        assertTrue(tiquete.getTiquetesMultiplesPalco().isEmpty(), "La lista de tiquetesMultiplesPalco debe iniciar vacía");
	    }

	    // Setters de valores numéricos y simples

	    @Test
	    void testSetSobrecargo_CambiaValor() {
	        tiquete.setSobrecargo_porcentual_servicio(0.25);
	        assertEquals(0.25, tiquete.getSobrecargo_porcentual_servicio(), 1e-9);
	    }

	    @Test
	    void testSetCuotaFija_CambiaValor() {
	        tiquete.setCuota_fija(3_500.0);
	        assertEquals(3_500.0, tiquete.getCuota_fija(), 1e-9);
	    }

	    @Test
	    void testSetIdentificador_CambiaValor() {
	        tiquete.setIdentificador("T-XYZ");
	        assertEquals("T-XYZ", tiquete.getIdentificador());
	    }

	    @Test
	    void testSetFecha_CambiaValor() {
	        Date nueva = new Date(1_800_000_000_000L);
	        tiquete.setFecha(nueva);
	        assertEquals(nueva, tiquete.getFecha());
	    }

	    @Test
	    void testSetHora_CambiaValor() {
	        tiquete.setHora(14);
	        assertEquals(14, tiquete.getHora());
	    }

	    @Test
	    void testSetPrecio_CambiaValor() {
	        tiquete.setPrecio(45_000.0);
	        assertEquals(45_000.0, tiquete.getPrecio(), 1e-9);
	    }

	    @Test
	    void testSetEsTransferible_CambiaValor() {
	        tiquete.setEsTransferible(false);
	        assertFalse(tiquete.isEsTransferible());
	    }

	    @Test
	    void testSetEstaVendido_CambiaValor() {
	        tiquete.setEstaVendido(true);
	        assertTrue(tiquete.isEstaVendido());
	    }

	    // Cliente y Localidad (referencias)

	    @Test
	    void testSetCliente_AsignaReferencia() {
	        Cliente nuevo = new Cliente("otro", "pwd2", 50_000.0);
	        tiquete.setCliente(nuevo);
	        assertSame(nuevo, tiquete.getCliente(), "setCliente debe asignar la referencia recibida");
	    }

	    @Test
	    void testSetLocalidad_AsignaReferencia() {
	        Localidad nuevaLoc = new Localidad("LOC-2", "VIP", false, 120_000.0, 10);
	        tiquete.setLocalidad(nuevaLoc);
	        assertSame(nuevaLoc, tiquete.getLocalidad(), "setLocalidad debe asignar la referencia recibida");
	    }

	    @Test
	    void testSetClienteNull_YSetLocalidadNull_Permitidos() {
	        tiquete.setCliente(null);
	        tiquete.setLocalidad(null);

	        assertNull(tiquete.getCliente(), "Debe permitir cliente null según implementación actual");
	        assertNull(tiquete.getLocalidad(), "Debe permitir localidad null según implementación actual");
	    }


	    // Lista tiquetesMultiplesPalco: referencia y mutación externa

	    @Test
	    void testSetTiquetesMultiplesPalco_AsignaReferencia() {
	        ArrayList<TiqueteMultiplesPalco> lista = new ArrayList<>();
	        tiquete.setTiquetesMultiplesPalco(lista);

	        assertSame(lista, tiquete.getTiquetesMultiplesPalco(),
	                "setTiquetesMultiplesPalco debe asignar la referencia recibida");
	        assertEquals(0, tiquete.getTiquetesMultiplesPalco().size());
	    }

	    @Test
	    void testMutacionExternaEnTiquetesMultiplesPalco_SeReflejaInternamente() {
	        ArrayList<TiqueteMultiplesPalco> ref = new ArrayList<>();
	        tiquete.setTiquetesMultiplesPalco(ref);
	        assertEquals(0, tiquete.getTiquetesMultiplesPalco().size());

	        // No necesitamos una instancia real de TiqueteMultiplesPalco, podemos usar null
	        ref.add(null);
	        assertEquals(1, tiquete.getTiquetesMultiplesPalco().size(),
	                "La mutación externa debe reflejarse internamente (no hay copia defensiva)");
	        assertNull(tiquete.getTiquetesMultiplesPalco().get(0));
	    }

	    @Test
	    void testSetTiquetesMultiplesPalcoNull_Permitido() {
	        tiquete.setTiquetesMultiplesPalco(null);
	        assertNull(tiquete.getTiquetesMultiplesPalco(),
	                "Según el código actual, la lista puede quedar null tras set(null)");
	    }
	}



