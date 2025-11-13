package boletamaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.UUID;

public class Cliente extends Usuario{
	
	private double saldoPlataforma;
	
	public double getSaldoPlataforma() {
		return saldoPlataforma;
	}
	private List<Tiquete> tiquetes;
	
	public Administrador admin; //cada cliente tiene contacto con el administrador
	
	public  HashMap<String, Evento> mapa_eventos;
	
	//NUEVO
	public HashMap<Integer, Operacion> solicitudes_market_place; //ID de la solicitud, operacion
	
	public HashMap<Integer, Operacion> getSolicitudes_market_place() {
		return solicitudes_market_place;
	}

	public void setSolicitudes_market_place(HashMap<Integer, Operacion> solicitudes_market_place) {
		this.solicitudes_market_place = solicitudes_market_place;
	}

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
	//TO-DO DEBE QUEDAR EN EL MAPA DEL MARKETPLACE
	//NO SE PUEDE DELUXE
	
	//En realidad la oferta ya hace que no tenga que crear una función que venda el tiquete
	//Pues el tiquete se venderá automáticamente 
	//PUBLICAR OFERTA
	public void publicar_oferta_Tiquete(Tiquete tiquete_a_vender) {
		//realizar la operacion
		Operacion ope = new Operacion(admin,this, null, true,tiquete_a_vender );
		ope.publicar_oferta_Tiquete();
	}
	//BORRAR OFERTA
	public void borrar_oferta(int id_operacion) {
		//Obtenemos a la operacion
		Operacion ope = solicitudes_market_place.get(id_operacion);
		ope.borrar_oferta();
	}
	
	public void comprar_tiquete(int id_compra) {
		Operacion operacion_encontrada = admin.buscar_operacion(id_compra);		
		//Si encontramos la operacion y si tenemos suficnete dinero
		if (operacion_encontrada != null && operacion_encontrada.getTiquet_ope().getPrecio() <= saldoPlataforma) {
			operacion_encontrada.comprar_tiquete(this); //yo compro el tiquete
		}
	}
	
	//CONTRAOFERTAR
	public void contra_ofertar(int id_compra, double valor_contra_oferta) {
		Operacion operacion_encontrada = admin.buscar_operacion(id_compra);	
		
		if (operacion_encontrada != null) {
			operacion_encontrada.contra_ofertar(this, valor_contra_oferta); //yo contraoferto
		}
		
	}
	
	//aceptar una contra oferta
	public void aceptar_contra_ofertar(int id_compra) {
		
		//buscar dentro de mi mapa de solicitudes a la operación
		Operacion operacion_encontrada = null;
		for (Operacion op : solicitudes_market_place.values()) {
	        if (op.id == id_compra && op.esta_activa) {
	            operacion_encontrada = op;
	            break;
	        }
	    }
		
		if (operacion_encontrada != null) {
			operacion_encontrada.aceptar_contra_oferta(); //yo acepto la contra_oferta
		}
		
		
	}
	//rechazar una contra oferta
public void rechazar_contra_ofertar(int id_compra) {
		
		//buscar dentro de mi mapa de solicitudes a la operación
		Operacion operacion_encontrada = null;
		for (Operacion op : solicitudes_market_place.values()) {
	        if (op.id == id_compra && op.esta_activa) {
	            operacion_encontrada = op;
	            break;
	        }
	    }
		
		if (operacion_encontrada != null) {
			operacion_encontrada.rechazar_contra_ofertar(); //yo rechazo la contra_oferta
		}
		
		
	}

//inscribirse en el marketplace
public void inscribirse_marketplace() {
	HashMap<String, Cliente> clientes_inscritos_marketplace = admin.getClientes_inscritos_marketplace();
	clientes_inscritos_marketplace.put(getLogin() , this);
	 admin.setClientes_inscritos_marketplace(clientes_inscritos_marketplace);
	
}
	

	
	
	
}

