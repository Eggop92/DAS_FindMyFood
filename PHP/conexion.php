<?php 
	// esta funcion se encarga de proporcionar la conexion a la base de datos
	function conectar()
	{
	    // Datos conexión a la Base de datos
		$bd_server="localhost";  // Host, nombre del servidor o IP del servidor Mysql.
		$bd_usuario="";  // Usuario de Mysql
		$bd_pass="";    // contraseña de Mysql
		$bd_db="FindMyFood";  // Base de datos que se usará.

		// Connexión
		$conexion = mysqli_connect($bd_server,$bd_usuario,$bd_pass,$bd_db);

		//comprobar la conexion
		if (mysqli_connect_error()){
			echo "Error en conexion:".mysqli_connect_error();
			exit();
		}
	    return $conexion;
	}
	function cerrarConexion($c){
		mysqli_close($c);//se supone q esto cierra la conexion q le paso
	}
	function imprimirError($error){
	$response = array();
	// required field is missing
    $response["ERROR"] = $error;
    // echoing JSON response
    echo json_encode($response);
    //exit;
}

?>