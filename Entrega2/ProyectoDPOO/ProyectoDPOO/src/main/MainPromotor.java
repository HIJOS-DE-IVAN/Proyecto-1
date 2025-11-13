package main;

import boletamaster.*;
import java.util.*;

public class MainPromotor {

    private static OrganizadorDeEventos promotor;
    private static Administrador admin;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== BoletaMaster - Modo Promotor ===");
        autenticar();

        // ======== DATOS DE PRUEBA ========
        admin = new Administrador("admin1", "1234");
        promotor.admin = admin;

        Venue v1 = new Venue("VEN001", "Teatro Nacional", "Bogotá", 1200, "Sin pirotecnia", true);
        admin.setMapa_venues(new HashMap<>());
        admin.getMapa_venues().put(v1.getNombre(), v1);

        Evento e1 = new Evento("EVT001", new java.util.Date(), "Obra teatral", "Programado");
        e1.getVenues().add(v1);
        promotor.getEventos().put(e1.getId(), e1);
        admin.setMapa_eventos(new HashMap<>());
        admin.getMapa_eventos().put(e1.getId(), e1);

        System.out.println("Datos iniciales cargados correctamente.\n");
        mostrarMenu();
    }

    private static void autenticar() {
        System.out.print("Usuario: ");
        String usuario = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();
        promotor = new OrganizadorDeEventos(usuario, pass);
        System.out.println("Inicio de sesión exitoso ✅");
    }

    private static void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENÚ PROMOTOR ---");
            System.out.println("1. Crear evento");
            System.out.println("2. Crear localidad en venue");
            System.out.println("3. Crear oferta de localidad");
            System.out.println("4. Sugerir nuevo venue");
            System.out.println("5. Sugerir cancelación de evento");
            System.out.println("6. Solicitar inscripción al marketplace");
            System.out.println("7. Mostrar mis eventos");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> crearEvento();
                case 2 -> crearLocalidadVenue();
                case 3 -> crearOfertaLocalidad();
                case 4 -> sugerirVenue();
                case 5 -> sugerirCancelarEvento();
                case 6 -> solicitarInscripcionMarketplace();
                case 7 -> mostrarEventos();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void crearEvento() {
        System.out.print("ID del evento: ");
        String id = sc.nextLine();
        System.out.print("Nombre del venue: ");
        String venue = sc.nextLine();
        System.out.print("Tipo de evento: ");
        String tipo = sc.nextLine();

        promotor.crear_evento(id, new java.util.Date(), tipo, "Programado", venue);
        System.out.println("Evento creado ✅");
    }

    private static void crearLocalidadVenue() {
        System.out.print("Nombre del venue: ");
        String venue = sc.nextLine();
        System.out.print("Nombre de la localidad: ");
        String loc = sc.nextLine();
        System.out.print("Precio base: ");
        double precio = Double.parseDouble(sc.nextLine());
        System.out.print("Capacidad: ");
        int cap = Integer.parseInt(sc.nextLine());

        promotor.crear_localidad_venue(venue, loc, false, precio, cap);
        System.out.println("Localidad creada ✅");
    }

    private static void crearOfertaLocalidad() {
        System.out.print("ID del evento: ");
        String evento = sc.nextLine();
        System.out.print("Nombre de la localidad: ");
        String loc = sc.nextLine();
        System.out.print("Porcentaje de descuento (0.1 = 10%): ");
        double descuento = Double.parseDouble(sc.nextLine());

        promotor.crear_oferta_localidad(evento, loc, descuento);
        System.out.println("Oferta aplicada ✅");
    }

    private static void sugerirVenue() {
        System.out.print("Nombre del nuevo venue: ");
        String nombre = sc.nextLine();
        System.out.print("Ubicación: ");
        String ubicacion = sc.nextLine();
        System.out.print("Capacidad máxima: ");
        int cap = Integer.parseInt(sc.nextLine());
        System.out.print("Restricciones: ");
        String restr = sc.nextLine();

        promotor.sugerir_venue(nombre, ubicacion, cap, restr);
        System.out.println("Venue sugerido ✅");
    }

    private static void sugerirCancelarEvento() {
        System.out.print("ID del evento a cancelar: ");
        String id = sc.nextLine();
        System.out.print("Motivo: ");
        String motivo = sc.nextLine();

        promotor.sugerir_cancelar_evento(id, motivo);
        System.out.println("Solicitud enviada ✅");
    }

    private static void solicitarInscripcionMarketplace() {
        promotor.solicitud_inscripcion_marketplace();
        System.out.println("Solicitud enviada al administrador ✅");
    }

    private static void mostrarEventos() {
        if (promotor.getEventos().isEmpty()) {
            System.out.println("No hay eventos registrados.");
        } else {
            System.out.println("\n--- MIS EVENTOS ---");
            promotor.getEventos().forEach((k, v) -> {
                System.out.println("ID: " + v.getId());
                System.out.println("Tipo: " + v.getTipoEvento());
                System.out.println("Estado: " + v.getEstadoEvento());
                System.out.println("---------------------");
            });
        }
    }
}
