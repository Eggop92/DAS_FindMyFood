<?php 
//require_once __DIR__ . "conexion.php";// esto ejecuta el conexion.php
include 'conexion.php';//esto tambien ejecuta el conexion.php
//Se usara para realizar las funciones relacionadas con los usuarios

	function registrarUsuario( $email, $pass){
    	$conexion=conectar();
        
        $sql="SELECT NombreUsuario FROM Usuario WHERE NombreUsuario='".$email."';";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error (1) mysql: ".mysqli_error($conexion));
        }else{
            if($fila = mysqli_fetch_row($resultado)){
                // required field is missing
                $response["FALLOUSUARIO"] = "Email ya esta registrado.";
                // echoing JSON response
                echo json_encode($response);
            }else{
                $sql="INSERT INTO Usuario(NombreUsuario, contrasena) VALUES ('".$email."', '".$pass."');";
                $resultado= mysqli_query($conexion,$sql);
                if(!$resultado){
                    imprimirError("Error (2) mysql: ".mysqli_error($conexion));
                }else{
                    // required field is missing
                    $response["EXITO"] = "Usuario registrado.";
                    // echoing JSON response
                    echo json_encode($response);
                }
            }
        }
        cerrarConexion($conexion);
    }

    function identificarUsuario( $email, $pass/*, $idgcm*/){
    	$conexion=conectar();
        
        $sql="SELECT NombreUsuario FROM Usuario WHERE NombreUsuario='".$email."' AND Contrasena='".$pass."';";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error (3) mysql: ".mysqli_error($conexion));
        }else{
            if($fila = mysqli_fetch_row($resultado)){
                /*$sql = "UPDATE Usuario SET idGCM='".$idgcm."' WHERE NombreUsuario='".$email."';";
                $resultado= mysqli_query($conexion,$sql);
                if(!$resultado){
                    imprimirError("Error (4) mysql: ".mysqli_error($conexion));
                 }else{*/
                    $response = array();
                    $response["EXITO"] = "Sin fallos";
                    echo json_encode($response);
                //}
            }else{
                $response = array();
                // required field is missing
                $response["FALLO"] = "La pareja email contraseña no coincide.";
                // echoing JSON response
                echo json_encode($response);
            }
            
        }
        cerrarConexion($conexion);
    }

    function EnviarGMC($email, $idGCM){
        $conexion=conectar();
        
        $sql="UPDATE Usuario SET idGCM='".$idGCM."' WHERE NombreUsuario='".$email."';";
        $resultado= mysqli_query($conexion,$sql);
        if(!$resultado){
            imprimirError("Error (4) mysql: ".mysqli_error($conexion));
        }else{
            $response = array();
            $response["EXITO"] = "Sin fallos";
            echo json_encode($response);
        }
        
        cerrarConexion($conexion);
    }

    function bajaUsuario($email){
    	$conexion=conectar();
        
        $sql="";
        $resultado= mysqli_query($conexion,$sql);
        
        echo json_encode($arrayresult);
        cerrarConexion($conexion);
    }

    //comprobamos si el parametro existe para emitir un error en caso de que no sea así
    if (isset($_POST['FUNCION']) ) {
        $funcion = $_POST['FUNCION'];

        //separamos por funciones
        switch ($funcion) {
            case 'Identificacion':
                if (isset($_POST['EMAIL']) && isset($_POST['PASSWORD']) /*&& isset($_POST['GCMID'])*/){
                    identificarUsuario($_POST['EMAIL'], $_POST['PASSWORD']/*, $_POST['GCMID']*/);
                }else{
                    imprimirError("No se ha definido el parametro necesario para la funcion.");
                }
                break;
            case 'Registro':
                if (isset($_POST['EMAIL']) && isset($_POST['PASSWORD'])){
                    registrarUsuario($_POST['EMAIL'], $_POST['PASSWORD']);
                }else{
                    imprimirError("No se ha definido el parametro necesario para la funcion.");
                }
                break;
            case 'Baja':
                if (isset($_POST['EMAIL'])){
                    bajaUsuario($_POST['EMAIL']);
                }else{
                    imprimirError("No se ha definido el parametro necesario para la funcion.");
                }
                break;
            case 'EnviarGMC':
                if (isset($_POST['EMAIL']) && isset($_POST['GCMID'])){
                    EnviarGMC($_POST['EMAIL'], $_POST['GCMID']);
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