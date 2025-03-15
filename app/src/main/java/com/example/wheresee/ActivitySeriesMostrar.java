package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ActivitySeriesMostrar muestra las series utilizando un RecyclerView en formato de cuadrícula.
 * Al pulsar un elemento, se lanza ActivityInfo mostrando la información de la serie seleccionada.
 * Además, incluye un Navigation Drawer para navegar entre secciones.
 */
public class ActivitySeriesMostrar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DrawerLayout drawerLayout;
    private Aplicacion app;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        // Obtenemos la instancia global de la aplicación y su adaptador para las series
        app = (Aplicacion) getApplication();

        // Configuramos el RecyclerView y su adaptador
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(app.getAdaptador());
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Configuramos el listener para los ítems del RecyclerView
        app.getAdaptador().setOnItemClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int posicion = recyclerView.getChildAdapterPosition(view);
                Series serieSeleccionada = app.getAdaptador().getItem(posicion);

                // Se crea un Intent para lanzar ActivityInfo pasando los datos de la serie
                Intent i2 = new Intent(ActivitySeriesMostrar.this, ActivityInfo.class);
                i2.putExtra("Titulo", serieSeleccionada.getTitulo());
                i2.putExtra("RecursoImagen", serieSeleccionada.getRecursoImagen());
                i2.putExtra("Temporadas", serieSeleccionada.getTemporadas());
                startActivity(i2);
            }
        });

        // Configuración del Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarSeries);
        setSupportActionBar(toolbar);

        // Configuración del Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout_Series);
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
        NavigationView navigationView = findViewById(R.id.nav_view_Series);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflamos el menú; este se muestra en la Toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.nuevolibro)
        {
            // Se inserta una nueva serie de ejemplo al adaptador
            app.getAdaptador().insertarLibro(new Series("aaaa", "aaaaa", "aaaaaa"));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
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
                Intent a = new Intent(this, ActivityGuardadas.class);
                startActivity(a);
                finish();
                break;
            }
            case R.id.UsuariosGuardados:
            {
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
                Intent y = new Intent(this, ActivityConfig.class);
                startActivity(y);
                finish();
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
