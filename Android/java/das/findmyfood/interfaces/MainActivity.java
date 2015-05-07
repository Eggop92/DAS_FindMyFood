package das.findmyfood.interfaces;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Locale;

import das.findmyfood.R;
import das.findmyfood.conexiones.BaseDeDatosLocal;
import das.findmyfood.estructuras_de_datos.C;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPlayServices();
        //de la sharedPreferences miramos si tenemos ya el usuario logeado
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String u = pref.getString(C.USUARIO, "");
        //si no tenemos informacion, cargamos la pantalla de login
        if(u.isEmpty()){
            //cargarRestaurantesFavoritos(u);
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        //Si no hay información, seguimos con la activity para proceder al logueo
        setContentView(R.layout.activity_main);
    }

    protected void onResume(){
        super.onResume();
        checkPlayServices();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.seleciona_idioma);
            dialog.setSingleChoiceItems(R.array.Idiomas, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cidioma = getResources().getStringArray(R.array.CodigoIdiomas)[which];
                    Locale nuevaloc = new Locale(cidioma);
                    Locale.setDefault(nuevaloc);
                    Configuration config = new Configuration();
                    config.locale = nuevaloc;
                    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                    setContentView(R.layout.activity_main);
                    dialog.cancel();
                }
            });
            dialog.show();
            return true;
        }else if(id == R.id.action_logOff){
            //borro la preferencia
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            pref.edit().remove(C.USUARIO).commit();
            //borro la base de datos local
            borrarBDLocal();
            //salgo a la activity de Login
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void borrarBDLocal() {
        BaseDeDatosLocal bd = new BaseDeDatosLocal(this);
        bd.clear();
        bd.close();
    }

    /**
     * Listener para el boton de verRestaurantes
     * @param v
     */
    public void clickBotonRestaurantes(View v){
        Intent i = new Intent(this, ListaRestaurantesActivity.class);
        startActivity(i);
    }

    /**
     * Listener para el boton de ver ofertas
     * @param v
     */
    public void clickBotonOfertas(View v){
        Intent i = new Intent(this, ListaOfertasActivity.class);
        startActivity(i);
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
