package com.example.wheresee;

/**
 * La clase Series representa una serie con su título, recurso de imagen y número de temporadas.
 */
public class Series
{
    // Variables de instancia
    private String titulo;
    private String recursoImagen;
    private String temporadas;

    /**
     * Constructor vacío necesario para algunas operaciones (por ejemplo, Firebase).
     */
    public Series()
    {
    }

    /**
     * Constructor para crear un objeto Series con los atributos especificados.
     *
     * @param titulo         Título de la serie.
     * @param recursoImagen  URL o ruta del recurso de imagen de la serie.
     * @param temporadas     Número de temporadas de la serie.
     */
    public Series(String titulo, String recursoImagen, String temporadas)
    {
        this.titulo = titulo;
        this.recursoImagen = recursoImagen;
        this.temporadas = temporadas;
    }

    /**
     * Obtiene el título de la serie.
     *
     * @return Título de la serie.
     */
    public String getTitulo()
    {
        return titulo;
    }

    /**
     * Establece el título de la serie.
     *
     * @param titulo Título de la serie.
     */
    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    /**
     * Obtiene el recurso de imagen de la serie.
     *
     * @return URL o ruta del recurso de imagen.
     */
    public String getRecursoImagen()
    {
        return recursoImagen;
    }

    /**
     * Establece el recurso de imagen de la serie.
     *
     * @param recursoImagen URL o ruta del recurso de imagen.
     */
    public void setRecursoImagen(String recursoImagen)
    {
        this.recursoImagen = recursoImagen;
    }

    /**
     * Obtiene el número de temporadas de la serie.
     *
     * @return Número de temporadas.
     */
    public String getTemporadas()
    {
        return temporadas;
    }

    /**
     * Establece el número de temporadas de la serie.
     *
     * @param temporadas Número de temporadas.
     */
    public void setTemporadas(String temporadas)
    {
        this.temporadas = temporadas;
    }
}
