package com.example.wheresee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * MainUser es la actividad de inicio de sesión.
 * Permite iniciar sesión mediante usuario/contraseña o con Google (Firebase).
 * También permite acceder a la pantalla de registro.
 */
public class MainUser extends AppCompatActivity implements View.OnClickListener
{
    // Variables para el manejo de la base de datos local y autenticación
    private DBHelper db;
    private EditText user;
    private EditText password;
    private Button entrar;
    private Button registrarse;
    private SignInButton loginFirebase;

    // Constante para la solicitud de inicio de sesión con Google
    private static final int RC_SIGN_IN = 9001;

    // Variables para autenticación con Firebase y Google
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configuración de Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Inicialización de vistas
        user = findViewById(R.id.editTextTextUsuarioLogin);
        password = findViewById(R.id.editTextTextPasswordLogin);
        entrar = findViewById(R.id.iniciarSesionLogin);
        registrarse = findViewById(R.id.RegistrarseLogin);
        loginFirebase = findViewById(R.id.LoginFirebase);

        // Asignamos listeners a los botones
        entrar.setOnClickListener(this);
        registrarse.setOnClickListener(this);
        loginFirebase.setOnClickListener(this);

        db = new DBHelper(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iniciarSesionLogin:
            {
                String u = user.getText().toString();
                String p = password.getText().toString();

                if (u.equals("") || p.equals(""))
                {
                    Toast.makeText(this, "ERROR: Usuario y/o Contraseña no introducidos", Toast.LENGTH_LONG).show();
                }
                else if (db.login(u, p) == 1)
                {
                    // Se obtiene la información del usuario desde la base de datos
                    Usuario ux = db.getUsuario(u, p);

                    // Asigna el ID del usuario de forma global:
                    ((Aplicacion) getApplication()).setUserId(ux.getId());

                    // Se pasa el ID del usuario a ActivityConfig mediante extra
                    Intent i2 = new Intent(MainUser.this, MainActivity.class);
                    i2.putExtra("Id", ux.getId());
                    startActivity(i2);
                    finish();
                    Toast.makeText(this, "Datos Correctos", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Usuario y/o Contraseña no son correctas", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.RegistrarseLogin:
            {
                Intent i = new Intent(MainUser.this, RegistrarUser.class);
                startActivity(i);
                break;
            }
            case R.id.LoginFirebase:
            {
                sign();
                break;
            }
        }
    }

    /**
     * Inicia el proceso de inicio de sesión con Google.
     */
    private void sign()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Procesa el resultado del Intent de Google Sign-In
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (ApiException e)
            {
                Toast.makeText(this, "En el onActivity: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Autentica con Firebase utilizando las credenciales de Google.
     *
     * @param idToken El token de identificación obtenido de Google.
     */
    private void firebaseAuthWithGoogle(String idToken)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(MainUser.this, "Fallo la autenticación", Toast.LENGTH_LONG).show();
                            updateUI(null);
                            finish();
                        }
                    }
                });
    }

    /**
     * Actualiza la interfaz en función del usuario autenticado.
     *
     * @param user Usuario de Firebase.
     */
    private void updateUI(FirebaseUser user)
    {
        if (user != null)
        {
            Intent i = new Intent(MainUser.this, MainActivity.class);
            startActivity(i);
            Toast.makeText(this, "Datos Correctos", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MainUser.this, "Error en el registro con Google", Toast.LENGTH_LONG).show();
        }
    }
}
