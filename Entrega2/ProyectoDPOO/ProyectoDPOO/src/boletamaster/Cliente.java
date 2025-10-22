package boletamaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	public void comprar_palco() {
		//Paso 1: Validaciones y parámetros por defecto
		int cantidad_palco = 2; // tamaño mínimo razonable de palco
		if (mapa_eventos == null || mapa_eventos.isEmpty()) {
			throw new IllegalStateException("No hay eventos disponibles para comprar palco.");
		}

		//Paso 2: Buscar el primer evento con localidad numerada disponible
		Evento evento_seleccionado = null;
		Localidad localidad_numerada = null;
		boolean encontrado = false;

		for (Evento evento : mapa_eventos.values()) {
			if (evento == null || evento.getVenues() == null) continue;
			ArrayList<Venue> listado_venues = evento.getVenues();
			int contador_i = 0;
			while(encontrado == false & contador_i<listado_venues.size()) {
				Venue venue_leido = listado_venues.get(contador_i);
				ArrayList<Localidad> localidades_venue = venue_leido.getLocalidades();
				int contador_j = 0;
				while(encontrado == false & localidades_venue != null & contador_j<localidades_venue.size()) {
					Localidad localidad_leida = localidades_venue.get(contador_j);
					if (localidad_leida != null && localidad_leida.isNumerada()) {
						// Verificamos si hay al menos 'cantidad_palco' disponibles
						ArrayList<Tiquete> listado_tiquetes = localidad_leida.getTiquetes();
						if (listado_tiquetes != null) {
							int disponibles = 0;
							int k = 0;
							while(k<listado_tiquetes.size()) {
								Tiquete t = listado_tiquetes.get(k);
								if (t instanceof TiqueteVendidoNumerado && t.isEstaVendido() == false) {
									disponibles ++;
								}
								k++;
							}
							if (disponibles >= cantidad_palco) {
								evento_seleccionado = evento;
								localidad_numerada = localidad_leida;
								encontrado = true;
							}
						}
					}
					contador_j ++;
				}
				contador_i ++;
			}
			if (encontrado) break;
		}

		if (localidad_numerada == null) {
			throw new IllegalStateException("No se encontró localidad numerada con disponibilidad para un palco.");
		}

		//Paso 3: Seleccionar 'cantidad_palco' asientos numerados disponibles
		ArrayList<Tiquete> listado_tiquetes = localidad_numerada.getTiquetes();
		int contador_t = 0;
		int tomados = 0;
		ArrayList<TiqueteVendidoNumerado> seleccionados = new ArrayList<TiqueteVendidoNumerado>();
		while (tomados < cantidad_palco && contador_t < listado_tiquetes.size()) {
			Tiquete base = listado_tiquetes.get(contador_t);
			if (base instanceof TiqueteVendidoNumerado) {
				TiqueteVendidoNumerado cand = (TiqueteVendidoNumerado) base;
				if (cand.isEstaVendido() == false) {
					seleccionados.add(cand);
					tomados++;
				}
			}
			contador_t++;
		}
		if (seleccionados.size() < cantidad_palco) {
			throw new IllegalStateException("No se pudo completar el palco, disponibilidad insuficiente.");
		}

		//Paso 4: Calcular costo total y verificar saldo
		double total = 0.0;
		int idx = 0;
		while(idx < seleccionados.size()) {
			total += seleccionados.get(idx).getPrecio();
			idx++;
		}
		if (consultar_saldo_disponible() < total) {
			throw new IllegalStateException("Saldo insuficiente para palco. Requiere: " + total + ", disponible: " + consultar_saldo_disponible());
		}

		//Paso 5: Efectuar compra (marcar vendidos, asignar cliente, debitar y almacenar)
		setSaldoPlataforma(consultar_saldo_disponible() - total);
		if (this.tiquetes == null) { this.tiquetes = new ArrayList<Tiquete>(); }

		int z = 0;
		while(z < seleccionados.size()) {
			TiqueteVendidoNumerado t = seleccionados.get(z);
			t.setEstaVendido(true);
			t.setCliente(this);
			this.tiquetes.add(t);
			z++;
		}
	}
	
	public void comprar_paquete_multiple_temporada() {
		//Paso 1: Validar existen eventos
		if (mapa_eventos == null || mapa_eventos.isEmpty()) {
			throw new IllegalStateException("No hay eventos para paquete de temporada.");
		}

		//Paso 2: Para cada evento, intentar comprar 1 tiquete NO numerado (primera localidad no numerada con disponibilidad)
		double total_a_pagar = 0.0;
		ArrayList<Tiquete> compras_temporada = new ArrayList<Tiquete>();

		for (Evento evento : mapa_eventos.values()) {
			if (evento == null || evento.getVenues() == null) continue;

			Localidad localidad_objetivo = null;
			boolean encontrado = false;
			ArrayList<Venue> listado_venues = evento.getVenues();
			int i = 0;
			while(encontrado == false & i<listado_venues.size()) {
				Venue venue_leido = listado_venues.get(i);
				ArrayList<Localidad> localidades_venue = venue_leido.getLocalidades();
				int j = 0;
				while(encontrado == false & localidades_venue != null & j<localidades_venue.size()) {
					Localidad loc = localidades_venue.get(j);
					if (loc != null && loc.isNumerada() == false) {
						ArrayList<Tiquete> lista = loc.getTiquetes();
						if (lista != null) {
							int k = 0;
							while(k<lista.size()) {
								Tiquete t = lista.get(k);
								if (t != null && !(t instanceof TiqueteVendidoNumerado) && t.isEstaVendido() == false) {
									localidad_objetivo = loc;
									compras_temporada.add(t);
									total_a_pagar += t.getPrecio();
									encontrado = true;
									break;
								}
								k++;
							}
						}
					}
					j++;
				}
				i++;
			}
			// Si no hay no-numerado en ese evento, se omite ese evento.
		}

		//Paso 3: Verificar saldo acumulado
		if (compras_temporada.isEmpty()) {
			throw new IllegalStateException("No se encontraron tiquetes no numerados disponibles para temporada.");
		}
		if (consultar_saldo_disponible() < total_a_pagar) {
			throw new IllegalStateException("Saldo insuficiente para paquete temporada. Requiere: " + total_a_pagar + ", disponible: " + consultar_saldo_disponible());
		}

		//Paso 4: Efectuar compras
		setSaldoPlataforma(consultar_saldo_disponible() - total_a_pagar);
		if (this.tiquetes == null) { this.tiquetes = new ArrayList<Tiquete>(); }

		int c = 0;
		while(c < compras_temporada.size()) {
			Tiquete t = compras_temporada.get(c);
			t.setEstaVendido(true);
			t.setCliente(this);
			this.tiquetes.add(t);
			c++;
		}
	}

	public void comprar_paquete_multiple_deluxe() {
		//Paso 1: Validar eventos
		if (mapa_eventos == null || mapa_eventos.isEmpty()) {
			throw new IllegalStateException("No hay eventos para paquete deluxe.");
		}

		//Paso 2: Para cada evento, priorizar compra de 1 numerado; si no hay, 1 no numerado.
		double total_a_pagar = 0.0;
		ArrayList<Tiquete> compras = new ArrayList<Tiquete>();

		for (Evento evento : mapa_eventos.values()) {
			if (evento == null || evento.getVenues() == null) continue;

			boolean adquirido = false;

			// 2.1 Intentar numerado
			ArrayList<Venue> venues = evento.getVenues();
			int i = 0;
			while (adquirido == false & i < venues.size()) {
				Venue v = venues.get(i);
				ArrayList<Localidad> locs = v.getLocalidades();
				int j = 0;
				while (adquirido == false & locs != null & j < locs.size()) {
					Localidad loc = locs.get(j);
					if (loc != null && loc.isNumerada()) {
						ArrayList<Tiquete> lista = loc.getTiquetes();
						int k = 0;
						while (adquirido == false & lista != null & k < lista.size()) {
							Tiquete base = lista.get(k);
							if (base instanceof TiqueteVendidoNumerado) {
								TiqueteVendidoNumerado tn = (TiqueteVendidoNumerado) base;
								if (tn.isEstaVendido() == false) {
									compras.add(tn);
									total_a_pagar += tn.getPrecio();
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
				while (adquirido == false & a < venues.size()) {
					Venue v = venues.get(a);
					ArrayList<Localidad> locs = v.getLocalidades();
					int b = 0;
					while (adquirido == false & locs != null & b < locs.size()) {
						Localidad loc = locs.get(b);
						if (loc != null && loc.isNumerada() == false) {
							ArrayList<Tiquete> lista = loc.getTiquetes();
							int k = 0;
							while (adquirido == false & lista != null & k < lista.size()) {
								Tiquete t = lista.get(k);
								if (t != null && !(t instanceof TiqueteVendidoNumerado) && t.isEstaVendido() == false) {
									compras.add(t);
									total_a_pagar += t.getPrecio();
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
			// Si un evento no tiene disponibilidad, se omite
		}

		//Paso 3: Verificar saldo
		if (compras.isEmpty()) {
			throw new IllegalStateException("No se encontraron tiquetes disponibles para paquete deluxe.");
		}
		if (consultar_saldo_disponible() < total_a_pagar) {
			throw new IllegalStateException("Saldo insuficiente para paquete deluxe. Requiere: " + total_a_pagar + ", disponible: " + consultar_saldo_disponible());
		}

		//Paso 4: Efectuar compras
		setSaldoPlataforma(consultar_saldo_disponible() - total_a_pagar);
		if (this.tiquetes == null) { this.tiquetes = new ArrayList<Tiquete>(); }

		int c = 0;
		while(c < compras.size()) {
			Tiquete t = compras.get(c);
			t.setEstaVendido(true);
			t.setCliente(this);
			this.tiquetes.add(t);
			c++;
		}
	}
	
	public void transferir_tiquete() {}
	public void transferir_paquete_multiple() {}
	
	
}

