package boletamaster;

import java.util.ArrayList;
import java.util.List;

public class MultiEvento {
	
	
	//Supongo que existen estas relaciones de 1 a muchos
	private List<TiqueteMultipleTemporada> temporadas;
    private List<TiqueteMultipleDeluxe> deluxes;
    
    private List<Evento> eventos;
	
    
	public MultiEvento() {
		this.temporadas = new ArrayList<TiqueteMultipleTemporada>();
		this.deluxes = new ArrayList<TiqueteMultipleDeluxe>();
		this.eventos = new ArrayList<Evento>();
	}

    
    public List<TiqueteMultipleTemporada> getTemporadas() {
		return temporadas;
	}
	public List<TiqueteMultipleDeluxe> getDeluxes() {
		return deluxes;
	}


	public List<Evento> getEventos() {
		return eventos;
	}


	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}


	public void setTemporadas(List<TiqueteMultipleTemporada> temporadas) {
		this.temporadas = temporadas;
	}


	public void setDeluxes(List<TiqueteMultipleDeluxe> deluxes) {
		this.deluxes = deluxes;
	}
       
	
    
}
