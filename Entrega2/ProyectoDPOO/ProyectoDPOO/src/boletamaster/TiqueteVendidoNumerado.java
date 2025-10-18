package boletamaster;

import java.util.Date;

public class TiqueteVendidoNumerado extends Tiquete {
    private int numero_asiento;

    public TiqueteVendidoNumerado(double sobrecargo_porcentual_servicio, double cuota_fija, String identificador,
            Date fecha, int hora, Localidad localidad, double precio,
            boolean esTransferible, boolean estaVendido, Cliente cliente, int numero_asiento) {
    	
        super(sobrecargo_porcentual_servicio, cuota_fija, identificador, fecha, hora, localidad, precio, esTransferible, estaVendido, cliente);
        this.numero_asiento = numero_asiento;
    }

    public int getNumero_asiento() { 
    	return numero_asiento; 
    }
    public void setNumero_asiento(int numero_asiento) { 
    	this.numero_asiento = numero_asiento; 
    }
}
