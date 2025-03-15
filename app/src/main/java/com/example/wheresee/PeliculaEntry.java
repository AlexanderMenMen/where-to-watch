package com.example.wheresee;

import android.provider.BaseColumns;

/**
 * PeliculaEntry define las constantes para la tabla de películas en la base de datos.
 */
public class PeliculaEntry implements BaseColumns
{
    public static final String PELICULA_NOMBRE_TABLA = "PELICULAS";
    public static final String PELICULA_ID = "id";
    public static final String PELICULA_NOMBRE = "nombre";
    public static final String PELICULA_GENERO = "genero";
    public static final String PELICULA_PLATAFORMA = "plataformas";
    public static final String PELICULA_DIRECTOR = "director";
    public static final String PELICULA_DURACION = "duracion";
}
