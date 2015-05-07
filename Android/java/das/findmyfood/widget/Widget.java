package das.findmyfood.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.RemoteViews;
import das.findmyfood.R;
import das.findmyfood.conexiones.BaseDeDatosLocal;
import das.findmyfood.estructuras_de_datos.C;
import das.findmyfood.estructuras_de_datos.Restaurante;
import das.findmyfood.interfaces.DetallesRestauranteActivity;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    public void onReceive(Context context, Intent intent){
        Log.i("Widge", "onReceive: "+intent.getAction());
        //comprobamos si las señales son las que nos interesan
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals("das.findmyfood.senales.ACTUALIZAR_WIDGET")){
            //la que nosotros definimos en el lanzamiento del widget
            Log.i("Widge", "dentro del if");
            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if(widgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
                Log.i("Widge", "dentro del if del idWidget");
                //actualizamos el widget
                updateAppWidget(context, widgetManager, widgetId);
            }
        }else if(intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")){
            //la que se define por defecto
            Log.i("WIDGET", "Widget.onReceive.intent.APPWIDGET_UPDATE");
            int[] widgetids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if(widgetids != null){
                Log.i("WIDGET", "Widget.onReceive.ids");
                //onUpdate(context, widgetManager, widgetids);
                Intent i = new Intent(context, ActualizadorWidget.class);
                i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetids);
                context.startService(i);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //obtenemos las vistas del widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        //obtenemos la localización del dispositivo
        LocationManager elManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criterios = new Criteria();
        criterios.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = elManager.getBestProvider(criterios, true);
        Location latLon = elManager.getLastKnownLocation(bestProvider);
        CharSequence widgetText, wd="";
        //comprobamos que haya localizacion
        if(latLon != null){
            //buscamos el restaurante mas cercano
            BaseDeDatosLocal bd = new BaseDeDatosLocal(context);
            Restaurante r = bd.getRestauranteCercano(latLon);
            if(r != null){
                //si se obtiene la informacion se pone esta en el text del widget
                // y se pone un listener a la etiqueta para abrir la activity con el
                //detalle del restaurante
                widgetText= r.getNombreRestaurante();
                wd=r.getDireccion();

                Intent iRestaurante = new Intent(context, DetallesRestauranteActivity.class);
                iRestaurante.putExtra(C.OPCION_SELECCIONADA, widgetText.toString().split("\n")[0]);
                PendingIntent piRestaurante = PendingIntent.getActivity(context, appWidgetId, iRestaurante, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.restaurante, piRestaurante);
            }else{
                widgetText = "No favorite restaurants.";
            }
        }else{
            widgetText = "No position aviable";
        }
        // Ponemos el texto correspondiente en la etiqueta del widget.
        views.setTextViewText(R.id.restaurante, widgetText);
        views.setTextViewText(R.id.direccion, wd);

        //forzar actualizaciones
        //Intent i = new Intent("das.findmyfood.senales.ACTUALIZAR_WIDGET");
        //i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //PendingIntent pi = PendingIntent.getBroadcast(context, appWidgetId, i, PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setOnClickPendingIntent(R.id.layoutWidget, pi);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


