package boletamaster;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario{
	
	private double saldoPlataforma;
	
	private List<Tiquete> tiquetes;
	
	public Administrador admin; //cada cliente tiene contacto con el administrador
	
	public Cliente(String login, String password, Double saldoPlataforma) {
		super(login,password);
		this.saldoPlataforma = saldoPlataforma;
		this.tiquetes= new ArrayList<Tiquete>();
	}

	public double getSaldoPlataforma() {
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
	
}

