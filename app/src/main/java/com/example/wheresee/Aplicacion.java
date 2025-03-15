package com.example.wheresee;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Clase Aplicacion que extiende de Application.
 * Se encarga de inicializar componentes globales de la app, como la instancia de FirebaseDatabase,
 * el adaptador para las series, y almacenar el ID del usuario logueado.
 */
public class Aplicacion extends Application
{
    private AdaptadorSeries adaptador;
    private FirebaseAuth auth;

    // Variable global para almacenar el ID del usuario logueado
    private int userId = 0;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Inicializa la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Habilita la persistencia para que funcione en modo offline
        database.setPersistenceEnabled(true);

        // Obtiene la referencia a la rama "series" en la base de datos
        DatabaseReference serieReference = database.getReference().child("series");

        // Crea la instancia del adaptador y la instancia de FirebaseAuth
        adaptador = new AdaptadorSeries(this, serieReference);
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Retorna la instancia de FirebaseAuth.
     *
     * @return auth Instancia de FirebaseAuth.
     */
    public FirebaseAuth getAuth()
    {
        return auth;
    }

    /**
     * Retorna el adaptador de series utilizado en toda la aplicación.
     *
     * @return adaptador AdaptadorSeries.
     */
    public AdaptadorSeries getAdaptador()
    {
        return adaptador;
    }

    /**
     * Asigna el ID del usuario logueado de forma global.
     * @param userId ID del usuario.
     */
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * Retorna el ID del usuario logueado almacenado globalmente.
     * @return ID del usuario.
     */
    public int getUserId()
    {
        return userId;
    }
}
