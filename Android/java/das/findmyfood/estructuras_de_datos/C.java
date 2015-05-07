package das.findmyfood.estructuras_de_datos;


public class C {

    public static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    //direcciones
    public static final String IP = ""; //IP SERVIDOR PHP
    public static final String DIRECCION_RESTAURANTE = "http://"+IP+"/DAS/FindMyFood/restaurante.php";
    public static final String DIRECCION_USUARIO = "http://"+IP+"/DAS/FindMyFood/usuario.php";


    //restaurtante
    public static final String Lista = "Lista";
    public static final String Info = "Info";
    public static final String NOMBRE = "NOMBRE";
    public static final String ANADIR_FAV = "AnadirFav";
    public static final String ELIMINAR_FAV = "EliminarFav";
    public static final String DESCARGA_FAV = "DescargaFavoritos";


    //resultados JSON
    public static final String ERROR = "ERROR";
    public static final String FALLO_USUARIO = "FALLOUSUARIO";
    public static final String FALLO = "FALLO";


    //Generales
    public static final String USUARIO = "USUARIO";
    public static final String ID = "ID";
    public static final String FUNCION = "FUNCION";
    public static final String OPCION_SELECCIONADA = "opcionSeleccionada";


    //USUARIOS
    public static final String IDENTIFICACION = "Identificacion";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String REGISTRO = "Registro";
    public static final String BAJA = "Baja";
    public static final String GCMID = "GCMID";
    public static final String SENDERID = "544446882832";
    public static final String ENVIAR_GMC = "EnviarGMC";


    //constantes usadas para acceso externo a ofertas
    public static final String DIRECCION_OFERTAS = "http://"+IP+"/DAS/FindMyFood/oferta.php";
    public static final String listarOfertas = "listarOfertas";
    public static final String listarOfertasRestaurante = "listarOfertasRestaurante";
    public static final String ID_RESTAURANTE = "ID_RESTAURANTE";
    public static final String obtenerOferta = "obtenerOferta";
    public static final String ID_OFERTA= "ID_OFERTA";


    //contantes para RESTAURANTES
    public static final String RESTAURANTES = "Restaurantes";
    public static final String RESTAURANTE_ID = "idRestaurante";
    public static final String RESTAURANTE_NOMBRE = "NombreRestaurante";
    public static final String RESTAURANTE_DIRECCION = "Direccion";
    public static final String RESTAURANTE_LATITUD = "Latitud";
    public static final String RESTAURANTE_LONGITUD = "Longitud";
    public static final String RESTAURANTE_DESCRIPCION = "Descripcion";
    public static final String RESTAURANTE_DESCRIPCIONIN = "DescripcionIN";
}
