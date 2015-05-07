package das.findmyfood.interfaces;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import das.findmyfood.R;
import das.findmyfood.conexiones.AccesoExternoOfertas;
import das.findmyfood.estructuras_de_datos.C;

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ListaOfertaFragment extends ListFragment {

    private ListenerListaOfertas mListener;
    private ArrayList<String> listaIDOfertas;
    private ArrayList<String> listaOfertasParaAdap;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListaOfertaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listaIDOfertas=new ArrayList<String>();
        listaOfertasParaAdap=new ArrayList<String>();
        String rest, opc;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        rest = pref.getString(C.OPCION_SELECCIONADA,"");

        try {
            //buscamos las ofertas en el servidor y las guardamos en el array
            AccesoExternoOfertas ofertas =new AccesoExternoOfertas(getActivity());

            if(rest.equals("")){
                ofertas.execute(C.listarOfertas);
            }else{
                ofertas.execute(C.listarOfertasRestaurante, rest);
            }

            JSONObject objetojson = ofertas.get();
            if(objetojson.has(C.ERROR)){
                Log.e("ListaOfertasFragment", "Ha ocurrido un error : "+objetojson.getString(C.ERROR));
            }else{
                //rellenamos la lista de id y la lista para el adaptador
                JSONArray array = objetojson.getJSONArray(C.listarOfertas);
                for(int i = 0; i<array.length(); i++){
                    JSONObject unaOferta = array.getJSONObject(i);
                    listaOfertasParaAdap.add(unaOferta.getString("Oferta"));
                    listaIDOfertas.add(unaOferta.getString("idOferta"));
                    if (i==0){
                        // esto lo hago para que cuando se pone el movil el horizontal muestre el detalle del primer elemento
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("firstOferta", unaOferta.getString("idOferta"));
                        editor.commit();
                    }
                }
                if(listaOfertasParaAdap.isEmpty()){
                    Toast.makeText(getActivity(),"Este restaurante no tiene ofertas.", Toast.LENGTH_SHORT).show();

                    getActivity().finish();

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

        /*Cambio del adaptador para mostrar el contenido,
        le paso la actividad, el objeto de la lista y la lista de strigs*/
        setListAdapter(new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, listaOfertasParaAdap));

        //getListView().setBackgroundColor(getResources().getColor(R.color.red));

    }

/*    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listaIDOfertas=new ArrayList<String>();
        listaOfertasParaAdap=new ArrayList<String>();
        String rest, opc;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        rest = pref.getString(C.OPCION_SELECCIONADA,"");

        try {
            //buscamos las ofertas en el servidor y las guardamos en el array
            AccesoExternoOfertas ofertas =new AccesoExternoOfertas(getActivity());

            if(rest.equals("")){
                ofertas.execute(C.listarOfertas);
            }else{
                ofertas.execute(C.listarOfertasRestaurante, rest);
            }

            JSONObject objetojson = ofertas.get();
            if(objetojson.has(C.ERROR)){
                Log.e("ListaOfertasFragment", "Ha ocurrido un error : "+objetojson.getString(C.ERROR));
            }else{
                //rellenamos la lista de id y la lista para el adaptador
                JSONArray array = objetojson.getJSONArray(C.listarOfertas);
                for(int i = 0; i<array.length(); i++){
                    JSONObject unaOferta = array.getJSONObject(i);
                    listaOfertasParaAdap.add(unaOferta.getString("Oferta"));
                    listaIDOfertas.add(unaOferta.getString("idOferta"));
                    if (i==0){
                        // esto lo hago para que cuando se pone el movil el horizontal muestre el detalle del primer elemento
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("firstOferta", unaOferta.getString("idOferta"));
                        editor.commit();
                    }
                }
                if(listaOfertasParaAdap.isEmpty()){
                    Toast.makeText(getActivity(),"Este restaurante no tiene ofertas.", Toast.LENGTH_SHORT).show();

                    getActivity().finish();

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

        *//*Cambio del adaptador para mostrar el contenido,
        le paso la actividad, el objeto de la lista y la lista de strigs*//*
        setListAdapter(new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, listaOfertasParaAdap));

        return inflater.inflate(R.layout.activity_lista_ofertas, container, false);

    }*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ListenerListaOfertas) activity;
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


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            /*Notificar a la interfaz de devoluciones de llamada activa
            (la actividad, si el fragmento se une a uno) que un artículo ha sido seleccionado.
            le doy el id de la oferta*/
            Log.i("EN DONDE ENVIA EL ID OFERTA",listaIDOfertas.get(position));
            mListener.onListItemClick(listaIDOfertas.get(position));
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
    public interface ListenerListaOfertas {
        public void onListItemClick(String id);
    }

}
