package boletamaster;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario{
	
	private double saldoPlataforma;
	
	private List<Tiquete> tiquetes;
	
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
	
}

