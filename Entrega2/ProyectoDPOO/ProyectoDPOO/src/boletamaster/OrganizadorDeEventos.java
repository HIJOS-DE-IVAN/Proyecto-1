package boletamaster;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrganizadorDeEventos extends Usuario{
	
	public  HashMap<String, Evento> mapa_eventos;
	public  HashMap<String, Venue> mapa_venues; //mapa de venues registrados en la plataforma
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

	
	public void crear_localidad_venue( String nombre_venue, String nombre_localidad, Boolean es_numerada, double precio_base, int capacidad_total) {
		//Paso 1: Obtener al venue
		Venue venue_buscado = mapa_venues.get(nombre_venue);
		//Paso 3: Crear la localidad
		Localidad localidad_creada = new Localidad(nombre_localidad, nombre_localidad, es_numerada, precio_base, capacidad_total);
		//Paso 2: agregar la localidad al venue
		venue_buscado.getLocalidades().add(localidad_creada);	
	}
	public void crear_evento(String id, Date fecha, String tipoEvento, String estadoEvento , String nombre_venue) {
		//Paso 1: Crear al evento
		Evento evento_creado = new Evento(id, fecha, tipoEvento, estadoEvento);
		//Paso 2: Obtener al venue
		Venue venue_buscado = mapa_venues.get(nombre_venue);
		//Paso 3: Asociar al venue con el eveno
		evento_creado.getVenues().add(venue_buscado);
		
	}
	public void crear_tiquetes_evento(String nombre_evento, int cantidad_tiquetes, String tipo_tiquetes) {
		//Paso 1: Obtener al evento
		Evento evento_encontrado = mapa_eventos.get(nombre_evento);
		//Paso 2: Obtener venues
		ArrayList<Venue> venues_creado = evento_encontrado.getVenues();
		//Paso 3: Recorrer localidades
		int contador = 0;
		while(cantidad_tiquetes > 0) {
			//Tomamos a las localidades del venue
			ArrayList<Localidad> localidades_venue = venues_creado.get(contador).getLocalidades();
			
			int contador_2 = 0;
			//Tomamos a una localidad en orden
			Localidad localidad_j = localidades_venue.get(contador);
			
			//Crear un nuevo tiquete
			//Obtener sobrecargo_porcentual_servicio, del evento 
			double sobrecargo_porcentual_servicio = evento_encontrado.getSobrecargo();
			
			//TODO: Obtener cuota_fija (fijar la cuota lo deberia hacer el administrador, entonces se inicia en null)
			Double cuota_fija = null;
			//TODO: ¿Cómo identificamos a los tiquetes?
			String identificador = "l";
			
			Date fecha = evento_encontrado.getFecha();
			
			int hora = (int) evento_encontrado.getFecha().getTime();
			
			//TODO:Fijar precios, se inicia en null
			Double precio = null;
			
			//TODO: ¿Qué hacemos con los combos?
			boolean esTransferible = true;
			
			boolean estaVendido = false;
			//TODO: Iniciamos cliente como nulo
			Cliente cliente = null;
			
			Tiquete tiquete_nuevo = new Tiquete(sobrecargo_porcentual_servicio, cuota_fija, identificador,
	                   fecha, hora, localidad_j, precio,
	                   esTransferible,  estaVendido, cliente);
			while((cantidad_tiquetes > 0) && (contador_2<localidad_j.getCapacidad()) ) {
				
				localidad_j.getTiquetes().add(tiquete_nuevo);
				contador_2++;
				cantidad_tiquetes--;
				
			}
			contador++;
			
		}
		
	}
	public void crear_oferta_localidad(String nombre_evento, String nombre_localidad, double evento_porcentual) {
		//Paso 1: Hallar al evento
		Evento evento_encontrado = mapa_eventos.get(nombre_evento);
		//Paso 2: Tomar a los venues
		ArrayList<Venue> venues_evento = evento_encontrado.getVenues();
		//Paso 3: buscar a la localidad
		Localidad localidad_j = null;
		int contador_i = 0;
		while(contador_i < venues_evento.size()) {
			Venue venue_i = venues_evento.get(contador_i);
			ArrayList<Localidad> localides = venue_i.getLocalidades();
			int contador_j = 0;
			while(contador_j < localides.size()) {
				localidad_j = localides.get(contador_j);
				if(localidad_j.getNombre().equals(nombre_localidad)) {
					contador_j = localides.size();
					contador_i = venues_evento.size();
				}
			}
			contador_i++;
		}
		//Paso 4: modificar a los tiquetes en esa localidad
		ArrayList<Tiquete> tiquetes_localidad = localidad_j.getTiquetes();
		for(Tiquete tiquete: tiquetes_localidad) {
			double precio_anterior = tiquete.getPrecio();
			tiquete.setPrecio(precio_anterior*(1-evento_porcentual));
		}
		
	}
	
	public void crear_tiquetes_multiples_temporada(ArrayList<String> listado_eventos) {
		//Paso 1: Obtener a los eventos
		ArrayList<Evento> eventos = null;
		for(String nombre_evento : listado_eventos) {
			eventos.add(mapa_eventos.get(nombre_evento));
		}
		
		//Paso 2: TODO crear tiquete multiple temporada
		
	}
	
	public void crear_tiquetes_mutliples_deluxe(String nombre_evento, String localidad, String beneficios_regalos, Boolean tiquetes_extra, String evento_extra) {
		//Paso 1: Tomar al evento
		Evento evento_encontrado = mapa_eventos.get(nombre_evento);
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
	
	public void consultar_estado_financiero(String nombre_evento, String nombre_localidad) {
		//Se debe reportar: precio sin recargos de cada boleta de un evento, las ganancias
		//y porcentaje de ventas globales,
		if (nombre_evento.equals("") == false && nombre_localidad.equals("") == false){
			//Caso 1: Se quiere buscar por localidad
			
		}else {
			//Caso 2: Se quiere buscar por evento
			Evento evento_encontrado = mapa_eventos.get(nombre_evento);
		}
		
		
	}
	
	
}

