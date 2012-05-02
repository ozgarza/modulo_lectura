package servlets;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.util.Iterator;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
/**
 *
 * @author Ruben
 */
@WebServlet(name = "ReadingModule", urlPatterns = {"/ReadingModule"})
public class ReadingModule extends HttpServlet {

    
    static String nombreDept="";
    
   
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
             
             // Periodo
             int idPeriodo = 0;
             String anio = "";
             int periodoValue = 0;
             String periodo = "";
             
             // Departamento
             int idDepartamento;
             String siglas = "";
             String departamento = "";
             
             // Grupo
             int crn = 0;
             String atributos = "";
             int horasClase = 0;
             int horasLabo = 0;
             int unidades = 0;
             String[] porcentaje = new String [3];
             int numeroProfesores = 0;
             int claseExclusiva = 0;
             int alumnosInscritos = 0;
             
             //Plan de estudios
             int idPlanDeEstudios = 0;
             int anioPlan = 0;
             String descripcionPlan = "";
             
             // Materia
             String materia = "";
             int curso = 0;
             String nombreMateria = "";
             String descripcionMateria = "";
             int idMateria;            
             
             // Horarios
             int idHorarios = 0;
             String salon = "";
             String [] diaSemana = new String [21];
             String horaInicio = "";
             String horaFin = "";
             
             // Telefono
             String telefono = "";
             String extension = "";
             
             // Semestre
             int idSemetre = 0;
             String smestre = "";
             
             // Usuario
             String[] idUsuario = new String [3];
             String[] nombreUsuario = new String [3];
             String[] apellidoUsuario = new String [3];;
             int[] indexUsuario = new int [3];

             
             Class.forName("com.mysql.jdbc.Driver").newInstance();
             conexion = null;
             conexion = DriverManager.getConnection(url,usuario,password); 
             PreparedStatement pstmt = null;
             String cleanTables = "delete from grupo; "
                                     + " delete from horarios;";
                  pstmt = conexion.prepareStatement(cleanTables);
                  pstmt.clearParameters();
                  
                  
             for (int i=1;i<dataHolder.size(); i++){
                   ArrayList cellStoreVector=(ArrayList)dataHolder.get(i);
                   ArrayList cellColName=(ArrayList)dataHolder.get(0);
                   diaSemana = new String [21];
                     porcentaje = new String [3];
                     
             for (int j=0; j < cellStoreVector.size();j++){
                 XSSFCell myCell = (XSSFCell)cellStoreVector.get(j);
                 String stringCellValue = myCell.toString();
                 XSSFCell cell = (XSSFCell)cellColName.get(j);
                 String columnName = cell.toString();
                 columnName = columnName.toLowerCase().trim();
                 columnName = columnName.replace(" ", "");
             
           
             // Leer por nombre de columna 
             switch(columnName){
                     case "período": idPeriodo = Integer.parseInt(stringCellValue.substring(0, 6));    //Lee el periodo    
                            anio = stringCellValue.substring(0, 4);
                            periodoValue = Integer.parseInt(stringCellValue.substring(4, 6));
                            switch(periodoValue){
                                case 11: periodo = "Enero-Mayo"; break;
                                case 12: periodo = "Verano"; break;
                                case 13: periodo = "Agosto-Diciembre"; break;
                                default: periodo = null;                                
                            }
                     case "departamento": siglas = stringCellValue; break;
                     case "nombredeldepartamento": departamento = stringCellValue; break;
                     case "crn" : crn = Integer.parseInt(stringCellValue.substring(0, stringCellValue.indexOf("."))); break;
                     case "materia": materia = stringCellValue; break;
                     case "curso": curso = Integer.parseInt(stringCellValue.substring(0, stringCellValue.indexOf("."))); break;
                     case "nombredelamateria": nombreMateria = stringCellValue; break;
                     case "hc": horasClase = Integer.parseInt(stringCellValue.substring(0, stringCellValue.indexOf("."))); break;
                     case "hl": horasLabo = Integer.parseInt(stringCellValue.substring(0, stringCellValue.indexOf("."))); break;
                     case "un": unidades = Integer.parseInt(stringCellValue.substring(0, stringCellValue.indexOf("."))); break;
                     case "inscritos": alumnosInscritos = Integer.parseInt(stringCellValue.substring(0, stringCellValue.indexOf("."))); break;
                     case "atributos": atributos = stringCellValue; break;
                     case "#excint": claseExclusiva = Integer.parseInt(stringCellValue.substring(0, stringCellValue.indexOf("."))); break;
                     case "horainicio1": horaInicio = stringCellValue; break;
                     case "horafin1": horaFin = stringCellValue; break;
                     case "lu1": if(stringCellValue != null && !stringCellValue.equals("")  ){ diaSemana[0] = "Lunes";} break;
                     case "ma1": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[1] = "Martes";} break;
                     case "mi1": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[2] = "Miercoles";} break;
                     case "ju1": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[3] = "Jueves";} break;
                     case "vi1": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[4] = "Viernes";} break;
                     case "sa1": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[5] = "Sabado";} break;
                     case "do1": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[6] = "Domingo";} break;
                     case "lu2": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[7] = "Lunes";} break;
                     case "ma2": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[8] = "Martes";} break;
                     case "mi2": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[9] = "Mercoles";} break;
                     case "ju2": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[10] = "Jueves";} break;
                     case "vi2": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[11] = "Viernes";} break;
                     case "sa2": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[12] = "Sabado";} break;
                     case "do2": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[13] = "Domingo";} break;
                     case "lu3": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[14] = "Lunes";} break;
                     case "ma3": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[15] = "Martes";} break;
                     case "mi3": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[16] = "Miercoles";} break;
                     case "ju3": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[17] = "Jueves";} break;
                     case "vi3": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[18] = "Viernes";} break;
                     case "sa3": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[19] = "Sabado";} break;
                     case "do3": if(stringCellValue != null && !stringCellValue.equals("")){ diaSemana[20] = "Domingo";} break;
                     case "edificio1": salon = stringCellValue; break;
                     case "salón1": salon.concat(stringCellValue); break;
                     case "#profesores": numeroProfesores = Integer.parseInt(stringCellValue.substring(0, stringCellValue.indexOf("."))); break;
                     case "porcentaje1": porcentaje[0] = stringCellValue; break;
                     case "porcentaje2": porcentaje[1] = stringCellValue; break;
                     case "porcentaje3": porcentaje[2] = stringCellValue; break;
                     case "nómina1": idUsuario[0] = stringCellValue; break;
                     case "nombre1": nombreUsuario[0] = stringCellValue; break;
                     case "apaterno1": apellidoUsuario[0] = stringCellValue; break;
                     case "amaterno1": apellidoUsuario[0].concat(stringCellValue); break;
                     case "nómina2": idUsuario[1] = stringCellValue; break;
                     case "nombre2": nombreUsuario[1] = stringCellValue; break;
                     case "apaterno2": apellidoUsuario[1] = stringCellValue; break;
                     case "amaterno2": apellidoUsuario[1].concat(stringCellValue); break;
                     case "nómina3": idUsuario[2] = stringCellValue; break;
                     case "nombre3": nombreUsuario[2] = stringCellValue; break;
                     case "apaterno3": apellidoUsuario[2] = stringCellValue; break;
                     case "amaterno3": apellidoUsuario[2].concat(stringCellValue); break;            
             }
                         
                 }
             
             try {
                  
                  conexion = DriverManager.getConnection(url,usuario,password);                  
                  String queryDepartamento = "insert into departamento(siglas, departamento) values(?, ?)";
                  String queryGrupo = "insert into grupo(CRN, materia, curso, idDepartamento, indexUsuario, idPeriodo, atributos, horasClase, horasLaboratorio, unidades, porcentajeClase, numeroProfesores, claseExclusiva, alumnosInscritos) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                  String queryHorarios = "insert into horarios(CRN, materia, curso, idDepartamento, indexUsuario, idPeriodo, salon, diaSemana, horaInicio, horaFin) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                  String queryPeriodo = "insert into periodo(anio, periodoValue, periodo) values(?, ?, ?)";
                  String queryUsuario = "insert into usuario(idDepartamento, idUsuario, nombreUsuario, apellidoUsuario, password) values(?, ?, ?, ?, ?)";     
                  
                  
                  ResultSet rs = null;
                  
                  String fkGrupo = "SELECT u.indexUsuario "
                          + " FROM  usuario u "
                          + " WHERE u.idUsuario = ? ";
                 
                  String checkUser = "SELECT u.indexUsuario "
                                    + "FROM usuario u "
                                    + "WHERE u.idUsuario = ? OR u.idUsuario = ? OR u.idUsuario = ?";
                  String checkPer = "SELECT p.periodo "
                          + " FROM periodo p "
                          + " WHERE p.idPeriodo = ?";
                  
                  String fkUsuario = "SELECT d.idDepartamento "
                          + " FROM departamento d "
                          + " WHERE d.siglas = ?";
                  
                  String checkDep = "SELECT d.idDepartamento "
                          + " FROM departamento d "
                          + " WHERE d.siglas = ? ";
                  
                  String checkGrupo = "SELECT g.crn "
                          + "FROM grupo g "
                          + "WHERE g.crn = ? AND g.materia = ? AND g.curso = ? AND g.indexUsuario = ? "
                          + "AND g.idDepartamento = ? AND g.idPeriodo = ?";
                   String checkMat = "SELECT m.materia, m.curso "
                  + "FROM materia m "
                  + "WHERE m.curso = ? AND m.materia = ?";
                   
                   String queryMateria = "insert into materia(materia, curso, nombreMateria, disciplina) values(?, ?, ?, ?)";
                 
                  String disciplina ="prueba";
                  pstmt = conexion.prepareStatement(fkUsuario); // create a statement
                  pstmt.setString(1, siglas); // set input parameter 1
                  rs = pstmt.executeQuery();
                  if (!rs.next()){
                      pstmt = conexion.prepareStatement(queryDepartamento); // create a statement
                      pstmt.setString(1, siglas); // set input parameter 1
                      pstmt.setString(2, departamento); // set input parameter 2
                      pstmt.executeUpdate(); // execute insert statement
                      pstmt.clearParameters();
                  }
                  
                pstmt = conexion.prepareStatement(checkPer); // create a statement
                  pstmt.setInt(1, idPeriodo); // set input parameter 1
                  rs = pstmt.executeQuery();
                  if (!rs.next()){
                      pstmt = conexion.prepareStatement(queryPeriodo); // create a statement
                      pstmt.setString(1, anio); // set input parameter 1
                      pstmt.setInt(2, periodoValue); // set input parameter 2
                      pstmt.setString(3, periodo);
                      pstmt.executeUpdate(); // execute insert statement
                      pstmt.clearParameters();                         
                  }
                  // getting fk para grupo
                  int cont = 0;
                  do{
                      pstmt = conexion.prepareStatement(fkUsuario);
                      pstmt.setString(1, siglas);
                      rs = pstmt.executeQuery();
                      rs.next();
                      idDepartamento = rs.getInt(1);
                      pstmt.clearParameters();
                      
                    pstmt = conexion.prepareStatement(checkUser);
                      pstmt.setString(1,idUsuario[0]);
                      pstmt.setString(2,idUsuario[1]);
                      pstmt.setString(3,idUsuario[2]);
                      rs = pstmt.executeQuery();
                      if(!rs.next()){
                          pstmt = conexion.prepareStatement(queryUsuario); // create a statement
                          pstmt.setInt(1, idDepartamento); // set input parameter 1
                          pstmt.setString(2, idUsuario[cont]); // set input parameter 2
                          pstmt.setString(3, nombreUsuario[cont]);
                          pstmt.setString(4, apellidoUsuario[cont]);
                          pstmt.setString(5, "pass");
                          pstmt.executeUpdate(); // execute insert statement
                          pstmt.clearParameters();
                      }else{ 
                          indexUsuario[0] = rs.getInt(1);
                          if(rs.next()){indexUsuario[1] = rs.getInt(1);}
                          if(rs.next()){indexUsuario[2] = rs.getInt(1);}
                      }
                      
                      pstmt = conexion.prepareStatement(fkGrupo);
                      pstmt.setString(1, idUsuario[cont]);
                      rs = pstmt.executeQuery();
                      if(rs.next()){
                      indexUsuario[cont] = rs.getInt(1);      
                      }
                      pstmt.clearParameters();                                                                       
                
                      pstmt = conexion.prepareStatement(checkMat);
                      pstmt.setInt(1, curso);
                      pstmt.setString(2, materia);
                      rs = pstmt.executeQuery();
                      if (!rs.next()) { // Si no existe la materia se inserta
                          pstmt = conexion.prepareStatement(queryMateria); // create a statement
                          pstmt.setString(1, materia); // set input parameter 1
                          pstmt.setInt(2, curso); // set input parameter 2
                          pstmt.setString(3, nombreMateria); // set input parameter 3
                          pstmt.setString(4, disciplina); // set input parameter 4
                          pstmt.executeUpdate(); // execute insert statement
                          pstmt.clearParameters();
                      }           
                  
                      
                      pstmt = conexion.prepareStatement(checkGrupo); // create a statement
                      pstmt.setInt(1, crn); // set input parameter 1
                      pstmt.setString(2, materia);
                      pstmt.setInt(3, curso);
                      pstmt.setInt(4, indexUsuario[cont]);
                      pstmt.setInt(5, idDepartamento);
                      pstmt.setInt(6, idPeriodo);
                      rs = pstmt.executeQuery();
                      if (!rs.next()){
                          pstmt = conexion.prepareStatement(queryGrupo); // create a statement
                          pstmt.setInt(1, crn); // set input parameter 1
                          pstmt.setString(2, materia); // set input parameter 2
                          pstmt.setInt(3, curso);
                          pstmt.setInt(4, idDepartamento);
                          pstmt.setInt(5, indexUsuario[cont]);
                          pstmt.setInt(6, idPeriodo);
                          pstmt.setString(7, atributos);
                          pstmt.setInt(8, horasClase);
                          pstmt.setInt(9, horasLabo);
                          pstmt.setInt(10, unidades);
                          pstmt.setString(11, porcentaje[cont]);
                          pstmt.setInt(12, numeroProfesores);
                          pstmt.setInt(13, claseExclusiva);
                          pstmt.setInt(14, alumnosInscritos);
                          pstmt.executeUpdate();
                          pstmt.clearParameters();
                      }pstmt.clearParameters(); 
                      
                   for(int k=0; k < 20; k++){
                       if(diaSemana[k] != null){
                       if(!diaSemana[k].equals("")){
                              pstmt = conexion.prepareStatement(queryHorarios);
                              pstmt.setInt(1, crn); // set input parameter 1
                              pstmt.setString(2, materia); // set input parameter 2
                              pstmt.setInt(3, curso);
                              pstmt.setInt(4, idDepartamento);
                              pstmt.setInt(5, indexUsuario[cont]);//
                              pstmt.setInt(6, idPeriodo);
                              pstmt.setString(7, salon);
                              pstmt.setString(8, diaSemana[k]);
                              pstmt.setString(9, horaInicio);
                              pstmt.setString(10, horaFin);                               
                              pstmt.executeUpdate(); // execute insert statement
                              pstmt.clearParameters();
                          }                          
                       }
                   }

                      
                      cont++;
                  }while(cont < numeroProfesores);
                  
                    nombreDept=departamento;
                                    

                  
                  
                  
                  
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
    
    private static void printCellDataToConsole(ArrayList dataHolder) {
 
        for (int i=0;i<dataHolder.size(); i++){
                   ArrayList cellStoreVector=(ArrayList)dataHolder.get(i);
            for (int j=0; j < cellStoreVector.size();j++){
                XSSFCell myCell = (XSSFCell)cellStoreVector.get(j);
                String stringCellValue = myCell.toString();
                System.out.print(stringCellValue+"\t");
            }
            System.out.println();
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
    String fileNameLocal = ruta + "\\" + request.getParameter("archivo");
    ArrayList dataHolder0= readExcelFile(fileNameLocal);
    insertIntoDb(dataHolder0);    
    PrintWriter out = response.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        out.println("<departamento>"+nombreDept+"</departamento>");
   
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


