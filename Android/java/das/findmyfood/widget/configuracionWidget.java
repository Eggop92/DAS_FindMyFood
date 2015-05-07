package das.findmyfood.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import das.findmyfood.R;

public class configuracionWidget extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_widget);
        setResult(RESULT_OK);
        Intent lanzador = getIntent();
        Bundle info = lanzador.getExtras();
        int id = info.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if(id != AppWidgetManager.INVALID_APPWIDGET_ID){
            Intent resultado = new Intent();
            resultado.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            setResult(RESULT_OK, resultado);
        }
        finish();
    }


}
