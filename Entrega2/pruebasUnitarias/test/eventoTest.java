package test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.Evento;

class eventoTest {

    private Evento evento;
    private Date fechaBase;

    @BeforeEach
    void setUp() {
        fechaBase = new Date(1_731_000_000_000L); // fecha fija para reproducibilidad
        evento = new Evento("E-001", fechaBase, "Concierto", "PROGRAMADO");
    }

    @AfterEach
    void tearDown() {
        evento = null;
        fechaBase = null;
    }


    // Constructor & getters

    @Test
    void testConstructor_InicializaCampos() {
        assertEquals("E-001", evento.getId(), "El id no se inicializó correctamente");
        assertEquals(fechaBase, evento.getFecha(), "La fecha no se inicializó correctamente");
        assertEquals("Concierto", evento.getTipoEvento(), "El tipo no se inicializó correctamente");
        assertEquals("PROGRAMADO", evento.getEstadoEvento(), "El estado no se inicializó correctamente");

        assertNotNull(evento.getVenues(), "La lista de venues debe inicializarse (no null)");
        assertTrue(evento.getVenues().isEmpty(), "La lista de venues debe iniciar vacía");

        assertEquals(0.0, evento.getSobrecargo(), 1e-9, "El sobrecargo inicial debe ser 0.0");
        assertNull(evento.getOrganizador(), "El organizador debe iniciar null");
    }

    // ------------------------------------------------------------
    // Setters simples
    // ------------------------------------------------------------
    @Test
    void testSettersBasicos_ModificanCampos() {
        Date nuevaFecha = new Date(1_800_000_000_000L);
        evento.setId("E-XYZ");
        evento.setFecha(nuevaFecha);
        evento.setTipoEvento("Teatro");
        evento.setEstadoEvento("CANCELADO");

        assertEquals("E-XYZ", evento.getId(), "No se actualizó el id");
        assertEquals(nuevaFecha, evento.getFecha(), "No se actualizó la fecha");
        assertEquals("Teatro", evento.getTipoEvento(), "No se actualizó el tipo");
        assertEquals("CANCELADO", evento.getEstadoEvento(), "No se actualizó el estado");
    }

    @Test
    void testSetSobrecargo() {
        evento.setSobrecargo(0.15);
        assertEquals(0.15, evento.getSobrecargo(), 1e-9, "No se actualizó el sobrecargo");
    }

 
    // Venues (lista): asignación por referencia y mutación externa

    @Test
    void testSetVenues_AsignaReferencia() {
        ArrayList<boletamaster.Venue> nuevaLista = new ArrayList<>(); // no requiere instancias concretas
        evento.setVenues(nuevaLista);
        assertSame(nuevaLista, evento.getVenues(),
                "setVenues debería asignar la referencia de la lista recibida");
        assertEquals(0, evento.getVenues().size(), "La lista debería quedar vacía tras asignación");
    }

    @Test
    void testMutacionExterna_SeReflejaInternamente() {
        ArrayList<boletamaster.Venue> ref = new ArrayList<>();
        evento.setVenues(ref);
        assertEquals(0, evento.getVenues().size(), "Debe iniciar vacía");

        // Agregamos un null para no depender del constructor de Venue
        ref.add(null);
        assertEquals(1, evento.getVenues().size(),
                "La mutación externa debe reflejarse internamente (no hay copia defensiva)");
        assertNull(evento.getVenues().get(0), "El elemento agregado (null) debe reflejarse tal cual");
    }


    // Organizador (solo null por no asumir constructor)

    @Test
    void testSetOrganizador_NullPermitido() {
        evento.setOrganizador(null);
        assertNull(evento.getOrganizador(), "El organizador debería poder quedar null");
    }


    // Robustez mínima con null en venues (según implementación actual)

    @Test
    void testSetVenues_Null_AsignacionPermisible() {
        evento.setVenues(null);
        assertNull(evento.getVenues(),
                "Según la implementación actual, getVenues puede quedar null tras setVenues(null)");
    }
}
