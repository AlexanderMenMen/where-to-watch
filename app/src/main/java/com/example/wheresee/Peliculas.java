package com.example.wheresee;

/**
 * La clase Peliculas representa una película con sus atributos básicos.
 */
public class Peliculas
{
    // Variables de instancia
    private int id;
    private String nombre;
    private String genero;
    private String plataformas;
    private String director;
    private String duracion;

    /**
     * Constructor para crear un objeto Peliculas.
     *
     * @param id                 Identificador único de la película.
     * @param nombre             Nombre de la película.
     * @param genero             Género de la película.
     * @param plataformas        Plataformas donde se puede ver la película.
     * @param nombreDelDirector  Nombre del director de la película.
     * @param duracion           Duración de la película.
     */
    public Peliculas(int id, String nombre, String genero, String plataformas, String nombreDelDirector, String duracion)
    {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.plataformas = plataformas;
        this.director = nombreDelDirector;
        this.duracion = duracion;
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

    public String getGenero()
    {
        return genero;
    }

    public void setGenero(String genero)
    {
        this.genero = genero;
    }

    public String getPlataformas()
    {
        return plataformas;
    }

    public void setPlataformas(String plataformas)
    {
        this.plataformas = plataformas;
    }

    public String getDirector()
    {
        return director;
    }

    public void setDirector(String director)
    {
        this.director = director;
    }

    public String getDuracion()
    {
        return duracion;
    }

    public void setDuracion(String duracion)
    {
        this.duracion = duracion;
    }
}
