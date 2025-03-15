package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * ActivityGuardadas muestra una lista de películas guardadas en la base de datos.
 * Al pulsar sobre un elemento se lanza DisplayFilm pasando el id correspondiente.
 * Además, cuenta con un Navigation Drawer para navegar entre las diferentes secciones.
 */
public class ActivityGuardadas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private ListView listaView;
    private DrawerLayout drawerLayout;
    private DBHelper mydb;

    // Ya NO necesitamos currentUserId aquí, lo leeremos de Aplicacion
    // private int currentUserId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peliculas_guardadas);

        // Inicializamos la base de datos
        mydb = new DBHelper(this);

        // Obtenemos el listado completo (cadena que contiene id y nombre)
        ArrayList<String> array_list = mydb.getAllFilms();

        // Separamos los ids y nombres en dos listas distintas
        ArrayList<String> nuevaListNombres = new ArrayList<>();
        final ArrayList<String> nuevaListIds = new ArrayList<>();

        for (String contenido : array_list)
        {
            int pos = contenido.indexOf(" ");
            nuevaListIds.add(contenido.substring(0, pos));
            nuevaListNombres.add(contenido.substring(pos + 1));
        }

        // Configuramos el ListView para mostrar los nombres
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nuevaListNombres);
        listaView = findViewById(R.id.listViewDatos);
        listaView.setAdapter(arrayAdapter);

        // Al pulsar un elemento del ListView, obtenemos su id y lanzamos DisplayFilm
        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posicion, long id)
            {
                int id_To_Search = Integer.parseInt(nuevaListIds.get(posicion));
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), DisplayFilm.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        // Configuración del Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_Pelis);
        setSupportActionBar(toolbar);

        // Configuración del Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout_PelisG);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configuración del NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view_pelis);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        // En este caso se inicia DisplayFilm sin id para mostrar pantalla vacía
        switch (item.getItemId())
        {
            case R.id.editar:
            {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(), DisplayFilm.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        // Obtenemos el ID del usuario logueado desde Aplicacion
        int currentUserId = ((Aplicacion) getApplication()).getUserId();

        switch (menuItem.getItemId())
        {
            case R.id.Inicio:
            {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            }
            case R.id.PelisGuardadas:
            {
                // Podemos seguir en la misma Activity,
                // pero si quisieras reiniciarla:
                Intent a = new Intent(this, ActivityGuardadas.class);
                startActivity(a);
                finish();
                break;
            }
            case R.id.UsuariosGuardados:
            {
                // Si necesitamos ID en la otra actividad, podemos leerlo allí directamente de Aplicacion
                Intent z = new Intent(this, ActivityMostrarUser.class);
                startActivity(z);
                finish();
                break;
            }
            case R.id.SeriesGuardadas:
            {
                Intent b = new Intent(this, ActivitySeriesMostrar.class);
                startActivity(b);
                finish();
                break;
            }
            case R.id.ConfiguredFragment:
            {
                // Verificamos que el ID del usuario sea válido antes de lanzar ActivityConfig.
                if (currentUserId > 0)
                {
                    Intent y = new Intent(this, ActivityConfig.class);
                    startActivity(y);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "No se encontró el ID de usuario", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.ConfiguredFirebase:
            {
                Intent v = new Intent(this, ActivityLoginFirebase.class);
                startActivity(v);
                finish();
                break;
            }
            case R.id.logOutFragment:
            {
                FirebaseAuth.getInstance().signOut();
                // Ponemos el ID a 0 para indicar que ya no hay sesión
                ((Aplicacion) getApplication()).setUserId(0);

                Intent x = new Intent(this, MainUser.class);
                startActivity(x);
                finish();
                break;
            }
            default:
            {
                throw new IllegalArgumentException("El menú no fue implementado!!");
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

