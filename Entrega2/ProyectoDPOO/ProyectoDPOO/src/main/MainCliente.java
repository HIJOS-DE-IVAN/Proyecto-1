package main;

import boletamaster.*;
import java.util.*;

public class MainCliente {

    private static Cliente cliente;
    private static Administrador admin;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== BoletaMaster - Modo Cliente ===");
        autenticar();

        // ======== DATOS DE PRUEBA ========
        admin = new Administrador("admin1", "1234");

        // Venue y Localidad
        Venue v1 = new Venue("VEN001", "Coliseo Central", "Bogotá", 8000, "Sin restricciones", true);
        Localidad loc1 = new Localidad("General", "General", false, 100000, 50);
        v1.getLocalidades().add(loc1);

        // Evento
        Evento e1 = new Evento("EVT001", new java.util.Date(), "Concierto Rock", "Programado");
        e1.getVenues().add(v1);

        // Tiquetes
        ArrayList<Tiquete> tiqs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Tiquete t = new Tiquete(0.1, 2000, "TQ-" + i, new java.util.Date(), 18, loc1, 120000, true, false, null);
            tiqs.add(t);
        }
        loc1.setTiquetes(tiqs);

        HashMap<String, Evento> mapaEventos = new HashMap<>();
        mapaEventos.put(e1.getId(), e1);
        admin.setMapa_eventos(mapaEventos);
        cliente.setMapa_eventos(mapaEventos);

        cliente.setAdmin(admin);
        cliente.getTiquetes().add(tiqs.get(0));

        HashMap<String, Cliente> mapaClientes = new HashMap<>();
        mapaClientes.put(cliente.getLogin(), cliente);
        admin.mapa_clientes = mapaClientes;
        // ==================================

        System.out.println("Datos de prueba cargados correctamente.\n");

        mostrarMenu();
    }

    private static void autenticar() {
        System.out.print("Usuario: ");
        String usuario = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();
        cliente = new Cliente(usuario, pass, 500000.0);
        System.out.println("Inicio de sesión exitoso ✅");
    }

    private static void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENÚ CLIENTE ---");
            System.out.println("1. Ver saldo disponible");
            System.out.println("2. Comprar tiquete no numerado");
            System.out.println("3. Ver mis tiquetes");
            System.out.println("4. Solicitar reembolso");
            System.out.println("5. Inscribirse al marketplace");
            System.out.println("6. Publicar tiquete en marketplace");
            System.out.println("7. Eliminar oferta del marketplace");
            System.out.println("8. Comprar tiquete del marketplace");
            System.out.println("9. Hacer contraoferta");
            System.out.println("10. Aceptar contraoferta");
            System.out.println("11. Rechazar contraoferta");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> mostrarSaldo();
                case 2 -> comprarTiquete();
                case 3 -> mostrarTiquetes();
                case 4 -> solicitarReembolso();
                case 5 -> inscribirseMarketplace();
                case 6 -> publicarTiqueteMarketplace();
                case 7 -> eliminarOferta();
                case 8 -> comprarDelMarketplace();
                case 9 -> contraofertar();
                case 10 -> aceptarContraoferta();
                case 11 -> rechazarContraoferta();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void mostrarSaldo() {
        System.out.println("Saldo disponible: $" + cliente.consultar_saldo_disponible());
    }

    private static void comprarTiquete() {
        System.out.print("Ingrese ID del evento: ");
        String idEvento = sc.nextLine();
        System.out.print("Ingrese nombre de la localidad: ");
        String nombreLoc = sc.nextLine();

        try {
            cliente.comprar_tiquete_no_numerado(idEvento, nombreLoc);
            System.out.println("Compra realizada con éxito ✅");
        } catch (Exception e) {
            System.out.println("Error al comprar: " + e.getMessage());
        }
    }

    private static void mostrarTiquetes() {
        if (cliente.getTiquetes().isEmpty()) {
            System.out.println("No tienes tiquetes comprados.");
        } else {
            System.out.println("\n--- TUS TIQUETES ---");
            for (Tiquete t : cliente.getTiquetes()) {
                System.out.println(t);
            }
        }
    }

    private static void solicitarReembolso() {
        System.out.print("Ingrese nombre del evento: ");
        String evento = sc.nextLine();
        System.out.print("Ingrese motivo: ");
        String motivo = sc.nextLine();
        cliente.solicitar_reembolso(evento, motivo);
        System.out.println("Solicitud enviada ✅");
    }

    private static void inscribirseMarketplace() {
        cliente.inscribirse_marketplace();
        System.out.println("Cliente inscrito en el marketplace ✅");
    }

    private static void publicarTiqueteMarketplace() {
        if (cliente.getTiquetes().isEmpty()) {
            System.out.println("No tienes tiquetes para publicar.");
            return;
        }
        System.out.println("Selecciona el índice del tiquete a publicar:");
        for (int i = 0; i < cliente.getTiquetes().size(); i++) {
            System.out.println(i + ". " + cliente.getTiquetes().get(i));
        }
        int idx = Integer.parseInt(sc.nextLine());
        Tiquete t = cliente.getTiquetes().get(idx);
        cliente.publicar_oferta_Tiquete(t);
        System.out.println("Tiquete publicado en el marketplace ✅");
    }

    private static void eliminarOferta() {
        System.out.print("Ingrese el ID de la operación a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        cliente.borrar_oferta(id);
        System.out.println("Oferta eliminada ✅");
    }

    private static void comprarDelMarketplace() {
        System.out.print("Ingrese el ID de la operación a comprar: ");
        int id = Integer.parseInt(sc.nextLine());
        cliente.comprar_tiquete(id);
        System.out.println("Compra del marketplace realizada ✅");
    }

    private static void contraofertar() {
        System.out.print("Ingrese ID de la operación: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Ingrese valor de la contraoferta: ");
        double valor = Double.parseDouble(sc.nextLine());
        cliente.contra_ofertar(id, valor);
        System.out.println("Contraoferta enviada ✅");
    }

    private static void aceptarContraoferta() {
        System.out.print("Ingrese ID de la operación a aceptar: ");
        int id = Integer.parseInt(sc.nextLine());
        cliente.aceptar_contra_ofertar(id);
        System.out.println("Contraoferta aceptada ✅");
    }

    private static void rechazarContraoferta() {
        System.out.print("Ingrese ID de la operación a rechazar: ");
        int id = Integer.parseInt(sc.nextLine());
        cliente.rechazar_contra_ofertar(id);
        System.out.println("Contraoferta rechazada ❌");
    }
}

