package clases;

/**
 * @author Ana
 *
 */
public class Usuario {

	private int idU; //primary key
	private int idD; //id departamento
	private String matricula;
	private String nombre;
	private String apellido;
	private char genero;
	private char rol; //p (profesor), c (dir. de carrera), d (dir. de dept.), o (otro)
	private boolean esAdmin;
	private String email;
	private boolean alta;
	private String contrasenia;
	private String[] telefonos;
	
	public Usuario() {
		this.idU = 0;
		this.idD = 0;
		this.matricula = "";
		this.nombre = "";
		this.apellido = "";
		this.genero = ' ';
		this.rol = ' ';
		this.esAdmin = false;
		this.email = "";
		this.alta = false;
		this.contrasenia = "";
		this.telefonos = new String[5];
	}
	
	public Usuario(int idU, int idD, String matricula, String nombre, String apellido, char genero, char rol, boolean esAdmin, String email, boolean alta, String contrasenia, String[] telefonos) {
		this.idU = idU;
		this.idD = idD;
		this.matricula = matricula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.genero = genero;
		this.rol = rol;
		this.esAdmin = esAdmin;
		this.email = email;
		this.alta = alta;
		this.contrasenia = contrasenia;
		this.telefonos = telefonos;
		
	}
	
	public int IdU() {
        return idU;
    }

    public void setIdU(int idU) {
        this.idU = idU;
    }
    
    public int IdD() {
        return idD;
    }

    public void setIdD(int idD) {
        this.idD = idD;
    }
    
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
	
	public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
	
	public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }
	
	public char getRol() {
        return rol;
    }

    public void setRol(char rol) {
        this.rol = rol;
    }
    
    public boolean getEsAdmin() {
    	return esAdmin;
    }
    
    public void setEsAdmin(boolean esAdmin) {
    	this.esAdmin =  esAdmin;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean getAlta() {
    	return alta;
    }
    
    public void setAlta(boolean alta) {
    	this.alta =  alta;
    }
    
    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    
    public String[] getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String[] telefonos) {
        for(int i = 0; i < telefonos.length; i++) {
        	this.telefonos[i] = telefonos[i];
        }
    }
    
}
