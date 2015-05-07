package das.findmyfood.estructuras_de_datos;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Estructura de datos que guarda la informacion relativa a un restaurante
 */
public class Restaurante {

    private int idRestaurante;
    private String nombreRestaurante;
    private String direccion;
    private double latitud;
    private double longitud;
    private String descripcion;
    private String descripcionIN;

    public Restaurante (int pIdRestaurante, String pNombreRestaurante, String pDireccion, double pLatitud, double pLongitud, String pDescripcion, String pDescripcionIN){
        idRestaurante = pIdRestaurante;
        nombreRestaurante = pNombreRestaurante;
        direccion = pDireccion;
        latitud = pLatitud;
        longitud = pLongitud;
        descripcion = pDescripcion;
        descripcionIN = pDescripcionIN;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getDescripcion(Context contexto) {
        SharedPreferences lasPref = PreferenceManager.getDefaultSharedPreferences(contexto);
        String idioma = lasPref.getString("Idioma", "ES");
        if(idioma.equals("ES")){
            return descripcion;
        }else{
            return descripcionIN;
        }
    }

    public String getDescripcionEN(){
        return descripcionIN;
    }

    public String getDescripcionES(){
        return descripcion;
    }

}
