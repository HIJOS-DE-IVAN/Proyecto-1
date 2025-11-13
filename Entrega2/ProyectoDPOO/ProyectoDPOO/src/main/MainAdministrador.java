package main;

import boletamaster.*;
import java.util.*;

public class MainAdministrador {

    private static Administrador admin;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== BoletaMaster - Modo Administrador ===");
        autenticar();
        System.out.println("Vamos a simular los datos de un Administrador");
     // ======== DATOS INICIALES DE PRUEBA ========
        admin = new Administrador("admin1", "1234");

        // Crear organizador
        OrganizadorDeEventos org = new OrganizadorDeEventos("organizador1", "abcd");
        org.admin = admin;

        // Crear cliente
        Cliente c1 = new Cliente("cliente1", "pass", 500000.0);
        c1.setAdmin(admin);

        // Crear venue
        Venue v1 = new Venue("VEN001", "Coliseo Mayor", "Bogotá", 1000, "Sin restricciones", true);

        // Crear localidad
        Localidad loc1 = new Localidad("VIP", "VIP", false, 200000, 100);
        v1.getLocalidades().add(loc1);

        // Crear evento
        Evento e1 = new Evento("EVT001", new java.util.Date(), "Concierto", "Programado");
        e1.getVenues().add(v1);

        // Crear tiquetes
        ArrayList<Tiquete> tiqs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tiquete t = new Tiquete(
                0.1,                // sobrecargo
                5000.0,             // cuota fija
                "TQ-" + i,
                new java.util.Date(),
                18,
                loc1,
                205000.0,
                true,
                false,
                null
            );
            tiqs.add(t);
        }
        loc1.setTiquetes(tiqs);

        // Asociar todo con el admin
        HashMap<String, Evento> mapaEventos = new HashMap<>();
        mapaEventos.put(e1.getId(), e1);
        admin.setMapa_eventos(mapaEventos);

        HashMap<String, Venue> mapaVenues = new HashMap<>();
        mapaVenues.put(v1.getNombre(), v1);
        admin.setMapa_venues(mapaVenues);

        HashMap<String, Cliente> mapaClientes = new HashMap<>();
        mapaClientes.put(c1.getLogin(), c1);
        admin.mapa_clientes = mapaClientes;

        HashMap<String, OrganizadorDeEventos> mapaPromotores = new HashMap<>();
        mapaPromotores.put(org.getLogin(), org);
        admin.setPromotor_inscritos_marketplace(mapaPromotores);

        System.out.println("Datos iniciales cargados con éxito.\n");
        // ===========================================

        mostrarMenu();
    }

    private static void autenticar() {
        System.out.print("Usuario: ");
        String usuario = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();
        admin = new Administrador(usuario, pass);
        System.out.println("Inicio de sesión exitoso ✅");
    }

    private static void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENÚ ADMINISTRADOR ---");
            System.out.println("1. Ver log de operaciones");
            System.out.println("2. Consultar solicitudes de cancelación");
            System.out.println("3. Consultar promotores inscritos");
            System.out.println("4. Aprobar solicitud de promotor");
            System.out.println("5. Consultar clientes inscritos al marketplace");
            System.out.println("6. Eliminar operación");
            System.out.println("7. Mostrar todos los eventos");
            System.out.println("8. Mostrar todos los venues");
            System.out.println("9. Mostrar todos los clientes");
            System.out.println("10. Mostrar promotores y sus eventos");
            System.out.println("11. Mostrar eventos cancelados");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> mostrarLog();
                case 2 -> mostrarSolicitudes();
                case 3 -> mostrarPromotores();
                case 4 -> aprobarPromotor();
                case 5 -> mostrarClientesMarketplace();
                case 6 -> eliminarOperacion();
                case 7 -> mostrarEventos();
                case 8 -> mostrarVenues();
                case 9 -> mostrarClientes();
                case 10 -> mostrarPromotoresYEventos();
                case 11 -> mostrarEventosCancelados();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void mostrarLog() {
        if (admin.getLog_registros().isEmpty()) {
            System.out.println("No hay operaciones registradas.");
        } else {
            admin.getLog_registros().forEach((k, v) -> System.out.println(v));
        }
    }

    private static void mostrarSolicitudes() {
        if (admin.getMapa_solicitudes().isEmpty()) {
            System.out.println("No hay solicitudes.");
        } else {
            admin.getMapa_solicitudes().forEach((k, v) -> System.out.println("Evento: " + k + " | Motivo: " + v));
        }
    }

    private static void mostrarPromotores() {
        if (admin.getPromotor_inscritos_marketplace().isEmpty()) {
            System.out.println("No hay promotores.");
        } else {
            admin.getPromotor_inscritos_marketplace().forEach((k, v) -> System.out.println(k));
        }
    }

    private static void aprobarPromotor() {
        System.out.println("Solicitudes pendientes:");
        admin.getSolicitudes_promotor_marketplace().forEach((k, v) -> System.out.println(k));
        System.out.print("Ingrese login del promotor a aprobar: ");
        String login = sc.nextLine();
        admin.aceptar_promotor(login);
        System.out.println("Promotor aprobado ✅");
    }

    private static void mostrarClientesMarketplace() {
        admin.getClientes_inscritos_marketplace().forEach((k, v) -> System.out.println(k));
    }

    private static void eliminarOperacion() {
        System.out.print("Ingrese ID de la operación a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        admin.eliminar_operacion(id);
        System.out.println("Operación eliminada ✅");
    }
 // === NUEVAS FUNCIONES ===

 // Mostrar todos los eventos registrados en el sistema
 private static void mostrarEventos() {
     if (admin.getMapa_eventos() == null || admin.getMapa_eventos().isEmpty()) {
         System.out.println("No hay eventos registrados.");
         return;
     }

     System.out.println("\n--- LISTADO DE EVENTOS ---");
     admin.getMapa_eventos().forEach((nombre, evento) -> {
         System.out.println("ID: " + evento.getId());
         System.out.println("Tipo: " + evento.getTipoEvento());
         System.out.println("Estado: " + evento.getEstadoEvento());
         System.out.println("Fecha: " + evento.getFecha());
         System.out.println("Venues asociados: " + (evento.getVenues() != null ? evento.getVenues().size() : 0));
         System.out.println("-----------------------------");
     });
 }

 // Mostrar todos los venues registrados
 private static void mostrarVenues() {
     if (admin.getMapa_venues() == null || admin.getMapa_venues().isEmpty()) {
         System.out.println("No hay venues registrados.");
         return;
     }

     System.out.println("\n--- LISTADO DE VENUES ---");
     admin.getMapa_venues().forEach((nombre, venue) -> {
         System.out.println("Nombre: " + venue.getNombre());
         System.out.println("Ubicación: " + venue.getUbicacion());
         System.out.println("Capacidad: " + venue.getCapacidadMax());
         System.out.println("Aprobado: " + venue.isAprobado());
         System.out.println("-----------------------------");
     });
 }

 // Mostrar todos los clientes registrados
 private static void mostrarClientes() {
     if (admin.mapa_clientes == null || admin.mapa_clientes.isEmpty()) {
         System.out.println("No hay clientes registrados.");
         return;
     }

     System.out.println("\n--- CLIENTES REGISTRADOS ---");
     admin.mapa_clientes.forEach((login, cliente) -> {
         System.out.println("Usuario: " + cliente.getLogin());
         System.out.println("Saldo en plataforma: $" + cliente.getSaldoPlataforma());
         System.out.println("Tiquetes adquiridos: " + cliente.getTiquetes().size());
         System.out.println("-----------------------------");
     });
 }

 // Mostrar promotores inscritos y sus eventos
 private static void mostrarPromotoresYEventos() {
     if (admin.getPromotor_inscritos_marketplace() == null || admin.getPromotor_inscritos_marketplace().isEmpty()) {
         System.out.println("No hay promotores inscritos en el marketplace.");
         return;
     }

     System.out.println("\n--- PROMOTORES INSCRITOS ---");
     admin.getPromotor_inscritos_marketplace().forEach((login, org) -> {
         System.out.println("Promotor: " + org.getLogin());
         System.out.println("Eventos a cargo: " + org.getEventos().size());
         org.getEventos().forEach((id, ev) -> {
             System.out.println("   - " + ev.getTipoEvento() + " (" + ev.getEstadoEvento() + ")");
         });
         System.out.println("-----------------------------");
     });
 }

 // Mostrar eventos cancelados
 private static void mostrarEventosCancelados() {
     if (admin.getMapa_eventos() == null || admin.getMapa_eventos().isEmpty()) {
         System.out.println("No hay eventos registrados.");
         return;
     }

     System.out.println("\n--- EVENTOS CANCELADOS ---");
     boolean alguno = false;
     for (Evento e : admin.getMapa_eventos().values()) {
         if (Administrador.CANCELADO.equals(e.getEstadoEvento())) {
             System.out.println("ID: " + e.getId() + " | Tipo: " + e.getTipoEvento());
             alguno = true;
         }
     }
     if (!alguno) System.out.println("Ningún evento ha sido cancelado.");
 }

}
