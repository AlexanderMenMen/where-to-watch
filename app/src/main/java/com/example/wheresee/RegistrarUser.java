package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * RegistrarUser permite al usuario registrarse en la aplicación.
 * Se recogen los datos de usuario (nombre, apellido, usuario y contraseña) y se insertan en la base de datos.
 * Si el registro es exitoso, se redirige a la pantalla de inicio de sesión (MainUser).
 */
public class RegistrarUser extends AppCompatActivity implements View.OnClickListener
{
    private DBHelper db;
    private EditText user;
    private EditText password;
    private EditText nombre;
    private EditText apellido;
    private Button registrarse;
    private Button cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // Inicializamos las vistas
        user = findViewById(R.id.editTextTextUsuarioReg);
        password = findViewById(R.id.editTextTextPasswordReg);
        nombre = findViewById(R.id.editTextTextNombreReg);
        apellido = findViewById(R.id.editTextTextApellidoreg);
        registrarse = findViewById(R.id.RegistrarseReg);
        cancelar = findViewById(R.id.CancelarReg);

        // Asignamos listeners a los botones
        registrarse.setOnClickListener(this);
        cancelar.setOnClickListener(this);

        db = new DBHelper(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.RegistrarseReg:
            {
                // Creamos un objeto Usuario con los datos ingresados
                Usuario u = new Usuario();
                u.setNombre(nombre.getText().toString());
                u.setApellidos(apellido.getText().toString());
                u.setUsuario(user.getText().toString());
                u.setContraseña(password.getText().toString());

                // Validamos que los campos no estén vacíos
                if (u.isNull())
                {
                    Toast.makeText(this, "ERROR: Campos vacíos", Toast.LENGTH_LONG).show();
                }
                else if (db.insertUser(u))
                {
                    Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_LONG).show();
                    Intent i2 = new Intent(RegistrarUser.this, MainUser.class);
                    startActivity(i2);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Usuario ya registrado", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.CancelarReg:
            {
                // Cancelamos el registro y volvemos a la pantalla de inicio de sesión
                Intent i = new Intent(RegistrarUser.this, MainUser.class);
                startActivity(i);
                finish();
                break;
            }
        }
    }
}
