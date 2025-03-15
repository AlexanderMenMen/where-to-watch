package com.example.wheresee;

/**
 * La clase Usuario representa un usuario de la aplicación con sus datos personales.
 */
public class Usuario
{
    // Variables de instancia
    private int id;
    private String nombre;
    private String apellidos;
    private String usuario;
    private String contraseña;

    /**
     * Constructor vacío necesario para ciertos frameworks y operaciones.
     */
    public Usuario()
    {
    }

    /**
     * Constructor para crear un objeto Usuario con todos sus datos.
     *
     * @param nombre      Nombre del usuario.
     * @param apellidos   Apellidos del usuario.
     * @param usuario     Nombre de usuario (nick).
     * @param contraseña  Contraseña del usuario.
     */
    public Usuario(String nombre, String apellidos, String usuario, String contraseña)
    {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    /**
     * Método para validar que ninguno de los campos esté vacío.
     *
     * @return true si algún campo es una cadena vacía, false en caso contrario.
     */
    public boolean isNull()
    {
        if (nombre.equals("") || apellidos.equals("") || usuario.equals("") || contraseña.equals(""))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // Getters y Setters

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public void setApellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }

    public String getUsuario()
    {
        return usuario;
    }

    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }

    public String getContraseña()
    {
        return contraseña;
    }

    public void setContraseña(String contraseña)
    {
        this.contraseña = contraseña;
    }
}
