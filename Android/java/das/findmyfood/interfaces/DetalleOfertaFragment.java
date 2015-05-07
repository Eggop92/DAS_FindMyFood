package das.findmyfood.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import das.findmyfood.R;
import das.findmyfood.conexiones.AccesoExternoOfertas;
import das.findmyfood.conexiones.AccesoExternoRestaurantes;
import das.findmyfood.conexiones.BaseDeDatosLocal;
import das.findmyfood.estructuras_de_datos.C;
import das.findmyfood.estructuras_de_datos.Restaurante;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DetalleOfertaFragment extends Fragment {

    public String elRestaurantedeOferta;
    public String idOferta="";

    public DetalleOfertaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_oferta, container, false);
    }

    @Override
    public void onResume() {
        if (idOferta.equals("")){
            // esto lo hago para que cuando se pone el movil el horizontal muestre el detalle del primer elemento
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            idOferta = pref.getString("firstOferta","");
        }
        actualizar(idOferta);
        super.onResume();
    }

    public void actualizar(String pIdOferta){
        idOferta=pIdOferta;
        try {
            //se llama al asyncTask con el id de la oferta, devuelve el JSON con la informacion
            AccesoExternoOfertas oferta = new AccesoExternoOfertas(getActivity());

            oferta.execute(C.obtenerOferta, idOferta);
            JSONObject json = oferta.get();

            //Se comprueba que no ha dado errores.
            if (json.has(C.ERROR)){
                Log.e("DetalleOfertaFragment", "Error oferta JSON: " + json.getString(C.ERROR));
            }else{
                //En caso de que no haya dado errores se carga la información
                JSONObject unaoferta= json.getJSONObject(C.obtenerOferta);

                //Se carga en la interfaz los datos.
                TextView titulo = (TextView) getActivity().findViewById(R.id.textTitulo);
                titulo.setText(unaoferta.getString("TituloOferta"));
                TextView descripcion = (TextView) getActivity().findViewById(R.id.textDescripcion);
                descripcion.setText(unaoferta.getString("DescripcionOferta"));

                Button restOferta = (Button) getActivity().findViewById(R.id.buttonRestaurante);
                elRestaurantedeOferta=unaoferta.getString("NombreResOferta");
                restOferta.setText(elRestaurantedeOferta);
                restOferta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Llamara al restaurante al que pertenece la oferta*/
                        Intent i = new Intent(getActivity().getApplicationContext(), DetallesRestauranteActivity.class);
                        i.putExtra(C.OPCION_SELECCIONADA, elRestaurantedeOferta);
                        startActivity(i);
                        //Log.i("DetalleOfertaFragment", "onClick");

                    }
                });
            }
            //Tratamiento de excepciones
        } catch (InterruptedException e) {
            Log.e("DetalleOfertaFragment", "Lanzada excepcion Interrupted");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e("DetalleOfertaFragment", "Lanzada excepcion Execution");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("DetalleOfertaFragment", "Lanzada excepcion JSON");
            e.printStackTrace();
        }



    }


}
