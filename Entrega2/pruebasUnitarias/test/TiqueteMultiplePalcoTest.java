package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.Tiquete;
import boletamaster.TiqueteMultiplesPalco;
public class TiqueteMultiplePalcoTest {


	    private TiqueteMultiplesPalco paquete;

	    @BeforeEach
	    void setUp() {
	        paquete = new TiqueteMultiplesPalco(5, 250_000.0);
	    }

	    @AfterEach
	    void tearDown() {
	        paquete = null;
	    }
	    // Constructor: inicializa campos y lista
	    @Test
	    void testConstructor_InicializaCamposYLista() {
	        assertEquals(5, paquete.getCantidad(), "Cantidad inicial incorrecta");
	        assertEquals(250_000.0, paquete.getPrecio(), 1e-9, "Precio inicial incorrecto");

	        assertNotNull(paquete.getTiquetes(), "La lista de tiquetes no debe ser null");
	        assertTrue(paquete.getTiquetes().isEmpty(), "La lista de tiquetes debe iniciar vacía");
	    }
	    // Setters básicos
	    @Test
	    void testSetCantidad_ModificaValor() {
	        paquete.setCantidad(10);
	        assertEquals(10, paquete.getCantidad(), "No se actualizó la cantidad correctamente");
	    }

	    @Test
	    void testSetPrecio_ModificaValor() {
	        paquete.setPrecio(300_000.0);
	        assertEquals(300_000.0, paquete.getPrecio(), 1e-9, "No se actualizó el precio correctamente");
	    }
	    // Lista de tiquetes: referencia y mutación externa
	    @Test
	    void testSetTiquetes_AsignaReferencia() {
	        ArrayList<Tiquete> lista = new ArrayList<>();
	        paquete.setTiquetes(lista);

	        assertSame(lista, paquete.getTiquetes(),
	                "setTiquetes debe asignar la referencia de la lista recibida");
	        assertEquals(0, paquete.getTiquetes().size(), "La lista recién asignada debería estar vacía");
	    }

	    @Test
	    void testMutacionExternaEnLista_SeReflejaInternamente() {
	        ArrayList<Tiquete> ref = new ArrayList<>();
	        paquete.setTiquetes(ref);
	        assertEquals(0, paquete.getTiquetes().size(), "Debe iniciar vacía");

	        // No necesitamos instancias reales de Tiquete para probar la lista; usamos null
	        ref.add(null);
	        assertEquals(1, paquete.getTiquetes().size(),
	                "La mutación externa (add) debe reflejarse internamente (no hay copia defensiva)");
	        assertNull(paquete.getTiquetes().get(0),
	                "El elemento agregado (null) debe verse igual desde getTiquetes()");
	    }

	    @Test
	    void testSetTiquetesNull_PermitidoSegunImplementacionActual() {
	        paquete.setTiquetes(null);
	        assertNull(paquete.getTiquetes(),
	                "Según el código actual, la lista puede quedar null tras setTiquetes(null)");
	    }
	}

