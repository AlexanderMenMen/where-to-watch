package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ActivityLoginFirebase muestra la información del usuario autenticado mediante Firebase/Google Sign-In.
 * En el layout se muestra el nombre y el email, junto con una imagen animada.
 * Además, incorpora un Navigation Drawer para navegar entre las diferentes secciones de la app.
 */
public class ActivityLoginFirebase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private TextView nombre;
    private TextView email;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_firebase);

        // Inicializamos las vistas
        nombre = findViewById(R.id.nombreCuentaFirebase);
        email = findViewById(R.id.emailCuentaFirebase);

        // Obtenemos la cuenta de Google del usuario
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null)
        {
            nombre.setText("Bienvenido: " + signInAccount.getDisplayName());
            email.setText("Tu email es: " + signInAccount.getEmail());
        }

        // Configuramos el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_firebase);
        setSupportActionBar(toolbar);

        // Configuración del Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout_firebase);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configuramos el NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view_firebase);
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

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
