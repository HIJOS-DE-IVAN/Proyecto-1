package boletamaster;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;


public class Administrador extends Usuario{
	
	public final static String CANCELADO = "CANCELADO";
	
	public HashMap<String, Cliente> mapa_clientes;
	public  HashMap<String, Evento> mapa_eventos; //nombre del evento, Evento
	public  HashMap<String, Venue> mapa_venues;
	public HashMap<String, String> mapa_solicitudes; //el primer string hace referencia al nombre del evento a cancelar, el segundo hace referencia a la descripción de la solicitud
	public HashMap<String, Venue> mapa_venues_sugeridos; //nombre, venue
	
	private HashMap<LocalDateTime, Operacion> log_registros = new HashMap<>(); //para que nadie pueda borrarlo debe ser private

	private HashMap<String, Cliente> clientes_inscritos_marketplace = new HashMap<>(); //llave: nombre, valor, cliente
	
	private HashMap<String,OrganizadorDeEventos> solicitudes_promotor_marketplace = new HashMap<>();
	
	private HashMap<String,OrganizadorDeEventos> promotor_inscritos_marketplace = new HashMap<>();

	
	public HashMap<String, OrganizadorDeEventos> getSolicitudes_promotor_marketplace() {
		return solicitudes_promotor_marketplace;
	}

	public void setSolicitudes_promotor_marketplace(
			HashMap<String, OrganizadorDeEventos> solicitudes_promotor_marketplace) {
		this.solicitudes_promotor_marketplace = solicitudes_promotor_marketplace;
	}

	private HashMap<String, OrganizadorDeEventos> promotor_inscritos_marketplace = new HashMap<>();
	
	public HashMap<String, Cliente> getClientes_inscritos_marketplace() {
		return clientes_inscritos_marketplace;
	}

	public void setClientes_inscritos_marketplace(HashMap<String, Cliente> clientes_inscritos_marketplace) {
		this.clientes_inscritos_marketplace = clientes_inscritos_marketplace;
	}

	public HashMap<String, OrganizadorDeEventos> getPromotor_inscritos_marketplace() {
		return promotor_inscritos_marketplace;
	}

	public void setPromotor_inscritos_marketplace(HashMap<String, OrganizadorDeEventos> promotor_inscritos_marketplace) {
		this.promotor_inscritos_marketplace = promotor_inscritos_marketplace;
	}

	public Administrador(String login, String password) {
		super(login,password);
	}
	
	public HashMap<String, Evento> getMapa_eventos() {
		return mapa_eventos;
	}

	public HashMap<String, Venue> getMapa_venues() {
		return mapa_venues;
	}

	public HashMap<String, String> getMapa_solicitudes() {
		return mapa_solicitudes;
	}
	//NUEVO ------
	public HashMap<LocalDateTime, Operacion> getLog_registros() {
		return log_registros;
	}
	public void setLog_registros(HashMap<LocalDateTime, Operacion> log_registros) {
		this.log_registros = log_registros;
	}
	
	//NUEVO ----

	public void setMapa_eventos(HashMap<String, Evento> mapa_eventos) {
		this.mapa_eventos = mapa_eventos;
	}

	public void setMapa_venues(HashMap<String, Venue> mapa_venues) {
		this.mapa_venues = mapa_venues;
	}

	public void setMapa_solicitudes(HashMap<String, String> mapa_solicitudes) {
		this.mapa_solicitudes = mapa_solicitudes;
	}
	
	public void crearVenue(String nombre_venue, String ubicacion, int capacidad_maxima, String restricciones_de_uso) {
		//por el momento, el id de cada venue es su propio nombre
		Venue venue_creado = new Venue(nombre_venue, nombre_venue, ubicacion, capacidad_maxima,restricciones_de_uso, true);
		//Agregamos el venue al mapa de venues, el id es el nombre del venue
		mapa_venues.put(nombre_venue, venue_creado);
	}
	public void aprobarVenue(String nombre_venue) {
		Venue venue = mapa_venues_sugeridos.get(nombre_venue);
		venue.setAprobado(true);
		//aprobarlo es simplemente que lo agregue al mapa de venues y volverlo aprobado
		mapa_venues.put(nombre_venue, venue);
	}
	public HashMap<String, Venue> getMapa_venues_sugeridos() {
		return mapa_venues_sugeridos;
	}

	public void fijar_sobrecargo_evento(Evento evento, double sobrecargo) {
		evento.setSobrecargo(sobrecargo);
	}
	public void fijar_cobro_emision_impresion(Evento evento, double cobro_impresion) {
		List<Venue> venues = evento.getVenues(); //obtenemos los venues asociados al evento
		for (Venue venue: venues) {
			List<Localidad> localidades = venue.getLocalidades(); //obtenemos a todas las localidades
			for (Localidad localidad: localidades) {
				List<Tiquete> tiquetes = localidad.getTiquetes();
				for(Tiquete tiquete: tiquetes) {
					tiquete.setCuota_fija(cobro_impresion);
				}
			}
			
		}
	}
	public void aprobar_cancelar_evento(String nombre_evento) {
		Evento evento = mapa_eventos.get(nombre_evento); //obtenemos al evento
		//String solicitud = mapa_solicitudes.get(nombre_evento); //obtenemos la solicitud del organizador de eventos
		evento.setEstadoEvento(CANCELADO); //aprobamos la solicitud, ahora el evento será cancelado
		
	}
	public void cancelar_evento(String nombre_evento) {
		Evento evento = mapa_eventos.get(nombre_evento);
		evento.setEstadoEvento(CANCELADO);
	}
	
	public void aprobar_reembolso(String evento_nombre, String nombre_cliente) {
		
		//Paso -1: Podría funcionar, que dentro de la información de la salud ya vaya el nombre del evento y el nombre del cliente
		//Para así no tener que pedirlo por a
		
		//Paso 0: Contar cuánto le debemos retornar al cliente
		double saldo_retorno = 0;
		//Paso 1: Obtener al cliente y su saldo en la plataforma
		Cliente cliente_encontrado = mapa_clientes.get(nombre_cliente);
		double saldo_plataforma = cliente_encontrado.consultar_saldo_disponible();
		
		//Paso 2: Obtener tiquetes del cliente
		List<Tiquete> tiquetes_cliente = cliente_encontrado.getTiquetes();
		
		//Paso 3: Obtener al evento
		Evento evento_encontrado = mapa_eventos.get(evento_nombre);
		
		//Paso 4: Obtener venues del evento
		List<Venue> venues = evento_encontrado.getVenues();
		
		//Paso 5: Recorrer todos los venues del evento
		for(Venue venue: venues) {
			List<Localidad> localidades = venue.getLocalidades(); //Paso 6: Obtener las localidades del venue_i
			for (Tiquete tiquete: tiquetes_cliente) { //Paso 7: Recorrer los tiquetes del cliente
				if (localidades.contains(tiquete.getLocalidad()) && tiquete.isEsTransferible()) { //Paso 8: Verificar que el tiquete se encuentre dentro del evento que se solicitó, además que no sea un tiquete ya vencido, ¿generar algo como "es válido"?
				saldo_retorno += tiquete.getPrecio(); //Paso 9: Aumentar el saldo a retornar, según el precio del tiquete
			}
			}
			
		}
		//Paso 10: Retornarle al usuario el valor del tiquete
		cliente_encontrado.setSaldoPlataforma(saldo_plataforma + saldo_retorno);
		
	}
	
	public void consultar_estado_finaniero() {
		//¿Consultar el estado financiero de todos los eventos que dirige?
	}
	
	
	//NUEVOOOOOOOOO 
	
	//Se cumple el hecho que solo el administrador puede leer el log
	public Operacion buscar_operacion(int id_ope) {
		Operacion operacion_encontrada = null;
		for (Operacion op : log_registros.values()) {
	        if (op.id == id_ope && op.esta_activa) {
	            operacion_encontrada = op;
	            break;
	        }
	    }
		return operacion_encontrada;
	}
	
	public void eliminar_operacion(int id_ope) {
		Operacion operacion_encontrada = buscar_operacion(id_ope);
		if (operacion_encontrada != null) {
			//es necesario eliminar esta operacion del mapa
			log_registros.remove(operacion_encontrada.getFechaHora());
			operacion_encontrada.borrar_oferta();
			//modificamos la operación en el mapa para que quede registro que eliminamos esta operación
			//para esto, la parte_1 y la parte_2 serán NUll, el resto no cambia
			operacion_encontrada.setParte_1(null);
			operacion_encontrada.setParte_2(null);
			
			//la añadimos de nuevo al log
			getLog_registros().put(operacion_encontrada.getFechaHora(), operacion_encontrada);
		}
	}
	
	public void aceptar_promotor(String login_promotr) {
		OrganizadorDeEventos organizador = solicitudes_promotor_marketplace.get(login_promotr);
		
		
	}
	
	
}

