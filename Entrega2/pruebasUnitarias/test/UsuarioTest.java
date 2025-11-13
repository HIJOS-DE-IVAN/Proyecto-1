package test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boletamaster.Usuario;


public class UsuarioTest {
		private Usuario usuario;

	    @BeforeEach
	    void setUp() {
	        usuario = new Usuario("user123", "pass123");
	    }

	    @AfterEach
	    void tearDown() {
	        usuario = null;
	    }
	    // Constructor: inicializa campos correctamente
	    @Test
	    void testConstructor_InicializaLoginYPassword() {
	        assertEquals("user123", usuario.getLogin(), "Login no se inicializó correctamente");
	        assertEquals("pass123", usuario.getPassword(), "Password no se inicializó correctamente");
	    }
	    // Setters y getters de login
	    @Test
	    void testSetLogin_ModificaValor() {
	        usuario.setLogin("nuevoUser");
	        assertEquals("nuevoUser", usuario.getLogin(), "No se actualizó el login correctamente");
	    }
	    // Setters y getters de password
	    @Test
	    void testSetPassword_ModificaValor() {
	        usuario.setPassword("nuevaPass");
	        assertEquals("nuevaPass", usuario.getPassword(), "No se actualizó el password correctamente");
	    }
	    // Permitir valores null según implementación actual
	    @Test
	    void testSetLoginNullYPwdNull_Permitidos() {
	        usuario.setLogin(null);
	        usuario.setPassword(null);

	        assertNull(usuario.getLogin(), "Debería permitir login null con la implementación actual");
	        assertNull(usuario.getPassword(), "Debería permitir password null con la implementación actual");
	    }
	}
