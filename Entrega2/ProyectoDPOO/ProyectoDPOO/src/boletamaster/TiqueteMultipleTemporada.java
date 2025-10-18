package boletamaster;

import java.util.Date;

public class TiqueteMultipleTemporada {
	
 private int precio;
 private double cargo_porcentual_servicio;
 private double cuota_fija;
 private Date fecha;
 private int hora;
 private String identificador;
 private boolean esTransferible;
 
 
 private MultiEvento multiEvento;

 public TiqueteMultipleTemporada(int precio, double cargo_porcentual_servicio, double cuota_fija,
                                 Date fecha, int hora, String identificador, boolean esTransferible, MultiEvento multiEvento) {
     this.precio = precio;
     this.cargo_porcentual_servicio = cargo_porcentual_servicio;
     this.cuota_fija = cuota_fija;
     this.fecha = fecha;
     this.hora = hora;
     this.identificador = identificador;
     this.esTransferible = esTransferible;
     this.multiEvento=multiEvento;
 }

public int getPrecio() {
	return precio;
}

public void setPrecio(int precio) {
	this.precio = precio;
}

public double getCargo_porcentual_servicio() {
	return cargo_porcentual_servicio;
}

public void setCargo_porcentual_servicio(double cargo_porcentual_servicio) {
	this.cargo_porcentual_servicio = cargo_porcentual_servicio;
}

public double getCuota_fija() {
	return cuota_fija;
}

public void setCuota_fija(double cuota_fija) {
	this.cuota_fija = cuota_fija;
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

public String getIdentificador() {
	return identificador;
}

public void setIdentificador(String identificador) {
	this.identificador = identificador;
}

public boolean isEsTransferible() {
	return esTransferible;
}

public void setEsTransferible(boolean esTransferible) {
	this.esTransferible = esTransferible;
}

public MultiEvento getMultiEvento() {
	return multiEvento;
}

public void setMultiEvento(MultiEvento multiEvento) {
	this.multiEvento = multiEvento;
}

 	

}
