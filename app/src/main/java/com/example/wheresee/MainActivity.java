package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * MainActivity es la actividad principal de la aplicación.
 * Muestra un campo de búsqueda para ingresar el nombre de una película y dispone de un menú lateral para navegar
 * entre las diferentes secciones de la app.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    // Variables de interfaz
    private EditText editTextPelicula;
    private TextView enfocarFoco;
    private DrawerLayout drawerLayout;

    // Etiqueta para identificar la actividad secundaria (DisplayFilm)
    private static final int SECONDARY_ACTIVITY_TAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Intent para lanzar la actividad DisplayFilm
        final Intent intentDisplayFilm = new Intent(this, DisplayFilm.class);

        // Inicialización de vistas
        editTextPelicula = findViewById(R.id.editTextPelicula);
        enfocarFoco = findViewById(R.id.enfocarFoco);

        // Configuración del TextInputLayout y su EditText
        TextInputLayout tiLayout = findViewById(R.id.tiLayout);
        tiLayout.setErrorEnabled(true);
        // Configuramos el listener para la acción de "send" en el teclado virtual
        editTextPelicula.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                // Si se pulsa el botón de enviar en el teclado
                if (actionId == EditorInfo.IME_ACTION_SEND)
                {
                    // Se añade el nombre de la película al intent y se lanza la actividad
                    intentDisplayFilm.putExtra("nombre", editTextPelicula.getText().toString());
                    startActivityForResult(intentDisplayFilm, SECONDARY_ACTIVITY_TAG);
                    // Se limpia el campo y se quita el foco
                    editTextPelicula.setText("");
                    editTextPelicula.clearFocus();
                    return true;
                }
                return false;
            }
        });
        // Solicita el foco en el TextView oculto para evitar que aparezca el teclado automáticamente
        enfocarFoco.requestFocus();

        // Configuración del Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        // Configuración del Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout_Main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configuración del NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Maneja las selecciones del menú del Navigation Drawer.
     *
     * @param menuItem El elemento seleccionado.
     * @return true si la selección fue manejada.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        Intent intent;
        switch (menuItem.getItemId())
        {
            case R.id.Inicio:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.PelisGuardadas:
                intent = new Intent(this, ActivityGuardadas.class);
                break;
            case R.id.UsuariosGuardados:
                intent = new Intent(this, ActivityMostrarUser.class);
                break;
            case R.id.SeriesGuardadas:
                intent = new Intent(this, ActivitySeriesMostrar.class);
                break;
            case R.id.ConfiguredFragment:
                intent = new Intent(this, ActivityConfig.class);
                break;
            case R.id.ConfiguredFirebase:
                intent = new Intent(this, ActivityLoginFirebase.class);
                break;
            case R.id.logOutFragment:
                // Realiza la desconexión del usuario de Firebase y redirige a MainUser
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(this, MainUser.class);
                break;
            default:
                throw new IllegalArgumentException("El menú no fue implementado!!");
        }
        // Inicia la actividad correspondiente y finaliza la actual
        startActivity(intent);
        finish();

        // Cierra el drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
