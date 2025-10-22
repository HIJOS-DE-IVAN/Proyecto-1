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
	
	public void comprar_tiquete_no_numerado(String nombre_evento, String nombre_localidad) {
		//Paso 1: Obtener el evento
		Evento evento = mapa_eventos.get(nombre_evento);
		if (evento == null) { throw new IllegalStateException("Evento no encontrado: " + nombre_evento); }

		//Paso 2: Buscar en los venues y localidades
		Localidad localidad_encontrada = null;
		boolean encontrado = false;
		ArrayList<Venue> listado_venues = evento.getVenues();
		int contador_i = 0;
		while(encontrado == false & listado_venues != null & contador_i<listado_venues.size()) {
			Venue venue_leido = listado_venues.get(contador_i);
			ArrayList<Localidad> localidades_venue = (venue_leido != null) ? venue_leido.getLocalidades() : null;
			int contador_j = 0;
			while(encontrado == false & localidades_venue != null & contador_j<localidades_venue.size()) {
				Localidad localidad_leida = localidades_venue.get(contador_j);
				if (localidad_leida != null && nombre_localidad.equals(localidad_leida.getNombre())) {
					localidad_encontrada = localidad_leida;
					encontrado = true;
				}
				contador_j ++;
			}
			contador_i ++;
		}
		if (localidad_encontrada == null) { throw new IllegalStateException("Localidad no encontrada: " + nombre_localidad); }
		if (localidad_encontrada.isNumerada()) { throw new IllegalStateException("La localidad es numerada; use comprar_tiquete_numerado()."); }

		//Paso 3: Elegir un tiquete disponible (no numerado)
		ArrayList<Tiquete> listado_tiquetes = localidad_encontrada.getTiquetes();
		int contador_t = 0;
		Tiquete tiquete_encontrado = null;

		while(tiquete_encontrado == null & listado_tiquetes != null & contador_t<listado_tiquetes.size()) {
			Tiquete tiquete_leido = listado_tiquetes.get(contador_t);
			//TODO: validar fecha si aplica
			if (tiquete_leido != null && !(tiquete_leido instanceof TiqueteVendidoNumerado) && tiquete_leido.isEstaVendido() == false) {
				tiquete_encontrado = tiquete_leido;
			}
			contador_t ++;
		}
		if (tiquete_encontrado == null) { throw new IllegalStateException("No hay tiquetes no numerados disponibles."); }

		//Paso 4: Efectuar compra (sin saldo)
		tiquete_encontrado.setEstaVendido(true);
		tiquete_encontrado.setCliente(null); // por qué: campo es Cliente; el organizador no es Cliente
		if (this.tiquetes == null) { this.tiquetes = new ArrayList<Tiquete>(); }
		this.tiquetes.add(tiquete_encontrado);
	}

	public void comprar_tiquete_numerado(String nombre_evento, String nombre_localidad, int numerado_escogido) {
		//Paso 1: Obtener el evento
		Evento evento = mapa_eventos.get(nombre_evento);
		if (evento == null) { throw new IllegalStateException("Evento no encontrado: " + nombre_evento); }

		//Paso 2: Buscar en los venues y localidades
		Localidad localidad_encontrada = null;
		boolean encontrado = false;
		ArrayList<Venue> listado_venues = evento.getVenues();
		int contador_i = 0;
		while(encontrado == false & listado_venues != null & contador_i<listado_venues.size()) {
			Venue venue_leido = listado_venues.get(contador_i);
			ArrayList<Localidad> localidades_venue = (venue_leido != null) ? venue_leido.getLocalidades() : null;
			int contador_j = 0;
			while(encontrado == false & localidades_venue != null & contador_j<localidades_venue.size()) {
				Localidad localidad_leida = localidades_venue.get(contador_j);
				if (localidad_leida != null && nombre_localidad.equals(localidad_leida.getNombre())) {
					localidad_encontrada = localidad_leida;
					encontrado = true;
				}
				contador_j ++;
			}
			contador_i ++;
		}
		if (localidad_encontrada == null) { throw new IllegalStateException("Localidad no encontrada: " + nombre_localidad); }
		if (localidad_encontrada.isNumerada() == false) { throw new IllegalStateException("La localidad no es numerada; use comprar_tiquete_no_numerado()."); }

		//Paso 3: Elegir un tiquete numerado disponible con el número solicitado
		ArrayList<Tiquete> listado_tiquetes = localidad_encontrada.getTiquetes();
		int contador_t = 0;
		TiqueteVendidoNumerado tiquete_encontrado = null;

		while(tiquete_encontrado == null & listado_tiquetes != null & contador_t<listado_tiquetes.size()) {
			Tiquete tiquete_base = listado_tiquetes.get(contador_t);
			if (tiquete_base instanceof TiqueteVendidoNumerado) {
				TiqueteVendidoNumerado tiquete_leido = (TiqueteVendidoNumerado) tiquete_base;
				//TODO: validar fecha si aplica
				if (tiquete_leido.isEstaVendido() == false && tiquete_leido.getNumero_asiento() == numerado_escogido) {
					tiquete_encontrado = tiquete_leido;
				}
			}
			contador_t ++;
		}
		if (tiquete_encontrado == null) { throw new IllegalStateException("Asiento no disponible: " + numerado_escogido); }

		//Paso 4: Efectuar compra (sin saldo)
		tiquete_encontrado.setEstaVendido(true);
		tiquete_encontrado.setCliente(null); // por qué: campo es Cliente; el organizador no es Cliente
		if (this.tiquetes == null) { this.tiquetes = new ArrayList<Tiquete>(); }
		this.tiquetes.add(tiquete_encontrado);
	}

	public void comprar_palco() {
		//Paso 1: Parámetro por defecto
		int cantidad_palco = 2;

		//Paso 2: Buscar primer evento con localidad numerada con al menos 2 asientos libres
		Evento evento_seleccionado = null;
		Localidad localidad_numerada = null;
		boolean encontrado = false;

		if (this.mapa_eventos == null || this.mapa_eventos.isEmpty()) {
			throw new IllegalStateException("No hay eventos disponibles para palco.");
		}

		for (Evento evento : this.mapa_eventos.values()) {
			if (evento == null || evento.getVenues() == null) continue;
			ArrayList<Venue> listado_venues = evento.getVenues();
			int contador_i = 0;
			while(encontrado == false & listado_venues != null & contador_i<listado_venues.size()) {
				Venue venue_leido = listado_venues.get(contador_i);
				ArrayList<Localidad> localidades_venue = (venue_leido != null) ? venue_leido.getLocalidades() : null;
				int contador_j = 0;
				while(encontrado == false & localidades_venue != null & contador_j<localidades_venue.size()) {
					Localidad localidad_leida = localidades_venue.get(contador_j);
					if (localidad_leida != null && localidad_leida.isNumerada()) {
						ArrayList<Tiquete> listado_tiquetes = localidad_leida.getTiquetes();
						int libres = 0;
						int k = 0;
						while(listado_tiquetes != null & k < listado_tiquetes.size()) {
							Tiquete base = listado_tiquetes.get(k);
							if (base instanceof TiqueteVendidoNumerado) {
								TiqueteVendidoNumerado tn = (TiqueteVendidoNumerado) base;
								if (tn.isEstaVendido() == false) { libres++; }
							}
							k++;
						}
						if (libres >= cantidad_palco) {
							evento_seleccionado = evento;
							localidad_numerada = localidad_leida;
							encontrado = true;
						}
					}
					contador_j ++;
				}
				contador_i ++;
			}
			if (encontrado) break;
		}

		if (localidad_numerada == null) {
			throw new IllegalStateException("No hay localidad numerada con disponibilidad suficiente para palco.");
		}

		//Paso 3: Seleccionar 2 asientos numerados
		ArrayList<Tiquete> listado_tiquetes = localidad_numerada.getTiquetes();
		ArrayList<TiqueteVendidoNumerado> seleccionados = new ArrayList<TiqueteVendidoNumerado>();
		int contador_t = 0;
		while(seleccionados.size() < cantidad_palco & listado_tiquetes != null & contador_t < listado_tiquetes.size()) {
			Tiquete base = listado_tiquetes.get(contador_t);
			if (base instanceof TiqueteVendidoNumerado) {
				TiqueteVendidoNumerado tn = (TiqueteVendidoNumerado) base;
				if (tn.isEstaVendido() == false) {
					seleccionados.add(tn);
				}
			}
			contador_t++;
		}
		if (seleccionados.size() < cantidad_palco) {
			throw new IllegalStateException("No se pudo completar el palco.");
		}

		//Paso 4: Marcar vendidos y agregar al organizador (sin saldo)
		if (this.tiquetes == null) { this.tiquetes = new ArrayList<Tiquete>(); }
		int z = 0;
		while(z < seleccionados.size()) {
			TiqueteVendidoNumerado t = seleccionados.get(z);
			t.setEstaVendido(true);
			t.setCliente(null); // organizador no es Cliente
			this.tiquetes.add(t);
			z++;
		}
	}

	public void comprar_paquete_multiple_temporada() {
		//Paso 1: Validar eventos
		if (this.mapa_eventos == null || this.mapa_eventos.isEmpty()) {
			throw new IllegalStateException("No hay eventos para paquete de temporada.");
		}

		//Paso 2: Por cada evento, tomar 1 no numerado disponible (primera localidad no numerada)
		if (this.tiquetes == null) { this.tiquetes = new ArrayList<Tiquete>(); }

		for (Evento evento : this.mapa_eventos.values()) {
			if (evento == null || evento.getVenues() == null) continue;

			boolean adquirido = false;
			ArrayList<Venue> listado_venues = evento.getVenues();
			int i = 0;
			while(adquirido == false & listado_venues != null & i < listado_venues.size()) {
				Venue v = listado_venues.get(i);
				ArrayList<Localidad> locs = (v != null) ? v.getLocalidades() : null;
				int j = 0;
				while(adquirido == false & locs != null & j < locs.size()) {
					Localidad loc = locs.get(j);
					if (loc != null && loc.isNumerada() == false) {
						ArrayList<Tiquete> lista = loc.getTiquetes();
						int k = 0;
						while(adquirido == false & lista != null & k < lista.size()) {
							Tiquete t = lista.get(k);
							if (t != null && !(t instanceof TiqueteVendidoNumerado) && t.isEstaVendido() == false) {
								//TODO: validar fecha si aplica
								t.setEstaVendido(true);
								t.setCliente(null);
								this.tiquetes.add(t);
								adquirido = true;
								break;
							}
							k++;
						}
					}
					j++;
				}
				i++;
			}
			// Si no hay disponibilidad en un evento, se omite ese evento.
		}
	}

	public void comprar_paquete_multiple_deluxe() {
		//Paso 1: Validar eventos
		if (this.mapa_eventos == null || this.mapa_eventos.isEmpty()) {
			throw new IllegalStateException("No hay eventos para paquete deluxe.");
		}

		//Paso 2: Por cada evento, priorizar 1 numerado; si no hay, 1 no numerado
		if (this.tiquetes == null) { this.tiquetes = new ArrayList<Tiquete>(); }

		for (Evento evento : this.mapa_eventos.values()) {
			if (evento == null || evento.getVenues() == null) continue;

			boolean adquirido = false;

			// 2.1 Intentar numerado
			ArrayList<Venue> venues = evento.getVenues();
			int i = 0;
			while(adquirido == false & venues != null & i < venues.size()) {
				Venue v = venues.get(i);
				ArrayList<Localidad> locs = (v != null) ? v.getLocalidades() : null;
				int j = 0;
				while(adquirido == false & locs != null & j < locs.size()) {
					Localidad loc = locs.get(j);
					if (loc != null && loc.isNumerada()) {
						ArrayList<Tiquete> lista = loc.getTiquetes();
						int k = 0;
						while(adquirido == false & lista != null & k < lista.size()) {
							Tiquete base = lista.get(k);
							if (base instanceof TiqueteVendidoNumerado) {
								TiqueteVendidoNumerado tn = (TiqueteVendidoNumerado) base;
								if (tn.isEstaVendido() == false) {
									tn.setEstaVendido(true);
									tn.setCliente(null);
									this.tiquetes.add(tn);
									adquirido = true;
									break;
								}
							}
							k++;
						}
					}
					j++;
				}
				i++;
			}

			// 2.2 Si no hubo numerado, intentar no numerado
			if (adquirido == false) {
				int a = 0;
				while(adquirido == false & venues != null & a < venues.size()) {
					Venue v = venues.get(a);
					ArrayList<Localidad> locs = (v != null) ? v.getLocalidades() : null;
					int b = 0;
					while(adquirido == false & locs != null & b < locs.size()) {
						Localidad loc = locs.get(b);
						if (loc != null && loc.isNumerada() == false) {
							ArrayList<Tiquete> lista = loc.getTiquetes();
							int k = 0;
							while(adquirido == false & lista != null & k < lista.size()) {
								Tiquete t = lista.get(k);
								if (t != null && !(t instanceof TiqueteVendidoNumerado) && t.isEstaVendido() == false) {
									t.setEstaVendido(true);
									t.setCliente(null);
									this.tiquetes.add(t);
									adquirido = true;
									break;
								}
								k++;
							}
						}
						b++;
					}
					a++;
				}
			}
			// Si un evento no tiene disponibilidad, se omite.
		}
	}

}

