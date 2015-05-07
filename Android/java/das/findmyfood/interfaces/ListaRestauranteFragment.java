package das.findmyfood.interfaces;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import das.findmyfood.conexiones.AccesoExternoRestaurantes;
import das.findmyfood.estructuras_de_datos.C;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ListaRestauranteFragment extends ListFragment {

    private ListenerListaRestaurante mListener;
    private LinkedList<String> restaurantes;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListaRestauranteFragment() {
    }

    public String getFirstRestaurant(){
        return restaurantes.getFirst();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantes = new LinkedList<String>();
        try {
            //buscamos los restaurantes en el servidor y descargamos los nombres de estos
            AccesoExternoRestaurantes aer = new AccesoExternoRestaurantes(getActivity());
            aer.execute(C.Lista);
            JSONObject json = aer.get();
            //Comprobamos que no existen errores.
            if(json.has(C.ERROR)){
                Log.e("ListaRestauranteFragment", "error has ocurred: "+json.getString(C.ERROR));
            }else{
                //cargamos los nombres en una lista para mostrar mas tarde
                JSONArray array = json.getJSONArray("Restaurantes");
                for(int i = 0; i<array.length(); i++){
                    if (i==0){
                        // esto lo hago para que cuando se pone el movil el horizontal muestre el detalle del primer elemento
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("firstRestaurant",array.getString(i));
                        editor.commit();
                    }
                    restaurantes.add(array.getString(i));
                }
            }
            //control de excepciones
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //creamos y asignamos el adapter entre la lista de la interfaz y la lista con los datos.
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, restaurantes));
    }

    //Obliga a que las actividades que contienen este fragment implementen la interzaz asociada
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ListenerListaRestaurante) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //por cada click en un elemento de la lista, se llama a este metodo
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            //llama al metodo de la activity.

            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onListItemClick(restaurantes.get(position));
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface ListenerListaRestaurante {
        public void onListItemClick(String id);
    }

}
