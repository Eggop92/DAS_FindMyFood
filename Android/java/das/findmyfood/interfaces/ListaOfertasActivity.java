package das.findmyfood.interfaces;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ListView;

import das.findmyfood.R;
import das.findmyfood.estructuras_de_datos.C;

public class ListaOfertasActivity extends FragmentActivity implements ListaOfertaFragment.ListenerListaOfertas {

    protected String opcionSeleccionada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String var="";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            var=extras.getString(C.OPCION_SELECCIONADA,"");
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
       // Log.i("OPION SELECCIONADA ON CREATE LISTA OFERTAS ",extras.getString(C.OPCION_SELECCIONADA,""));
        editor.putString(C.OPCION_SELECCIONADA, var);
        editor.commit();

        setContentView(R.layout.activity_lista_ofertas);
        ListaOfertaFragment d= (ListaOfertaFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentOferta);
        d.getListView().setBackgroundColor(getResources().getColor(R.color.red));
        

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_ofertas, menu);
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

    public void onListItemClick(String id) {
        //comprobamos la posicion del dipositivo
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();
        //Si esta en posicion apaisado, cargamos el fragment de detalles y lo actualizamos
        if(mDisplay.getRotation() != Surface.ROTATION_0 &&  mDisplay.getRotation() != Surface.ROTATION_180) {
            DetalleOfertaFragment d= (DetalleOfertaFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentOfertaDetalle);
            d.actualizar(id);
        } else {
            //Si esta en posicion retrato, llamamos a la activity con el detalle de la oferta.
            Intent i = new Intent(getApplicationContext(), DetalleOfertaActivity.class);
            Log.i("AQUI RECIBO EL ID Y ENVIO EN EL INTENT",id);
            i.putExtra(C.OPCION_SELECCIONADA, id);
            startActivity(i);
        }
    }
}
