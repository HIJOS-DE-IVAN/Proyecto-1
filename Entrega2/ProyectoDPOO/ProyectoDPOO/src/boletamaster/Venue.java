package boletamaster;

import java.util.ArrayList;

public class Venue {
	
	private String id;
	private String nombre;
	private String ubicacion;
	private int capacidadMax;
	private String restriccionesUso;
	private boolean aprobado;
	
	private ArrayList<Evento> evento;

    private ArrayList<Localidad> localidades;
	
	public Venue(String id, String nombre, String ubicacion, int capacidadMax, String restriccionesUso,
			boolean aprobado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.ubicacion = ubicacion;
		this.capacidadMax = capacidadMax;
		this.restriccionesUso = restriccionesUso;
		this.aprobado = aprobado;
		this.evento = new ArrayList<Evento>();
		this.localidades= new ArrayList<Localidad>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public int getCapacidadMax() {
		return capacidadMax;
	}
	public void setCapacidadMax(int capacidadMax) {
		this.capacidadMax = capacidadMax;
	}
	public String getRestriccionesUso() {
		return restriccionesUso;
	}
	public void setRestriccionesUso(String restriccionesUso) {
		this.restriccionesUso = restriccionesUso;
	}
	public boolean isAprobado() {
		return aprobado;
	}
	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}

	public ArrayList<Evento> getEvento() {
		return evento;
	}

	public void setEvento(ArrayList<Evento> evento) {
		this.evento = evento;
	}

	public ArrayList<Localidad> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(ArrayList<Localidad> localidades) {
		this.localidades = localidades;
	}
	
	
	
	
	
}
