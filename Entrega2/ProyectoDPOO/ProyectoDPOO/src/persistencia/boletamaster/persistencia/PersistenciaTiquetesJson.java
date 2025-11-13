package boletamaster.persistencia;

import java.io.*;
import boletamaster.Administrador;

public class PersistenciaTiquetesJson {
    private static final String ARCHIVO_DATOS = "datos_boletamaster.dat";

    public static void guardar(Administrador admin) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(admin);
            System.out.println("✅ Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("⚠️ Error al guardar datos: " + e.getMessage());
        }
    }

    public static Administrador cargar() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_DATOS))) {
            Administrador admin = (Administrador) ois.readObject();
            System.out.println("✅ Datos cargados correctamente.");
            return admin;
        } catch (Exception e) {
            System.out.println("ℹ️ No se encontró archivo de datos, creando nuevo administrador.");
            return new Administrador("admin", "admin");
        }
    }
}
