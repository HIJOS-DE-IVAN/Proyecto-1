package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.Evento;
import boletamaster.Localidad;
import boletamaster.Venue;

public class VenueTest {

	    private Venue venue;

	    @BeforeEach
	    void setUp() {
	        venue = new Venue(
	            "V-001",                  // id
	            "Movistar Arena",         // nombre
	            "Bogotá",                 // ubicacion
	            15000,                    // capacidadMax
	            "No pirotecnia",          // restriccionesUso
	            false                     // aprobado
	        );
	    }

	    @AfterEach
	    void tearDown() {
	        venue = null;
	    }
	    // Constructor: inicialización de campos y listas
	    @Test
	    void testConstructor_InicializaCamposYListas() {
	        assertEquals("V-001", venue.getId(), "Id incorrecto");
	        assertEquals("Movistar Arena", venue.getNombre(), "Nombre incorrecto");
	        assertEquals("Bogotá", venue.getUbicacion(), "Ubicación incorrecta");
	        assertEquals(15000, venue.getCapacidadMax(), "Capacidad máxima incorrecta");
	        assertEquals("No pirotecnia", venue.getRestriccionesUso(), "Restricciones de uso incorrectas");
	        assertFalse(venue.isAprobado(), "Aprobado debería ser false inicialmente");

	        assertNotNull(venue.getEvento(), "La lista de eventos no debe ser null");
	        assertTrue(venue.getEvento().isEmpty(), "La lista de eventos debe iniciar vacía");

	        assertNotNull(venue.getLocalidades(), "La lista de localidades no debe ser null");
	        assertTrue(venue.getLocalidades().isEmpty(), "La lista de localidades debe iniciar vacía");
	    }
	    // Setters y getters de campos simples
	    @Test
	    void testSetId_ModificaValor() {
	        venue.setId("V-XYZ");
	        assertEquals("V-XYZ", venue.getId(), "No se actualizó el id correctamente");
	    }

	    @Test
	    void testSetNombre_ModificaValor() {
	        venue.setNombre("Coliseo Live");
	        assertEquals("Coliseo Live", venue.getNombre(), "No se actualizó el nombre correctamente");
	    }

	    @Test
	    void testSetUbicacion_ModificaValor() {
	        venue.setUbicacion("Chía");
	        assertEquals("Chía", venue.getUbicacion(), "No se actualizó la ubicación correctamente");
	    }

	    @Test
	    void testSetCapacidadMax_ModificaValor() {
	        venue.setCapacidadMax(20000);
	        assertEquals(20000, venue.getCapacidadMax(), "No se actualizó la capacidad máxima correctamente");
	    }

	    @Test
	    void testSetRestriccionesUso_ModificaValor() {
	        venue.setRestriccionesUso("Prohibido comida");
	        assertEquals("Prohibido comida", venue.getRestriccionesUso(), "No se actualizaron las restricciones de uso");
	    }

	    @Test
	    void testSetAprobado_ModificaValor() {
	        venue.setAprobado(true);
	        assertTrue(venue.isAprobado(), "No se actualizó el estado aprobado correctamente");
	    }
	    // Listas: eventos y localidades (referencias + mutación externa)
	    @Test
	    void testSetEvento_AsignaReferenciaLista() {
	        ArrayList<Evento> listaEventos = new ArrayList<>();
	        venue.setEvento(listaEventos);

	        assertSame(listaEventos, venue.getEvento(),
	                "setEvento debe asignar la referencia de la lista recibida");
	        assertEquals(0, venue.getEvento().size(), "La lista recién asignada debería estar vacía");
	    }

	    @Test
	    void testMutacionExternaEnListaEventos_SeReflejaInternamente() {
	        ArrayList<Evento> refEventos = new ArrayList<>();
	        venue.setEvento(refEventos);
	        assertEquals(0, venue.getEvento().size());

	        // No necesitamos instancias reales de Evento para probar la lista; podemos usar null
	        refEventos.add(null);
	        assertEquals(1, venue.getEvento().size(),
	                "La mutación externa (add) debe reflejarse internamente (no hay copia defensiva)");
	        assertNull(venue.getEvento().get(0));
	    }

	    @Test
	    void testSetLocalidades_AsignaReferenciaLista() {
	        ArrayList<Localidad> listaLocs = new ArrayList<>();
	        venue.setLocalidades(listaLocs);

	        assertSame(listaLocs, venue.getLocalidades(),
	                "setLocalidades debe asignar la referencia de la lista recibida");
	        assertEquals(0, venue.getLocalidades().size(), "La lista recién asignada debería estar vacía");
	    }

	    @Test
	    void testMutacionExternaEnListaLocalidades_SeReflejaInternamente() {
	        ArrayList<Localidad> refLocs = new ArrayList<>();
	        venue.setLocalidades(refLocs);
	        assertEquals(0, venue.getLocalidades().size());

	        // De nuevo, podemos usar null; no necesitamos crear Localidad real para esta prueba
	        refLocs.add(null);
	        assertEquals(1, venue.getLocalidades().size(),
	                "La mutación externa (add) debe reflejarse internamente");
	        assertNull(venue.getLocalidades().get(0));
	    }

	    @Test
	    void testSetEventoNullYPuedeQuedarNullSegunImplementacion() {
	        venue.setEvento(null);
	        assertNull(venue.getEvento(), "Según la implementación actual, evento puede quedar null");
	    }

	    @Test
	    void testSetLocalidadesNullYPuedeQuedarNullSegunImplementacion() {
	        venue.setLocalidades(null);
	        assertNull(venue.getLocalidades(), "Según la implementación actual, localidades puede quedar null");
	    }
	}
