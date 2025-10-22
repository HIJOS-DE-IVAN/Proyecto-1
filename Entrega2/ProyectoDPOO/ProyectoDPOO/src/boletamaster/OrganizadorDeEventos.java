package boletamaster;

import java.util.ArrayList;
import java.util.HashMap;

public class OrganizadorDeEventos extends Usuario{
	
	public  HashMap<String, Evento> mapa_eventos;
	private ArrayList<Tiquete> tiquetes;
	public Administrador admin;
	
	public OrganizadorDeEventos(String login, String password) {
		super(login,password);
		this.mapa_eventos = new HashMap<String, Evento>();
		this.tiquetes = new ArrayList<Tiquete>();
	}

	public HashMap<String, Evento> getEventos() {
		return mapa_eventos;
	}

	public void setEventos(HashMap<String, Evento> eventos) {
		this.mapa_eventos = eventos;
	}

	public ArrayList<Tiquete> getTiquetes() {
		return tiquetes;
	}

	public void setTiquetes(ArrayList<Tiquete> tiquetes) {
		this.tiquetes = tiquetes;
	}
	public void sugerir_cancelar_evento(String nombre_evento, String solicitud) {
		//agregar la peticion a su administrador
		admin.getMapa_solicitudes().put(nombre_evento, solicitud);
	}
	
}

