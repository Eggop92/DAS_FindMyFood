<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" >
    <link rel="stylesheet" type="text/css" href="CSSForm.css">
	<title> </title>
</head>
<body>
<div id="cabecera"><h1>A&ntilde;adir Ofertas</h1></div>
	
<div id="principal">
<h2>Selecciona una oferta para restaurante: </h2>
<p>
<form method="post" action="Validar_Oferta.php">
	<span>Selecciona el Restaurante:</span>
	<select name="Restaurante">
		<?php
			include 'conexion.php';//esto tambien ejecuta el conexion.php
			$conexion=conectar();
			//listar restaurantes
			$sql="(SELECT `IdRestaurante`, `NombreRestaurante` FROM `Restaurante`);";
		    $resultado= mysqli_query($conexion,$sql);
			if (!$resultado) {
	        	echo "Ha ocurrido un error: ".mysql_error($conexion);
	        	exit();
	        }else{
	        	$cont=0;
	        	while ($fila=mysqli_fetch_row($resultado) ) {
	        		print"<option value=\"".$fila[0]."\">".$fila[1]."</option>\n";
	        		$cont++;   		
	        	}
	        }
		?>
	</select></p>
	<p>Titulo Oferta: <input type="text" name="TituloOferta" size="20"></p>

	Descripcion:<p><textarea name="Descripcion" rows="10" cols="40">Describe aqui tu oferta.</textarea></p>

<p >
<input class="botones" type="reset" value="Borrar Datos">
<input class="botones" type="submit" value="Enviar">
</p>

</form>
</p>

</div>		
<div id="pie_de_pag">
	<p>By: H.Paz & E. Puerta</p>
</div>		
</body>
</html>