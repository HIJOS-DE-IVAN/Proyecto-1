package interfaz;

import boletamaster.*;
import boletamaster.persistencia.PersistenciaTiquetesJson;

public class ConsolaCliente {

    public static void ejecutar(Administrador admin, Cliente cliente) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- MENÚ CLIENTE ---");
            System.out.println("1. Publicar oferta de tiquete");
            System.out.println("2. Comprar tiquete");
            System.out.println("3. Contraofertar");
            System.out.println("4. Aceptar contraoferta");
            System.out.println("5. Rechazar contraoferta");
            System.out.println("0. Salir");

            int opcion = UtilidadesConsola.leerEntero("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    System.out.println("Simulando publicación...");
                    break;
                case 2:
                    int idCompra = UtilidadesConsola.leerEntero("ID de compra: ");
                    cliente.comprar_tiquete(idCompra);
                    break;
                case 3:
                    int idContra = UtilidadesConsola.leerEntero("ID de compra: ");
                    double valor = UtilidadesConsola.leerDouble("Valor de contraoferta: ");
                    cliente.contra_ofertar(idContra, valor);
                    break;
                case 4:
                    cliente.aceptar_contra_ofertar(UtilidadesConsola.leerEntero("ID: "));
                    break;
                case 5:
                    cliente.rechazar_contra_ofertar(UtilidadesConsola.leerEntero("ID: "));
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
