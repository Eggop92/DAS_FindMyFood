package das.findmyfood.NotifPush;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import das.findmyfood.R;
import das.findmyfood.estructuras_de_datos.C;
import das.findmyfood.interfaces.ListaOfertasActivity;

public class GCMService extends IntentService {

    public GCMService() {
        super("GCMService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();
        Log.i("GCMService", "Se Recive el OnHandleIntent");
        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            Log.i("GCMService", "Es de tipo message");
            if (!extras.isEmpty()) {
                Log.i("GCMService", "Los extras no estan vacion");
                String restaurante = intent.getExtras().getString("NombreRestaurante");
                String idRestaurante = intent.getExtras().getString("idRestaurante");
                String tituloOferta = intent.getExtras().getString("tituloOferta");
                if (!restaurante.isEmpty() && !idRestaurante.isEmpty() && !tituloOferta.isEmpty()) {
                    Log.i("GCMService", "Los extras tienen la info que necesitamos");
                    Intent i = new Intent(this, ListaOfertasActivity.class);
                    //Intent i = new Intent(this, MainMenu.class);
                    i.putExtra(C.OPCION_SELECCIONADA, idRestaurante);
                    PendingIntent intentParaLaNotificacion = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                    mBuilder.setSmallIcon(R.drawable.ic_launcher)
                            .setAutoCancel(true)
                            .setLargeIcon((((BitmapDrawable)getResources().getDrawable(R.drawable.ic_launcher)).getBitmap()))
                            .setContentTitle(tituloOferta)
                            .setContentText(getApplicationContext().getString(R.string.En_Restaurante) + restaurante)
                            .setTicker(getApplicationContext().getString(R.string.Nueva_Oferta))
                            .setContentIntent(intentParaLaNotificacion);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }
            }
        }
    }


}
