package das.findmyfood.conexiones;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import das.findmyfood.estructuras_de_datos.Restaurante;

/**
 * Clase que comunica con la base de datos local, en el propio telefono. Esta base de datos
 * contendra los restaurantes que el usuario haya marcado como favorito
 */
public class BaseDeDatosLocal extends SQLiteOpenHelper{

    SQLiteDatabase bd;

    //constructora
    public BaseDeDatosLocal(Context context){
        super(context, "FindMyFood", null, 1);
        bd = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Restaurantes ('idRestaurante' INTEGER, 'NombreRestaurante' TEXT, 'Direccion' TEXT, 'Latitud' TEXT, 'Longitud' TEXT, 'Descripcion' TEXT, 'DescripcionIN' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //obtiene todos los datos del restaurante, si el restaurante existe, sino devuelve null.
    public Restaurante obtenerRestaurante(String nombreRestaurante){
        Restaurante rdo = null;
        Cursor c = bd.rawQuery("SELECT idRestaurante, Direccion, Latitud, Longitud, Descripcion, DescripcionIN FROM Restaurantes WHERE NombreRestaurante='"+nombreRestaurante+"'", null);
        if(c.moveToFirst()){
            int idRestaurante = c.getInt(c.getColumnIndex("idRestaurante"));
            String direccion = c.getString(c.getColumnIndex("Direccion"));
            double latitud = c.getDouble(c.getColumnIndex("Latitud"));
            double longitud = c.getDouble(c.getColumnIndex("Longitud"));
            String descripcion = c.getString(c.getColumnIndex("Descripcion"));
            String descripcionIN = c.getString(c.getColumnIndex("DescripcionIN"));
            rdo = new Restaurante(idRestaurante, nombreRestaurante, direccion, latitud, longitud, descripcion, descripcionIN);
        }
        c.close();
        return rdo;
    }

    //Introduce en la base de datos un nuevo restaurante
    public void anadirRestaurante(Restaurante restaurante) {
        String sql = "INSERT INTO Restaurantes( 'idRestaurante', 'NombreRestaurante', 'Direccion', 'Latitud', 'Longitud', 'Descripcion', 'DescripcionIN') VALUES ('"+restaurante.getIdRestaurante()+"', '"+restaurante.getNombreRestaurante()+"', '"+restaurante.getDireccion()+"', '"+restaurante.getLatitud()+"', '"+restaurante.getLongitud()+"', '"+restaurante.getDescripcionES()+"', '"+restaurante.getDescripcionEN()+"')";
        try{
            bd.execSQL(sql);
        }catch(SQLException e){
            Log.e("BaseDeDatosLocal", "anadirRestaurante: " + sql);
            e.printStackTrace();
        }
    }

    //elimina de la base de datos el restaurante
    public void eliminarRestaurante(Restaurante restaurante) {
        String sql = "DELETE FROM Restaurantes WHERE idRestaurante='"+restaurante.getIdRestaurante()+"'";
        try{
            bd.execSQL(sql);
        }catch(SQLException e){
            Log.e("BaseDeDatosLocal", "eliminarRestaurante: " + sql);
            e.printStackTrace();
        }
    }

    public Restaurante getRestauranteCercano(Location latLon) {
        Restaurante rdo = null;
        float distB=Float.MAX_VALUE;
        float[] result = new float[1];
        Cursor c = bd.rawQuery("SELECT idRestaurante, NombreRestaurante, Direccion, Latitud, Longitud, Descripcion, DescripcionIN FROM Restaurantes", null);
        c.moveToPosition(-1);
        while(c.moveToNext()){
            double latitud = c.getDouble(c.getColumnIndex("Latitud"));
            double longitud = c.getDouble(c.getColumnIndex("Longitud"));
            Location.distanceBetween(latitud, longitud, latLon.getLatitude(), latLon.getLongitude(), result);
            if(result[0] < distB){
                distB = result[0];
                int idRestaurante = c.getInt(c.getColumnIndex("idRestaurante"));
                String nombreRestaurante = c.getString(c.getColumnIndex("NombreRestaurante"));
                String direccion = c.getString(c.getColumnIndex("Direccion"));
                String descripcion = c.getString(c.getColumnIndex("Descripcion"));
                String descripcionIN = c.getString(c.getColumnIndex("DescripcionIN"));
                rdo = new Restaurante(idRestaurante, nombreRestaurante, direccion, latitud, longitud, descripcion, descripcionIN);
            }
        }
        c.close();
        return rdo;
    }

    public void clear() {
        String sql = "DELETE FROM Restaurantes";
        try{
            bd.execSQL(sql);
        }catch(SQLException e){
            Log.e("BaseDeDatosLocal", "clear: " + sql);
            e.printStackTrace();
        }
    }
}
