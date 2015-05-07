<?php 
//require_once __DIR__ . "conexion.php";// esto ejecuta el conexion.php
include 'conexion.php';//esto tambien ejecuta el conexion.php
//Se usara para realizar las funciones relacionadas con los reastaurantes

    function listarRestaurantes( ){
        $conexion=conectar();
        
        $sql="SELECT NombreRestaurante FROM Restaurante;";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error (1) mysql: ".mysqli_error($conexion));
        }else{
            $response = array();
            $response['Restaurantes'] = array();
            while($fila = mysqli_fetch_row($resultado)){
                //$array = array();
                //$array['NombreRestaurante'] = $fila[0];
                array_push($response['Restaurantes'], utf8_encode($fila[0]));
            }
            echo json_encode($response);
        }
        
        cerrarConexion($conexion);
    }

    function obtenerRestaurante($NombreRestaurante){
        $conexion=conectar();
        
        $sql="SELECT idRestaurante, Direccion, Latitud, Longitud, Descripcion, DescripcionIN FROM Restaurante WHERE NombreRestaurante='".$NombreRestaurante."';";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error (2) mysql: ".mysqli_error($conexion));
        }else{
            $response = array();
            if($fila = mysqli_fetch_row($resultado)){
                $response['idRestaurante']=utf8_encode($fila[0]);
                $response['Direccion']=utf8_encode($fila[1]);
                $response['Latitud']=utf8_encode($fila[2]);
                $response['longitud']=utf8_encode($fila[3]);
                $response['Descripcion']=utf8_encode($fila[4]);
                $response['DescripcionIN']=utf8_encode($fila[5]);
            }
            $json = array('Info' => $response );
            echo json_encode($json);
        }        
        cerrarConexion($conexion);
    }

    function AnadirFav($idRestaurante, $usuario){
        $conexion=conectar();
        
        $sql="INSERT INTO favoritos(NombreUsuario, idRestaurante) VALUES ('".$usuario."', '".$idRestaurante."');";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error (3) mysql: ".mysqli_error($conexion));
        }else{
            $response = array();
            // required field is missing
            $response["EXITO"] = "Sin fallos";
            // echoing JSON response
            echo json_encode($response);
        }
        cerrarConexion($conexion);
    }

    function EliminarFav($idRestaurante, $usuario){
        $conexion=conectar();
        
        $sql="DELETE FROM favoritos WHERE NombreUsuario='".$usuario."' AND idRestaurante='".$idRestaurante."';";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error (4) mysql: ".mysqli_error($conexion));
        }else{
            $response = array();
            // required field is missing
            $response["EXITO"] = "Sin fallos";
            // echoing JSON response
            echo json_encode($response);
        }
        cerrarConexion($conexion);
    }

    function DescargaFavoritos($usuario){
        $conexion=conectar();
        
        $sql="SELECT idRestaurante, NombreRestaurante, Direccion, Latitud, Longitud, Descripcion, DescripcionIN FROM Restaurante NATURAL JOIN favoritos WHERE NombreUsuario='".$usuario."';";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error (5) mysql: ".mysqli_error($conexion));
        }else{
            $response = array();
            $response['Restaurantes'] = array();
            while($fila = mysqli_fetch_row($resultado)){
                $array = array();
                $array['idRestaurante'] = utf8_encode($fila[0]);
                $array['NombreRestaurante'] = utf8_encode($fila[1]);
                $array['Direccion'] = utf8_encode($fila[2]);
                $array['Latitud'] = utf8_encode($fila[3]);
                $array['Longitud'] = utf8_encode($fila[4]);
                $array['Descripcion'] = utf8_encode($fila[5]);
                $array['DescripcionIN'] = utf8_encode($fila[6]);
                array_push($response['Restaurantes'], $array);
            }
            echo json_encode($response);
        }
        cerrarConexion($conexion);
    }


    //comprobamos si el parametro existe para emitir un error en caso de que no sea así
    if (isset($_POST['FUNCION']) ) {
        $funcion = $_POST['FUNCION'];

        //separamos por funciones
        switch ($funcion) {
            case 'Lista':
                listarRestaurantes();
                break;
            case 'Info':
                if (isset($_POST['NOMBRE'])){
                    obtenerRestaurante($_POST['NOMBRE']);
                }else{
                    imprimirError("No se ha definido el parametro necesario para la funcion.");
                }
                break;
            case 'AnadirFav':
                if (isset($_POST['ID']) && isset($_POST['USUARIO'])){
                    AnadirFav($_POST['ID'], $_POST['USUARIO']);
                }else{
                    imprimirError("No se ha definido el parametro necesario para la funcion.");
                }
                break;
            case 'EliminarFav':
                if (isset($_POST['ID']) && isset($_POST['USUARIO'])){
                    EliminarFav($_POST['ID'], $_POST['USUARIO']);
                }else{
                    imprimirError("No se ha definido el parametro necesario para la funcion.");
                }
                break;
            case 'DescargaFavoritos':
                if (isset($_POST['USUARIO'])){
                    DescargaFavoritos($_POST['USUARIO']);
                }else{
                    imprimirError("No se ha definido el parametro necesario para la funcion.");
                }
                break;
            default:
                imprimirError("No se ha definido la funcion especificada");
                break;
        }
        
    }else{
        imprimirError("No se ha definido la variable FUNCION");
    }    

?>