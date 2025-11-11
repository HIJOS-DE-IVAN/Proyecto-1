package test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.Evento;
import boletamaster.MultiEvento;
import boletamaster.TiqueteMultipleDeluxe;
import boletamaster.TiqueteMultipleTemporada;
public class multiEventoTest {
	private MultiEvento multi;

    @BeforeEach
    void setUp() {
        multi = new MultiEvento();
    }

    @AfterEach
    void tearDown() {
        multi = null;
    }

    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------
    @Test
    void testConstructor_InicializaListasNoNulasYVacias() {
        assertNotNull(multi.getTemporadas(), "La lista de temporadas no debe ser null");
        assertNotNull(multi.getDeluxes(),    "La lista de deluxes no debe ser null");
        assertNotNull(multi.getEventos(),    "La lista de eventos no debe ser null");

        assertTrue(multi.getTemporadas().isEmpty(), "Temporadas debe iniciar vacía");
        assertTrue(multi.getDeluxes().isEmpty(),    "Deluxes debe iniciar vacía");
        assertTrue(multi.getEventos().isEmpty(),    "Eventos debe iniciar vacía");
    }

 
    // Setters: asignación por referencia
    
    @Test
    void testSetEventos_AsignaReferencia() {
        List<Evento> eventos = new ArrayList<>();
        eventos.add(new Evento("E-1", new Date(0L), "Tipo", "OK"));

        multi.setEventos(eventos);

        assertSame(eventos, multi.getEventos(), "setEventos debe asignar la referencia recibida");
        assertEquals(1, multi.getEventos().size(), "Tamaño inesperado tras setEventos");
    }

    @Test
    void testSetTemporadas_AsignaReferencia() {
        List<TiqueteMultipleTemporada> temporadas = new ArrayList<>();
        temporadas.add(new TiqueteMultipleTemporada(0, 0, 0, null, 0, "T-1", false, multi));

        multi.setTemporadas(temporadas);

        assertSame(temporadas, multi.getTemporadas(), "setTemporadas debe asignar la referencia recibida");
        assertEquals(1, multi.getTemporadas().size(), "Tamaño inesperado tras setTemporadas");
    }

    @Test
    void testSetDeluxes_AsignaReferencia() {
        List<TiqueteMultipleDeluxe> deluxes = new ArrayList<>();
        deluxes.add(new TiqueteMultipleDeluxe(0, 0, 0, null, 0, "D-1", null, false, multi));

        multi.setDeluxes(deluxes);

        assertSame(deluxes, multi.getDeluxes(), "setDeluxes debe asignar la referencia recibida");
        assertEquals(1, multi.getDeluxes().size(), "Tamaño inesperado tras setDeluxes");
    }

    // ------------------------------------------------------------
    // Mutación externa: documenta que no hay copia defensiva
    // ------------------------------------------------------------
    @Test
    void testMutacionExterna_Eventos_SeReflejaInternamente() {
        List<Evento> ref = new ArrayList<>();
        multi.setEventos(ref);
        assertEquals(0, multi.getEventos().size(), "Debe iniciar vacía");

        ref.add(new Evento("E-2", new Date(1L), "Tipo", "OK"));
        assertEquals(1, multi.getEventos().size(), "La mutación externa debe reflejarse internamente");
    }

    @Test
    void testMutacionExterna_Temporadas_SeReflejaInternamente() {
        List<TiqueteMultipleTemporada> ref = new ArrayList<>();
        multi.setTemporadas(ref);
        assertEquals(0, multi.getTemporadas().size(), "Debe iniciar vacía");

        ref.add(new TiqueteMultipleTemporada("T-2"));
        assertEquals(1, multi.getTemporadas().size(), "La mutación externa debe reflejarse internamente");
    }

    @Test
    void testMutacionExterna_Deluxes_SeReflejaInternamente() {
        List<TiqueteMultipleDeluxe> ref = new ArrayList<>();
        multi.setDeluxes(ref);
        assertEquals(0, multi.getDeluxes().size(), "Debe iniciar vacía");

        ref.add(new TiqueteMultipleDeluxe(0, 0, 0, null, 0, "D-2", null, false, multi));
        assertEquals(1, multi.getDeluxes().size(), "La mutación externa debe reflejarse internamente");
    }

    // ------------------------------------------------------------
    // Nulls (robustez mínima, acorde a tu implementación actual)
    // ------------------------------------------------------------
    @Test
    void testSettersPermitenNull_SegunImplementacionActual() {
        multi.setEventos(null);
        multi.setTemporadas(null);
        multi.setDeluxes(null);

        assertNull(multi.getEventos(),    "getEventos puede quedar null tras setEventos(null)");
        assertNull(multi.getTemporadas(), "getTemporadas puede quedar null tras setTemporadas(null)");
        assertNull(multi.getDeluxes(),    "getDeluxes puede quedar null tras setDeluxes(null)");
    }
}



