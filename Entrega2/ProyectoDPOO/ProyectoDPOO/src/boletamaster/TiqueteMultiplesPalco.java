package boletamaster;

import java.util.ArrayList;
import java.util.List;

public class TiqueteMultiplesPalco {
    private int cantidad;
    private double precio;
    
    private ArrayList<Tiquete> tiquetes;

    public TiqueteMultiplesPalco(int cantidad, double precio) {
        this.cantidad = cantidad;
        this.precio = precio;
        this.tiquetes = new ArrayList<>();
    }
    public int getCantidad() { 
    	return cantidad; 
    	}
    
    public void setCantidad(int cantidad) { 
    	this.cantidad = cantidad; 
    	}

    public double getPrecio() { 
    	return precio; 
    	}
    
    public void setPrecio(double precio) { 
    	this.precio = precio; 
    	}

    public List<Tiquete> getTiquetes() { 
    	return tiquetes; 
    	}
    
	public void setTiquetes(ArrayList<Tiquete> tiquetes) {
		this.tiquetes = tiquetes;
	}
    

}
