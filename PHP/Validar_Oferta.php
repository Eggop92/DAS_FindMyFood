
<?php
function enviarNotificacionPOST($IdRestaurante, $NombreRestaurante, $TituloOferta){
	$con = conectar();
	$cabecera = array(
			'Authorization:key=AIzaSyDUGfRrUrqIjavjgpNi3m11zICBj-mybfo',
			'Content-Type:application/json'
		);
	$ids = array();
	$resultado = mysqli_query($con, "SELECT idGCM FROM Usuario NATURAL JOIN favoritos WHERE idRestaurante ='".$IdRestaurante."';");
	if(!$resultado){
    	echo "Error (3) mysql: ".mysqli_error($con);
    }else{
    	while($fila = mysqli_fetch_row($resultado)){
    		array_push($ids, $fila[0]);
    	}
    }
    //rellenar la info que se quiere enviar
	$data = array(
		'idRestaurante' => utf8_encode($IdRestaurante),
		'NombreRestaurante' => utf8_encode($NombreRestaurante),
		'tituloOferta' => utf8_encode($TituloOferta)
		);
	$info = array(
			'registration_ids' => $ids,
			'collapse_key' => 'Actualizacion',
			'time_to_live' => 200,
			'delay_while_idle' => true,
			'data' => $data
		);
	echo json_encode($info)."<br><br>";
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, 'https://android.googleapis.com/gcm/send');
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_HTTPHEADER, $cabecera);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($info));
	$result = curl_exec($ch);
	curl_close($ch);
	echo $result;
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" >
    <link rel="stylesheet" type="text/css" href="CSSForm.css">
	<title> </title>
	<script language="Javascript">
		setTimeout("location.href='formularioAnadirOfertas.php'", 200);
	</script>
</head>
<body>
<div id="cabecera"><h1>Datos Oferta</h1></div>
	
<div id="principal">
<?php
include 'conexion.php';//esto tambien ejecuta el conexion.php
			
print"<pre>";
$errores=array();

	if (strlen(trim($_POST['TituloOferta']))==0)
		$errores[]="Debes rellenar el titulo de la oferta.";
	
	if (!$errores){
		//$_POST['Restaurante']//TE DEVUELVE EL ID DEL RESTAURANTE 
		$conexion=conectar();
			//listar restaurantes
		$IdRestaurante = $_POST['Restaurante'];
		$sql = "SELECT `NombreRestaurante` FROM `Restaurante` WHERE `IdRestaurante`=".$IdRestaurante.";";
		$resultado= mysqli_query($conexion,$sql);
		if (!$resultado) {
	        echo "Ha ocurrido un error: ".mysql_error($conexion);
	        exit();
	    }else{
	        $fila=mysqli_fetch_row($resultado);
	        $NombreRestaurante = $fila[0];
	        print"<p>Restaurante: ".$NombreRestaurante." </p>";
	    }
		print"<p>Nueva oferta añadida:</p>";
		$des=$_POST['Descripcion'];	
		//int strcmp ( string $str1 , string $str2 ) 
		//Devuelve < 0 si str1 es menor que str2; > 0 si str1 es mayor que str2 y 0 si son iguales.
		if(strcmp ($des,"Describe aqui tu oferta.")==0){
			$des="";
		}
		$TituloOferta = $_POST['TituloOferta'];
		print"<p><b>".$_POST['TituloOferta']."</b></p><p>".$des."</p>";
		$sqlInsert="INSERT INTO `Oferta`(`IdResOferta`, `Titulo`, `Descripcion`) VALUES ('".$IdRestaurante."', '".$TituloOferta."', '".$des."');";
		$r= mysqli_query($conexion,$sqlInsert);

		enviarNotificacionPOST($IdRestaurante, $NombreRestaurante, $TituloOferta);

	}	
	else{
		for ($i=0,$n=count($errores);$i<$n;$i++)
		{	
			print $errores[$i]."<br>\n";
		}
	}	
	print"</pre>";

?>

</div>		
<div id="pie_de_pag">
	<p>By: H.Paz & E. Puerta</p>
</div>
 </body>
</html>