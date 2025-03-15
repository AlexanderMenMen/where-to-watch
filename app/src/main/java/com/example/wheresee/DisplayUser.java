package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * DisplayUser permite al usuario visualizar y editar sus datos.
 * Se muestran los campos: nombre, apellido, usuario y contraseña.
 * Permite actualizar la información en la base de datos o cancelar la operación.
 */
public class DisplayUser extends AppCompatActivity implements View.OnClickListener
{
    private EditText nombreUser;
    private EditText apellido;
    private EditText usuario;
    private EditText contraseña;
    private int id = 0;
    private Usuario u;
    private DBHelper db;
    private Button botonActualizar;
    private Button botonCancelar;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cuenta);

        // Inicializamos las vistas
        nombreUser = findViewById(R.id.editTextTextNombreEdit);
        apellido = findViewById(R.id.editTextTextApellidoEdit);
        usuario = findViewById(R.id.editTextTextUsuarioEdit);
        contraseña = findViewById(R.id.editTextTextPasswordEdit);
        botonCancelar = findViewById(R.id.CancelarEdit);
        botonActualizar = findViewById(R.id.ActualizarEdit);

        // Asignamos los listeners a los botones
        botonActualizar.setOnClickListener(this);
        botonCancelar.setOnClickListener(this);

        // Recuperamos el ID enviado a través del Intent
        Bundle b = getIntent().getExtras();
        id = b.getInt("Id");

        // Inicializamos la base de datos y obtenemos el usuario correspondiente
        db = new DBHelper(this);
        u = db.getUsuarioById(id);

        // Rellenamos los campos con la información del usuario
        nombreUser.setText(u.getNombre());
        apellido.setText(u.getApellidos());
        usuario.setText(u.getUsuario());
        contraseña.setText(u.getContraseña());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ActualizarEdit:
            {
                // Actualizamos el objeto Usuario con los nuevos datos
                u.setNombre(nombreUser.getText().toString());
                u.setApellidos(apellido.getText().toString());
                u.setUsuario(usuario.getText().toString());
                u.setContraseña(contraseña.getText().toString());

                // Validamos que los campos no estén vacíos
                if (u.isNull())
                {
                    Toast.makeText(this, "ERROR: Campos vacíos", Toast.LENGTH_LONG).show();
                }
                else if (db.updateUser(u))
                {
                    Toast.makeText(this, "Actualización Exitosa!!!", Toast.LENGTH_LONG).show();
                    Intent i2 = new Intent(DisplayUser.this, ActivityConfig.class);
                    i2.putExtra("Id", u.getId());
                    startActivity(i2);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "No se puede actualizar", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.CancelarEdit:
            {
                // Cancelamos la edición y volvemos a ActivityConfig
                i = new Intent(DisplayUser.this, ActivityConfig.class);
                i.putExtra("Id", u.getId());
                startActivity(i);
                finish();
                break;
            }
        }
    }
}

