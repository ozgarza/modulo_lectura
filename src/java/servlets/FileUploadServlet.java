/*
 * Servlet para realizar la subida del documento tipo excel.
 * Se usa una carpeta por default.
 */
package servlets;
//Librerias necesarias
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Librerias externas necesarias
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet FileUploadServlet
 */
public class FileUploadServlet extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 2740693677625051632L;

    public FileUploadServlet() {
        super();
    }
    /*
     * Metodo doGet 
     * Recibe la peticion de la funcion ajax correspondiente para verificar el 
     * progreso de la subida del documento.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        FileUploadListener listener = null;
        StringBuilder buffy = new StringBuilder();
        long bytesRead = 0, contentLength = 0;

        // Se ha iniciado la sesion
        if (session == null) {
            return;
        } else if (session != null) {
            // Se crea un objeto listener
            listener = (FileUploadListener) session.getAttribute("LISTENER");
            
            if (listener == null) {
                return;
            } else {
                // Obtener los datos de subida del documento
                bytesRead = listener.getBytesRead();
                contentLength = listener.getContentLength();

            }
        }
      
        //Respuesta xml        
        response.setContentType("text/xml");
        buffy.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        buffy.append("<response>\n");
        // Si termina el proceso, anexar terminado a la respuesta.
        if (bytesRead == contentLength) {
            buffy.append("\t<finished />\n");
            // Se termina la sesion al subir completamente el documento.
            session.setAttribute("LISTENER", null);
        } else {
            //Se calcula el porcentaje de progreso
            long percentComplete = ((100 * bytesRead) / contentLength);
            System.out.println("percent"+percentComplete);
            buffy.append("\t<percent_complete>" + percentComplete + "</percent_complete>\n");
        }
        buffy.append("</response>\n");
        out.println(buffy.toString());
        out.flush();
        out.close();
    }
    
    /*
     * Metodo doPost
     * Recibe la peticion al enviar la forma de subida del documento.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        ServletContext context = getServletContext();
        String ruta = context.getRealPath(request.getContextPath());
        
        //Se crea un objeto FileItemFactory y ServletFileUpload para la subida del archivo
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //Se crea un objeto FileUploadListener para obtener el progreso de subida.
        FileUploadListener listener = new FileUploadListener();

        HttpSession session = request.getSession();
        session.setAttribute("LISTENER", listener);
    
        upload.setProgressListener(listener);

        List uploadedItems = null;
        FileItem fileItem = null;
        String filePath = ruta;	// Carpeta para guardar los exceles
        try {
            // Se recorren los archivos a subir
            uploadedItems = upload.parseRequest(request);
            Iterator i = uploadedItems.iterator();

            while (i.hasNext()) {
                fileItem = (FileItem) i.next();

                if (fileItem.isFormField() == false) {

                    if (fileItem.getSize() > 0) {
                        File uploadedFile = null;
                        String myFullFileName = fileItem.getName(),
                                myFileName = "",
                                slashType = (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/"; // Windows o UNIX
                        int startIndex = myFullFileName.lastIndexOf(slashType);

                        // Se ignora la ruta y se obtiene el nombre de archivo
                        myFileName = myFullFileName.substring(startIndex + 1, myFullFileName.length());

                        //Crear un nuevo objeto File
                        uploadedFile = new File(filePath, myFileName);

                        // Escribir el archivo subido al sistema.
                        fileItem.write(uploadedFile);
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
