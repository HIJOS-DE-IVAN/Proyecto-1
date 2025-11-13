package interfaz;

import boletamaster.*;
import boletamaster.persistencia.PersistenciaTiquetesJson;

public class ConsolaPromotor {

    public static void ejecutar(Administrador admin, OrganizadorDeEventos promotor) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- MENÚ PROMOTOR ---");
            System.out.println("1. Solicitar inscripción al marketplace");
            System.out.println("0. Salir");

            int opcion = UtilidadesConsola.leerEntero("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    promotor.solicitud_inscripcion_marketplace();
                    System.out.println("✅ Solicitud enviada.");
                    break;
                case 0:
                	PersistenciaTiquetesJson.guardar(admin);
                    continuar = false;
                    break;
                default:
                    System.out.println("❌ Opción inválida.");
            }
        }
    }
}
