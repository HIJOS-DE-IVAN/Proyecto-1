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
	public void comprar_palco() {}
	public void comprar_paquete_multiple_temporada() {}
	public void comprar_paquete_multiple_deluxe() {}
	public void transferir_tiquete() {}
	public void transferir_paquete_multiple() {}
	
	
}

