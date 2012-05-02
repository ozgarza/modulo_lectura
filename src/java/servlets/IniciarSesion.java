package servlets;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import clases.Usuario;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
*
* @author Ana
*/
@WebServlet("/IniciarSesion")
public class IniciarSesion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException, SQLException {
	        response.setContentType("text/html;charset=UTF-8");
	        try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        String url = "jdbc:mysql://localhost/SISCON";
	        Connection con = (Connection) DriverManager.getConnection(url, "root", "");
	        Statement query = (Statement) con.createStatement();
	        
	        HttpSession session = request.getSession();
	        
	        int cont = 0;
	        String msg = "";
	        String forward;
	        
	        //Variables del usuario
	        String matricula = request.getParameter("usuario");
	        String contrasenia = request.getParameter("pswd");
	        int idU = 0;
	    	int idD = 0;
	    	String nombre = "";
	    	String apellido = "";
	    	char genero = ' ';    	
	    	String email = "";
	    	boolean alta = false;
	    	boolean esAdmin = false;
	    	char rol = ' ';
//	    	String telefonos[] = {"0", "0", "0", "0", "0"};
	    	
	    	
	        String q = "SELECT * FROM Usuario WHERE idUsuario='" + matricula + "' and password ='" + contrasenia + "'";
	        ResultSet rs = query.executeQuery(q);
	        while (rs.next()) {
	        	cont++;
	        	idU = rs.getInt("indexUsuario");
	        	idD = rs.getInt("idDepartamento");
	        	nombre = rs.getString("nombreUsuario");
	        	apellido = rs.getString("apellidoUsuario");
	        	genero = rs.getString("genero").charAt(0);
	        	email = rs.getString("email");
	        	alta = rs.getBoolean("alta");
	        	esAdmin = rs.getBoolean("administrador");
	        	rol = rs.getString("rol").charAt(0);
	        }

	        if (cont == 1) {
//	        	q = "SELECT telefono, extension FROM Telefono, Usuario WHERE Telefono.indexUsuario = Usuario.indexUsuario AND Usuario.idUsuario = '" + matricula + "'";
//	            rs = query.executeQuery(q);
//	            int i = 0;
//	            while (rs.next()) {
//	            	telefonos[i] = rs.getString("telefono");
//	            	if(!rs.getString("extension").equals("")) {
//	            		telefonos[i] += "-" + rs.getString("extension");
//	            	}
//	            	i++;
//	            }
	        	msg = "";
	            Usuario usuario = new Usuario();
	            
	            usuario.setMatricula(matricula);
	            usuario.setContrasenia(contrasenia);
	            usuario.setIdU(idU);
	            usuario.setIdD(idD);
	            usuario.setNombre(nombre);
	            usuario.setApellido(apellido);
	            usuario.setGenero(genero);
	            usuario.setEmail(email);
	            usuario.setAlta(alta);
	            usuario.setEsAdmin(esAdmin);
	            usuario.setRol(rol);
//	            usuario.setTelefonos(telefonos);
	            
	            if (alta) {
	            	session.setAttribute("usuario", usuario);
		            forward = "/bienvenido.jsp";
		            	
	            }
	            else {
	            	msg = "Porfavor espera a que confirmen tu solicitud";
		            forward = "/index.jsp";
	            }
	            
	        } else {
	            msg = "Usuario o contrase&ntilde;a incorrecta";
	            forward = "/index.jsp";
	        }
	        
	        session.setAttribute("msg", msg);

	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forward);
	        dispatcher.forward(request, response);
	     
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
    @Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null; 
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(IniciarSesion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(IniciarSesion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
