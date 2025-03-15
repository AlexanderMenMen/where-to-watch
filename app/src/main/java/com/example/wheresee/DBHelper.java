package com.example.wheresee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * DBHelper se encarga de gestionar la base de datos SQLite para almacenar películas y usuarios.
 * Incluye métodos para insertar, actualizar, eliminar y consultar datos.
 */
public class DBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "MisPeliculas.db";
    private ArrayList<Usuario> listaUser;
    private SQLiteDatabase db;

    /**
     * Constructor del DBHelper.
     * Se utiliza la versión 1; para resetear la BDD, se puede aumentar la versión.
     *
     * @param context Contexto de la aplicación.
     */
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Se crea la base de datos y se definen las tablas para películas y usuarios.
     *
     * @param db La base de datos SQLite.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        this.db = db;
        db.execSQL(
                "create table if not exists " + PeliculaEntry.PELICULA_NOMBRE_TABLA + " (" +
                        PeliculaEntry.PELICULA_ID + " integer primary key, " +
                        PeliculaEntry.PELICULA_NOMBRE + " text, " +
                        PeliculaEntry.PELICULA_GENERO + " text, " +
                        PeliculaEntry.PELICULA_PLATAFORMA + " text, " +
                        PeliculaEntry.PELICULA_DIRECTOR + " text, " +
                        PeliculaEntry.PELICULA_DURACION + " text)"
        );
        db.execSQL(
                "create table if not exists " + UsuariosEntry.USUARIO_NOMBRE_TABLA + " (" +
                        UsuariosEntry.USUARIO_ID + " integer primary key autoincrement, " +
                        UsuariosEntry.USUARIO_USUARIO + " text, " +
                        UsuariosEntry.USUARIO_CONTRASEÑA + " text, " +
                        UsuariosEntry.USUARIO_NOMBRE + " text, " +
                        UsuariosEntry.USUARIO_APELLIDO + " text)"
        );
    }

    /**
     * Actualiza la base de datos cuando se detecta un cambio de versión.
     *
     * @param db         La base de datos.
     * @param oldVersion Versión antigua.
     * @param newVersion Nueva versión.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Se eliminan las tablas antiguas y se recrean.
        db.execSQL("DROP TABLE IF EXISTS " + PeliculaEntry.PELICULA_NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + UsuariosEntry.USUARIO_NOMBRE_TABLA);
        onCreate(db);
    }

    /**
     * Inserta una película en la base de datos.
     *
     * @param nombre      Nombre de la película.
     * @param genero      Género de la película.
     * @param plataformas Plataformas donde se puede ver.
     * @param director    Director de la película.
     * @param duracion    Duración de la película.
     * @return true si la inserción fue exitosa.
     */
    public boolean insertFilms(String nombre, String genero, String plataformas, String director, String duracion)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PeliculaEntry.PELICULA_NOMBRE, nombre);
        contentValues.put(PeliculaEntry.PELICULA_GENERO, genero);
        contentValues.put(PeliculaEntry.PELICULA_PLATAFORMA, plataformas);
        contentValues.put(PeliculaEntry.PELICULA_DIRECTOR, director);
        contentValues.put(PeliculaEntry.PELICULA_DURACION, duracion);
        db.insert(PeliculaEntry.PELICULA_NOMBRE_TABLA, null, contentValues);
        return true;
    }

    /**
     * Inserta un usuario en la base de datos, siempre que no exista ya.
     *
     * @param u Objeto Usuario a insertar.
     * @return true si se insertó, false en caso contrario.
     */
    public boolean insertUser(Usuario u)
    {
        // Si el usuario no existe, se inserta.
        if (buscar(u.getUsuario()) == 0)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(UsuariosEntry.USUARIO_USUARIO, u.getUsuario());
            contentValues.put(UsuariosEntry.USUARIO_CONTRASEÑA, u.getContraseña());
            contentValues.put(UsuariosEntry.USUARIO_NOMBRE, u.getNombre());
            contentValues.put(UsuariosEntry.USUARIO_APELLIDO, u.getApellidos());
            return (db.insert(UsuariosEntry.USUARIO_NOMBRE_TABLA, null, contentValues) > 0);
        }
        else
        {
            return false;
        }
    }

    /**
     * Comprueba si un usuario ya existe en la base de datos.
     *
     * @param u Nombre de usuario a buscar.
     * @return Número de veces que aparece el usuario.
     */
    public int buscar(String u)
    {
        int x = 0;
        listaUser = selectUsuario();
        for (Usuario us : listaUser)
        {
            if (us.getUsuario().equals(u))
            {
                x++;
            }
        }
        return x;
    }

    /**
     * Recupera la lista de usuarios registrados en la base de datos.
     *
     * @return ArrayList de objetos Usuario.
     */
    public ArrayList<Usuario> selectUsuario()
    {
        db = this.getWritableDatabase();
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        Cursor res = db.rawQuery("select * from " + UsuariosEntry.USUARIO_NOMBRE_TABLA, null);
        if (res != null && res.moveToFirst())
        {
            do
            {
                Usuario user = new Usuario();
                user.setId(res.getInt(0));
                user.setUsuario(res.getString(1));
                user.setContraseña(res.getString(2));
                user.setNombre(res.getString(3));
                user.setApellidos(res.getString(4));
                usuarios.add(user);
            }
            while (res.moveToNext());
        }
        return usuarios;
    }

    /**
     * Obtiene los datos de una película según su ID.
     *
     * @param id ID de la película.
     * @return Cursor con los datos de la película.
     */
    public Cursor getData(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PeliculaEntry.PELICULA_NOMBRE_TABLA +
                " where " + PeliculaEntry.PELICULA_ID + " = " + id, null);
        return res;
    }

    /**
     * Actualiza los datos de una película.
     *
     * @param id         ID de la película a actualizar.
     * @param nombre     Nuevo nombre.
     * @param genero     Nuevo género.
     * @param plataformas Nuevas plataformas.
     * @param director   Nuevo director.
     * @param duracion   Nueva duración.
     * @return true si la actualización fue exitosa.
     */
    public boolean updateFilm(Integer id, String nombre, String genero, String plataformas, String director, String duracion)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PeliculaEntry.PELICULA_NOMBRE, nombre);
        contentValues.put(PeliculaEntry.PELICULA_GENERO, genero);
        contentValues.put(PeliculaEntry.PELICULA_PLATAFORMA, plataformas);
        contentValues.put(PeliculaEntry.PELICULA_DIRECTOR, director);
        contentValues.put(PeliculaEntry.PELICULA_DURACION, duracion);
        db.update(PeliculaEntry.PELICULA_NOMBRE_TABLA,
                contentValues, PeliculaEntry.PELICULA_ID + " = ? ", new String[] { Integer.toString(id) });
        return true;
    }

    /**
     * Elimina una película de la base de datos.
     *
     * @param id ID de la película a eliminar.
     * @return Número de registros eliminados.
     */
    public Integer deleteFilm(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PeliculaEntry.PELICULA_NOMBRE_TABLA,
                PeliculaEntry.PELICULA_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    /**
     * Recupera una lista con el ID y el nombre de todas las películas.
     *
     * @return ArrayList de cadenas con formato "ID NOMBRE".
     */
    public ArrayList<String> getAllFilms()
    {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PeliculaEntry.PELICULA_NOMBRE_TABLA, null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            int columna = res.getColumnIndex(PeliculaEntry.PELICULA_ID);
            String idString = String.valueOf(res.getInt(columna));
            columna = res.getColumnIndex(PeliculaEntry.PELICULA_NOMBRE);
            array_list.add(idString + " " + res.getString(columna));
            res.moveToNext();
        }
        return array_list;
    }

    /**
     * Realiza el proceso de login comprobando las credenciales del usuario.
     *
     * @param user     Usuario.
     * @param password Contraseña.
     * @return Número de coincidencias (0 si no existe).
     */
    public int login(String user, String password)
    {
        db = this.getWritableDatabase();
        int i = 0;
        Cursor res = db.rawQuery("select * from " + UsuariosEntry.USUARIO_NOMBRE_TABLA, null);
        if (res != null && res.moveToFirst())
        {
            do
            {
                if (res.getString(1).equals(user) && res.getString(2).equals(password))
                {
                    i++;
                }
            }
            while (res.moveToNext());
        }
        return i;
    }

    /**
     * Recupera un objeto Usuario a partir de las credenciales.
     *
     * @param user     Usuario.
     * @param password Contraseña.
     * @return Objeto Usuario o null si no se encuentra.
     */
    public Usuario getUsuario(String user, String password)
    {
        listaUser = selectUsuario();
        for (Usuario u : listaUser)
        {
            if (u.getUsuario().equals(user) && u.getContraseña().equals(password))
            {
                return u;
            }
        }
        return null;
    }

    /**
     * Recupera un objeto Usuario a partir de su ID.
     *
     * @param id ID del usuario.
     * @return Objeto Usuario o null si no se encuentra.
     */
    public Usuario getUsuarioById(int id)
    {
        listaUser = selectUsuario();
        for (Usuario u : listaUser)
        {
            if (u.getId() == id)
            {
                return u;
            }
        }
        return null;
    }

    /**
     * Actualiza la información de un usuario en la base de datos.
     *
     * @param u Objeto Usuario con los datos actualizados.
     * @return true si la actualización fue exitosa.
     */
    public boolean updateUser(Usuario u)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsuariosEntry.USUARIO_USUARIO, u.getUsuario());
        contentValues.put(UsuariosEntry.USUARIO_CONTRASEÑA, u.getContraseña());
        contentValues.put(UsuariosEntry.USUARIO_NOMBRE, u.getNombre());
        contentValues.put(UsuariosEntry.USUARIO_APELLIDO, u.getApellidos());
        return (db.update(UsuariosEntry.USUARIO_NOMBRE_TABLA, contentValues, "id=" + u.getId(), null) > 0);
    }

    /**
     * Elimina un usuario de la base de datos a partir de su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return true si la eliminación fue exitosa.
     */
    public boolean deleteUsuario(int id)
    {
        return (db.delete(UsuariosEntry.USUARIO_NOMBRE_TABLA, "id=" + id, null) > 0);
    }
}

