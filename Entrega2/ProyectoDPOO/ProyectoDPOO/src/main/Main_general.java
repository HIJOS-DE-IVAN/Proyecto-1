package main;

import boletamaster.*;
import java.util.*;

/**
 * MainGeneral — Punto central de demostración del sistema BoletaMaster.
 * Simula escenarios completos entre Administrador, Promotor y Clientes.
 */
public class Main_general {

    public static void main(String[] args) {

        System.out.println("=== SISTEMA BOLETAMASTER ===\n");
        System.out.println("Inicializando datos del sistema...\n");

        // ======== CREACIÓN DE ACTORES ========
        Administrador admin = new Administrador("admin1", "1234");

        OrganizadorDeEventos promotor = new OrganizadorDeEventos("promotor1", "abcd");
        promotor.admin = admin;

        Cliente clienteA = new Cliente("clienteA", "passA", 800000.0);
        clienteA.setAdmin(admin);
        Cliente clienteB = new Cliente("clienteB", "passB", 700000.0);
        clienteB.setAdmin(admin);
        Cliente clienteC = new Cliente("clienteC", "passC", 900000.0);
        clienteC.setAdmin(admin);

        // Registrar clientes y promotores en el admin
        admin.mapa_clientes = new HashMap<>();
        admin.mapa_clientes.put(clienteA.getLogin(), clienteA);
        admin.mapa_clientes.put(clienteB.getLogin(), clienteB);
        admin.mapa_clientes.put(clienteC.getLogin(), clienteC);

        admin.setPromotor_inscritos_marketplace(new HashMap<>());
        admin.getPromotor_inscritos_marketplace().put(promotor.getLogin(), promotor);

        // ======== PASO 1: PROMOTOR CREA VENUE Y EVENTO ========
        System.out.println("\n--- PASO 1: Promotor crea un venue y un evento ---");


        Venue v1 = new Venue("VEN001", "Coliseo BoletaFest", "Bogotá", 1000, "Sin restricciones", true);
        Localidad loc1 = new Localidad("General", "General", false, 100000, 50);
        v1.getLocalidades().add(loc1);
        Evento e1 = new Evento("EVT001", new java.util.Date(), "Concierto BoletaFest", "Programado");
        e1.getVenues().add(v1);

        ArrayList<Tiquete> tiqs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tiquete t = new Tiquete(0.1, 2000, "TQ-" + i, new java.util.Date(), 18, loc1, 120000, true, false, null);
            tiqs.add(t);
        }
        loc1.setTiquetes(tiqs);

        HashMap<String, Evento> mapaEventos = new HashMap<>();
        mapaEventos.put(e1.getId(), e1);
        admin.setMapa_eventos(mapaEventos);

        HashMap<String, Venue> mapaVenues = new HashMap<>();
        mapaVenues.put(v1.getNombre(), v1);
        admin.setMapa_venues(mapaVenues);

        clienteA.setMapa_eventos(mapaEventos);
        clienteB.setMapa_eventos(mapaEventos);
        clienteC.setMapa_eventos(mapaEventos);
        
        promotor.crear_localidad_venue("VEN001", "General", false, 100000, 100);
        promotor.crear_evento("EVT001", new java.util.Date(), "Concierto BoletaFest", "Programado", "VEN001");
        System.out.println("Evento y venue creados con éxito ✅");

        // ======== PASO 2: CLIENTE A COMPRA TIQUETE ========
        System.out.println("\n--- PASO 2: Cliente A compra un tiquete ---");
        loc1.setTiquetes(tiqs);
        try {
            clienteA.comprar_tiquete_no_numerado("EVT001", "General");
            System.out.println("Cliente A compró un tiquete ✅");
        } catch (Exception e) {
            System.out.println("Error al comprar tiquete: " + e.getMessage());
        }

        // ======== PASO 3: CLIENTE A PUBLICA EN MARKETPLACE ========
        System.out.println("\n--- PASO 3: Cliente A publica su tiquete en el marketplace ---");
        clienteA.inscribirse_marketplace();
        if (!clienteA.getTiquetes().isEmpty()) {
            Tiquete tPublicar = clienteA.getTiquetes().get(0);
            clienteA.publicar_oferta_Tiquete(tPublicar);
            System.out.println("Cliente A publicó el tiquete ✅");
        }

        // ======== PASO 4: CLIENTE B COMPRA DEL MARKETPLACE ========
        System.out.println("\n--- PASO 4: Cliente B compra la publicación ---");
        if (!admin.getLog_registros().isEmpty()) {
            Operacion primera = admin.getLog_registros().values().iterator().next();
            clienteB.comprar_tiquete(primera.getId());
            System.out.println("Cliente B compró el tiquete del marketplace ✅");
        }

        // ======== PASO 5: CLIENTE C HACE UNA CONTRAOFERTA ========
        System.out.println("\n--- PASO 5: Cliente C realiza una contraoferta ---");
        if (!admin.getLog_registros().isEmpty()) {
            Operacion ultima = admin.getLog_registros().values().iterator().next();
            clienteC.contra_ofertar(ultima.getId(), 130000);
            System.out.println("Cliente C envió una contraoferta ✅");
        }

        // ======== PASO 6: CLIENTE A ACEPTA CONTRAOFERTA ========
        System.out.println("\n--- PASO 6: Cliente A acepta la contraoferta ---");
        if (!clienteA.getSolicitudes_market_place().isEmpty()) {
            int idContra = clienteA.getSolicitudes_market_place().values().iterator().next().getId();
            clienteA.aceptar_contra_ofertar(idContra);
            System.out.println("Cliente A aceptó la contraoferta ✅");
        }

        // ======== PASO 7: ADMIN MUESTRA EL LOG COMPLETO ========
        System.out.println("\n--- PASO 7: Administrador revisa el log de operaciones ---");
        if (admin.getLog_registros().isEmpty()) {
            System.out.println("No hay operaciones registradas.");
        } else {
            System.out.println("\n--- LOG DE OPERACIONES ---");
            for (Operacion op : admin.getLog_registros().values()) {
                System.out.println("ID: " + op.getId() +
                        " | Parte1: " + (op.getParte_1() != null ? op.getParte_1().getLogin() : "null") +
                        " | Parte2: " + (op.getParte_2() != null ? op.getParte_2().getLogin() : "null") +
                        " | Activa: " + op.isEsta_activa() +
                        " | Fecha: " + op.getFechaHora());
            }
        }

        // ======== PASO 8: ADMIN CONSULTA EVENTOS Y CLIENTES ========
        System.out.println("\n--- PASO 8: Administrador consulta eventos y clientes ---");
        admin.getMapa_eventos().forEach((k, v) -> {
            System.out.println("Evento: " + v.getTipoEvento() + " | Estado: " + v.getEstadoEvento());
        });

        admin.mapa_clientes.forEach((k, v) -> {
            System.out.println("Cliente: " + v.getLogin() + " | Saldo: $" + v.getSaldoPlataforma());
        });

        System.out.println("\n=== FIN DE LA SIMULACIÓN ===");
    }
}
