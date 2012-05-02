/*
 * Servlet para registrar en la base de datos la informacion de la lectura del
 * documento tipo excel.
 */
package servlets;
//Librerias necesarias
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet registroExcel
 * @author Oziel
 */
public class registroExcel extends HttpServlet {
private static Connection connection;
    /** 
     * Procesa las peticiones para los metodos HTTP <code>GET</code> y <code>POST</code>.
     * Registra en la base de datos la informacion de la lectura, almacena la fecha y hora, 
     * el departamento, y el nombre del archivo. 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Informacion que sera registrada
        int Uid = Integer.parseInt(request.getParameter("idUsuario"));
        String deptPlan = request.getParameter("deptPlan");
        String fecha = request.getParameter("fecha");
        String hora = request.getParameter("hora");
        String nombreArchivo = request.getParameter("nombreArchivo");
        int tipo=Integer.parseInt(request.getParameter("tipo"));
        
        try {
            //Establecer la conexion a la base de datos
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/siscon?user=root");
            Statement statement = connection.createStatement();
            //Consulta para obtener el id del departamento de acuerdo a su nombre
            //ResultSet results = statement.executeQuery("SELECT idDepartamento FROM siscon.departamento WHERE departamento='" + departamento + "'");
            //if (results.next()) {
            //    Did = results.getInt(1);
            //}
            //Consulta para saber si ya existe un registro de un departamento de un mismo usuario          
            ResultSet results2 = statement.executeQuery("SELECT * FROM siscon.registroexcel WHERE deptPlan='" + deptPlan + "' AND "
                    + "indexUsuario=" + Uid);
            //Si existe, borra el registro
            if (results2.next()) {
                statement.executeUpdate("DELETE FROM siscon.registroexcel WHERE idregistroExcel=" + results2.getString(1));
            }
            //Registra la informacion en la base de datos.
            statement.executeUpdate("INSERT INTO `siscon`.`registroexcel` ( `indexUsuario`,"
                    + " `deptPlan`, `fecha`, `nombreArchivo`,`tipo`) VALUES (" + Uid + ",'" + deptPlan + "','" + fecha 
                    + " " + hora + "'," + "'" + nombreArchivo + "', "+ tipo +")");

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Enviar la respuesta
        PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        out.println("<response>\n");
        out.println("\t<fecha>" + fecha + "</fecha>\n");
        out.println("\t<hora>" + hora + "</hora>\n");
        out.println("\t<deptPlan>" + deptPlan + "</deptPlan>\n");
        out.println("\t<nombreArchivo>" + nombreArchivo + "</nombreArchivo>\n");
        out.println("\t<tipo>" + tipo + "</tipo>\n");
        out.println("</response>\n");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
