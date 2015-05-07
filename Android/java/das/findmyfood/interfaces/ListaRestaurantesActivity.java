package das.findmyfood.interfaces;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;

import das.findmyfood.R;
import das.findmyfood.estructuras_de_datos.C;


public class ListaRestaurantesActivity extends FragmentActivity implements ListaRestauranteFragment.ListenerListaRestaurante {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_restaurantes);

        ListaRestauranteFragment d= (ListaRestauranteFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentRestaurantes);
        d.getListView().setBackgroundColor(getResources().getColor(R.color.red));
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_restaurantes, menu);
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

    @Override
    public void onListItemClick(String id) {
        //comprobamos si el dispositivo esta en posicion retraro o apaisado.
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();
        if(mDisplay.getRotation() != Surface.ROTATION_0 &&  mDisplay.getRotation() != Surface.ROTATION_180) {
            //Si esta en posicion apaisado, cargamos el fragment de detalles y lo actualizamos
            DetalleRestauranteFragment details = (DetalleRestauranteFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentRestaurantes2);
            details.actualizar(id);
        } else {
            //Si esta en posicion retrato, llamamos a la activity con el detalle del restaurante.
            Intent i = new Intent(getApplicationContext(), DetallesRestauranteActivity.class);
            i.putExtra(C.OPCION_SELECCIONADA, id);
            startActivity(i);
        }
    }
}
