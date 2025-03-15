package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity muestra una pantalla de splash (pantalla de bienvenida) durante 2 segundos.
 * Después de ese tiempo, redirige al usuario a la pantalla de inicio de sesión (MainUser).
 */
public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Creamos un Handler para retrasar la ejecución de la siguiente acción
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // Después de 2 segundos, se lanza la actividad MainUser y se cierra el Splash
                Intent i = new Intent(SplashActivity.this, MainUser.class);
                startActivity(i);
                finish();
            }
        }, 2000); // Tiempo de retraso en milisegundos (2000ms = 2 segundos)
    }
}
