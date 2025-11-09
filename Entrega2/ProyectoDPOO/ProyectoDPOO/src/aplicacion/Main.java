package aplicacion;

import boletamaster.Cliente;
import boletamaster.Evento;
import boletamaster.Localidad;
import boletamaster.Venue;
import boletamaster.OrganizadorDeEventos;
import boletamaster.Tiquete;
import boletamaster.TiqueteMultipleDeluxe;
import boletamaster.TiqueteMultipleTemporada;
import boletamaster.Administrador;

import persistencia.PersistenciaUsuarios;
import persistencia.PersistenciaEvento;
import persistencia.PersistenciaLocalidad;
import persistencia.PersistenciaVenue;
import persistencia.PersistenciaOrganizadores;
import persistencia.PersistenciaTiquete;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Main {

    private static void separador(String titulo) {
        System.out.println("\n----- " + titulo + " -----");
    }

    public static void main(String[] args) {

        PersistenciaUsuarios pc = new PersistenciaUsuarios();
        PersistenciaEvento pe = new PersistenciaEvento();
        PersistenciaLocalidad pl = new PersistenciaLocalidad();
        PersistenciaVenue pv = new PersistenciaVenue();
        PersistenciaOrganizadores po = new PersistenciaOrganizadores();
        PersistenciaTiquete pt = new PersistenciaTiquete();

        List<Cliente> clientes = pc.cargarClientes();
        List<Administrador> administradores = pc.cargarAdministradores();
        List<Evento> eventos = pe.cargarEventos();
        List<Localidad> localidades = pl.cargarLocalidades();
        List<Venue> venues = pv.cargarVenues();
        List<OrganizadorDeEventos> organizadores= po.cargarOrganizadores();
       

        System.out.println("===== Resumen de datos =====");
        System.out.println("Clientes:     " + clientes.size());
        System.out.println("Administradores:     " + administradores.size());
        System.out.println("Eventos:      " + eventos.size());
        System.out.println("Localidades:  " + localidades.size());
        System.out.println("Venues:       " + venues.size());
        System.out.println("Organizadores:       " + organizadores.size());
        
        
        HashMap<String, Cliente> usuariosPorLogin = new HashMap<>();
	     for (Cliente c : clientes) {
	         usuariosPorLogin.put(c.getLogin(), c);
	     }
	
	     HashMap<String, Localidad> localidadesPorNombre = new HashMap<>();
	     for (Localidad l : localidades) {
	         localidadesPorNombre.put(l.getNombre(), l);
	     }

        List<Tiquete> tiquetes = pt.cargarTiquetes(usuariosPorLogin,localidadesPorNombre);
        List<TiqueteMultipleDeluxe> deluxe = pt.cargarTiquetesMultipleDeluxe();
        List<TiqueteMultipleTemporada> temporada = pt.cargarTiquetesMultipleTemporada();
        System.out.println("Tiquetes:       " + tiquetes.size());
        System.out.println("Tiquetes Deluxe:    " + deluxe.size());
        System.out.println("Tiquetes Temporada:    " + temporada.size());
        System.out.println("============================\n");
        
        
        if (!clientes.isEmpty()) {
            System.out.println("Primer Cliente:");
            System.out.println(clientes.get(0));
            System.out.println();
        }

        if (!eventos.isEmpty()) {
            System.out.println("Primer Evento:");
            System.out.println(eventos.get(0));
            System.out.println();
        }

        if (!localidades.isEmpty()) {
            System.out.println("Primera Localidad:");
            System.out.println(localidades.get(0));
            System.out.println();
        }

        if (!venues.isEmpty()) {
            System.out.println("Primer Venue:");
            System.out.println(venues.get(0));
            System.out.println();
        }
        if (!organizadores.isEmpty()) {
            System.out.println("Primer Organizador:");
            System.out.println(organizadores.get(0));
            System.out.println();
        }
        if (!administradores.isEmpty()) {
            System.out.println("Primer administrador:");
            System.out.println(administradores.get(0));
            System.out.println();
        }



        System.out.println("Pruebas de la persistencia:");
        
        
        clientes.get(0).setSaldoPlataforma(2000000);
        pc.guardarClientes(clientes);
        eventos.get(0).setTipoEvento("NombreCambiado");
        pe.guardarEventos(eventos);
        localidades.get(0).setNombre("NombreAlterado");
        pl.guardarLocalidades(localidades);
        venues.get(0).setNombre("NombreCambiadoVenue");
        pv.guardarVenues(venues);
        organizadores.get(0).setLogin("OtroLogin");
        po.guardarOrganizadores(organizadores);
        
        List<Cliente> clientesChanged = pc.cargarClientes();
        List<Evento> eventosChanged = pe.cargarEventos();
        List<Localidad> localidadesChanged = pl.cargarLocalidades();
        List<Venue> venuesChanged = pv.cargarVenues();
        List<OrganizadorDeEventos> organizadoresChanged = po.cargarOrganizadores();
        
        System.out.println("Cambio en Saldo" + clientesChanged.get(0));
        System.out.println("Cambio en Tipo" + eventosChanged.get(0));
        System.out.println("Cambio en Nombre" + localidadesChanged.get(0));
        System.out.println("Cambio en Nombre" + venuesChanged.get(0));
        System.out.println("Cambio en Login" + organizadoresChanged.get(0));
        
        
        
      
        // ----- Pruebas Cliente -----
        if (!clientes.isEmpty()) {
            System.out.println("PRUEBAS Cliente");
            Cliente c0 = clientes.get(0);

            System.out.println("Saldo inicial: " + c0.consultar_saldo_disponible());

            System.out.println("Intentando comprar tiquete no numerado...");
            HashMap<String, Evento> mapaEventos = new java.util.HashMap<>();
            
            mapaEventos.put(eventos.get(0).getId(), eventos.get(0));
            mapaEventos.put(eventos.get(0).getTipoEvento(), eventos.get(0));
            c0.setMapa_eventos(mapaEventos);
            c0.setAdmin(administradores.get(0));
            ArrayList<Localidad> locals= new ArrayList<Localidad>();
            venues.get(0).setLocalidades(locals);
            venues.get(0).getLocalidades().add(localidades.get(0));
            eventos.get(0).getVenues().add(venues.get(0));
            eventos.get(0).getVenues().remove(0);
            localidades.get(0).setTiquetes(new  ArrayList<Tiquete>());
            localidades.get(0).getTiquetes().add(tiquetes.get(0));
            System.out.println(localidades.get(0));
            try {
            	c0.comprar_tiquete_no_numerado("EVT001", "NombreAlterado");
            }catch(Exception e) {
            	e.printStackTrace();
            }
            System.out.println("Saldo después de compra: " + c0.consultar_saldo_disponible());
            System.out.println("Tiquetes del cliente ahora: " + c0.getTiquetes()); // si tienes otro getter dime cuál

            /**
             * Dice que esta en proceso
             
            System.out.println("\nSolicitando reembolso...");
            c0.solicitar_reembolso("EVT001", "Motivo de prueba");
            System.out.println("Historial de reembolsos: " + c0.getSolicitudesReembolso());*/
            
        }
        
        
        // ----- Pruebas OrganizadorDeEventos -----
        if (!clientes.isEmpty()) {
            System.out.println("PRUEBAS Organizador de eventos");
     
            OrganizadorDeEventos org = organizadores.get(0);
            Evento e0 = eventos.get(0);
            Venue v0 = venues.get(0);
            Localidad l0 = localidades.get(0);

            org.getEventos().put(e0.getId(), e0);
            org.mapa_venues.put(v0.getNombre(), v0);
            
            boolean l0Dentro = false;
            for (Localidad lx : v0.getLocalidades()) {
                if (lx.getNombre().equals(l0.getNombre())) { l0Dentro = true; break; }
            }
            if (!l0Dentro) {
                v0.getLocalidades().add(l0);
            }
            
            System.out.println("\n-- crear_localidad_venue (antes/después) --");
            System.out.println("Localidades en venue ANTES: " + (v0.getLocalidades() != null ? v0.getLocalidades().size() : 0));
            org.crear_localidad_venue(v0.getNombre(), "LocalidadPrueba", false, 100000.0, 500);
            System.out.println("Localidades en venue DESPUÉS: " + v0.getLocalidades().size());
            // Mostrar última localidad creada
            System.out.println("\n-- crear_evento (efecto en el evento creado) --");
            int venuesAntes = (e0.getVenues() != null) ? e0.getVenues().size() : 0;
            org.crear_evento("EVT-ORG-1", new java.util.Date(), "OrgShow", "Programado", v0.getNombre());
            System.out.println("Evento existente (e0) venues ANTES: " + venuesAntes + " | AHORA: " + e0.getVenues().size());
            
            if (!l0.getTiquetes().isEmpty()) {
                double precioAntes = l0.getTiquetes().get(0).getPrecio();
                System.out.println("Precio primer tiquete ANTES: " + precioAntes);
                org.crear_oferta_localidad(e0.getId(), l0.getNombre(), 0.10); // 10% de descuento
                double precioDespues = l0.getTiquetes().get(0).getPrecio();
                System.out.println("Precio primer tiquete DESPUÉS: " + precioDespues);
            } else {
                System.out.println("No hay tiquetes en la localidad para probar oferta.");
            }
            
            // Sugerir_venue y sugerir_cancelar_evento
            if (!administradores.isEmpty()) {
                System.out.println("\n-- sugerir_venue / sugerir_cancelar_evento (antes/después en mapas del admin) --");
                org.admin = administradores.get(0);

                int sugeridosAntes = org.admin.getMapa_venues_sugeridos().size();
                org.sugerir_venue("NuevoSugerido", "Ciudad X", 12345, "Restricción Y");
                int sugeridosDespues = org.admin.getMapa_venues_sugeridos().size();
                System.out.println("Sugeridos ANTES: " + sugeridosAntes + " | DESPUÉS: " + sugeridosDespues);

                int solicitudesAntes = org.admin.getMapa_solicitudes().size();
                org.sugerir_cancelar_evento(e0.getId(), "Motivo de prueba");
                int solicitudesDespues = org.admin.getMapa_solicitudes().size();
                System.out.println("Solicitudes ANTES: " + solicitudesAntes + " | DESPUÉS: " + solicitudesDespues);
            } else {
                System.out.println("\n(No hay administradores para probar sugerencias del organizador).");
            }
            

        System.out.println("\n==== FIN ====");
    }

}
}