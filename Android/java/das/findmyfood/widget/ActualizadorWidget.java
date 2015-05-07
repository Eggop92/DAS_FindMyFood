package das.findmyfood.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ActualizadorWidget extends Service {

    private int[] widgetids;

    public ActualizadorWidget() {
    }

    public int onStartCommand(Intent i, int flags, int startID){
        widgetids = i.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        Toast.makeText(getApplicationContext(), "El servicio esta en ejecucion", Toast.LENGTH_SHORT).show();
        Timer mTimer = new Timer();
        mTimer.scheduleAtFixedRate(
                new TimerTask(){
                    @Override
                    public void run() {
                        Thread t = new Thread(new Runnable() {
                            public void run() {
                                //Qué se tiene que ejecutar
                                ejecutar();
                            }
                        });
                        t.start();
                    }
                }
                , 0, 1000 * 60 * 3);//Cada 1 min
        return flags;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void ejecutar(){
        //Toast.makeText(getApplicationContext(), "Actualizacion de widget en curso", Toast.LENGTH_SHORT).show();
        Log.i("ActualizadorWidget", "Actualizacion de widget en curso");
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
        Widget.updateAppWidget(getApplicationContext(),widgetManager , widgetids);
    }
}
