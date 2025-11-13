package interfaz;

import boletamaster.*;
import boletamaster.persistencia.PersistenciaTiquetesJson;

public class ConsolaAdministrador {

    public static void ejecutar(Administrador admin) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- MENÚ ADMINISTRADOR ---");
            System.out.println("1. Aceptar promotor");
            System.out.println("2. Eliminar operación");
            System.out.println("3. Ver log de operaciones");
            System.out.println("0. Salir");

            int opcion = UtilidadesConsola.leerEntero("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    String login = UtilidadesConsola.leerTexto("Login del promotor a aceptar: ");
                    admin.aceptar_promotor(login);
                    System.out.println("✅ Promotor aceptado.");
                    break;
                case 2:
                    int id = UtilidadesConsola.leerEntero("ID de operación a eliminar: ");
                    admin.eliminar_operacion(id);
                    break;
                case 3:
                    admin.getLog_registros().forEach((fecha, op) -> 
                        System.out.println(fecha + " → " + op));
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
