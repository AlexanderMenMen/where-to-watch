package com.example.wheresee;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

/**
 * ActivityInfo muestra la información de una serie:
 * - Imagen de portada (usando Picasso para cargar la imagen)
 * - Título de la serie
 * - Número de temporadas
 * Los datos se reciben mediante un Bundle del Intent.
 */
public class ActivityInfo extends AppCompatActivity
{
    private ImageView imagen;
    private TextView titulo;
    private TextView temporada;
    private String nombreTitulo;
    private String recursoImagen;
    private String numTemporadas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_series);

        // Inicializamos las vistas
        imagen = findViewById(R.id.portadaVista);
        titulo = findViewById(R.id.tituloVista);
        temporada = findViewById(R.id.temporadasVista);

        // Recuperamos los datos enviados desde la actividad anterior
        Bundle b = getIntent().getExtras();
        if (b != null)
        {
            nombreTitulo = b.getString("Titulo");
            recursoImagen = b.getString("RecursoImagen");
            numTemporadas = b.getString("Temporadas");
        }

        // Mostramos los datos en las vistas correspondientes
        titulo.setText("Titulo: " + nombreTitulo);

        // Cargamos la imagen en el ImageView utilizando Picasso
        Picasso.get().load(recursoImagen).into(imagen);

        temporada.setText("Temporadas: " + numTemporadas);
    }
}
