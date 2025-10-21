package boletamaster;

import java.util.HashMap;
import java.util.List;

public class Administrador extends Usuario{
	
	
	public  HashMap<String, Evento> mapa_eventos;
	public  HashMap<String, Venue> mapa_venues;
	
	public Administrador(String login, String password) {
		super(login,password);
	}
	
	public void crearVenue(String id, String nombre_evento, String nombre_venue, String ubicacion, int capacidad_maxima, String restricciones_de_uso) {
		Evento evento_encontrado = mapa_eventos.get(nombre_evento);
		Venue venue_creado = new Venue(id, nombre_venue, ubicacion, capacidad_maxima,restricciones_de_uso, true);
		
		mapa_venues.put(restricciones_de_uso, venue_creado);
		
		evento_encontrado.getVenues().add(venue_creado);
		this.mapa_eventos = null;
	}
	
	
}

