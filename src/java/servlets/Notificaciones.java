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
 * Servlet implementation class Notificaciones
 */
@WebServlet("/Notificaciones")
public class Notificaciones extends HttpServlet {
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

		        String q = "SELECT nombreUsuario, apellidoUsuario FROM Usuario WHERE idUsuario='" + matricula + "' and password ='" + contrasenia + "'";
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
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Notificaciones() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
