package boletamaster;

import java.util.HashMap;

import java.time.LocalDateTime;

public class Operacion {
	public Administrador admin;
	public Cliente parte_1;
	public Cliente parte_2;
	public LocalDateTime fechaHora;
	public boolean esta_activa; //si la operacion se sigue pudiendo realizar
	
	private static int contadorID = 0;
	public int id;
	
	public Tiquete tiquet_ope;
	
	
	//para realizar cualquier operacion, necesitamos dos partes
	public Operacion(Administrador admin,Cliente parte_1, Cliente parte_2, boolean esta_activa, Tiquete tiquet_op) {
		
		this.admin = admin;
		this.parte_1 = parte_1;
		this.parte_2 = parte_2; //si no es ninguna compra, es por defecto Null
		this.esta_activa = esta_activa;
		this.fechaHora = LocalDateTime.now();;
		this.tiquet_ope = tiquet_op; //el tiquete de la operacion
		this.id = ++contadorID; //identificador de la operacion
	}
	

	public Administrador getAdmin() {
		return admin;
	}

	public Cliente getParte_1() {
		return parte_1;
	}

	public Cliente getParte_2() {
		return parte_2;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public boolean isEsta_activa() {
		return esta_activa;
	}

	public static int getContadorID() {
		return contadorID;
	}

	public int getId() {
		return id;
	}

	public Tiquete getTiquet_ope() {
		return tiquet_ope;
	}

	
	public void publicar_oferta_Tiquete() {
		//Ver si el tiquete sea transferible
		if (tiquet_ope.isEsTransferible()) {

			//obtenemos el mapa de registros
			HashMap<LocalDateTime, Operacion> log_registros = admin.getLog_registros();
		
			//le quitamos el tiquete a la parte_1
			parte_1.getTiquetes().remove(tiquet_ope);
			
			//le añadimos al usuario la operación a su mapa de solicitudes de marketplace
			parte_1.getSolicitudes_market_place().put(this.id, this);
			
			//agregamos la operacion al log
			log_registros.put(fechaHora, this);		
			
		}
	}
	
	public void borrar_oferta() {		
		//Modificar el estado activo, esto ya se modifica dentro del mismo mapa
		this.esta_activa = false;	
		
		//Quitarle la solicitud al cliente
		parte_1.getSolicitudes_market_place().remove(this.id);
		//Devolverle el tiquete al cliente
		parte_1.getTiquetes().add(this.tiquet_ope);
		
	}
	
	public void setParte_1(Cliente parte_1) {
		this.parte_1 = parte_1;
	}


	public void setParte_2(Cliente parte_2) {
		this.parte_2 = parte_2;
	}


	public void comprar_tiquete(Cliente comprador) {
		//La idea es que en el sistema aparezcan todos los id_s de las transacciones
		//Entonces el usuario pueda escoger cuál id_transaccion le interesa
		
		//Toca cambiar el estado de la transacción actual (yo mismo)
		this.esta_activa = false;
		
		
		//crear una nueva operacion
		//ahora la parte_2 es comprador
		Operacion nueva_operacion = new Operacion(admin,parte_1, comprador, false, tiquet_ope);
		
		//añadir la operación al sistema
		admin.getLog_registros().put(nueva_operacion.getFechaHora(), nueva_operacion);
		
		//Quitarle el dinero al comprador
		double saldo_anterior = comprador.getSaldoPlataforma();
		comprador.setSaldoPlataforma(saldo_anterior-tiquet_ope.getPrecio());
		
		//añadirle al comprador el tiquete comprado
		comprador.getTiquetes().add(tiquet_ope);
		
		//darle el dinero al vendedor
		parte_1.setSaldoPlataforma(parte_1.getSaldoPlataforma()+tiquet_ope.getPrecio());
		
	}
	
	public void contra_ofertar(Cliente comprador, double valor_contra_oferta) {
		//contra ofertar significa añadirle a la parte_1 en su lista de solicitudes_market_place una operacion
		//la parte 2 es el comprador
		//la nueva transacción va a tener que el precio del tiquete es el propuesto por el comprador
		tiquet_ope.setPrecio(valor_contra_oferta);
		Operacion nueva_operacion = new Operacion (admin, parte_1,comprador, true, tiquet_ope);
		parte_1.getSolicitudes_market_place().put(nueva_operacion.getId(), nueva_operacion);	
		
		//lo añadimos al mapa de log
		//añadir la operación al sistema
		admin.getLog_registros().put(nueva_operacion.getFechaHora(), nueva_operacion);
				
	}
	
	public void aceptar_contra_oferta() {
		//esto funciona como comprar un tiquete
		//Toca cambiar el estado de la transacción actual (yo mismo)
		this.esta_activa = false;
		
		
		//crear una nueva operacion
		//ahora la parte_2 es la de quien hizo la contra_oferta
		Operacion nueva_operacion = new Operacion(admin,parte_1, parte_2, false, tiquet_ope);
		
		//añadir la operación al sistema
		admin.getLog_registros().put(nueva_operacion.getFechaHora(), nueva_operacion);
		
		//Quitarle el dinero al comprador
		double saldo_anterior = parte_2.getSaldoPlataforma();
		parte_2.setSaldoPlataforma(saldo_anterior-tiquet_ope.getPrecio());
		
		//añadirle al comprador el tiquete comprado
		parte_2.getTiquetes().add(tiquet_ope);
		
		//quitarle al usuario de su mapa de solicitudes 
		parte_1.getSolicitudes_market_place().remove(nueva_operacion.getId());
		
		//darle el dinero al vendedor
		parte_1.setSaldoPlataforma(parte_1.getSaldoPlataforma()+tiquet_ope.getPrecio());
		
	}
	public void rechazar_contra_ofertar() {
		this.esta_activa = false; //ya nosotros mismos no estamos activos
		Operacion nueva_operacion = new Operacion(admin,parte_1, parte_2, false, tiquet_ope); //esto lo hacemos para reportar que efectivamente se canceló
		//quitarle al usuario de su mapa de solicitudes
		parte_1.getSolicitudes_market_place().remove(this.getId());
		//añadir la nueva operacion a la log
		admin.getLog_registros().put(nueva_operacion.getFechaHora(), nueva_operacion);
	}

}
