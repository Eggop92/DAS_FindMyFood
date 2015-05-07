package das.findmyfood.conexiones;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import das.findmyfood.estructuras_de_datos.C;
import das.findmyfood.excepciones.FuncionDesconocidaException;

public class AccesoExternoUsuario extends AsyncTask<String, Void, JSONObject> {

    private Context contexto;

    public AccesoExternoUsuario(Context context){
        contexto = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        JSONObject jsonObject = null;
        try {
            //creamos la peticion http y pone
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            HttpConnectionParams.setSoTimeout(httpParameters, 15000);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            //comprobamos que las funciones a enviar estan definidas y incluimos los parametros necesarios
            String funcion = params[0];
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair(C.FUNCION, funcion));
            String url = C.DIRECCION_USUARIO;
            Log.i("AccesoExternoRestaurantes", "url: " + url);
            if(funcion.equals(C.IDENTIFICACION)){
                //String idGCM =
                //hacemos el registro en el GCM pero no recogemos el resultado ahora
                registrarGCM();
                parametros.add(new BasicNameValuePair(C.EMAIL, params[1]));
                parametros.add(new BasicNameValuePair(C.PASSWORD, cifrarClave(params[2])));
                //parametros.add(new BasicNameValuePair(C.GCMID, idGCM));
            }else if(funcion.equals(C.REGISTRO)){
                parametros.add(new BasicNameValuePair(C.EMAIL, params[1]));
                parametros.add(new BasicNameValuePair(C.PASSWORD, cifrarClave(params[2])));
            }else if(funcion.equals(C.BAJA)){
                parametros.add(new BasicNameValuePair(C.EMAIL, params[1]));
            }else if(funcion.equals(C.ENVIAR_GMC)){
                parametros.add(new BasicNameValuePair(C.GCMID, params[1]));
                parametros.add(new BasicNameValuePair(C.EMAIL, params[2]));
            }else{
                throw new FuncionDesconocidaException();
            }
            //ejecutamos la paticion
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(parametros));
            HttpResponse response = httpclient.execute(httppost);
            //Obtenemos el resultado y lo convertimos en JSON
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            Log.i("AccesoExternoRestaurantes", "result: "+result);
            jsonObject = new JSONObject(result);
            //tratamos las excepciones
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AccesoExternoRestaurantes", "IOException Lanzada");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("AccesoExternoRestaurantes", "JSONException Lanzada");
        } catch (FuncionDesconocidaException e) {
            e.printStackTrace();
            Log.e("AccesoExternoRestaurantes", "FuncionDesconocidaException Lanzada");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("AccesoExternoRestaurantes", "NoSuchAlgorithm Lanzada");
        }

        return jsonObject;

    }

    private void registrarGCM() {
        //enviamos el registro al GCM y en lugar de recogerlo ahora, esperamos a que llege el
        //BroadcastReceive (GCMBroadcastReceiver) y es ahi dnd lo guardamos y lo enviamos al
        //servidor para que lo actualice.
        //esto lo hacemos para tener el id independientemente de que el register nos de
        //un SERVICE_NOT_AVIABLE
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(contexto);
            gcm.register(C.SENDERID);
            //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
            //pref.edit().putString(C.GCMID, id).commit();
            //return id;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return "";
    }

    /**
     * algoritmo de cifrado de claves
     * @param pass
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String cifrarClave(String pass) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(pass.getBytes());
        int size = b.length;
        StringBuffer h = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            int u = b[i];
            if (u<16){
                h.append("0"+Integer.toHexString(u));
            }else {
                h.append(Integer.toHexString(u));
            }
        }
        return h.toString();
    }
}
