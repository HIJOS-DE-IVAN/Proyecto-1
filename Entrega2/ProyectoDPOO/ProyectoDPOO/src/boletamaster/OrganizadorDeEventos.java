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
	public void sugerir_venue(String nombre_venue, String ubicacion, int capacidad_max, String restricciones_uso) {
		//Paso 1: Creamos el venue
		Venue nuevo_venue = new Venue(nombre_venue, nombre_venue, ubicacion, capacidad_max, restricciones_uso, false);
		//Paso 2: Enviamos la solicitud
		admin.getMapa_venues_sugeridos().put(nombre_venue, nuevo_venue);
	}
	public void sugerir_cancelar_evento(String nombre_evento, String solicitud) {
		//agregar la peticion a su administrador
		admin.getMapa_solicitudes().put(nombre_evento, solicitud);
	}
	
	public void crear_localidad_venue(String nombre_venue, String localidad, Boolean es_numerada, double precio_base, int capacidad_total) {
		//Paso 1: Obtener al venue
		
	}
	
	
}

