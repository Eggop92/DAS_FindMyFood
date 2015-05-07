-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 01-06-2014 a las 17:47:34
-- Versión del servidor: 5.5.37-0ubuntu0.14.04.1
-- Versión de PHP: 5.5.9-1ubuntu4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `FindMyFood`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `favoritos`
--

CREATE TABLE IF NOT EXISTS `favoritos` (
  `NombreUsuario` varchar(20) NOT NULL,
  `IdRestaurante` int(11) NOT NULL,
  PRIMARY KEY (`NombreUsuario`,`IdRestaurante`),
  KEY `IdRestaurante` (`IdRestaurante`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `favoritos`
--

INSERT INTO `favoritos` (`NombreUsuario`, `IdRestaurante`) VALUES
('correo@hotmail.com', 4),
('correo@hotmail.com', 5),
('ego@g', 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Oferta`
--

CREATE TABLE IF NOT EXISTS `Oferta` (
  `IdOferta` int(11) NOT NULL AUTO_INCREMENT,
  `IdResOferta` int(11) NOT NULL,
  `Titulo` varchar(20) NOT NULL,
  `Descripcion` varchar(40) NOT NULL,
  `TituloIN` varchar(20) NOT NULL,
  `DescripcionIN` varchar(40) NOT NULL,
  PRIMARY KEY (`IdOferta`),
  KEY `IdResOferta` (`IdResOferta`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=23 ;

--
-- Volcado de datos para la tabla `Oferta`
--

INSERT INTO `Oferta` (`IdOferta`, `IdResOferta`, `Titulo`, `Descripcion`, `TituloIN`, `DescripcionIN`) VALUES
(14, 1, '2x1 Familiares', 'Disfruta del 2x1 en familiares todos los', '', ''),
(15, 4, '3x2 Solo Martes', 'Martes locos llevas 3 y pagas 2, mediana', '', ''),
(19, 5, 'Menú del dia', 'Menú económico.', '', ''),
(20, 1, 'Lunes Buffet', 'Todos los lunes buffete con bebida incli', '', ''),
(21, 6, 'Menú Especial Doming', 'Domingos de amigos disfruta de tu menú e', '', ''),
(22, 4, 'Te regalamos 1 pizza', 'Con la compra de un menú te regalamos 1 ', '', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Restaurante`
--

CREATE TABLE IF NOT EXISTS `Restaurante` (
  `IdRestaurante` int(11) NOT NULL AUTO_INCREMENT,
  `NombreRestaurante` varchar(30) NOT NULL,
  `Direccion` varchar(60) NOT NULL,
  `Latitud` varchar(10) NOT NULL,
  `Longitud` varchar(10) NOT NULL,
  `Descripcion` varchar(50) NOT NULL,
  `DescripcionIN` varchar(50) NOT NULL,
  PRIMARY KEY (`IdRestaurante`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Volcado de datos para la tabla `Restaurante`
--

INSERT INTO `Restaurante` (`IdRestaurante`, `NombreRestaurante`, `Direccion`, `Latitud`, `Longitud`, `Descripcion`, `DescripcionIN`) VALUES
(1, 'PizzaHut', 'Autonomia 60-62, 48012 Bilbao', '43.258053', '-2.944566', 'Pizzas con el mejor sabor.', 'Pizzas with the best flavor.'),
(4, 'TelePizza', 'Calle Iparraguirre, 26', '43.263929', '-2.936251', 'Pizzas, pastas, ...', 'Pizzas, pastas, ...'),
(5, 'Rest.Etxanobe', 'Avda. Abandoibarra, 4 Bilbao. Vizcaya ', '43.266642', '-2.943508', 'Cuenta con dos comedores y una espectacular terraz', 'It has two dining rooms and a spectacular all year'),
(6, 'Rest.ZORTZIKO', 'Calle Alameda Mazarredo, 17 Bilbao. Vizcaya.', '43.265153', '-2.929539', 'Zortziko cuenta con cuatro espacios diferentes que', 'Zortziko has four different spaces to suit the nee');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Usuario`
--

CREATE TABLE IF NOT EXISTS `Usuario` (
  `NombreUsuario` varchar(50) NOT NULL,
  `IdGCM` varchar(200) NOT NULL,
  `Contrasena` varchar(150) NOT NULL,
  PRIMARY KEY (`NombreUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Usuario`
--

INSERT INTO `Usuario` (`NombreUsuario`, `IdGCM`, `Contrasena`) VALUES
('correo@hotmail.com', 'APA91bGHnjtliqQxi9ZemuOcpcQ2uaqV6f4zVdUge3V5npA6UoLHX_89gIqOpaWie5coxBbx2JsK_yZpiPFwuLi73KW7f8dVZ0fuCNYdMvHiuAktweb_NyXfslECcebKuNeJq_N8M1HN-I-RBKShpgcLanPGAp7rkw', '0ffffff827c0ffffffcb0e0ffffffea0ffffff8a706c4c340ffffffa1680ffffff910fffffff84e7b'),
('ego@g', 'APA91bEM4DEs8NhZkHsf9nakARkkYZpT27SW6osPrAV0-OJj5QYDo383mSGH2ng2Y1ZbzHSzhraACXfrQVAVCNms5lQzIONv83At7yvw3SyCpbRMKhopMjSeRO1XSfnJZKpZUIzlTq4fDwhmI8w8wVGIEzWEcZF9sw', '0ffffff827c0ffffffcb0e0ffffffea0ffffff8a706c4c340ffffffa1680ffffff910fffffff84e7b'),
('egoitz.puerta', '', '');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `favoritos`
--
ALTER TABLE `favoritos`
  ADD CONSTRAINT `favoritos_ibfk_1` FOREIGN KEY (`NombreUsuario`) REFERENCES `Usuario` (`NombreUsuario`),
  ADD CONSTRAINT `favoritos_ibfk_2` FOREIGN KEY (`IdRestaurante`) REFERENCES `Restaurante` (`IdRestaurante`);

--
-- Filtros para la tabla `Oferta`
--
ALTER TABLE `Oferta`
  ADD CONSTRAINT `Oferta_ibfk_1` FOREIGN KEY (`IdResOferta`) REFERENCES `Restaurante` (`IdRestaurante`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
