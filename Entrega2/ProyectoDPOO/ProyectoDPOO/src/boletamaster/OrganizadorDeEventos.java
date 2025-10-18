package boletamaster;

import java.util.ArrayList;

public class OrganizadorDeEventos extends Usuario{
	
	private ArrayList<Evento> eventos; 
	private ArrayList<Tiquete> tiquetes;
	
	public OrganizadorDeEventos(String login, String password) {
		super(login,password);
		this.eventos= new ArrayList<Evento>();
		this.tiquetes = new ArrayList<Tiquete>();
	}

	public ArrayList<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(ArrayList<Evento> eventos) {
		this.eventos = eventos;
	}

	public ArrayList<Tiquete> getTiquetes() {
		return tiquetes;
	}

	public void setTiquetes(ArrayList<Tiquete> tiquetes) {
		this.tiquetes = tiquetes;
	}
	
}

