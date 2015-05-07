package das.findmyfood.interfaces;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import das.findmyfood.R;
import das.findmyfood.estructuras_de_datos.C;


public class DetalleOfertaActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //busco en el intent la oferta que se quiere mostrar
        Bundle extras = getIntent().getExtras();
        String idOferta = extras.getString(C.OPCION_SELECCIONADA, null);
        if(idOferta != null){
            //Si existe, se carga la interfaz, se busca el fragment y se actualiza con la oferta.
            setContentView(R.layout.activity_detalle_oferta);
            DetalleOfertaFragment d= (DetalleOfertaFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentOfertaDetalle);
            d.actualizar(idOferta);
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detalle_oferta, menu);
        return true;
    }*/

   /* @Override
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
