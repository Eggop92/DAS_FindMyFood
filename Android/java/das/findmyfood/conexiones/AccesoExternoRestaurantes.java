package das.findmyfood.conexiones;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import das.findmyfood.estructuras_de_datos.C;
import das.findmyfood.excepciones.FuncionDesconocidaException;

/**
 * Clase AsyncTask relacionada con los Restaurantes. Recibe un contexto para poder mostrar
 * mensajes de espera en la descarga.
 */
public class AccesoExternoRestaurantes extends AsyncTask<String, Void, JSONObject> {

    private String funcion;
    private Context contexto;
    private ProgressDialog barProgressDialog;

    public AccesoExternoRestaurantes(Context context){
        contexto = context;
    }

    protected void onPreExecute() {
        //muestra el dialogo de espera.
        super.onPreExecute();
        barProgressDialog = new ProgressDialog(contexto);
        barProgressDialog.setTitle("Descargando Datos");
        barProgressDialog.setMessage("Se estan descargando los datos necesarios, espere unos segundos");
        barProgressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject jsonObject = null;
        try {
            //Creamos la peticion HTTP a enviar al servicio web con sus parametros
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            HttpConnectionParams.setSoTimeout(httpParameters, 15000);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            //cargamos los parametros de la peticion POST para que el servicio web
            //sepa que tiene que hacer y con que datos.
            funcion = params[0];
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair(C.FUNCION, funcion));
            String url = C.DIRECCION_RESTAURANTE;
            Log.i("AccesoExternoRestaurantes", "url: " + url);
            //Filtra las funciones por si acaso alguna no es reconocida
            //Y añade los parametros necesarios
            if(funcion.equals(C.Lista)){
                //no necesita parametros extra
            }else if(funcion.equals(C.Info)){
                parametros.add(new BasicNameValuePair(C.NOMBRE, params[1]));
            }else if(funcion.equals(C.ANADIR_FAV)){
                parametros.add(new BasicNameValuePair(C.ID, params[1]));
                parametros.add(new BasicNameValuePair(C.USUARIO, params[2]));
            }else if(funcion.equals(C.ELIMINAR_FAV)){
                parametros.add(new BasicNameValuePair(C.ID, params[1]));
                parametros.add(new BasicNameValuePair(C.USUARIO, params[2]));
            }else if(funcion.equals(C.DESCARGA_FAV)){
                parametros.add(new BasicNameValuePair(C.USUARIO, params[1]));
            }else{
                throw new FuncionDesconocidaException();
            }
            //Enviamos y ejecutamos la peticion post
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(parametros));
            HttpResponse response = httpclient.execute(httppost);
            //recibimos el JSON resultante de la ejecucion y lo transformamos para que podamos
            //manejarlo
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            Log.i("AccesoExternoRestaurantes", "result: "+result);
            jsonObject = new JSONObject(result);
        //Control de excepciones
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AccesoExternoRestaurantes", "IOException Lanzada");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("AccesoExternoRestaurantes", "JSONException Lanzada");
        } catch (FuncionDesconocidaException e) {
            e.printStackTrace();
            Log.e("AccesoExternoRestaurantes", "FuncionDesconocidaException Lanzada");
        }

        return jsonObject;
    }

    protected void onPostExecute(JSONObject rdo) {
        //oculta el dialogo de espera.
		super.onPostExecute(rdo);
        barProgressDialog.dismiss();
    }
}
