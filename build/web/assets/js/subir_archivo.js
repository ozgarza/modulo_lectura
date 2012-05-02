/*
 *SISCON 2012
 *Archivo para guardar las funciones javascript del modulo de subir archivo
*/
var req;                        //Request para subir archivo.
var req2;                       //Request para lectura de grupos.
var req3;                       //Request para lectura plan.
var requestRegistro;            //Variable requestRegistro para actualizar la base de datos 
                                //al subir un nuevo archivo de excel.

/*
 *Funcion ajax para llamar al servlet encargado de subir el documento
 */
function ajaxFunction()
{
    var url = "servlet/FileUploadServlet";
	
    if (window.XMLHttpRequest) // Non-IE browsers
    { 
        req = new XMLHttpRequest();
        req.onreadystatechange = processStateChange;

        try 
        {
            req.open("GET", url, true);
        } 
        catch (e) 
        {
            alert(e);
        }
        req.send(null);
    } 
    else if (window.ActiveXObject) // IE Browsers
    { 
        req = new ActiveXObject("Microsoft.XMLHTTP");
	
        if (req) 
        {
            req.onreadystatechange = processStateChange;
            req.open("GET", url, true);
            req.send();
        }
    }
}

/*
 *Funcion para procesar la respuesta de la funcion ajax para subir el archivo.
 *Despliega el progreso y la respuesta en el espacio correspondiente.
 */
function processStateChange()
{   
    if (req.readyState == 4)
    {
        if (req.status == 200) // OK response
        {
            var xml = req.responseXML;                        
            // Variables para determinar el porcentaje de progreso y la finalizacion de la subida
            var isNotFinished = xml.getElementsByTagName("finished")[0];
            var myPercent = xml.getElementsByTagName("percent_complete")[0];
                                     
            //Verificar si aun no ha comenzado la subida
            if ((isNotFinished == null) && (myPercent == null))
            { 
                document.getElementById("estatus").style.visibility = "visible";
                // Vuelve a llamar la funcion despues de 10ms
                window.setTimeout("ajaxFunction();", 100);
            } else                
            {
                document.getElementById("estatus").style.visibility = "visible";
                document.getElementById("progressbar").style.visibility = "visible";
							
                // Empezo la subida, se despliega el progreso y vuelve a llamar la función
                if (myPercent != null)
                {
                    myPercent = myPercent.firstChild.nodeValue;							
                    document.getElementById("estatus").innerHTML ="Progreso:" + myPercent + "%";
                    document.getElementById("bar").style.width=myPercent+"%";
                    // Vuelve a llamar la funcion despues de 10ms
                    window.setTimeout("ajaxFunction();", 100);
                }
                else {
                    //Finaliza la subida y despliega el mensaje exitoso y el espacio para realizar la lectura
                     document.getElementById("estatus").innerHTML = "Subida exitosa";
                     document.getElementById("estatus").className="alert alert-success";
                     document.getElementById("progressbar").style.visibility = "hidden";
                    muestraLectura();
                } 
            }
        }
        else
        {
            //Despliega el mensaje de respuesta
            document.getElementById("estatus").innerHTML ="" +req.statusText;
        }
    }
}

/*
 * Funcion para verificar la extension del documento que se subira. 
 * Solo se aceptan archivos con formato .xls y .xlsx
 * Si se sube un documento de extension diferente despliega un mensaje de error.
 */
function comprueba_extension(formulario, archivo) { 
    //Arreglo de extensiones permitidas
    extensiones_permitidas = new Array(".xls",".xlsx"); 
    //Mensaje de error
    mierror = ""; 
    if (!archivo) { 
        //Si no tengo archivo, es que no se ha seleccionado un archivo en la forma 
        mierror = "No has seleccionado ningún archivo"; 
    }else{ 
        //Se obtiene la extensión de este nombre de archivo 
        extension = (archivo.substring(archivo.lastIndexOf("."))).toLowerCase(); 
        //Se comprueba si la extensión está entre las permitidas 
        permitida = false; 
        for (var i = 0; i < extensiones_permitidas.length; i++) { 
            if (extensiones_permitidas[i] == extension) { 
                permitida = true; 
                break; 
            } 
        } 
        //Si no es permitida, se despliega el mensaje de error.
        if (!permitida) { 
            mierror = "Comprueba la extensión de los archivos a subir. \nSólo se pueden subir archivos con extensiones: " + extensiones_permitidas.join(); 
        }else{ 
            //Si se permite, llamar a la funcion ajax encargada de invocar al servlet que sube el archivo
            ajaxFunction();    
            return 1; 
        } 
    } 
    //Si no se pudo subir, se despliega el mensaje de error.    
    document.getElementById("estatus").innerHTML ="" +mierror;
    document.getElementById("estatus").className="alert alert-error";
    document.getElementById("estatus").style.visibility= "visible";    
    return 0; 
}

/*
 *Funcion para desplegar el espacio que realiza la lectura 
 */
function muestraLectura(){
    var lectura = document.getElementById("lectura");
    lectura.style.display = "block";
    document.getElementById("submitID").disabled=true;
}

/*
 * Funcion para ocultar el espacio de lectura y reinicializar los campos de la subida
 * del documento
 */
function cancelaLectura(){                        
    var lectura = document.getElementById("lectura");
    lectura.style.display = "none";
    document.getElementById("submitID").disabled=false;
    document.getElementById("estatus").style.visibility="hidden";
    document.getElementById("txtFile").value="";
}

/*
 * Funcion para desplegar el mensaje de respuesta de la lectura
 */
function errorLectura()	{
    var msg="La lectura fue exitosa"
    document.getElementById("errorLectura").innerHTML=""+msg;
    muestraResultado();
}

/*
 *Funcion para desplegar el registro de exceles subidos por un usuario al ingresar
 *a la pagina de subirArchivo.
*/
function cargarRegistroExceles(fecha, hora, deptPlan, nombreArchivo){
    addRow(hora, fecha, deptPlan, nombreArchivo);
}

/*
 * Funcion para insertar un renglon de manera cronologica respecto a la fecha.
 */
function addRow(hora, fecha, deptPlan, nombreArchivo){
    var table = document.getElementById("tabla1");
    var row=table.insertRow(1);
    var cell1=row.insertCell(0);
    var cell2=row.insertCell(1);
    var cell3=row.insertCell(2);
    var cell4=row.insertCell(3);
    cell1.innerHTML=""+fecha;
    cell2.innerHTML=""+hora;
    cell3.innerHTML=""+deptPlan;
    cell4.innerHTML=""+nombreArchivo;                       
}	
                 
/*
 *Funcion ajax para llamar al servlet encargado de realizar la lectura del documento
 *Se envia como parametro el nombre del archivo
 */
function lectura(){             
    var  archivo =  document.getElementById("txtFile").value;
    document.getElementById("estatusLectura").style.visibility = "visible";
    document.getElementById("progressbarLectura").style.visibility = "visible";
    document.getElementById("iniciarLectura").disabled=true;
    document.getElementById("cancelarLectura").disabled=true;
    //alert("dep "+departamento+ " archivo "+archivo);
    //Objeto request utilizado por AJAX
    req2 =getRequestObject();
    req2.onreadystatechange=procesarRespuesta;
    req2.open("GET","ReadingModule?archivo="+archivo, true );
    //req2.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=ISO-8859-1');
    //req2.send("archivo="+archivo);  
    req2.send(null); 
}

/*
 * Funcion para procesar la respuesta de la funcion ajax de lectura de documento
 * Desactiva los botones para realizar la lectura y llama a la funcion para registrar
 * en la base de datos la informacion de la lectura y el registro del excel.
 */
function procesarRespuesta(){
    //var respuesta = request.responseXML;
    if(req2.readyState==4){
        //procesar el resultado de la lectura        
        document.getElementById("progressbarLectura").style.visibility = "hidden";
        document.getElementById("estatusLectura").innerHTML = "Lectura exitosa";
        document.getElementById("estatusLectura").className="alert alert-success";
        var xml=req2.responseXML;
        var departamento = xml.getElementsByTagName("departamento")[0].firstChild.nodeValue;  
        actualizarRegistroExcel(departamento,0);
    }
}

/*
 *Funcion ajax para llamar al servlet encargado de realizar la lectura del documento de planes de estudio
 *Se envia como parametro el nombre del archivo
 */
function lecturaPlan(){             
    var  archivo =  document.getElementById("txtFile").value;
    document.getElementById("estatusLectura").style.visibility = "visible";
    document.getElementById("progressbarLectura").style.visibility = "visible";
    document.getElementById("iniciarLectura").disabled=true;
    document.getElementById("cancelarLectura").disabled=true;
    //alert("dep "+departamento+ " archivo "+archivo);
    //Objeto request utilizado por AJAX
    req3 =getRequestObject();
    req3.onreadystatechange=procesarRespuestaPlan;
    req3.open("GET","PlanReader?archivo="+archivo, true );
    //req2.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=ISO-8859-1');
    //req2.send("archivo="+archivo);  
    req3.send(null); 
}

/*
 * Funcion para procesar la respuesta de la funcion ajax de lectura de documento de planes de estudio
 * Desactiva los botones para realizar la lectura y llama a la funcion para registrar
 * en la base de datos la informacion de la lectura y el registro del excel.
 */
function procesarRespuestaPlan(){
    //var respuesta = request.responseXML;
    if(req3.readyState==4){
        //procesar el resultado de la lectura
        
        document.getElementById("estatusLectura").style.visibility = "hidden";
         document.getElementById("progressbarLectura").style.visibility = "hidden";
         document.getElementById("estatusLectura").innerHTML = "Lectura exitosa";
        document.getElementById("estatusLectura").className="alert alert-success";
        var xml=req3.responseXML;
        var plan = xml.getElementsByTagName("plan")[0].firstChild.nodeValue;  
        actualizarRegistroExcel(plan,1);
    }
}                

/*
 * Funcion ajax para registrar en la base de datos la informacion de la lectura del archivo.
 */
function actualizarRegistroExcel(deptPlan, tipo){ 
    //Variables para obtener la fecha y hora actual
    var currentTime = new Date();
    var mes = currentTime.getMonth() + 1;
    var dia = currentTime.getDate();
    var anio = currentTime.getFullYear();
    var fecha =anio + "-" + mes + "-" + dia +" ";
    var hora = currentTime.getHours() + ":" + (currentTime.getMinutes() < 10 ? ("0" + 
        currentTime.getMinutes()): currentTime.getMinutes()   ) ;
    //Variable para obtener el nombre del documento      
    var  nombreArchivo =  document.getElementById("txtFile").value;
    //Variable para obtener el id del usuario        
    var usuario = document.getElementById("usuario").value;
            
    //Objeto request utilizado por AJAX
    requestRegistro =getRequestObject();
    requestRegistro.onreadystatechange=procesarRespuestaRegistro;       
    requestRegistro.open("POST","registroExcel", true ); 
    requestRegistro.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=ISO-8859-1');
    requestRegistro.send("idUsuario="+usuario+"&deptPlan="+deptPlan+"&fecha="+fecha+"&hora="+hora
        +"&nombreArchivo="+nombreArchivo+"&tipo="+tipo);  
            
           
}

/*
 *Funcion getRequestObject. Utilizado por AJAX para crear el objetoRequest y pasar el control al servlet.
*/
function getRequestObject(){
    if(window.ActiveXObject){
        return (new ActiveXObject("Microsoft.XMLHTTP"));
    } else if(window.XMLHttpRequest){
        return (new XMLHttpRequest());
    } else {
        return null;
    }
}

/*
 * Funcion que procesa la respuesta de la funcion ajax para registrar la lectura del archivo
 */
function procesarRespuestaRegistro(){
    //var respuesta = request.responseXML;
    if(requestRegistro.readyState==4){
        //Obtiene los datos para desplegarlos en la tabla de ultimas actualizaciones
        var xml=requestRegistro.responseXML;
        var fecha = xml.getElementsByTagName("fecha")[0].firstChild.nodeValue;           
        var hora = xml.getElementsByTagName("hora")[0].firstChild.nodeValue;
        var tipo = xml.getElementsByTagName("tipo")[0].firstChild.nodeValue;
        var deptPlan = xml.getElementsByTagName("deptPlan")[0].firstChild.nodeValue;
        var nombreArchivo = xml.getElementsByTagName("nombreArchivo")[0].firstChild.nodeValue;
        //Despliega el mensaje del procesos de subida y lectura             
        document.getElementById("resultadolectura").style.display="block";         
        if(tipo==0){
            document.getElementById("dept").innerHTML=""+deptPlan;
        } else {
        document.getElementById("plan").innerHTML=""+deptPlan;
        }
        document.getElementById("fecha").innerHTML=""+fecha+" a las "+ hora;
        //Despliega la informacion del registro en la tabla
        cargarRegistroExceles(fecha, hora, deptPlan, nombreArchivo);
    }
}
        