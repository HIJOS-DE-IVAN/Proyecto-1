package boletamaster;

import java.util.ArrayList;

public class Localidad {
	
	private String id;
	private String nombre;
	private boolean numerada;
	private double precioBase;
	private int capacidad;
	
	private ArrayList<Tiquete> tiquetes;
	private ArrayList<Venue> venues;
	
	
	
	public Localidad(String id, String nombre, boolean numerada, double precioBase, int capacidad) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.numerada = numerada;
		this.precioBase = precioBase;
		this.capacidad = capacidad;
		this.tiquetes=new ArrayList<Tiquete>();
		this.venues= new ArrayList<Venue>();
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
	public boolean isNumerada() {
		return numerada;
	}
	public void setNumerada(boolean numerada) {
		this.numerada = numerada;
	}
	public double getPrecioBase() {
		return precioBase;
	}
	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public ArrayList<Tiquete> getTiquetes() {
		return tiquetes;
	}

	public void setTiquetes(ArrayList<Tiquete> tiquetes) {
		this.tiquetes = tiquetes;
	}

	public ArrayList<Venue> getVenues() {
		return venues;
	}

	public void setVenues(ArrayList<Venue> venues) {
		this.venues = venues;
	}
	
	
}
