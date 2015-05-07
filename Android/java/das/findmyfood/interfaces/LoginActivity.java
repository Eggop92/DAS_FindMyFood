package das.findmyfood.interfaces;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import das.findmyfood.R;
import das.findmyfood.conexiones.AccesoExternoRestaurantes;
import das.findmyfood.conexiones.AccesoExternoUsuario;
import das.findmyfood.conexiones.BaseDeDatosLocal;
import das.findmyfood.estructuras_de_datos.C;
import das.findmyfood.estructuras_de_datos.Restaurante;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

   //private UserLoginTask mAuthTask = null;
    private AccesoExternoUsuario mAuthTask=null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPlayServices();

        //Obtener las vistas de la interfaz
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView textRegistro = (TextView) findViewById(R.id.textRegistro);
        textRegistro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //La etiqueta abre una activity con el registro
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(i);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    protected void onResume(){
        super.onResume();
        checkPlayServices();
    }

    private void cargarRestaurantesFavoritos(String usuario) {
        try {
            AccesoExternoRestaurantes aer = new AccesoExternoRestaurantes(this);
            aer.execute(C.DESCARGA_FAV, usuario);
            JSONObject json = aer.get();
            if (json.has(C.ERROR)){
                Log.e("LoginActivity", "Error has ocurred: "+json.getString(C.ERROR));
            }else{
                BaseDeDatosLocal bd = new BaseDeDatosLocal(this);
                JSONArray restaurantes = json.getJSONArray(C.RESTAURANTES);
                JSONObject JSONr;
                Restaurante r;
                String nombreRestaurante, pDireccion, pDescripcion, pDescripcionIN;
                double pLatitud, pLongitud;
                int idRestaurante;
                for (int i = 0; i< restaurantes.length(); i++){
                    JSONr = restaurantes.getJSONObject(i);
                    idRestaurante = JSONr.getInt(C.RESTAURANTE_ID);
                    nombreRestaurante = JSONr.getString(C.RESTAURANTE_NOMBRE);
                    pDireccion = JSONr.getString(C.RESTAURANTE_DIRECCION);
                    pLatitud = JSONr.getDouble(C.RESTAURANTE_LATITUD);
                    pLongitud = JSONr.getDouble(C.RESTAURANTE_LONGITUD);
                    pDescripcion = JSONr.getString(C.RESTAURANTE_DESCRIPCION);
                    pDescripcionIN = JSONr.getString(C.RESTAURANTE_DESCRIPCIONIN);
                    r = new Restaurante(idRestaurante, nombreRestaurante, pDireccion, pLatitud, pLongitud, pDescripcion, pDescripcionIN);
                    bd.anadirRestaurante(r);
                }
                bd.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
       // if (mAuthTask != null) {
        //    return;
        //}

        //Se borran los errores que hubiera
        mEmailView.setError(null);
        mPasswordView.setError(null);

        //Cargamos los datos introducidos
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // comprobamos la contraseña
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // comprobamos el email
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {
            //Si ha habido un error ponemos el foco en el error y cancelamos
            focusView.requestFocus();
        } else {
            //Si esta todoo ok se llama al servidor para que haga la comprobacion de credenciales
            showProgress(true);

            try {
                //llamamos al servidor y esperamos la respuesta
                mAuthTask= new AccesoExternoUsuario(this);
                mAuthTask.execute(C.IDENTIFICACION, email, password);
                JSONObject json = mAuthTask.get();
                if(json.has(C.ERROR)){
                    //si hay un error cancelamos
                    Log.e("LoginActivity", json.getString(C.ERROR));
                }else if(json.has(C.FALLO)){
                    //si las credenciales son erroneas, mostramos el error en el email
                    showProgress(false);
                    mEmailView.setError("La pareja email y contraseña no coinciden.");
                    mEmailView.requestFocus();
                }else{
                    //Si no ha habido fallos, guardamos el usuario en las sharedPreferences y
                    //llamamos a la activity principal.
                    SharedPreferences.Editor pref = PreferenceManager.getDefaultSharedPreferences(this).edit();
                    pref.putString(C.USUARIO, email);
                    pref.commit();
                    cargarRestaurantesFavoritos(email);
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                //Dispositivo no configurado. Mostrar ventana de configuración
                //de Google Play Services
                //PLAY_SERVICES_RESOLUTION_REQUEST debe valer 9000
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, C.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Dispositivo no compatible. Terminar la aplicación
                Log.i("LoginActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}



