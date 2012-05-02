<%-- 
    Document   : subirFuente
    Created on : 24/04/2012, 02:49:56 PM
    Author     : Oziel
--%>
<%
  String pageTitle = "SISCON | Subir plan de estudios";
%>
 <%@ include file="includes/header_aplicacion.jsp" %>
 <%@page import="clases.Usuario"%> 
 <%@page import="com.mysql.jdbc.Connection"%>
 <%@page import="com.mysql.jdbc.Statement"%>
 
 <script src="assets/js/subir_archivo.js"></script>
  
<%
    //Variables necesarias para obtener la informacion de los registros de lectura de los exceles.
    Usuario usuariologgeado = (Usuario) session.getAttribute("usuario");    
    int Uid = usuariologgeado.IdU(); 
    String fecha="";
    String hora="";
    String[] temp; //String temporal para la fecha y hora que regresa la consulta tipo DATETIME de la base de datos.
    
    //Establecer la conexion a la base de datos y sentencias para realizar las consultas.
    Connection connection=null;
    Class.forName("com.mysql.jdbc.Driver");
    String url = "jdbc:mysql://localhost/SISCON";
    connection = (Connection) DriverManager.getConnection(url,"root", "");         
    Statement statement = (Statement) connection.createStatement(); 
    
    //Consulta de los exceles registrados    
    //Verificar idUsuario de session********************************************
    ResultSet results = statement.executeQuery("SELECT * FROM registroexcel WHERE indexUsuario = " + Uid + " AND tipo = 1");                     
%>
    
       
    <div class="container">        
        <div class="row">            
            <h1>Lectura de plan de estudios</h1>
            <br />
            <!-- Tabla de los ultimos archivos de excel que se subiero y actualizaron la base de datos -->
            <p>Últimas actualizaciones</p>
            <table id="tabla1" class="table table-striped table-bordered table-condensed">
                <tr>
                    <td>Fecha</td>
                    <td>Hora</td>
                    <td>Plan</td>
                    <td>Archivo</td>
                </tr>
                <% //Mostrar todos los exceles que ha subido el usuario
                    while (results.next()) {
                        //Obtener la fecha y hora
                        temp = results.getString(4).split(" ");
                        fecha = temp[0];
                        //Remover los segundos para mostrar solo horas y minutos.
                        hora = temp[1].substring(0, 5);
                %>  
                <tr>
                    <td>
                        <%= fecha%>
                    </td>
                    <td>
                        <%= hora%>
                    </td>
                    <td>
                        <%=results.getString(3)%>
                    </td>                        
                    <td>
                        <%=results.getString(5)%>
                    </td>
                </tr>
                <%
     }//fin de while              
%>
            </table>
            <br />              

               <!--Espacio para realizar la selección y subida del documento-->
               <!--Se usa la funcion "comprueba_extension" para verificar el tipo de archivo a subir -->
               <!--Se usa la funcion ajax "ajaxFunction" para la subida del archivo -->
               <div class="span3">                   
                   <h3><i class="icon-upload"></i> Subir documento nuevo</h3> 
                   <form id="myForm" enctype="multipart/form-data" method="post" target="uploadFrame" 
                         action="servlet/FileUploadServlet"  >
                       <label class="control-label">Selecciona el documento:</label>
                       <input type="file" name="txtFile" id="txtFile" /><br />
                       <input type="submit" id="submitID" name="enviar" value="Subir" onclick="comprueba_extension(this.form, this.form.txtFile.value)"/>
                   </form>
                       
                   <iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>              
                       
                   <!-- Espacio para el progreso y respuesta de la subida del documento -->                                              
                   <div id="estatus" style="visibility: hidden;">
                       <b>Iniciando subida...</b>                      
                   </div> 
                   <!--ProgressBar-->
                   <div id="progressbar" style="visibility: hidden;" class="progress progress-striped active">
                           <div id="bar" class="bar" style="width: 0%;"></div>
                       </div>
                   
               </div>
               
               <!--Espacio para realizar la lectura del documento-->
               <!--Se usa la funcion ajax "lectura" para la lectura del archivo -->
               <div id="lectura" style="display: none" class="span3">
                   <h3><i class="icon-file"></i> Realizar lectura de archivo</h3>
                   <p>Ejecutar la lectura del documento y actualizar la base de datos.</p>        				
                   <button id="iniciarLectura" type="button" class="btn" onclick="lecturaPlan()" >Aceptar</button>                                                                             
                   <button id="cancelarLectura" type="button" class="btn" onclick="cancelaLectura()" >Cancelar</button> 
                   <label id="errorLectura" class="control-label"></label>
                   <!--Entrada oculta para almacenar el id del usuario-->
                   <input type="hidden" id="usuario" value="<%=Uid%>"/>
                   <div id="estatusLectura" style="visibility: hidden;">
                       <b>Realizando lectura...</b>                      
                   </div> 
                    <div id="progressbarLectura" style="visibility: hidden;" class="progress progress-striped active">
                           <div id="bar" class="bar" style="width: 100%;"></div>
                       </div>
               </div>
                   
               <!--Espacio para mostrar el resultado de la lectura del documento-->                       
               <div id="resultadolectura" style="display: none" class="span3">
                   <h3><i class="icon-ok-circle"></i> Lectura de archivo realizada</h3>
                   <span>Se ejecut&oacute; la lectura del documento de </span><span id="plan" class="control-label"></span> 
                   <span>satisfactoriamente el </span><span id="fecha" class="control-label"></span>
                   </br>  
                   <a href="index.jsp"><button type="button" class="btn">Terminar</button> </a>              
               </div>
                
        </div> <!-- /row -->
    </div> <!-- /container -->
   <%@ include file="includes/footer_aplicacion_1.html" %>
    
    