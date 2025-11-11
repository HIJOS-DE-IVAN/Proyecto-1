package boletamaster.persistencia.json;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import boletamaster.Evento;
import boletamaster.Tiquete;

public class PersistenciaTiquetesJson {
	public List<Evento> cargar(String ruta) throws IOException {
        File f = new File(ruta);
        if (!f.exists()) return new ArrayList<>();
        
    }

    public void salvar(String ruta, List<Evento> eventos) throws IOException {
        
    }

}
