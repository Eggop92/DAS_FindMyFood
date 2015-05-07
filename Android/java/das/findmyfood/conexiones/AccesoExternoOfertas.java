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


public class AccesoExternoOfertas extends AsyncTask<String, Void, JSONObject> {
    private String funcion;
    private Context contexto;
    private ProgressDialog barProgressDialog;

    public AccesoExternoOfertas(Context context){
        contexto = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        //Se muestra el dialogo de espera
        barProgressDialog = new ProgressDialog(contexto);
        barProgressDialog.setTitle("Descargando Datos");
        barProgressDialog.setMessage("Se estan descargando los datos necesarios, espere unos segundos");
        barProgressDialog.show();
    }

    protected JSONObject doInBackground(String... params) {
        JSONObject jsonObject = null;
        try {
            //Creamos la peticion HTTP a enviar al servicio web con sus parametros
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            HttpConnectionParams.setSoTimeout(httpParameters, 150000);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            /*Cargamos los parametros de la peticion POST para que el servicio web
            * sepa que tiene que hacer y con que datos*/
            funcion = params[0];
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair(C.FUNCION, funcion));
            String url = C.DIRECCION_OFERTAS;
            Log.i("AccesoExternoOfertas", "url: " + url);
            /*Filtra las funciones y añade los parametro necesarios*/
            if(funcion.equals(C.listarOfertas)){
            }else if(funcion.equals(C.listarOfertasRestaurante)){
                parametros.add(new BasicNameValuePair(C.ID_RESTAURANTE, params[1]));
            }else if(funcion.equals(C.obtenerOferta)){
                parametros.add(new BasicNameValuePair(C.ID_OFERTA, params[1]));
            }else{
                throw new FuncionDesconocidaException();
            }
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(parametros));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            Log.i("AccesoExternoOfertas", "result: "+result);
            jsonObject = new JSONObject(result);


        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AccesoExternoOfertas", "IOException Lanzada");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("AccesoExternoOfertas", "JSONException Lanzada");
        } catch (FuncionDesconocidaException e) {
            e.printStackTrace();
            Log.e("AccesoExternoOfertas", "FuncionDesconocidaException Lanzada");
        }
        return jsonObject;
}

    protected void onPostExecute(JSONObject rdo) {
        super.onPostExecute(rdo);
        barProgressDialog.dismiss();
    }



}

