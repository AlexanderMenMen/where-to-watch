package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
 * ActivityMostrarUser muestra la lista de usuarios registrados en la base de datos.
 * Se carga un ListView con los nombres completos obtenidos de la base de datos.
 * Además, cuenta con un Navigation Drawer para navegar entre las diferentes secciones.
 */
public class ActivityMostrarUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private ListView lista;
    private DBHelper db;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_usuario);

        // Inicializamos las vistas
        lista = findViewById(R.id.listViewUsuarios);
        db = new DBHelper(this);

        // Obtenemos el listado de usuarios desde la base de datos
        ArrayList<Usuario> usuarios = db.selectUsuario();
        ArrayList<String> list = new ArrayList<>();

        // Para cada usuario, concatenamos nombre y apellidos y lo agregamos a la lista
        for (Usuario u : usuarios)
        {
            list.add(u.getNombre() + " " + u.getApellidos());
        }

        // Configuramos el ListView usando un ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lista.setAdapter(adapter);

        // Configuración del Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarMostrar);
        setSupportActionBar(toolbar);

        // Configuración del Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout_Mostrar);
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
        NavigationView navigationView = findViewById(R.id.nav_view_mostrar);
        navigationView.setNavigationItemSelectedListener(this);
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

        // Cerramos el Navigation Drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
