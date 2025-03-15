package com.example.wheresee;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ActivityConfig permite al usuario editar o eliminar su cuenta.
 */
public class ActivityConfig extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{
    // Variables de interfaz
    private TextView nombre;
    private Button editarConfig, eliminarConfig;
    // Variables para el usuario y la base de datos
    private Usuario u;
    private DBHelper db;
    private DrawerLayout drawerLayout;
    // Variable para guardar el ID del usuario
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        // Inicializamos las vistas
        nombre = findViewById(R.id.nombreCuenta);
        editarConfig = findViewById(R.id.botonEditarCuenta);
        eliminarConfig = findViewById(R.id.botonEliminarCuenta);

        editarConfig.setOnClickListener(this);
        eliminarConfig.setOnClickListener(this);

        // Intentamos obtener el ID del Intent; si no se encuentra, lo leemos de Aplicacion
        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("Id"))
        {
            id = b.getInt("Id");
        }
        else
        {
            id = ((Aplicacion) getApplication()).getUserId();
        }

        // Verificamos que el ID sea válido
        if (id <= 0)
        {
            Toast.makeText(this, "No se encontró el ID de usuario", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainUser.class));
            finish();
            return;
        }

        // Inicializamos la base de datos y obtenemos el usuario con ese ID
        db = new DBHelper(this);
        u = db.getUsuarioById(id);
        if (u != null)
        {
            nombre.setText("BIENVENIDO " + u.getNombre() + " " + u.getApellidos() + "\n" + "¿QUE DESEAS REALIZAR?");
        }
        else
        {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainUser.class));
            finish();
            return;
        }

        // Configuración del Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarConfig);
        setSupportActionBar(toolbar);

        // Configuración del Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout_Config);
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
        NavigationView navigationView = findViewById(R.id.nav_view_config);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.botonEditarCuenta:
            {
                // Lanzamos DisplayUser para editar la cuenta
                Intent intent = new Intent(ActivityConfig.this, DisplayUser.class);
                intent.putExtra("Id", id);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.botonEliminarCuenta:
            {
                // Muestra un diálogo de confirmación para eliminar la cuenta
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Estas seguro de eliminar tu cuenta?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (db.deleteUsuario(id))
                                {
                                    Toast.makeText(ActivityConfig.this, "Se ha eliminado correctamente la cuenta", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ActivityConfig.this, MainUser.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(ActivityConfig.this, "ERROR: no se eliminó la cuenta", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();
                            }
                        });
                builder.show();
                break;
            }
        }
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
                a.putExtra("Id", id);
                startActivity(a);
                finish();
                break;
            }
            case R.id.UsuariosGuardados:
            {
                Intent z = new Intent(this, ActivityMostrarUser.class);
                z.putExtra("Id", id);
                startActivity(z);
                finish();
                break;
            }
            case R.id.SeriesGuardadas:
            {
                Intent b = new Intent(this, ActivitySeriesMostrar.class);
                b.putExtra("Id", id);
                startActivity(b);
                finish();
                break;
            }
            case R.id.ConfiguredFragment:
            {
                // Se vuelve a lanzar ActivityConfig pasando el ID del usuario
                Intent y = new Intent(this, ActivityConfig.class);
                y.putExtra("Id", id);
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


