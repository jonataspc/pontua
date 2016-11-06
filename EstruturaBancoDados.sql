/*
SQLyog Ultimate v12.14 (64 bit)
MySQL - 5.5.52-0+deb8u1 : Database - infosgi03
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`infosgi03` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `infosgi03`;

/*Table structure for table `area` */

DROP TABLE IF EXISTS `area`;

CREATE TABLE `area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Table structure for table `avaliacao` */

DROP TABLE IF EXISTS `avaliacao`;

CREATE TABLE `avaliacao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_rel_entidade_evento` int(11) NOT NULL,
  `id_rel_item_inspecao_evento` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `pontuacao` decimal(15,2) NOT NULL,
  `metodo` int(11) NOT NULL,
  `data_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_rel_entidade_evento` (`id_rel_entidade_evento`,`id_rel_item_inspecao_evento`),
  KEY `avaliacao_ibfk_3` (`id_usuario`),
  KEY `avaliacao_fk2` (`id_rel_item_inspecao_evento`),
  CONSTRAINT `avaliacao_fk1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `avaliacao_fk2` FOREIGN KEY (`id_rel_item_inspecao_evento`) REFERENCES `rel_item_inspecao_evento` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `avaliacao_fk3` FOREIGN KEY (`id_rel_entidade_evento`) REFERENCES `rel_entidade_evento` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Table structure for table `entidade` */

DROP TABLE IF EXISTS `entidade`;

CREATE TABLE `entidade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Table structure for table `evento` */

DROP TABLE IF EXISTS `evento`;

CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(30) NOT NULL,
  `datahora_criacao` datetime NOT NULL,
  `usuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_evento_nome` (`nome`),
  KEY `evento_ibfk_1` (`usuario`),
  CONSTRAINT `evento_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Table structure for table `item_inspecao` */

DROP TABLE IF EXISTS `item_inspecao`;

CREATE TABLE `item_inspecao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_area` int(11) unsigned NOT NULL,
  `nome` varchar(30) NOT NULL,
  `pontuacao_minima` decimal(15,2) NOT NULL,
  `pontuacao_maxima` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_nome_item` (`nome`),
  KEY `ix_area_item` (`id_area`),
  CONSTRAINT `AREA` FOREIGN KEY (`id_area`) REFERENCES `area` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Table structure for table `rel_entidade_evento` */

DROP TABLE IF EXISTS `rel_entidade_evento`;

CREATE TABLE `rel_entidade_evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_entidade` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_entidade` (`id_entidade`,`id_evento`),
  KEY `fk1_idx` (`id_entidade`),
  KEY `fk2_idx` (`id_evento`),
  CONSTRAINT `fk1` FOREIGN KEY (`id_entidade`) REFERENCES `entidade` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk2` FOREIGN KEY (`id_evento`) REFERENCES `evento` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `rel_item_inspecao_evento` */

DROP TABLE IF EXISTS `rel_item_inspecao_evento`;

CREATE TABLE `rel_item_inspecao_evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_item_inspecao` int(11) NOT NULL,
  `id_evento` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_item_inspecao` (`id_item_inspecao`,`id_evento`),
  KEY `rel_item_inspecao_evento_fk1` (`id_evento`),
  CONSTRAINT `rel_item_inspecao_evento_fk1` FOREIGN KEY (`id_evento`) REFERENCES `evento` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rel_item_inspecao_evento_fk2` FOREIGN KEY (`id_item_inspecao`) REFERENCES `item_inspecao` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `usuario` */

DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_entidade` int(11) DEFAULT NULL,
  `nome` varchar(40) NOT NULL,
  `senha` varchar(40) NOT NULL,
  `nivel_acesso` varchar(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`),
  UNIQUE KEY `ix_id_entidade` (`id_entidade`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`id_entidade`) REFERENCES `entidade` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
