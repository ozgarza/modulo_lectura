/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Ruben
 */
@WebServlet(name = "PlanReader", urlPatterns = {"/PlanReader"})
public class PlanReader extends HttpServlet {
    
         static String plan="";
        public static ArrayList readExcelFile(String fileName){
        
        ArrayList cellVectorHolder= new ArrayList();
        
         try{
       
             InputStream is =new BufferedInputStream(new FileInputStream(fileName));
 
        /** Create a workbook using the File System**/
         XSSFWorkbook myWorkBook = new XSSFWorkbook(is);
 
         /** Get the first sheet from workbook**/
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
 
        /** We now need something to iterate through the cells.**/
          Iterator rowIter = mySheet.rowIterator(); 
 
          while(rowIter.hasNext()){
              XSSFRow myRow = (XSSFRow) rowIter.next();
              Iterator cellIter = myRow.cellIterator();
              ArrayList cellStoreVector=new ArrayList();
              while(cellIter.hasNext()){
                  XSSFCell myCell = (XSSFCell) cellIter.next();
                  cellStoreVector.add(myCell);
              }
              cellVectorHolder.add(cellStoreVector);
          }
        }catch (Exception e){e.printStackTrace(); }
        return cellVectorHolder;  
        
                
    }
    
        private static void insertIntoDb(ArrayList dataHolder) throws Exception{
        String bd = "siscon";
        String usuario = "root";
        String password = "";
        String url = "jdbc:mysql://localhost/"+bd;
        
        Connection conexion = null;
        
         try{
             
             
             //Plan de estudios
             int idPlanDeEstudios = 0;
             int anioPlan = 0;
             String descripcionPlan = "";
             
             // Materia
             String materia = "";
             int curso = 0;
             String nombreMateria = "";
             String disciplina = "";
             
             // Carrera
             int idCarrera = 0;
             String nombreCarrera = "";
             String siglasCarrera = "";
             
             
             // Semestre
             int idSemestre = 0;
             int semestre = 0;
             

             
             Class.forName("com.mysql.jdbc.Driver").newInstance();
             conexion = null;
             PreparedStatement pstmt = null;
             
             for (int i=1;i<dataHolder.size(); i++){
                   ArrayList cellStoreVector=(ArrayList)dataHolder.get(i);
                   ArrayList cellColName=(ArrayList)dataHolder.get(0);
             for (int j=0; j < cellStoreVector.size();j++){
                 XSSFCell myCell = (XSSFCell)cellStoreVector.get(j);
                 String stringCellValue = myCell.toString();
                 XSSFCell cell = (XSSFCell)cellColName.get(j);
                 String columnName = cell.toString();
                 columnName = columnName.toLowerCase().trim();
                 columnName = columnName.replace(" ", "");
             
             // Leer por nombre de columna 
             switch(columnName){
                 case "disciplina": disciplina = stringCellValue; break;
                 case "clave": materia = stringCellValue.substring(0,stringCellValue.length()-4);
                               curso = Integer.parseInt(stringCellValue.substring(stringCellValue.length()-4)); break;
                 case "semestrenumérico": semestre = Integer.parseInt(stringCellValue); break;
                 case "nombre": nombreMateria = stringCellValue; break;
                 case "programaacadémico": anioPlan = Integer.parseInt(stringCellValue.substring(stringCellValue.length()-2));
                                           siglasCarrera = stringCellValue.substring(0,stringCellValue.length()-2); break;
                 case "descripciónárea": descripcionPlan = stringCellValue; break;  
             }

                 }
             
             try {
                  conexion = DriverManager.getConnection(url,usuario,password);
                  String queryMateria = "insert into materia(materia, curso, nombreMateria, disciplina) values(?, ?, ?, ?)";
                  String queryCarrera = "insert into carrera(nombreCarrera, siglasCarrera) values(?, ?)";
                  String queryPlan = "insert into plandeestudios(idCarrera, anioPlan, descripcion) values(?, ?, ?)";
                  String querySemestre = "insert into semestre(idPlan, idCarrera, semestre) values(?, ?, ?)";
                  String querySemestreMat = "insert into semestremateria(materia, curso, idSemestre, idPlan, idCarrera) values(?, ?, ?, ?, ?)";  
                  
                  
                  ResultSet rs = null;
                  
                  String checkMat = "SELECT m.materia, m.curso "
                  + "FROM materia m "
                  + "WHERE m.curso = ? AND m.materia = ?";
                  
                  String fkCarrera = "SELECT c.idCarrera "
                  + "FROM carrera c "
                  + "WHERE c.siglasCarrera = ?";
                  
                  String fkPlan = "SELECT p.idPlanDeEstudios "
                  + "FROM plandeestudios p "
                  + "WHERE p.idCarrera = ?";
                  
                  String fkSemMat = "SELECT s.idSemestre "
                  + "FROM semestre s "
                  + "WHERE s.idPlan = ? AND s.idCarrera = ? AND s.semestre = ?";
                  
                  
                  
                  pstmt = conexion.prepareStatement(queryCarrera); // create a statement
                  pstmt.setString(1, nombreCarrera ); // set input parameter 1
                  pstmt.setString(2, siglasCarrera); // set input parameter 2
                  pstmt.executeUpdate(); // execute insert statement
                  pstmt.clearParameters();
                                    
                  pstmt = conexion.prepareStatement(checkMat);
                  pstmt.setInt(1, curso);
                  pstmt.setString(2, materia);
                  rs = pstmt.executeQuery();
                  if(!rs.next()){ // Si no existe la materia se inserta
                      pstmt = conexion.prepareStatement(queryMateria); // create a statement
                      pstmt.setString(1, materia); // set input parameter 1
                      pstmt.setInt(2, curso); // set input parameter 2
                      pstmt.setString(3, nombreMateria); // set input parameter 3
                      pstmt.setString(4, disciplina); // set input parameter 4
                      pstmt.executeUpdate(); // execute insert statement
                      pstmt.clearParameters();
                  }               
                  
                  pstmt = conexion.prepareStatement(fkCarrera);
                  pstmt.setString(1, siglasCarrera);
                  rs = pstmt.executeQuery();
                  if(!rs.next()){
                      idCarrera = rs.getInt(1);
                      pstmt = conexion.prepareStatement(queryPlan); // create a statement
                      pstmt.setInt(1, idCarrera); // set input parameter 1
                      pstmt.setInt(2, anioPlan); // set input parameter 2
                      pstmt.setString(3, descripcionPlan);
                      pstmt.executeUpdate(); // execute insert statement
                      pstmt.clearParameters();
                  }
                  
                  
                  pstmt = conexion.prepareStatement(fkPlan);
                  pstmt.setInt(1, idCarrera);
                  rs = pstmt.executeQuery();
                  if(rs.next()){
                      idPlanDeEstudios = rs.getInt(1);
                      pstmt = conexion.prepareStatement(querySemestre); // create a statement
                      pstmt.setInt(1, idPlanDeEstudios); // set input parameter 1
                      pstmt.setInt(2, idCarrera); // set input parameter 2
                      pstmt.setInt(3, semestre);
                      pstmt.executeUpdate(); // execute insert statement
                      pstmt.clearParameters();
                  }         
                  
                  pstmt = conexion.prepareStatement(fkSemMat);
                  pstmt.setInt(1, idPlanDeEstudios);
                  pstmt.setInt(2, idCarrera);
                  pstmt.setInt(3, semestre);
                  rs = pstmt.executeQuery();
                  if(rs.next()){
                      idSemestre = rs.getInt(1);
                      pstmt = conexion.prepareStatement(querySemestreMat); // create a statement
                      pstmt.setString(1, materia); // set input parameter 1
                      pstmt.setInt(2, curso); // set input parameter 2
                      pstmt.setInt(3, idSemestre);
                      pstmt.setInt(4, idPlanDeEstudios);
                      pstmt.setInt(5, idCarrera);
                      pstmt.executeUpdate(); // execute insert statement
                      pstmt.clearParameters();
                  }
                  
                  plan="" + siglasCarrera + anioPlan;
                  
                  } catch (Exception e) {
                      e.printStackTrace();
                  } finally {
                    pstmt.close();
                    conexion.close();
                    }
                 
                 }      
             } catch (SQLException ex){ 
             System.out.println("Hubo un problema al intentar conectarse con la base de datos "+url); 
                    }
            catch(ClassNotFoundException ex) { 
                System.out.println(ex); 
                }
        
 

    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
    ServletContext context = getServletContext();
    String ruta = context.getRealPath(request.getContextPath());
    String fileNameLocal = ruta + "/" + request.getParameter("archivo");//aqui va el path
    ArrayList dataHolder0= readExcelFile(fileNameLocal);
    insertIntoDb(dataHolder0);
       
    PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        out.println("<plan>\n"+plan+"</plan>\n");
   
        out.close();
    
}       catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
