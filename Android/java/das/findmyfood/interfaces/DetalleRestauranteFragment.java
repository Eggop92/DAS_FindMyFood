package das.findmyfood.interfaces;



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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import das.findmyfood.R;
import das.findmyfood.conexiones.AccesoExternoRestaurantes;
import das.findmyfood.conexiones.BaseDeDatosLocal;
import das.findmyfood.estructuras_de_datos.C;
import das.findmyfood.estructuras_de_datos.Restaurante;

public class DetalleRestauranteFragment extends Fragment {

    private Restaurante restaurante;
    private String rest = "";

    public DetalleRestauranteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalle_restaurante, container, false);
    }

    @Override
    public void onResume() {
        if (rest.equals("")){
            // esto lo hago para que cuando se pone el movil el horizontal muestre el detalle del primer elemento
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            rest = pref.getString("firstRestaurant","");
        }
        actualizar(rest);
        super.onResume();
    }

    public void actualizar(String nombreRestaurante){
        //Buscamos en la base de datos local si el restaurante existe
        //en cuyo caso el restaurante es uno favorito.
        rest = nombreRestaurante;
        boolean fav = true;
        BaseDeDatosLocal bd = new BaseDeDatosLocal(getActivity());
        restaurante = bd.obtenerRestaurante(nombreRestaurante);
        if(restaurante == null){
            //Si no esta en la base de datos local, se busca en el servidor
            //y se marca que no es favorito
            fav = false;
            try {
                //se llama al asyncTask con el nombre del restaurante, devuelve el JSON con la informacion
                AccesoExternoRestaurantes aer = new AccesoExternoRestaurantes(getActivity());
                aer.execute(C.Info, nombreRestaurante);
                JSONObject json = aer.get();
                //Se comprueba que no ha dado errores.
                if (json.has(C.ERROR)){
                    Log.e("DetalleRestauranteFragment", "somethin gone bad: "+json.getString(C.ERROR));
                }else{
                    //En caso de que no haya dado errores se carga la información en una estructura de datos
                    JSONObject info = json.getJSONObject(C.Info);
                    int idRestaurante = info.getInt("idRestaurante");
                    String direccion = info.getString("Direccion");
                    double latitud = info.getDouble("Latitud");
                    double longitud = info.getDouble("longitud");
                    String descripcion = info.getString("Descripcion");
                    String descripcionIN = info.getString("DescripcionIN");
                    restaurante = new Restaurante(idRestaurante, nombreRestaurante, direccion, latitud, longitud, descripcion, descripcionIN);
                }
                //Tratamiento de excepciones
            } catch (InterruptedException e) {
                Log.e("DetalleRestauranteFragment", "Lanzada excepcion Interrupted");
                e.printStackTrace();
            } catch (ExecutionException e) {
                Log.e("DetalleRestauranteFragment", "Lanzada excepcion Execution");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("DetalleRestauranteFragment", "Lanzada excepcion JSON");
                e.printStackTrace();
            }
        }
        //Se carga en la interfaz los datos y los listener del switch y el boton.
        TextView nombre = (TextView) getActivity().findViewById(R.id.textNombre);
        nombre.setText(nombreRestaurante);
        TextView direccion = (TextView) getActivity().findViewById(R.id.textDireccion);
        direccion.setText(restaurante.getDireccion());
        TextView descripcion = (TextView) getActivity().findViewById(R.id.textDescripcion);
        descripcion.setText(restaurante.getDescripcion(getActivity()));
        Switch esFav = (Switch) getActivity().findViewById(R.id.switchFav);
        esFav.setChecked(fav);
        esFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //listener de switch que indica si el restaurante es favorito o no.
                Log.i("DetalleRestauranteFragment", "onCheckedChanged");
                BaseDeDatosLocal bd = new BaseDeDatosLocal(getActivity());
                AccesoExternoRestaurantes aer = new AccesoExternoRestaurantes(getActivity());
                SharedPreferences lasPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String usuario = lasPref.getString(C.USUARIO, null);
                //usuario = "egoitz.puerta";
                //Si el switch esta marcado, indica que se ha marcado como favorito y se introduce en la base
                //de datos local y se manda al servidor el cambio.
                if(isChecked){
                    bd.anadirRestaurante(restaurante);
                    aer.execute(C.ANADIR_FAV, restaurante.getIdRestaurante()+"", usuario);
                    //JSONObject json = aer.get();
                }else{
                    //en caso contrario se notifica al servidor del cambio y se elimina de la base de datos local.
                    bd.eliminarRestaurante(restaurante);
                    aer.execute(C.ELIMINAR_FAV, restaurante.getIdRestaurante()+"", usuario);
                }
            }
        });
        Button verOfertas = (Button) getActivity().findViewById(R.id.buttonOfertas);
        verOfertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llama a la lista de ofertas que se vera filtrada por este restaurante, dejando
                //visible solo las que pertenezcan a este.
                Log.i("DetalleRestauranteFragment", "onClick");
                Intent i = new Intent(getActivity().getApplicationContext(), ListaOfertasActivity.class);
                i.putExtra(C.OPCION_SELECCIONADA, restaurante.getIdRestaurante()+"");
                startActivity(i);

            }
        });
        GoogleMap mapa;
        //si esta el configuro el mapa
        mapa = ((SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.elmapa)).getMap();
        //borro los markers antiguos
        mapa.clear();
        //Añado la posicion del restaurante
        mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(restaurante.getLatitud(), restaurante.getLongitud()))
                        .title(restaurante.getNombreRestaurante())
                                //poner color al market del mapa
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );

        //centrar la camara en la pocision del restaurante
        LatLng mapCenter = new LatLng(restaurante.getLatitud(), restaurante.getLongitud());
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 16));

    }


}
