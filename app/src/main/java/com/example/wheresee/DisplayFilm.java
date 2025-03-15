package com.example.wheresee;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * DisplayFilm muestra la información de una película (o la posibilidad de insertarla/editarla).
 * Permite visualizar los datos, habilitar la edición y guardar o eliminar la película.
 * Además, incluye la opción para seleccionar una imagen de la galería.
 */
public class DisplayFilm extends AppCompatActivity
{
    private DBHelper mydb;
    private EditText nombrePelicula;
    private TextView genero;
    private TextView plataformas;
    private TextView director;
    private TextView duracion;
    private int id_To_Update = 0;
    private String nombre;
    private ImageView imagenView;

    private Bundle extras;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_display_film);

        // Recuperamos el nombre pasado desde la actividad anterior
        nombre = getIntent().getStringExtra("nombre");
        nombrePelicula = findViewById(R.id.editTextNombre);
        nombrePelicula.setText(nombre);

        genero = findViewById(R.id.editTextGenero);
        plataformas = findViewById(R.id.editTextPlataforma);
        director = findViewById(R.id.editTextDirector);
        duracion = findViewById(R.id.editTextDuracion);
        imagenView = findViewById(R.id.imageViewFilm);

        mydb = new DBHelper(this);
        extras = getIntent().getExtras();

        // Si se pasó un "id", se trata de una actualización (mostrar datos existentes)
        if (extras != null)
        {
            value = extras.getInt("id");
            if (value > 0)
            {
                Cursor rs = mydb.getData(value);
                id_To_Update = value;
                rs.moveToFirst();

                int columna = rs.getColumnIndex(PeliculaEntry.PELICULA_NOMBRE);
                String nom = rs.getString(columna);
                columna = rs.getColumnIndex(PeliculaEntry.PELICULA_GENERO);
                String gene = rs.getString(columna);
                columna = rs.getColumnIndex(PeliculaEntry.PELICULA_PLATAFORMA);
                String plataf = rs.getString(columna);
                columna = rs.getColumnIndex(PeliculaEntry.PELICULA_DIRECTOR);
                String direc = rs.getString(columna);
                columna = rs.getColumnIndex(PeliculaEntry.PELICULA_DURACION);
                String dur = rs.getString(columna);

                if (!rs.isClosed())
                {
                    rs.close();
                }

                // Ocultamos el botón de guardar en modo visualización
                Button b = findViewById(R.id.Guardar);
                b.setVisibility(View.INVISIBLE);

                // Mostramos los datos en los campos y los deshabilitamos para evitar edición accidental
                nombrePelicula.setText(nom);
                nombrePelicula.setFocusable(false);
                nombrePelicula.setClickable(false);

                genero.setText(gene);
                genero.setFocusable(false);
                genero.setClickable(false);

                plataformas.setText(plataf);
                plataformas.setFocusable(false);
                plataformas.setClickable(false);

                director.setText(direc);
                director.setFocusable(false);
                director.setClickable(false);

                duracion.setText(dur);
                duracion.setFocusable(false);
                duracion.setClickable(false);

                imagenView.setFocusable(false);
                imagenView.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Si se pasó un id (actualización), inflamos el menú para editar/eliminar
        if (extras != null)
        {
            if (value > 0)
            {
                getMenuInflater().inflate(R.menu.display_film, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.editar:
            {
                // Habilitamos la edición: mostramos el botón "Guardar" y habilitamos los campos
                Button b = findViewById(R.id.Guardar);
                b.setVisibility(View.VISIBLE);

                nombrePelicula.setEnabled(true);
                nombrePelicula.setFocusableInTouchMode(true);
                nombrePelicula.setClickable(true);

                genero.setEnabled(true);
                genero.setFocusableInTouchMode(true);
                genero.setClickable(true);

                plataformas.setEnabled(true);
                plataformas.setFocusableInTouchMode(true);
                plataformas.setClickable(true);

                director.setEnabled(true);
                director.setFocusableInTouchMode(true);
                director.setClickable(true);

                duracion.setEnabled(true);
                duracion.setFocusableInTouchMode(true);
                duracion.setClickable(true);

                imagenView.setEnabled(true);
                imagenView.setFocusableInTouchMode(true);
                imagenView.setClickable(true);
                return true;
            }
            case R.id.eliminar:
            {
                // Muestra un diálogo de confirmación para eliminar la película
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.eliminarPeli)
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                mydb.deleteFilm(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Eliminación completa", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ActivityGuardadas.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                // Se cancela la eliminación
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("¿Estas seguro?");
                d.show();
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * Método invocado al pulsar el botón "Guardar".
     * Realiza una actualización o inserción según corresponda.
     *
     * @param view Vista que invoca el método.
     */
    public void run(View view)
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            int Value = extras.getInt("id");
            // Si se inició con un ID, se realiza un UPDATE
            if (Value > 0)
            {
                if (mydb.updateFilm(id_To_Update,
                        nombrePelicula.getText().toString(),
                        genero.getText().toString(),
                        plataformas.getText().toString(),
                        director.getText().toString(),
                        duracion.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ActivityGuardadas.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No actualizado", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // Si se inició sin ID, se trata de una inserción
                if (mydb.insertFilms(
                        nombrePelicula.getText().toString(),
                        genero.getText().toString(),
                        plataformas.getText().toString(),
                        director.getText().toString(),
                        duracion.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Pelicula Insertada", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No se pudo insertar la pelicula", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * Invocado al pulsar sobre la imagen para cambiarla.
     *
     * @param view Vista que invoca el método.
     */
    public void GuardarImagen(View view)
    {
        cargarImagen();
    }

    /**
     * Lanza un Intent para seleccionar una imagen de la galería.
     */
    private void cargarImagen()
    {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(i.createChooser(i, "Seleccione la Aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri path = data.getData();
            imagenView.setImageURI(path);
        }
    }
}

