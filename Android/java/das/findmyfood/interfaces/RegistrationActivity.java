package das.findmyfood.interfaces;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import das.findmyfood.R;
import das.findmyfood.conexiones.AccesoExternoUsuario;
import das.findmyfood.estructuras_de_datos.C;


public class RegistrationActivity extends ActionBarActivity {

    private Button boton;
    private AutoCompleteTextView emailBox;
    private EditText passBox;
    private EditText passConfBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //obtener los elementos de la interfaz necesarios
        boton = (Button) findViewById(R.id.botonConfirmar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
        emailBox = (AutoCompleteTextView) findViewById(R.id.editTextEmail);
        passBox = (EditText) findViewById(R.id.editTextContrasena);
        passConfBox = (EditText) findViewById(R.id.editTextContraseñaRepetida);
    }

    private void registrar() {
        //reseteamos los errores en caso de haberlos
        emailBox.setError(null);
        passBox.setError(null);
        passConfBox.setError(null);

        //obtenemos los datos de las cajas de texto
        String email = emailBox.getText().toString();
        String contrasena = passBox.getText().toString();
        String contConf = passConfBox.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Comprobamos la contraseña
        if (!TextUtils.isEmpty(contrasena) && !isPasswordValid(contrasena)) {
            passBox.setError(getString(R.string.error_invalid_password));
            focusView = passBox;
            cancel = true;
        }
        //Comprobamos que ambas contraseñas coincidan
        if(!contrasena.equals(contConf)){
            passConfBox.setError("La contraseña no coincide.");
            focusView = passConfBox;
            cancel = true;
        }
        // Comprobamos el email
        if (TextUtils.isEmpty(email)) {
            emailBox.setError(getString(R.string.error_field_required));
            focusView = emailBox;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailBox.setError(getString(R.string.error_invalid_email));
            focusView = emailBox;
            cancel = true;
        }
        if (cancel) {
            //Si ha habido algun error llevamos el foco hasta el error
            focusView.requestFocus();
        } else {
            try {
                //Si no ha habido error, llamamos al servidor, pasandole las credenciales verificadas
                AccesoExternoUsuario aeu = new AccesoExternoUsuario(this);
                aeu.execute(C.REGISTRO, email, contrasena);
                JSONObject json = aeu.get();
                if(json.has(C.ERROR)){
                    //Comprobamos si devuelve errores y abortamos
                    Log.e("RegistrationActivity", json.getString(C.ERROR));
                }else if(json.has(C.FALLO_USUARIO)){
                    //Comprobamos si hay un error de duplicados
                    emailBox.setError("El correo electronico utilizado ya existe");
                    emailBox.requestFocus();
                }else{
                    //Si se ha tenido éxito, volvemos a la pantalla anterior.
                    Toast.makeText(this,"El registro se ha llevado a cabo con exito.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
