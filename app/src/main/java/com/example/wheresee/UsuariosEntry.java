package com.example.wheresee;

import android.provider.BaseColumns;

/**
 * UsuariosEntry define las constantes para la tabla de usuarios en la base de datos.
 */
public class UsuariosEntry implements BaseColumns
{
    public static final String USUARIO_NOMBRE_TABLA = "USUARIOS";
    public static final String USUARIO_ID = "id";
    public static final String USUARIO_NOMBRE = "nombre";
    public static final String USUARIO_APELLIDO = "apellido";
    public static final String USUARIO_USUARIO = "usuario";
    public static final String USUARIO_CONTRASEÑA = "contraseña";
}
