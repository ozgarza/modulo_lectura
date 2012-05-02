<%@page import="java.io.IOException"%>

<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.logging.Logger"%>

<%-- <%@page import="clases.Usuario"%> --%>

<%@page import="com.mysql.jdbc.Connection"%>
<%@page import="com.mysql.jdbc.Statement"%>
<%
	int rowCount = 0;
	try {
		Class.forName("com.mysql.jdbc.Driver");
		
		String url = "jdbc:mysql://localhost/SISCON";
		Connection con = (Connection) DriverManager.getConnection(url,"root", "");
		Statement query = (Statement) con.createStatement();
		
		Usuario usuariologgeado = (Usuario) session.getAttribute("usuario");
		
		String q = "SELECT u.indexUsuario, u.nombreUsuario, u.apellidoUsuario FROM Usuario u, tablaNotificacion t  WHERE t.idDepartamento='"
		+ usuariologgeado.IdD() + "' and t.indexUsuario=u.indexUsuario";
		
		
		ResultSet rs = query.executeQuery(q);
		while (rs.next()) {
			
		}
		rs.last();
	    rowCount = rs.getRow();
	}
	catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
%>
    
    <div id="wrap" class="wrapper">
      <!-- Barra de navegacion -->
      <div id="nav" class="navbar navbar-fixed-top">
        <div class="navbar-inner">
          <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </a> <!-- / boton de colapsar -->
            <span class="brand">SISCON</span>
            <div class="nav-collapse">
              <ul class="nav">
                <li><a href="bienvenido.jsp"><i class="icon-home icon-white"></i> Inicio</a></li>
                <li><a href="interfaz_admin_administraUsuario.jsp"><i class="icon-th-list icon-white"></i> Administrar usuarios</a></li>
                <li><a href="subir_archivo.jsp"><i class="icon-upload icon-white"></i> Subir archivo fuente</a></li>
                <li><a href="notificaciones.jsp">Centro de Notificaciones <span class="badge"><%= rowCount %></span>
                </a></li>
              </ul>
              <ul class="nav pull-right">
				<li><a href="#"><i class="icon-user icon-white"></i> <%= usuario.getNombre() + " " + usuario.getApellido() %></a></li>
                <li><a href="CerrarSesion"><i class="icon-off icon-white"></i> Cerrar Sesi&oacute;n</a></li>
                <!-- <li><a href="iniciar_sesion.jsp"><i class="icon-user icon-white"></i> Iniciar Sesi&oacute;n</a></li>
                <li class="divider-vertical"></li>
                <li><a href="registrar_usuario.jsp"><i class="icon-pencil icon-white"></i> Registrarse</a></li> -->
              </ul>
            </div><!--/.nav-collapse -->
          </div> <!-- /.container -->
        </div> <!-- /navbar-inner -->
      </div> <!-- /navbar -->