package boletamaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.UUID;

public class Cliente extends Usuario{
	
	private double saldoPlataforma;
	
	private List<Tiquete> tiquetes;
	
	public Administrador admin; //cada cliente tiene contacto con el administrador
	
	public  HashMap<String, Evento> mapa_eventos;
	
	public Cliente(String login, String password, Double saldoPlataforma) {
		super(login,password);
		this.saldoPlataforma = saldoPlataforma;
		this.tiquetes= new ArrayList<Tiquete>();
	}

	public double consultar_saldo_disponible() {
		return saldoPlataforma;
	}

	public void setSaldoPlataforma(double saldoPlataforma) {
		this.saldoPlataforma = saldoPlataforma;
	}

	public List<Tiquete> getTiquetes() {
		return tiquetes;
	}

	public void setTiquetes(List<Tiquete> tiquetes) {
		this.tiquetes = tiquetes;
	}	
	
	public HashMap<String, Evento> getMapa_eventos() {
		return mapa_eventos;
	}

	public void setMapa_eventos(HashMap<String, Evento> mapa_eventos) {
		this.mapa_eventos = mapa_eventos;
	}
	
	public void solicitar_reembolso(String nombre_evento, String motivo_reembolso) {
		admin.getMapa_solicitudes().put(nombre_evento, motivo_reembolso);
		//El administrador se encargará de saber el nombre del usuario que mandó la solicitud (toca mejorarlo)
		//Una opción es que dentro de la solicitud vaya información de qué usuario la envío, pero sigue en proceso
		//Sería añadir 
	}
	
	public void comprar_tiquete_no_numerado(String nombre_evento, String nombre_localidad) {
		//Paso 1: Obtener el evento
		Evento evento = mapa_eventos.get(nombre_evento);
		//Paso 2: Buscar en los venues y localidades
		Localidad localidad_encontrada = null;
		Boolean encontrado = false;
		ArrayList<Venue> listado_venues = evento.getVenues();
		int contador_i = 0;
		while(encontrado == false & contador_i<listado_venues.size()) {
			Venue venue_leido = listado_venues.get(contador_i);
			ArrayList<Localidad> localidades_venue = venue_leido.getLocalidades();
			int contador_j = 0;
			
			while(encontrado == false & contador_j<localidades_venue.size()) {
				Localidad localidad_j = localidades_venue.get(contador_j);
				if (nombre_localidad.equals(localidad_j.getNombre())){
					encontrado = true;
					localidad_encontrada = localidad_j;
					
				}
				contador_j++;
			}
			contador_i++;
		}
		
		//Nos aseguramos que no sea null y además no sea numerada
		if (localidad_encontrada != null && localidad_encontrada.isNumerada() == false) {
			//Obtenemos el precio
			double precio_localidad = localidad_encontrada.getPrecioBase();
			//Verificamos si el usuario tiene suficiente saldo
			if (consultar_saldo_disponible() >= precio_localidad) {
				ArrayList<Tiquete> listado_tiquetes = localidad_encontrada.getTiquetes();
				int contador_t = 0;
				Tiquete tiquete_encontrado = null;
				while(tiquete_encontrado == null && contador_t<listado_tiquetes.size()) {
					Tiquete tiquete_leido = listado_tiquetes.get(contador_t);
					//TODO: Deberíamos hacer algo con la fecha, para asegurarnos que el tiquete sigue vigente
					if (tiquete_leido.getPrecio() <= consultar_saldo_disponible() && tiquete_leido.isEstaVendido() == false) {
						tiquete_encontrado = tiquete_leido;
					}
					contador_t++;
				}
				if (tiquete_encontrado != null) {
					//Quitarle el dinero al usuario
					this.saldoPlataforma-=tiquete_encontrado.getPrecio();
					//Modificar al tiquete
					tiquete_encontrado.setEstaVendido(true);
					tiquete_encontrado.setCliente(this);
					//Agregar el tiquete a la lista de tiquetes del usuario
					getTiquetes().add(tiquete_encontrado);
				}
				
			}
				
		}
	}
	public void comprar_tiquete_numerado(String nombre_evento, String nombre_localidad, int numerado_escogido) {
		//Paso 1: Obtener el evento
				Evento evento = mapa_eventos.get(nombre_evento);
				//Paso 2: Buscar en los venues y localidades
				Localidad localidad_encontrada = null;
				Boolean encontrado = false;
				ArrayList<Venue> listado_venues = evento.getVenues();
				int contador_i = 0;
				while(encontrado == false & contador_i<listado_venues.size()) {
					Venue venue_leido = listado_venues.get(contador_i);
					ArrayList<Localidad> localidades_venue = venue_leido.getLocalidades();
					int contador_j = 0;
					
					while(encontrado == false & contador_j<localidades_venue.size()) {
						Localidad localidad_j = localidades_venue.get(contador_j);
						if (nombre_localidad.equals(localidad_j.getNombre())){
							encontrado = true;
							localidad_encontrada = localidad_j;
							
						}
						contador_j++;
					}
					contador_i++;
				}
				
				//Nos aseguramos que no sea null y además sea numerada
				if (localidad_encontrada != null && localidad_encontrada.isNumerada() == true) {
					//Obtenemos el precio
					double precio_localidad = localidad_encontrada.getPrecioBase();
					//Verificamos si el usuario tiene suficiente saldo
					if (consultar_saldo_disponible() >= precio_localidad) {
						ArrayList<Tiquete> listado_tiquetes = localidad_encontrada.getTiquetes();
						int contador_t = 0;
						TiqueteVendidoNumerado tiquete_encontrado = null;
						
						while(tiquete_encontrado == null && contador_t<listado_tiquetes.size()) {
							//Se busca  a un tiquete numerado
							TiqueteVendidoNumerado tiquete_leido = (TiqueteVendidoNumerado) listado_tiquetes.get(contador_t);
							//TODO: Deberíamos hacer algo con la fecha, para asegurarnos que el tiquete sigue vigente
							
							//Añadimos que el tiquete que encontramos tenga el número deseado
							if (tiquete_leido.getPrecio() <= consultar_saldo_disponible() && tiquete_leido.isEstaVendido() == false && tiquete_leido.getNumero_asiento() == numerado_escogido) {
								tiquete_encontrado = tiquete_leido;
							}
							contador_t++;
						}
						if (tiquete_encontrado != null) {
							//Quitarle el dinero al usuario
							this.saldoPlataforma-=tiquete_encontrado.getPrecio();
							//Modificar al tiquete
							tiquete_encontrado.setEstaVendido(true);
							tiquete_encontrado.setCliente(this);
							//Agregar el tiquete a la lista de tiquetes del usuario
							getTiquetes().add(tiquete_encontrado);
						}
						
					}
						
				}
	}
	public void comprar_palco(String nombre_evento, String nombre_localidad, int cantidad) {
		// Por qué: ahora se exige saber evento/localidad/cantidad (requerimiento).
		validarCantidad(cantidad);
		Evento evento = obtenerEventoPorNombre(nombre_evento);
		Localidad localidad = obtenerLocalidadPorNombre(evento, nombre_localidad);
		if (!localidad.isNumerada()) {
			throw new IllegalStateException("La localidad seleccionada no es numerada; un palco requiere asientos numerados.");
		}
		if (this.tiquetes == null) this.tiquetes = new ArrayList<>();
		if (localidad.getTiquetes() == null) localidad.setTiquetes(new ArrayList<>());

		for (int i = 0; i < cantidad; i++) {
			Tiquete t = new Tiquete(
				0.0,                     // sobrecargo_porcentual_servicio
				0.0,                     // cuota_fija
				UUID.randomUUID().toString(), // identificador
				new Date(),              // fecha
				0,                       // hora
				localidad,               // localidad
				localidad.getPrecioBase(), // precio (ajusta si necesitas sobrecargos)
				true,                    // esTransferible
				true,                    // estaVendido
				this                     // cliente comprador
			);
			this.tiquetes.add(t);
			localidad.getTiquetes().add(t);
		}
	}

	public void comprar_paquete_multiple_temporada(java.util.List<String> nombres_eventos, String nombre_localidad, int cantidadPorEvento) {
		// Por qué: se necesita control explícito sobre eventos/localidad/cantidad.
		validarCantidad(cantidadPorEvento);
		if (nombres_eventos == null || nombres_eventos.isEmpty()) {
			throw new IllegalArgumentException("Debe indicar al menos un evento.");
		}
		if (this.tiquetes == null) this.tiquetes = new ArrayList<>();

		for (String ne : nombres_eventos) {
			Evento evento = obtenerEventoPorNombre(ne);
			Localidad loc = obtenerLocalidadPorNombre(evento, nombre_localidad);
			if (loc.getTiquetes() == null) loc.setTiquetes(new ArrayList<>());

			for (int i = 0; i < cantidadPorEvento; i++) {
				Tiquete t = new Tiquete(
					0.0, UUID.randomUUID().variant(), // valores dummy/placeholder si decides ajustar cargos
					UUID.randomUUID().toString(),
					new Date(), 0, loc, loc.getPrecioBase(),
					true, true, this
				);
				this.tiquetes.add(t);
				loc.getTiquetes().add(t);
			}
		}
	}

	public void comprar_paquete_multiple_deluxe(java.util.List<String> nombres_eventos, String nombre_localidad, int cantidadPorEvento) {
		// Por qué: misma necesidad; si hay reglas "deluxe" (merch, sobrecargos), aplícalas aquí.
		validarCantidad(cantidadPorEvento);
		if (nombres_eventos == null || nombres_eventos.isEmpty()) {
			throw new IllegalArgumentException("Debe indicar al menos un evento.");
		}
		if (this.tiquetes == null) this.tiquetes = new ArrayList<>();

		for (String ne : nombres_eventos) {
			Evento evento = obtenerEventoPorNombre(ne);
			Localidad loc = obtenerLocalidadPorNombre(evento, nombre_localidad);
			if (loc.getTiquetes() == null) loc.setTiquetes(new ArrayList<>());

			for (int i = 0; i < cantidadPorEvento; i++) {
				Tiquete t = new Tiquete(
					0.0, 0.0, // aquí podrías aplicar sobrecargos deluxe
					UUID.randomUUID().toString(),
					new Date(), 0, loc, loc.getPrecioBase(),
					true, true, this
				);
				this.tiquetes.add(t);
				loc.getTiquetes().add(t);
			}
		}
	}
	private Evento obtenerEventoPorNombre(String nombre_evento) {
		if (this.mapa_eventos == null) throw new IllegalStateException("No hay eventos cargados.");
		Evento e = this.mapa_eventos.get(nombre_evento);
		if (e == null) throw new IllegalStateException("Evento no encontrado: " + nombre_evento);
		return e;
	}
	private Localidad obtenerLocalidadPorNombre(Evento evento, String nombre_localidad) {
		if (evento == null) throw new IllegalArgumentException("Evento nulo.");
		if (evento.getVenues() == null) throw new IllegalStateException("El evento no tiene venues asociados.");
		for (Venue v : evento.getVenues()) {
			if (v == null || v.getLocalidades() == null) continue;
			for (Localidad loc : v.getLocalidades()) {
				if (loc != null && nombre_localidad.equals(loc.getNombre())) return loc;
			}
		}
		throw new IllegalStateException("Localidad no encontrada en el evento: " + nombre_localidad);
	}
	private void validarCantidad(int cantidad) {
		if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser > 0");
	}
	
	public void transferir_tiquete() {}
	public void transferir_paquete_multiple() {}
	
	
	//FUNCIONES DE LA ENTREGA 2
	
	
}

