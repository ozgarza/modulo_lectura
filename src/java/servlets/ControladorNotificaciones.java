package servlets;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
/**
 * Servlet implementation class ControladorNotificaciones
 */
@WebServlet("/ControladorNotificaciones")
public class ControladorNotificaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorNotificaciones() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acepta = (String) request.getParameter("acepta");
		String rechaza = (String) request.getParameter("rechaza");
		String tipo = (String) request.getParameter("tipo");
		String id = (String)request.getParameter("id");
		String esAdmin = (String)request.getParameter("Admin");
		
		if(acepta.equals("true")){
			String url = "jdbc:mysql://localhost/SISCON";
	        try {
	            Connection con = (Connection) DriverManager.getConnection(url,"root","");
	            Statement query = (Statement) con.createStatement();
	            if(esAdmin != null){
	            	query.executeUpdate("UPDATE Usuario SET alta = 1,rol ='"+tipo+"',administrador = 1 WHERE indexUsuario='"+ id +"'");
	            }else{
	            	query.executeUpdate("UPDATE Usuario SET alta = 1,rol ='"+tipo+"' WHERE indexUsuario='"+ id +"'");
	            }
	            query.executeUpdate("delete from tablaNotificacion where indexUsuario ='"+ id +"'");
	        } catch (SQLException ex) {
//	            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
	        }
		}else if(rechaza.equals("true")){
			String url = "jdbc:mysql://localhost/SISCON";
	        try {
	            Connection con = (Connection) DriverManager.getConnection(url,"root","");
	            Statement query = (Statement) con.createStatement();
	            query.executeUpdate("DELETE FROM tablaNotificacion WHERE indexUsuario ='"+ id +"'");
	        } catch (SQLException ex) {
//	            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
	        }
		}
		String url = "/notificaciones.jsp";
		ServletContext sc = this.getServletContext();
        RequestDispatcher dispatcher = sc.getRequestDispatcher(url);
        dispatcher.forward(request, response);
		
	}
}
