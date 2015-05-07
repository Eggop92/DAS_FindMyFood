<?php 
//require_once __DIR__ . "conexion.php";// esto ejecuta el conexion.php
include 'conexion.php';//esto tambien ejecuta el conexion.php

//Se usara para realizar las funciones relacionadas con las ofertas

    function listarOfertas( ){
        $conexion=conectar();
        
        $sql="SELECT IdOferta, Titulo FROM Oferta;";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error Oferta (1) mysql: ".mysqli_error($conexion));
        }else{
           $response = array();
           $response['listarOfertas'] = array();
            while($fila = mysqli_fetch_row($resultado)){
                $listaofertas = array();
                $listaofertas['idOferta']=utf8_encode($fila[0]);
                $listaofertas['Oferta']=utf8_encode($fila[1]);
                array_push($response['listarOfertas'],$listaofertas);
            }
        	echo json_encode($response);
        }
        cerrarConexion($conexion);
    }
    
    function listarOfertasRestaurante($idRestaurante){
        $conexion=conectar();

        $sql="SELECT `IdOferta`, `Titulo` FROM `Oferta` WHERE `IdResOferta`='".$idRestaurante."';";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error Oferta (2) mysql: ".mysqli_error($conexion));
        }else{
           $response = array();
           $response['listarOfertas'] = array();
            while($fila = mysqli_fetch_row($resultado)){
                $listaofertas = array();
                $listaofertas['idOferta']=utf8_encode($fila[0]);
                $listaofertas['Oferta']=utf8_encode($fila[1]);
                array_push($response['listarOfertas'],$listaofertas);
            }
        
        }
        echo json_encode($response);
        
        cerrarConexion($conexion);
    }

    function obtenerOferta($idOferta){
        $conexion=conectar();
  
        $sql="SELECT IdOferta, Titulo, Oferta.Descripcion, TituloIN, Oferta.DescripcionIN, NombreRestaurante FROM Oferta INNER JOIN Restaurante ON IdResOferta=idRestaurante WHERE IdOferta='".$idOferta."';";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error Oferta (2) mysql: ".mysqli_error($conexion));
        }else{
            $listaoferta = array();
            if($fila = mysqli_fetch_row($resultado)){
                $listaoferta['idOferta'] = utf8_encode($fila[0]);
                $listaoferta['TituloOferta'] = utf8_encode($fila[1]);
                $listaoferta['DescripcionOferta'] = utf8_encode($fila[2]);
                $listaoferta['TituloOfertaIN'] = utf8_encode($fila[3]);
                $listaoferta['DescripcionOfertaIN'] = utf8_encode($fila[4]);
                $listaoferta['NombreResOferta'] = utf8_encode($fila[5]);
            }
            $json = array('obtenerOferta' => $listaoferta);
            echo json_encode($json);
        }
        
        cerrarConexion($conexion);
    }


    //comprobamos si el parametro existe para emitir un error en caso de que no sea así
    if (isset($_POST['FUNCION']) ) {
        $funcion = $_POST['FUNCION'];

        //separamos por funciones
        switch ($funcion) {
            case 'listarOfertas':
                listarOfertas();
                break;
            case 'listarOfertasRestaurante':
                if (isset($_POST['ID_RESTAURANTE'])){
                    listarOfertasRestaurante($_POST['ID_RESTAURANTE']);
                }else{
                    imprimirError("No se ha definido el parametro necesario para la funcion.");
                }
                break;
            case 'obtenerOferta':
                if (isset($_POST['ID_OFERTA'])){
                    obtenerOferta($_POST['ID_OFERTA']);
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