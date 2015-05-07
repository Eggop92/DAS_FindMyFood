package das.findmyfood.NotifPush;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import das.findmyfood.conexiones.AccesoExternoUsuario;
import das.findmyfood.estructuras_de_datos.C;


public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //este Receiver se ejecuta cada vez que el GCM lanza o una notificacion o un registro
        Log.i("GCMBroadcastReceier", "Action="+intent.getAction());
        if(intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")){
            //Si es un registro, se recupera el id y se envia al servidor para que este lo tenga
            Log.i("GCMBroadcastReceiver", "action=intent.REGISTRATION");
            String regId = intent.getExtras().getString("registration_id");
            if(regId != null && !regId.equals("")) {
                Log.e("GCMBroadcastReceiver", "GCMID: "+regId);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                new AccesoExternoUsuario(context).execute(C.ENVIAR_GMC, regId, pref.getString(C.USUARIO, ""));
                pref.edit().putString(C.GCMID, regId).commit();
            }

        }else if(intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")){
            //si es una notificacion, se llama al servicio que lanza la notificacion.
            Log.i("GCMBroadcastReceiver", "action=intent.RECEIVE");
            ComponentName comp = new ComponentName(context.getPackageName(), GCMService.class.getName());
            startWakefulService(context, intent.setComponent(comp));
            setResultCode(Activity.RESULT_OK);
        }



    }
}
