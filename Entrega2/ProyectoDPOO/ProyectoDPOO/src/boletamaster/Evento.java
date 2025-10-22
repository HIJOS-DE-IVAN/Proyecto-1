package boletamaster;

import java.util.ArrayList;
import java.util.Date;

public class Evento {
	
	private String id;
	private Date fecha;
	private String tipoEvento;
	private String estadoEvento;
	public double sobrecargo; //le agregamos el sobrecargo, el cual editara el administrador
	
	private OrganizadorDeEventos organizador;
	private ArrayList<Venue> venues;
	
	public Evento(String id, Date fecha, String tipoEvento, String estadoEvento) {
		this.id = id;
		this.fecha = fecha;
		this.tipoEvento = tipoEvento;
		this.estadoEvento = estadoEvento;
		this.venues = new ArrayList<Venue>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public double getSobrecargo() {
		return sobrecargo;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public String getEstadoEvento() {
		return estadoEvento;
	}
	public void setEstadoEvento(String estadoEvento) {
		this.estadoEvento = estadoEvento;
	}

	public OrganizadorDeEventos getOrganizador() {
		return organizador;
	}

	public void setOrganizador(OrganizadorDeEventos organizador) {
		this.organizador = organizador;
	}
	
	public void setSobrecargo(double sobrecargo) {
		this.sobrecargo=  sobrecargo;
	}

	public ArrayList<Venue> getVenues() {
		return venues;
	}

	public void setVenues(ArrayList<Venue> venues) {
		this.venues = venues;
	}
	
}
