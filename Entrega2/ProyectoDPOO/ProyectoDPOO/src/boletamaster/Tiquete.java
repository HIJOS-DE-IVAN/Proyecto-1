package boletamaster;

import java.util.ArrayList;
import java.util.Date;

public class Tiquete {
    private double sobrecargo_porcentual_servicio;
    private double cuota_fija;
    private String identificador;
    private Date fecha;
    private int hora;
    
    //Si ya tiene relacion, no debería ir en atributos del UML.
    //private String localidad;
    
    private double precio;
    private boolean esTransferible;
    private boolean estaVendido;
    
    private Cliente cliente;
    private Localidad localidad;
    private ArrayList<TiqueteMultiplesPalco> tiquetesMultiplesPalco= new ArrayList<TiqueteMultiplesPalco>();

    public Tiquete(double sobrecargo_porcentual_servicio, double cuota_fija, String identificador,
                   Date fecha, int hora, Localidad localidad, double precio,
                   boolean esTransferible, boolean estaVendido, Cliente cliente) {
        this.sobrecargo_porcentual_servicio = sobrecargo_porcentual_servicio;
        this.cuota_fija = cuota_fija;
        this.identificador = identificador;
        this.fecha = fecha;
        this.hora = hora;
        this.localidad = localidad;
        this.precio = precio;
        this.esTransferible = esTransferible;
        this.estaVendido = estaVendido;
        this.cliente = cliente;
    }

	public double getSobrecargo_porcentual_servicio() {
		return sobrecargo_porcentual_servicio;
	}

	public void setSobrecargo_porcentual_servicio(double sobrecargo_porcentual_servicio) {
		this.sobrecargo_porcentual_servicio = sobrecargo_porcentual_servicio;
	}

	public double getCuota_fija() {
		return cuota_fija;
	}

	public void setCuota_fija(double cuota_fija) {
		this.cuota_fija = cuota_fija;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getHora() {
		return hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public boolean isEsTransferible() {
		return esTransferible;
	}

	public void setEsTransferible(boolean esTransferible) {
		this.esTransferible = esTransferible;
	}

	public boolean isEstaVendido() {
		return estaVendido;
	}

	public void setEstaVendido(boolean estaVendido) {
		this.estaVendido = estaVendido;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public Localidad getLocalidad() {
		return localidad;
	}

	public ArrayList<TiqueteMultiplesPalco> getTiquetesMultiplesPalco() {
		return tiquetesMultiplesPalco;
	}

	public void setTiquetesMultiplesPalco(ArrayList<TiqueteMultiplesPalco> tiquetesMultiplesPalco) {
		this.tiquetesMultiplesPalco = tiquetesMultiplesPalco;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

    //intentare crear una funcion que relacion las herencias de tiquete y hare expcetions
    
}
