-- MySQL dump 10.11
--
-- Host: localhost    Database: evaluator23
-- ------------------------------------------------------
-- Server version	5.0.45-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `agilefant_revisions`
--

DROP TABLE IF EXISTS `agilefant_revisions`;
CREATE TABLE `agilefant_revisions` (
  `id` int(11) NOT NULL auto_increment,
  `timestamp` bigint(20) NOT NULL,
  `userId` int(11) NOT NULL,
  `userName` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=315 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `agilefant_revisions`
--

LOCK TABLES `agilefant_revisions` WRITE;
/*!40000 ALTER TABLE `agilefant_revisions` DISABLE KEYS */;
INSERT INTO `agilefant_revisions` VALUES (1,1334086190343,1,'Administrator'),(2,1334087166859,1,'Administrator'),(3,1334087185031,1,'Administrator'),(4,1334087252906,1,'Administrator'),(5,1334087260390,1,'Administrator'),(6,1334087276437,1,'Administrator'),(7,1334087288453,1,'Administrator'),(8,1334087298515,1,'Administrator'),(9,1334087301421,1,'Administrator'),(10,1334087302843,1,'Administrator'),(11,1334087379734,1,'Administrator'),(12,1334087403062,1,'Administrator'),(13,1334087406500,1,'Administrator'),(14,1334087409406,1,'Administrator'),(15,1334087411500,1,'Administrator'),(16,1334087415546,1,'Administrator'),(17,1334087419000,1,'Administrator'),(18,1334087462828,1,'Administrator'),(19,1334087479390,1,'Administrator'),(20,1334087484593,1,'Administrator'),(21,1334087521796,1,'Administrator'),(22,1334087678437,1,'Administrator'),(23,1334087846375,1,'Administrator'),(24,1334087887343,1,'Administrator'),(25,1334087895078,1,'Administrator'),(26,1334087971671,1,'Administrator'),(27,1334087997859,1,'Administrator'),(28,1334088027046,1,'Administrator'),(29,1334088080859,1,'Administrator'),(30,1334088110109,1,'Administrator'),(31,1334088295390,1,'Administrator'),(32,1334088332859,1,'Administrator'),(33,1334088364421,1,'Administrator'),(34,1334088409453,1,'Administrator'),(35,1334088433640,1,'Administrator'),(36,1334088475234,1,'Administrator'),(37,1334088482187,1,'Administrator'),(38,1334088542046,1,'Administrator'),(39,1334088584359,1,'Administrator'),(40,1334088614781,1,'Administrator'),(41,1334088728640,1,'Administrator'),(42,1334088750000,1,'Administrator'),(43,1334088786546,1,'Administrator'),(44,1334089041953,1,'Administrator'),(45,1334089067671,1,'Administrator'),(46,1334089077156,1,'Administrator'),(47,1334089087906,1,'Administrator'),(48,1334089091812,1,'Administrator'),(49,1334089103437,1,'Administrator'),(50,1334089114484,1,'Administrator'),(51,1334089116953,1,'Administrator'),(52,1334089120140,1,'Administrator'),(53,1334089126437,1,'Administrator'),(54,1334089154656,1,'Administrator'),(55,1334089154671,1,'Administrator'),(56,1334089154703,1,'Administrator'),(57,1334089154718,1,'Administrator'),(58,1334089154750,1,'Administrator'),(59,1334089154765,1,'Administrator'),(60,1334089154781,1,'Administrator'),(61,1334089154812,1,'Administrator'),(62,1334089154828,1,'Administrator'),(63,1334089154859,1,'Administrator'),(64,1334089154875,1,'Administrator'),(65,1334089154906,1,'Administrator'),(66,1334089154921,1,'Administrator'),(67,1334089154937,1,'Administrator'),(68,1334089154968,1,'Administrator'),(69,1334089155000,1,'Administrator'),(70,1334089155015,1,'Administrator'),(71,1334089182875,1,'Administrator'),(72,1334089191968,1,'Administrator'),(73,1334089239156,1,'Administrator'),(74,1334089267437,1,'Administrator'),(75,1334089271562,1,'Administrator'),(76,1334089281250,1,'Administrator'),(77,1334089284625,1,'Administrator'),(78,1334089499093,1,'Administrator'),(79,1334089527015,1,'Administrator'),(80,1334089603937,1,'Administrator'),(81,1334089607578,1,'Administrator'),(82,1334089608312,1,'Administrator'),(83,1334089621187,1,'Administrator'),(84,1334089633890,1,'Administrator'),(85,1334089638578,1,'Administrator'),(86,1334089642546,1,'Administrator'),(87,1334089655015,1,'Administrator'),(88,1334089665328,1,'Administrator'),(89,1334089678000,1,'Administrator'),(90,1334089683828,1,'Administrator'),(91,1334089692187,1,'Administrator'),(92,1334089694140,1,'Administrator'),(93,1334089697328,1,'Administrator'),(94,1334089701187,1,'Administrator'),(95,1334089708703,1,'Administrator'),(96,1334089722031,1,'Administrator'),(97,1334089741921,1,'Administrator'),(98,1334089747343,1,'Administrator'),(99,1334089757250,1,'Administrator'),(100,1334089763187,1,'Administrator'),(101,1334089793109,1,'Administrator'),(102,1334089799578,1,'Administrator'),(103,1334089805343,1,'Administrator'),(104,1334089818906,1,'Administrator'),(105,1334089883984,1,'Administrator'),(106,1334089896281,1,'Administrator'),(107,1334089898625,1,'Administrator'),(108,1334089901265,1,'Administrator'),(109,1334089973234,1,'Administrator'),(110,1334089999609,1,'Administrator'),(111,1334090103312,1,'Administrator'),(112,1334090115078,1,'Administrator'),(113,1334090132015,1,'Administrator'),(114,1334090187484,1,'Administrator'),(115,1334090191187,1,'Administrator'),(116,1334090205750,1,'Administrator'),(117,1334090301343,1,'Administrator'),(118,1334090307687,1,'Administrator'),(119,1334090309796,1,'Administrator'),(120,1334090359843,1,'Administrator'),(121,1334090364250,1,'Administrator'),(122,1334090370718,1,'Administrator'),(123,1334090373937,1,'Administrator'),(124,1334090376046,1,'Administrator'),(125,1334090420390,1,'Administrator'),(126,1334090422187,1,'Administrator'),(127,1334090427500,1,'Administrator'),(128,1334090429234,1,'Administrator'),(129,1334090431640,1,'Administrator'),(130,1334090457546,1,'Administrator'),(131,1334090492937,1,'Administrator'),(132,1334090523812,1,'Administrator'),(133,1334090528312,1,'Administrator'),(134,1334090533984,1,'Administrator'),(135,1334090544437,1,'Administrator'),(136,1334090547859,1,'Administrator'),(137,1334090550078,1,'Administrator'),(138,1334090558000,1,'Administrator'),(139,1334090569890,1,'Administrator'),(140,1334090574625,1,'Administrator'),(141,1334090580734,1,'Administrator'),(142,1334090601187,1,'Administrator'),(143,1334090649453,1,'Administrator'),(144,1334090665187,1,'Administrator'),(145,1334090667875,1,'Administrator'),(146,1334090673562,1,'Administrator'),(147,1334090678687,1,'Administrator'),(148,1334090686156,1,'Administrator'),(149,1334090692046,1,'Administrator'),(150,1334141680500,2,'Jarno Vähäniitty'),(151,1334141696875,2,'Jarno Vähäniitty'),(152,1334141750921,2,'Jarno Vähäniitty'),(153,1334141797515,2,'Jarno Vähäniitty'),(154,1334141801843,2,'Jarno Vähäniitty'),(155,1334141873843,2,'Jarno Vähäniitty'),(156,1334259121031,1,'Administrator'),(157,1334259139593,1,'Administrator'),(158,1334259143656,1,'Administrator'),(159,1334259163406,1,'Administrator'),(160,1334259167812,1,'Administrator'),(161,1334259177140,1,'Administrator'),(162,1334259210828,1,'Administrator'),(163,1334259214703,1,'Administrator'),(164,1334259225765,1,'Administrator'),(165,1336039394593,1,'Administrator'),(166,1336977050778,1,'Administrator'),(167,1336977056684,1,'Administrator'),(168,1336977059153,1,'Administrator'),(169,1336977069965,1,'Administrator'),(170,1336977080122,1,'Administrator'),(171,1336977092168,1,'Administrator'),(172,1336977104106,1,'Administrator'),(173,1336977123137,1,'Administrator'),(174,1336977150028,1,'Administrator'),(175,1336977158856,1,'Administrator'),(176,1336977170403,1,'Administrator'),(177,1336977200997,1,'Administrator'),(178,1336977211793,1,'Administrator'),(179,1336977215715,1,'Administrator'),(180,1336977219762,1,'Administrator'),(181,1336977234684,1,'Administrator'),(182,1336977238465,1,'Administrator'),(183,1336977238700,1,'Administrator'),(184,1336977253075,1,'Administrator'),(185,1336977259622,1,'Administrator'),(186,1336977398184,1,'Administrator'),(187,1336977403293,1,'Administrator'),(188,1336977458168,1,'Administrator'),(189,1336977506793,1,'Administrator'),(190,1336977520153,1,'Administrator'),(191,1336977556231,1,'Administrator'),(192,1336977577434,1,'Administrator'),(193,1336977595262,1,'Administrator'),(194,1336977596887,1,'Administrator'),(195,1336977620153,1,'Administrator'),(196,1336977640215,1,'Administrator'),(197,1336977654653,1,'Administrator'),(198,1336977668840,1,'Administrator'),(199,1336977681918,1,'Administrator'),(200,1336977686387,1,'Administrator'),(201,1336977690059,1,'Administrator'),(202,1336977695747,1,'Administrator'),(203,1336977697825,1,'Administrator'),(204,1336977716825,1,'Administrator'),(205,1336977732512,1,'Administrator'),(206,1336977735528,1,'Administrator'),(207,1336977748418,1,'Administrator'),(208,1336977752872,1,'Administrator'),(209,1336977755684,1,'Administrator'),(210,1336977791622,1,'Administrator'),(211,1336977844200,1,'Administrator'),(212,1336977846622,1,'Administrator'),(213,1336977849809,1,'Administrator'),(214,1336977855137,1,'Administrator'),(215,1336977861887,1,'Administrator'),(216,1336977870918,1,'Administrator'),(217,1336977873309,1,'Administrator'),(218,1336977915215,1,'Administrator'),(219,1340106760330,2,'Agilefant Dev Team'),(220,1340106772330,2,'Agilefant Dev Team'),(221,1340106776158,2,'Agilefant Dev Team'),(222,1340106791955,2,'Agilefant Dev Team'),(223,1340106899518,2,'Agilefant Dev Team'),(224,1340106951424,2,'Agilefant Dev Team'),(225,1340106957486,2,'Agilefant Dev Team'),(226,1340106962830,2,'Agilefant Dev Team'),(227,1340106965861,2,'Agilefant Dev Team'),(228,1340106992783,2,'Agilefant Dev Team'),(229,1340107061690,2,'Agilefant Dev Team'),(230,1340107063580,2,'Agilefant Dev Team'),(231,1340107068424,2,'Agilefant Dev Team'),(232,1340107070002,2,'Agilefant Dev Team'),(233,1340107073111,2,'Agilefant Dev Team'),(234,1340107168205,2,'Agilefant Dev Team'),(235,1340107205627,2,'Agilefant Dev Team'),(236,1340107210455,2,'Agilefant Dev Team'),(237,1340107225908,2,'Agilefant Dev Team'),(238,1340107233846,2,'Agilefant Dev Team'),(239,1340107237143,2,'Agilefant Dev Team'),(240,1340107244940,2,'Agilefant Dev Team'),(241,1340107248283,2,'Agilefant Dev Team'),(242,1340107250424,2,'Agilefant Dev Team'),(243,1340107285471,2,'Agilefant Dev Team'),(244,1340107299033,2,'Agilefant Dev Team'),(245,1340107349705,2,'Agilefant Dev Team'),(246,1340107352236,2,'Agilefant Dev Team'),(247,1340107361268,2,'Agilefant Dev Team'),(248,1340107370768,2,'Agilefant Dev Team'),(249,1340107399143,2,'Agilefant Dev Team'),(250,1340107401908,2,'Agilefant Dev Team'),(251,1340107405768,2,'Agilefant Dev Team'),(252,1340107421174,2,'Agilefant Dev Team'),(253,1340107430815,2,'Agilefant Dev Team'),(254,1340107437658,2,'Agilefant Dev Team'),(255,1340107445736,2,'Agilefant Dev Team'),(256,1340107448815,2,'Agilefant Dev Team'),(257,1340107456705,2,'Agilefant Dev Team'),(258,1340107487611,2,'Agilefant Dev Team'),(259,1340107493690,2,'Agilefant Dev Team'),(260,1340107512299,2,'Agilefant Dev Team'),(261,1340107518299,2,'Agilefant Dev Team'),(262,1340107521205,2,'Agilefant Dev Team'),(263,1340107528830,2,'Agilefant Dev Team'),(264,1340107559908,2,'Agilefant Dev Team'),(265,1340107610424,2,'Agilefant Dev Team'),(266,1340107615111,2,'Agilefant Dev Team'),(267,1340107649268,2,'Agilefant Dev Team'),(268,1340107653299,2,'Agilefant Dev Team'),(269,1340107663533,2,'Agilefant Dev Team'),(270,1340107670736,2,'Agilefant Dev Team'),(271,1340107674018,2,'Agilefant Dev Team'),(272,1340107677549,2,'Agilefant Dev Team'),(273,1340107702565,2,'Agilefant Dev Team'),(274,1340107714158,2,'Agilefant Dev Team'),(275,1340107725861,2,'Agilefant Dev Team'),(276,1340107743815,2,'Agilefant Dev Team'),(277,1345032284468,1,'Administrator'),(278,1345032319734,1,'Administrator'),(279,1345032334250,1,'Administrator'),(280,1345032348640,1,'Administrator'),(281,1345032359234,1,'Administrator'),(282,1345032364046,1,'Administrator'),(283,1345032423031,1,'Administrator'),(284,1345032426734,1,'Administrator'),(285,1345032429640,1,'Administrator'),(286,1345032456343,1,'Administrator'),(287,1345032480968,1,'Administrator'),(288,1345032528953,1,'Administrator'),(289,1345032539843,1,'Administrator'),(290,1345032549765,1,'Administrator'),(291,1345032612203,1,'Administrator'),(292,1345032636921,1,'Administrator'),(293,1345032650656,1,'Administrator'),(294,1345032669203,1,'Administrator'),(295,1345032674562,1,'Administrator'),(296,1345032696640,1,'Administrator'),(297,1345032760375,1,'Administrator'),(298,1345032788718,1,'Administrator'),(299,1345032813781,1,'Administrator'),(300,1345032816781,1,'Administrator'),(301,1345032827750,1,'Administrator'),(302,1345032833718,1,'Administrator'),(303,1345032859328,1,'Administrator'),(304,1345032865062,1,'Administrator'),(305,1345032886406,1,'Administrator'),(306,1345032927515,1,'Administrator'),(307,1345032944250,1,'Administrator'),(308,1345032954781,1,'Administrator'),(309,1345032954781,1,'Administrator'),(310,1345032961109,1,'Administrator'),(311,1345032980609,1,'Administrator'),(312,1345033128015,1,'Administrator'),(313,1345033145359,1,'Administrator'),(314,1345033239140,1,'Administrator');
/*!40000 ALTER TABLE `agilefant_revisions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignment`
--

DROP TABLE IF EXISTS `assignment`;
CREATE TABLE `assignment` (
  `id` int(11) NOT NULL auto_increment,
  `availability` int(11) NOT NULL,
  `delta_personal_load` bigint(20) default NULL,
  `backlog_id` int(11) default NULL,
  `user_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK3D2B86CDF8762ABE` (`backlog_id`),
  KEY `FK3D2B86CDE537EC82` (`backlog_id`),
  KEY `FK3D2B86CDC1610AD2` (`user_id`),
  KEY `FK3D2B86CDF63400A2` (`backlog_id`),
  CONSTRAINT `FK3D2B86CDF63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK3D2B86CDC1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK3D2B86CDE537EC82` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK3D2B86CDF8762ABE` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `assignment`
--

LOCK TABLES `assignment` WRITE;
/*!40000 ALTER TABLE `assignment` DISABLE KEYS */;
INSERT INTO `assignment` VALUES (1,100,0,2,2),(2,100,0,3,2),(3,100,0,4,2),(5,100,0,8,2);
/*!40000 ALTER TABLE `assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignment_AUD`
--

DROP TABLE IF EXISTS `assignment_AUD`;
CREATE TABLE `assignment_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  `availability` int(11) default NULL,
  `delta_personal_load` bigint(20) default NULL,
  `backlog_id` int(11) default NULL,
  `user_id` int(11) default NULL,
  PRIMARY KEY  (`id`,`REV`),
  KEY `FK1B6D861E20258526` (`REV`),
  CONSTRAINT `FK1B6D861E20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `assignment_AUD`
--

LOCK TABLES `assignment_AUD` WRITE;
/*!40000 ALTER TABLE `assignment_AUD` DISABLE KEYS */;
INSERT INTO `assignment_AUD` VALUES (1,37,0,100,0,2,2),(2,76,0,100,0,3,2),(3,77,0,100,0,4,2),(4,223,0,100,0,8,2),(4,281,2,100,0,8,2),(5,282,0,100,0,8,2);
/*!40000 ALTER TABLE `assignment_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backlogs`
--

DROP TABLE IF EXISTS `backlogs`;
CREATE TABLE `backlogs` (
  `backlogtype` varchar(31) NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  `description` longtext,
  `name` varchar(255) default NULL,
  `backlogSize` bigint(20) default NULL,
  `baselineLoad` bigint(20) default NULL,
  `endDate` datetime default NULL,
  `startDate` datetime default NULL,
  `rank` int(11) default NULL,
  `status` int(11) default NULL,
  `parent_id` int(11) default NULL,
  `readonlyToken` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `readonlyToken` (`readonlyToken`),
  KEY `FK7E75FF16B8A85DD5` (`parent_id`),
  CONSTRAINT `FK7E75FF16B8A85DD5` FOREIGN KEY (`parent_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `backlogs`
--

LOCK TABLES `backlogs` WRITE;
/*!40000 ALTER TABLE `backlogs` DISABLE KEYS */;
INSERT INTO `backlogs` VALUES ('Product',1,'This \'Product\' is a simple example of how you might want to plan and work on a major product - or a business using Agilefant.<div>illustrating how hierarchical goals, and how the steps for advancing them has been refined into iteration-sized chunks.&nbsp;</div><div>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>Use the degree of accuracy that fits you. Right tool for the right job!</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a></div>','Agilefant (this is an example product)',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Project',2,'','2012 Spring (this is an example project)',0,0,'2012-05-31 18:00:00','2012-04-10 08:00:00',0,0,1,NULL),('Project',3,'','2012 Summer',0,0,'2012-08-31 18:00:00','2012-06-01 08:00:00',0,3,1,NULL),('Project',4,'','2012 Fall',0,0,'2012-12-31 18:00:00','2012-09-01 08:00:00',0,3,1,NULL),('Product',5,'Creating a bunch of stories in the story tree view is the simplest way to create a to-do list.&nbsp;<div><br></div><div>With Agilefant you can create such - as well plan full-fledged business, product and development efforts (click on the \'product\' Agilefant in the left hand tree)</div>','ToDo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Iteration',6,NULL,'April (this is an example iteration)',0,0,'2012-04-30 18:00:00','2012-04-10 08:00:00',NULL,NULL,2,NULL),('Iteration',7,NULL,'May',0,0,'2012-05-31 18:00:00','2012-05-01 08:00:00',NULL,NULL,2,NULL),('Iteration',8,NULL,'June-Aug',0,0,'2012-08-15 23:59:00','2012-06-01 08:00:00',NULL,NULL,3,NULL),('Iteration',9,'This is an example standalone iteration. Standalone iterations can contain stories from many products.','Standalone iteration',NULL,NULL,'2012-09-27 15:09:00','2012-08-15 15:09:00',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `backlogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backlogs_AUD`
--

DROP TABLE IF EXISTS `backlogs_AUD`;
CREATE TABLE `backlogs_AUD` (
  `backlogtype` varchar(31) NOT NULL,
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  `description` longtext,
  `name` varchar(255) default NULL,
  `parent_id` int(11) default NULL,
  `backlogSize` bigint(20) default NULL,
  `baselineLoad` bigint(20) default NULL,
  `endDate` datetime default NULL,
  `rank` int(11) default NULL,
  `startDate` datetime default NULL,
  `status` int(11) default NULL,
  PRIMARY KEY  (`id`,`REV`),
  KEY `FK46C0B1E720258526` (`REV`),
  CONSTRAINT `FK46C0B1E720258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `backlogs_AUD`
--

LOCK TABLES `backlogs_AUD` WRITE;
/*!40000 ALTER TABLE `backlogs_AUD` DISABLE KEYS */;
INSERT INTO `backlogs_AUD` VALUES ('Product',1,2,0,'','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,3,1,'See product vision','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,4,1,'You can find Agilefant\'s&nbsp;p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div><u>that\'s<br></u><div><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a></div></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,5,1,'You can find Agilefant\'s&nbsp;p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>that\'s<br><div><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a></div></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,22,1,'You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>that\'s<br><div><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a></div></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,23,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div>For more info on how to use Agilefant and the ideas behind it, check out</div><div>+ Jarno\'s Ph.D.: Towards Agile Product and Portfolio Management</div><div>+ A practitioner-oriented book written by Jarno and his colleagues by the same title</div><div>+ A short guide describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,24,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Jarno\'s Ph.D.: Towards Agile Product and Portfolio Management</div><div>+ A practitioner-oriented book written by Jarno and his colleagues by the same title</div><div>+ A short guide describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,25,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Jarno\'s Ph.D.: \"Towards Agile Product and Portfolio Management\"</div><div>+ A practitioner-oriented book written by Jarno and his colleagues by the same title</div><div>+ A short guide describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,26,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Jarno\'s Ph.D.: \"Towards Agile Product and Portfolio Management\"</div><div>+ A practitioner-oriented book written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">guide</a> describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,27,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page</div><div>+ Jarno\'s Ph.D.: \"Towards Agile Product and Portfolio Management\"</div><div>+ A practitioner-oriented book written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,28,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ Jarno\'s Ph.D.: \"Towards Agile Product and Portfolio Management\"</div><div>+ A practitioner-oriented book written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,29,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ Jarno\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A practitioner-oriented book written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,30,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A practitioner-oriented book written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,31,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,32,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s knowledge hub</div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,33,1,'<div>Below you\'ll find an example set of hierarchical goals, and how the steps for advancing them have been refined into iteration-sized chuncks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,78,1,'This \'Product\' is a simple example of how you might want to plan and work on a major product - or a business using Agilefant.<div>illustrating how hierarchical goals, and how the steps for advancing them has been refined into iteration-sized chunks.</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,111,1,'This \'Product\' is a simple example of how you might want to plan and work on a major product - or a business using Agilefant.<div>illustrating how hierarchical goals, and how the steps for advancing them has been refined into iteration-sized chunks.&nbsp;</div><div>But, Agilefant scales down into simple to-do -lists as well.&nbsp;</div><div><br></div><div>Use the degree of accuracy that fits you. Right tool for the right job!</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,113,1,'This \'Product\' is a simple example of how you might want to plan and work on a major product - or a business using Agilefant.<div>illustrating how hierarchical goals, and how the steps for advancing them has been refined into iteration-sized chunks.&nbsp;</div><div>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>Use the degree of accuracy that fits you. Right tool for the right job!</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,150,1,'This \'Product\' is a simple example of how you might want to plan and work on a major product - or a business using Agilefant.<div>illustrating how hierarchical goals, and how the steps for advancing them has been refined into iteration-sized chunks.&nbsp;</div><div>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>Use the degree of accuracy that fits you. Right tool for the right job!</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a></div>','Agilefant (an example multi-release product development effort)',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,151,1,'This \'Product\' is a simple example of how you might want to plan and work on a major product - or a business using Agilefant.<div>illustrating how hierarchical goals, and how the steps for advancing them has been refined into iteration-sized chunks.&nbsp;</div><div>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>Use the degree of accuracy that fits you. Right tool for the right job!</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',1,288,1,'This \'Product\' is a simple example of how you might want to plan and work on a major product - or a business using Agilefant.<div>illustrating how hierarchical goals, and how the steps for advancing them has been refined into iteration-sized chunks.&nbsp;</div><div>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>Use the degree of accuracy that fits you. Right tool for the right job!</div><div><br></div>You can find Agilefant\'s full p<b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">roduct vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u></div><div>+ Agilefant\'s built-in help page (see top right corner!)</div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a></div>','Agilefant (this is an example product)',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Project',2,36,0,'','2012',1,0,0,'2012-05-10 18:00:00',0,'2012-04-10 08:00:00',0),('Project',2,71,1,'','2012 Spring',1,0,0,'2012-05-10 18:00:00',0,'2012-04-10 08:00:00',0),('Project',2,72,1,'','2012 Spring',1,0,0,'2012-05-31 18:00:00',0,'2012-04-10 08:00:00',0),('Project',2,289,1,'','2012 Spring (this is an example project)',1,0,0,'2012-05-31 18:00:00',0,'2012-04-10 08:00:00',0),('Project',3,73,0,'','2012 Summer',1,0,0,'2012-08-31 18:00:00',0,'2012-06-01 08:00:00',0),('Project',3,75,1,'','2012 Summer',1,0,0,'2012-08-31 18:00:00',0,'2012-06-01 08:00:00',3),('Project',4,74,0,'','2012 Fall',1,0,0,'2012-12-31 18:00:00',0,'2012-09-01 08:00:00',3),('Product',5,79,0,'','todo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,109,1,'This is a simple to-do list. With Agilefant you can create such - as well plan full-fledged business, product and development efforts.','todo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,110,1,'This is a simple to-do list. With Agilefant you can create such - as well plan<a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=1\"> full-fledged business, product and development efforts</a>.','todo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,112,1,'This is a simple to-do list. With Agilefant you can create such - as well plan<a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=1\"> full-fledged business, product and development efforts</a>.','ToDo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,155,1,'Creating a bunch of stories in the story tree view is the simplest way to create a to-do list.&nbsp;<div><br></div><div>With Agilefant you can create such - as well plan<a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=1\"> full-fledged business, product and development efforts</a>.</div>','ToDo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,165,1,'Creating a bunch of stories in the story tree view is the simplest way to create a to-do list.&nbsp;<div><br></div><div>With Agilefant you can create such - as well plan full-fledged business, product and development efforts (click on the \'product\' Agilefant)</div>','ToDo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,218,1,'Creating a bunch of stories in the story tree view is the simplest way to create a to-do list.&nbsp;<div><br></div><div>With Agilefant you can create such - as well plan full-fledged business, product and development efforts (click on the \'product\' Agilefant in the left hand tree)</div>','ToDo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Iteration',6,114,0,NULL,'April',2,0,0,'2012-04-30 18:00:00',NULL,'2012-04-10 08:00:00',NULL),('Iteration',6,290,1,NULL,'April (this is an example iteration)',2,0,0,'2012-04-30 18:00:00',NULL,'2012-04-10 08:00:00',NULL),('Iteration',7,115,0,NULL,'May',2,0,0,'2012-04-24 18:00:00',NULL,'2012-04-10 08:00:00',NULL),('Iteration',7,116,1,NULL,'May',2,0,0,'2012-05-31 18:00:00',NULL,'2012-05-01 08:00:00',NULL),('Iteration',8,223,0,NULL,'June',3,0,0,'2012-07-31 23:59:00',NULL,'2012-06-01 08:00:00',NULL),('Iteration',8,264,1,NULL,'June-July',3,0,0,'2012-07-31 23:59:00',NULL,'2012-06-01 08:00:00',NULL),('Iteration',8,279,1,NULL,'June-Aug',3,0,0,'2012-07-31 23:59:00',NULL,'2012-06-01 08:00:00',NULL),('Iteration',8,280,1,NULL,'June-Aug',3,0,0,'2012-08-15 23:59:00',NULL,'2012-06-01 08:00:00',NULL),('Iteration',9,291,0,'This is an example standalone iteration. Standalone iterations can contain stories from many products.','Standalone iteration',NULL,NULL,NULL,'2012-09-27 15:09:00',NULL,'2012-08-15 15:09:00',NULL);
/*!40000 ALTER TABLE `backlogs_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history_backlogs`
--

DROP TABLE IF EXISTS `history_backlogs`;
CREATE TABLE `history_backlogs` (
  `id` int(11) NOT NULL auto_increment,
  `branchMax` bigint(20) NOT NULL,
  `doneSum` bigint(20) NOT NULL,
  `estimateSum` bigint(20) NOT NULL,
  `rootSum` bigint(20) NOT NULL,
  `timestamp` datetime default NULL,
  `backlog_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKA6B13261F8762ABE` (`backlog_id`),
  KEY `FKA6B13261F63400A2` (`backlog_id`),
  CONSTRAINT `FKA6B13261F63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FKA6B13261F8762ABE` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `history_backlogs`
--

LOCK TABLES `history_backlogs` WRITE;
/*!40000 ALTER TABLE `history_backlogs` DISABLE KEYS */;
INSERT INTO `history_backlogs` VALUES (1,0,0,0,0,'2012-04-10 23:44:46',2),(2,0,0,0,0,'2012-04-10 23:42:37',3),(3,0,0,0,0,'2012-04-10 23:43:21',4),(4,0,0,0,0,'2012-05-14 09:44:14',2),(5,0,0,0,0,'2012-05-14 09:40:20',3),(6,0,0,0,0,'2012-06-19 15:08:45',3),(7,0,0,0,0,'2012-06-19 15:04:16',2),(8,0,0,0,0,'2012-08-15 15:16:20',3),(9,0,0,0,0,'2012-08-15 15:16:00',2);
/*!40000 ALTER TABLE `history_backlogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history_iterations`
--

DROP TABLE IF EXISTS `history_iterations`;
CREATE TABLE `history_iterations` (
  `id` int(11) NOT NULL auto_increment,
  `deltaOriginalEstimate` bigint(20) NOT NULL,
  `effortLeftSum` bigint(20) NOT NULL,
  `originalEstimateSum` bigint(20) NOT NULL,
  `timestamp` date NOT NULL,
  `iteration_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `iteration_id` (`iteration_id`,`timestamp`),
  KEY `FK9DD479C14157D2A2` (`iteration_id`),
  CONSTRAINT `FK9DD479C14157D2A2` FOREIGN KEY (`iteration_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `history_iterations`
--

LOCK TABLES `history_iterations` WRITE;
/*!40000 ALTER TABLE `history_iterations` DISABLE KEYS */;
INSERT INTO `history_iterations` VALUES (1,0,0,0,'2012-04-10',6),(2,0,0,0,'2012-04-10',7),(3,720,180,720,'2012-04-12',6),(4,-720,0,0,'2012-05-14',6),(5,0,0,0,'2012-05-14',7),(6,0,0,0,'2012-06-19',8),(7,0,0,0,'2012-06-19',7),(8,0,0,0,'2012-08-15',9),(9,0,0,0,'2012-08-15',8);
/*!40000 ALTER TABLE `history_iterations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Holiday`
--

DROP TABLE IF EXISTS `Holiday`;
CREATE TABLE `Holiday` (
  `id` int(11) NOT NULL auto_increment,
  `endDate` datetime default NULL,
  `startDate` datetime default NULL,
  `user_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKA44DC038C1610AD2` (`user_id`),
  CONSTRAINT `FKA44DC038C1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Holiday`
--

LOCK TABLES `Holiday` WRITE;
/*!40000 ALTER TABLE `Holiday` DISABLE KEYS */;
/*!40000 ALTER TABLE `Holiday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HolidayAnomaly`
--

DROP TABLE IF EXISTS `HolidayAnomaly`;
CREATE TABLE `HolidayAnomaly` (
  `id` int(11) NOT NULL auto_increment,
  `date` date default NULL,
  `user_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK267567ABC1610AD2` (`user_id`),
  CONSTRAINT `FK267567ABC1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `HolidayAnomaly`
--

LOCK TABLES `HolidayAnomaly` WRITE;
/*!40000 ALTER TABLE `HolidayAnomaly` DISABLE KEYS */;
/*!40000 ALTER TABLE `HolidayAnomaly` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hourentries`
--

DROP TABLE IF EXISTS `hourentries`;
CREATE TABLE `hourentries` (
  `DTYPE` varchar(31) NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  `date` datetime default NULL,
  `description` longtext,
  `minutesSpent` bigint(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `backlog_id` int(11) default NULL,
  `story_id` int(11) default NULL,
  `task_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKF6FE62CE0E4BFA2` (`story_id`),
  KEY `FKF6FE62CF63400A2` (`backlog_id`),
  KEY `FKF6FE62CC1610AD2` (`user_id`),
  KEY `FKF6FE62C6E84F892` (`task_id`),
  CONSTRAINT `FKF6FE62C6E84F892` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`),
  CONSTRAINT `FKF6FE62CC1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKF6FE62CE0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`),
  CONSTRAINT `FKF6FE62CF63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `hourentries`
--

LOCK TABLES `hourentries` WRITE;
/*!40000 ALTER TABLE `hourentries` DISABLE KEYS */;
INSERT INTO `hourentries` VALUES ('TaskHourEntry',1,'2012-04-12 22:32:00','',60,1,NULL,NULL,1),('TaskHourEntry',2,'2012-04-12 22:33:00','',300,1,NULL,NULL,2);
/*!40000 ALTER TABLE `hourentries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labels`
--

DROP TABLE IF EXISTS `labels`;
CREATE TABLE `labels` (
  `id` int(11) NOT NULL auto_increment,
  `displayName` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `timestamp` datetime default NULL,
  `creator_id` int(11) default NULL,
  `story_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `label_name` (`name`),
  KEY `FKBDD05FFFE0E4BFA2` (`story_id`),
  KEY `FKBDD05FFF1C5D0ED1` (`creator_id`),
  CONSTRAINT `FKBDD05FFF1C5D0ED1` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKBDD05FFFE0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `labels`
--

LOCK TABLES `labels` WRITE;
/*!40000 ALTER TABLE `labels` DISABLE KEYS */;
/*!40000 ALTER TABLE `labels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
CREATE TABLE `settings` (
  `id` int(11) NOT NULL auto_increment,
  `description` varchar(255) default NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1,'Agilefant database version','AgilefantDatabaseVersion','304'),(2,NULL,'RangeLow','0'),(3,NULL,'RangeHigh','120'),(4,NULL,'OptimalLow','70'),(5,NULL,'OptimalHigh','85'),(6,NULL,'CriticalLow','100'),(7,NULL,'HourReporting','true'),(8,NULL,'DevPortfolio','true'),(9,NULL,'DailyWork','true'),(10,NULL,'StoryTreeFieldOrder','state,storyPoints,labels,name,backlog,breadcrumb'),(11,NULL,'branchMetricsType','estimate'),(12,NULL,'labelsInStoryList','true'),(13,NULL,'weekendsInBurndown','true');
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stories`
--

DROP TABLE IF EXISTS `stories`;
CREATE TABLE `stories` (
  `id` int(11) NOT NULL auto_increment,
  `description` longtext,
  `name` varchar(255) NOT NULL,
  `state` int(11) NOT NULL,
  `storyPoints` int(11) default NULL,
  `storyValue` int(11) default NULL,
  `treeRank` int(11) NOT NULL default '0',
  `backlog_id` int(11) default NULL,
  `parent_id` int(11) default NULL,
  `iteration_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK8FB06053F63400A2` (`backlog_id`),
  KEY `FK8FB06053F5E897CD` (`parent_id`),
  KEY `FK_iteration_id` (`iteration_id`),
  CONSTRAINT `FK8FB06053F5E897CD` FOREIGN KEY (`parent_id`) REFERENCES `stories` (`id`),
  CONSTRAINT `FK8FB06053F63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK_iteration_id` FOREIGN KEY (`iteration_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `stories`
--

LOCK TABLES `stories` WRITE;
/*!40000 ALTER TABLE `stories` DISABLE KEYS */;
INSERT INTO `stories` VALUES (1,NULL,'MEANEST MACHINE',1,NULL,NULL,0,1,5,NULL),(2,NULL,'MOST USERS',1,NULL,NULL,1,1,5,NULL),(3,NULL,'FULL POWER COMMUNITY',1,NULL,NULL,2,1,5,NULL),(4,NULL,'The best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',5,NULL,NULL,0,2,1,NULL),(5,NULL,'FREE, OPEN SOURCE MARKET LEADER OF BACKLOG MGMT TOOLS',1,NULL,NULL,1,1,NULL,NULL),(6,NULL,'Agilefant has gained the market leadership of free tools',1,NULL,NULL,1,1,2,NULL),(7,'(Includes Jarno, Petteri and Benjamin)','At least 10 people are contributing to Agilefant\'s development',5,NULL,NULL,4,1,3,NULL),(8,NULL,'Efforts to advance Agilefant are fully funded',1,NULL,NULL,2,1,3,NULL),(9,NULL,'A new SW project course at Aalto has in September working on Agilefant',0,NULL,NULL,0,4,3,NULL),(10,NULL,'Development + consulting partnerships arranged with several companies',1,NULL,NULL,1,2,8,7),(11,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and won the pitching competition',5,NULL,NULL,0,3,8,8),(12,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',5,NULL,NULL,0,2,7,7),(13,NULL,'Free evaluation instances are readily available online',5,NULL,NULL,0,2,36,NULL),(14,NULL,'Releases',1,NULL,NULL,0,1,23,NULL),(15,NULL,'2.5.0',5,NULL,NULL,0,2,14,6),(16,NULL,'3.0 alpha 1',5,NULL,NULL,1,2,14,7),(17,NULL,'3.0 beta released',4,NULL,NULL,5,3,14,8),(18,'Blocked. No better free tool than Agilefant exists.','Find a better backlog management tool, one that is free as well as scales!',3,5,NULL,0,5,NULL,NULL),(19,'Blocked, \'cause the store is not open','Buy milk',3,1,NULL,2,5,NULL,NULL),(20,'<i>Pending for a raise</i>','Get a new car',2,40,0,3,5,NULL,NULL),(21,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,1,5,NULL,9),(22,'','Agilefant Inc. has been launched',1,NULL,NULL,2,1,8,NULL),(23,NULL,'Agilefant has been recognized as the meanest machine',1,NULL,NULL,1,1,1,NULL),(24,NULL,'3.0 alpha 2',5,NULL,NULL,2,2,14,7),(25,NULL,'Instances 1-13',5,NULL,NULL,0,2,13,6),(26,NULL,'Instances 14-30',5,NULL,NULL,1,2,13,7),(27,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',5,NULL,NULL,2,3,22,8),(28,NULL,'Set Agilefant as one of the topics at EIT ICT labs Helsinki Summer School',5,NULL,NULL,0,2,22,6),(29,NULL,'Killer team recruited',5,NULL,NULL,1,2,22,7),(30,NULL,'Pre-course coding sessions',5,NULL,NULL,1,2,7,7),(31,NULL,'3.0 alpha 3..7',5,NULL,NULL,3,1,14,NULL),(32,NULL,'3.0 alpha 8',1,NULL,NULL,4,1,14,NULL),(33,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',5,NULL,NULL,3,3,3,8),(34,NULL,'Agilefant has 100 registered user organizations',5,NULL,NULL,1,3,6,8),(35,NULL,'Agilefant has 150 registered user organizations',1,NULL,NULL,0,3,6,NULL),(36,NULL,'Agilefant Cloud',1,NULL,NULL,0,1,2,NULL),(37,NULL,'\"Unlimited\" supply of readily available 30-day evaluation instances',1,NULL,NULL,1,3,36,NULL),(38,NULL,'Cloudification of evaluation \'fants',1,NULL,NULL,0,3,37,9),(39,'','Celebrate the release of Agilefant 3.0 Beta 1',1,NULL,NULL,1,3,3,9);
/*!40000 ALTER TABLE `stories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stories_AUD`
--

DROP TABLE IF EXISTS `stories_AUD`;
CREATE TABLE `stories_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  `description` longtext,
  `name` varchar(255) default NULL,
  `state` int(11) default NULL,
  `storyPoints` int(11) default NULL,
  `storyValue` int(11) default NULL,
  `treeRank` int(11) default '0',
  `backlog_id` int(11) default NULL,
  `parent_id` int(11) default NULL,
  `iteration_id` int(11) default NULL,
  PRIMARY KEY  (`id`,`REV`),
  KEY `FK853E2CA420258526` (`REV`),
  CONSTRAINT `FK853E2CA420258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `stories_AUD`
--

LOCK TABLES `stories_AUD` WRITE;
/*!40000 ALTER TABLE `stories_AUD` DISABLE KEYS */;
INSERT INTO `stories_AUD` VALUES (1,6,0,NULL,'MEANEST MACHINE',0,NULL,NULL,0,1,NULL,NULL),(1,7,1,NULL,'MEANEST MACHINE',0,NULL,NULL,1,1,NULL,NULL),(1,8,1,NULL,'MEANEST MACHINE',0,NULL,NULL,2,1,NULL,NULL),(1,9,1,NULL,'MEANEST MACHINE',0,NULL,NULL,0,1,NULL,NULL),(1,12,1,NULL,'MEANEST MACHINE',0,NULL,NULL,1,1,NULL,NULL),(1,13,1,NULL,'MEANEST MACHINE',0,NULL,NULL,0,1,5,NULL),(1,14,1,NULL,'MEANEST MACHINE',0,NULL,NULL,1,1,5,NULL),(1,15,1,NULL,'MEANEST MACHINE',0,NULL,NULL,2,1,5,NULL),(1,16,1,NULL,'MEANEST MACHINE',0,NULL,NULL,0,1,5,NULL),(1,61,1,NULL,'MEANEST MACHINE',1,NULL,NULL,0,1,5,NULL),(2,7,0,NULL,'MOST USERS',0,NULL,NULL,0,1,NULL,NULL),(2,8,1,NULL,'MOST USERS',0,NULL,NULL,1,1,NULL,NULL),(2,9,1,NULL,'MOST USERS',0,NULL,NULL,2,1,NULL,NULL),(2,10,1,NULL,'MOST USERS',0,NULL,NULL,1,1,NULL,NULL),(2,12,1,NULL,'MOST USERS',0,NULL,NULL,2,1,NULL,NULL),(2,14,1,NULL,'MOST USERS',0,NULL,NULL,0,1,5,NULL),(2,15,1,NULL,'MOST USERS',0,NULL,NULL,1,1,5,NULL),(2,16,1,NULL,'MOST USERS',0,NULL,NULL,2,1,5,NULL),(2,17,1,NULL,'MOST USERS',0,NULL,NULL,1,1,5,NULL),(2,54,1,NULL,'MOST USERS',1,NULL,NULL,1,1,5,NULL),(3,8,0,NULL,'FULL POWER COMMUNITY',0,NULL,NULL,0,1,NULL,NULL),(3,9,1,NULL,'FULL POWER COMMUNITY',0,NULL,NULL,1,1,NULL,NULL),(3,10,1,NULL,'FULL POWER COMMUNITY',0,NULL,NULL,2,1,NULL,NULL),(3,12,1,NULL,'FULL POWER COMMUNITY',0,NULL,NULL,3,1,NULL,NULL),(3,15,1,NULL,'FULL POWER COMMUNITY',0,NULL,NULL,0,1,5,NULL),(3,16,1,NULL,'FULL POWER COMMUNITY',0,NULL,NULL,1,1,5,NULL),(3,17,1,NULL,'FULL POWER COMMUNITY',0,NULL,NULL,2,1,5,NULL),(3,58,1,NULL,'FULL POWER COMMUNITY',1,NULL,NULL,2,1,5,NULL),(4,11,0,NULL,'Best usability, best fit for the real world context, ',0,NULL,NULL,0,1,1,NULL),(4,18,1,NULL,'Best features, excellent user eXperience and conteptually best fit ',0,NULL,NULL,0,1,1,NULL),(4,19,1,NULL,'Best features, excellent user eXperience and conteptually best fit with real/world agile/lean development)',0,NULL,NULL,0,1,1,NULL),(4,20,1,NULL,'Best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',0,NULL,NULL,0,1,1,NULL),(4,64,1,NULL,'Best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',1,NULL,NULL,0,1,1,NULL),(4,143,1,NULL,'The best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',1,NULL,NULL,0,1,1,NULL),(4,144,1,NULL,'The best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',1,NULL,NULL,1,1,1,NULL),(4,147,1,NULL,'The best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',4,NULL,NULL,1,1,1,NULL),(4,148,1,NULL,'The best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',4,NULL,NULL,1,2,1,NULL),(4,149,1,NULL,'The best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',4,NULL,NULL,0,2,1,NULL),(4,176,1,NULL,'The best features, excellent user eXperience and conteptually best fit with real/world agile/lean development',5,NULL,NULL,0,2,1,NULL),(5,12,0,NULL,'FREE, OPEN SOURCE MARKET LEADER OF BACKLOG MGMT TOOLS',0,NULL,NULL,0,1,NULL,NULL),(5,70,1,NULL,'FREE, OPEN SOURCE MARKET LEADER OF BACKLOG MGMT TOOLS',1,NULL,NULL,0,1,NULL,NULL),(5,135,1,NULL,'FREE, OPEN SOURCE MARKET LEADER OF BACKLOG MGMT TOOLS',1,NULL,NULL,1,1,NULL,NULL),(6,21,0,NULL,'Agilefant has gained the market leadership of free tools',0,NULL,NULL,0,1,2,NULL),(6,69,1,NULL,'Agilefant has gained the market leadership of free tools',1,NULL,NULL,0,1,2,NULL),(6,265,1,NULL,'Agilefant has gained the market leadership of free tools',1,NULL,NULL,1,1,2,NULL),(7,34,0,NULL,'At least 10 of SuperAmerica participants are contributing to Agilefant\'s development',0,NULL,NULL,0,1,3,NULL),(7,35,1,NULL,'At least 10 of SuperAmerica participants are contributing to Agilefant\'s development',0,NULL,NULL,1,1,3,NULL),(7,39,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 of SuperAmerica participants are contributing to Agilefant\'s development',0,NULL,NULL,1,1,3,NULL),(7,40,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 of SuperAmerica participants are contributing to Agilefant\'s development',0,NULL,NULL,2,1,3,NULL),(7,57,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 of SuperAmerica participants are contributing to Agilefant\'s development',1,NULL,NULL,2,1,3,NULL),(7,139,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 of SuperAmerica participants are contributing to Agilefant\'s development',1,NULL,NULL,3,1,3,NULL),(7,140,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 of SuperAmerica participants are contributing to Agilefant\'s development',1,NULL,NULL,2,1,3,NULL),(7,204,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 people are contributing to Agilefant\'s development',1,NULL,NULL,2,1,3,NULL),(7,245,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 people are contributing to Agilefant\'s development',1,NULL,NULL,3,1,3,NULL),(7,306,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 people are contributing to Agilefant\'s development',1,NULL,NULL,4,1,3,NULL),(7,308,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 people are contributing to Agilefant\'s development',5,NULL,NULL,4,1,3,NULL),(7,309,1,'(Includes Jarno, Petteri and Benjamin)','At least 10 people are contributing to Agilefant\'s development',5,NULL,NULL,4,1,3,NULL),(8,35,0,NULL,'Agilefant\'s funding is fully funded',0,NULL,NULL,0,1,3,NULL),(8,38,1,NULL,'Efforts to advance Agilefant are fully funded',0,NULL,NULL,0,1,3,NULL),(8,40,1,NULL,'Efforts to advance Agilefant are fully funded',0,NULL,NULL,1,1,3,NULL),(8,66,1,NULL,'Efforts to advance Agilefant are fully funded',1,NULL,NULL,1,1,3,NULL),(8,139,1,NULL,'Efforts to advance Agilefant are fully funded',1,NULL,NULL,2,1,3,NULL),(8,140,1,NULL,'Efforts to advance Agilefant are fully funded',1,NULL,NULL,1,1,3,NULL),(8,245,1,NULL,'Efforts to advance Agilefant are fully funded',1,NULL,NULL,2,1,3,NULL),(8,248,1,NULL,'Efforts to advance Agilefant are fully funded',1,NULL,NULL,0,1,3,NULL),(8,306,1,NULL,'Efforts to advance Agilefant are fully funded',1,NULL,NULL,1,1,3,NULL),(8,307,1,NULL,'Efforts to advance Agilefant are fully funded',1,NULL,NULL,2,1,3,NULL),(9,40,0,NULL,'A new SW project course at Aalto has in September working on Agilefant',0,NULL,NULL,0,1,3,NULL),(9,62,1,NULL,'A new SW project course at Aalto has in September working on Agilefant',1,NULL,NULL,0,1,3,NULL),(9,124,1,NULL,'A new SW project course at Aalto has in September working on Agilefant',1,NULL,NULL,0,4,3,NULL),(9,142,1,NULL,'A new SW project course at Aalto has in September working on Agilefant',0,NULL,NULL,0,4,3,NULL),(9,245,1,NULL,'A new SW project course at Aalto has in September working on Agilefant',0,NULL,NULL,1,4,3,NULL),(9,248,1,NULL,'A new SW project course at Aalto has in September working on Agilefant',0,NULL,NULL,2,4,3,NULL),(9,306,1,NULL,'A new SW project course at Aalto has in September working on Agilefant',0,NULL,NULL,3,4,3,NULL),(9,307,1,NULL,'A new SW project course at Aalto has in September working on Agilefant',0,NULL,NULL,0,4,3,NULL),(10,41,0,NULL,'Development + consulting partnerships arranged with several companies',0,NULL,NULL,0,1,8,NULL),(10,42,1,NULL,'Development + consulting partnerships arranged with several companies',0,NULL,NULL,1,1,8,NULL),(10,55,1,NULL,'Development + consulting partnerships arranged with several companies',1,NULL,NULL,1,1,8,NULL),(10,122,1,NULL,'Development + consulting partnerships arranged with several companies',1,NULL,NULL,1,7,8,NULL),(10,140,1,NULL,'Development + consulting partnerships arranged with several companies',1,NULL,NULL,2,7,8,NULL),(10,152,1,NULL,'Development + consulting partnerships arranged with several companies',1,NULL,NULL,1,7,8,NULL),(11,42,0,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School',0,NULL,NULL,0,1,8,NULL),(11,68,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School',1,NULL,NULL,0,1,8,NULL),(11,123,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School',1,NULL,NULL,0,3,8,NULL),(11,131,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School, and the Agilefant Co-op is coming on strong',1,NULL,NULL,0,3,8,NULL),(11,132,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and enjoyed it immensel',1,NULL,NULL,0,3,8,NULL),(11,133,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and enjoyed it immensely',1,NULL,NULL,0,3,8,NULL),(11,134,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and enjoyed it immensely',0,NULL,NULL,0,3,8,NULL),(11,138,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and enjoyed it immensely',1,NULL,NULL,0,3,8,NULL),(11,140,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and enjoyed it immensely',1,NULL,NULL,1,3,8,NULL),(11,141,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and enjoyed it immensely',1,NULL,NULL,0,3,8,NULL),(11,226,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and enjoyed it immensely',5,NULL,NULL,0,3,8,NULL),(11,228,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and enjoyed it immensely',5,NULL,NULL,0,8,8,NULL),(11,229,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and won the pitching competition',5,NULL,NULL,0,8,8,NULL),(11,230,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and won the pitching competition',4,NULL,NULL,0,8,8,NULL),(11,296,1,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and won the pitching competition',5,NULL,NULL,0,3,8,8),(12,43,0,NULL,'The 3.0 beta backlog has been prioritized, published and drummed about',0,NULL,NULL,0,1,7,NULL),(12,44,1,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',0,NULL,NULL,0,1,7,NULL),(12,56,1,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',1,NULL,NULL,0,1,7,NULL),(12,121,1,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',1,NULL,NULL,0,6,7,NULL),(12,205,1,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',1,NULL,NULL,0,7,7,NULL),(12,206,1,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',4,NULL,NULL,0,7,7,NULL),(12,208,1,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',4,NULL,NULL,1,7,7,NULL),(12,209,1,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',4,NULL,NULL,0,7,7,NULL),(12,310,1,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',5,NULL,NULL,0,2,7,7),(13,45,0,NULL,'Free evaluation instances are readily available online',0,NULL,NULL,0,1,6,NULL),(13,67,1,NULL,'Free evaluation instances are readily available online',1,NULL,NULL,0,1,6,NULL),(13,120,1,NULL,'Free evaluation instances are readily available online',1,NULL,NULL,0,6,6,NULL),(13,177,1,NULL,'Free evaluation instances are readily available online',1,NULL,NULL,0,2,6,NULL),(13,182,1,NULL,'Free evaluation instances are readily available online',4,NULL,NULL,0,2,6,NULL),(13,183,1,NULL,'Free evaluation instances are readily available online',4,NULL,NULL,0,2,6,NULL),(13,249,1,NULL,'Free evaluation instances are readily available online',4,NULL,NULL,1,2,6,NULL),(13,252,1,NULL,'Free evaluation instances are readily available online',4,NULL,NULL,2,2,6,NULL),(13,255,1,NULL,'Free evaluation instances are readily available online',5,NULL,NULL,2,2,6,NULL),(13,266,1,NULL,'Free evaluation instances are readily available online',5,NULL,NULL,0,2,36,NULL),(13,267,1,NULL,'Free evaluation instances are readily available online',5,NULL,NULL,1,2,36,NULL),(13,268,1,NULL,'Free evaluation instances are readily available online',5,NULL,NULL,0,2,37,NULL),(13,269,1,NULL,'Free evaluation instances are readily available online',5,NULL,NULL,1,2,37,NULL),(13,273,1,NULL,'Free evaluation instances are readily available online',5,NULL,NULL,0,2,36,NULL),(14,46,0,NULL,'Release of 2.5.0',0,NULL,NULL,0,1,4,NULL),(14,47,1,NULL,'Releases',0,NULL,NULL,0,1,4,NULL),(14,65,1,NULL,'Releases',1,NULL,NULL,0,1,4,NULL),(14,146,1,NULL,'Releases',1,NULL,NULL,0,1,23,NULL),(15,48,0,NULL,'2.5.0',0,NULL,NULL,0,1,14,NULL),(15,49,1,NULL,'2.5.0',0,NULL,NULL,1,1,14,NULL),(15,50,1,NULL,'2.5.0',0,NULL,NULL,2,1,14,NULL),(15,51,1,NULL,'2.5.0',0,NULL,NULL,0,1,14,NULL),(15,60,1,NULL,'2.5.0',1,NULL,NULL,0,1,14,NULL),(15,117,1,NULL,'2.5.0',1,NULL,NULL,0,2,14,NULL),(15,166,1,NULL,'2.5.0',5,NULL,NULL,0,2,14,NULL),(15,169,1,NULL,'2.5.0',5,NULL,NULL,0,6,14,NULL),(15,237,1,NULL,'2.5.0',5,NULL,NULL,1,6,14,NULL),(15,238,1,NULL,'2.5.0',5,NULL,NULL,0,6,14,NULL),(15,240,1,NULL,'2.5.0',5,NULL,NULL,1,6,14,NULL),(15,241,1,NULL,'2.5.0',5,NULL,NULL,0,6,14,NULL),(16,49,0,NULL,'3.0.0 alpha',0,NULL,NULL,0,1,14,NULL),(16,50,1,NULL,'3.0.0 alpha',0,NULL,NULL,1,1,14,NULL),(16,51,1,NULL,'3.0.0 alpha',0,NULL,NULL,2,1,14,NULL),(16,52,1,NULL,'3.0.0 alpha',0,NULL,NULL,1,1,14,NULL),(16,59,1,NULL,'3.0.0 alpha',1,NULL,NULL,1,1,14,NULL),(16,119,1,NULL,'3.0.0 alpha',1,NULL,NULL,1,2,14,NULL),(16,167,1,NULL,'3.0.0 alpha',5,NULL,NULL,1,2,14,NULL),(16,168,1,NULL,'3.0.0 alpha 1',5,NULL,NULL,1,2,14,NULL),(16,170,1,NULL,'3.0.0 alpha 1',5,NULL,NULL,1,7,14,NULL),(16,171,1,NULL,'3.0 alpha 1',5,NULL,NULL,1,7,14,NULL),(16,214,1,NULL,'3.0 alpha 1',4,NULL,NULL,1,7,14,NULL),(16,236,1,NULL,'3.0 alpha 1',5,NULL,NULL,1,7,14,NULL),(16,237,1,NULL,'3.0 alpha 1',5,NULL,NULL,2,7,14,NULL),(16,238,1,NULL,'3.0 alpha 1',5,NULL,NULL,1,7,14,NULL),(16,240,1,NULL,'3.0 alpha 1',5,NULL,NULL,2,7,14,NULL),(16,241,1,NULL,'3.0 alpha 1',5,NULL,NULL,1,7,14,NULL),(17,50,0,NULL,'3.0.0 beta',0,NULL,NULL,0,1,14,NULL),(17,51,1,NULL,'3.0.0 beta',0,NULL,NULL,1,1,14,NULL),(17,52,1,NULL,'3.0.0 beta',0,NULL,NULL,2,1,14,NULL),(17,53,1,NULL,'3.0.x beta',0,NULL,NULL,2,1,14,NULL),(17,63,1,NULL,'3.0.x beta',1,NULL,NULL,2,1,14,NULL),(17,118,1,NULL,'3.0.x beta',1,NULL,NULL,2,3,14,NULL),(17,172,1,NULL,'3.0.x beta',1,NULL,NULL,3,3,14,NULL),(17,173,1,NULL,'3.0 beta',1,NULL,NULL,3,3,14,NULL),(17,186,1,NULL,'3.0 beta',0,NULL,NULL,3,3,14,NULL),(17,224,1,NULL,'3.0 beta',1,NULL,NULL,3,3,14,NULL),(17,225,1,NULL,'3.0 beta released',1,NULL,NULL,3,3,14,NULL),(17,232,1,NULL,'3.0 beta released',1,NULL,NULL,3,8,14,NULL),(17,237,1,NULL,'3.0 beta released',1,NULL,NULL,4,8,14,NULL),(17,240,1,NULL,'3.0 beta released',1,NULL,NULL,5,8,14,NULL),(17,293,1,NULL,'3.0 beta released',4,NULL,NULL,5,3,14,8),(18,80,0,NULL,'Find a better backlog management tool, one that is free as well as scales for us',0,NULL,NULL,0,5,NULL,NULL),(18,81,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',0,NULL,NULL,1,5,NULL,NULL),(18,82,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',0,NULL,NULL,2,5,NULL,NULL),(18,84,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',0,NULL,NULL,3,5,NULL,NULL),(18,85,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',0,NULL,NULL,2,5,NULL,NULL),(18,90,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',1,NULL,NULL,2,5,NULL,NULL),(18,91,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',1,NULL,NULL,0,5,21,NULL),(18,92,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',1,NULL,NULL,0,5,NULL,NULL),(18,100,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',1,7,NULL,0,5,NULL,NULL),(18,102,1,NULL,'Find a better backlog management tool, one that is free as well as scales for us',1,5,NULL,0,5,NULL,NULL),(18,153,1,NULL,'Find a better backlog management tool, one that is free as well as scales!',1,5,NULL,0,5,NULL,NULL),(18,154,1,NULL,'Find a better backlog management tool, one that is free as well as scales!',4,5,NULL,0,5,NULL,NULL),(18,312,1,NULL,'Find a better backlog management tool, one that is free as well as scales!',3,5,NULL,0,5,NULL,NULL),(18,313,1,'Blocked. No better free tool than Agilefant exists.','Find a better backlog management tool, one that is free as well as scales!',3,5,NULL,0,5,NULL,NULL),(19,81,0,NULL,'Buy milk',0,NULL,NULL,0,5,NULL,NULL),(19,82,1,NULL,'Buy milk',0,NULL,NULL,1,5,NULL,NULL),(19,84,1,NULL,'Buy milk',0,NULL,NULL,2,5,NULL,NULL),(19,85,1,NULL,'Buy milk',0,NULL,NULL,1,5,NULL,NULL),(19,86,1,NULL,'Buy milk',3,NULL,NULL,1,5,NULL,NULL),(19,87,1,'Blocked, \'cause the store is not open','Buy milk',3,NULL,NULL,1,5,NULL,NULL),(19,92,1,'Blocked, \'cause the store is not open','Buy milk',3,NULL,NULL,2,5,NULL,NULL),(19,94,1,'Blocked, \'cause the store is not open','Buy milk',3,NULL,NULL,3,5,NULL,NULL),(19,98,1,'Blocked, \'cause the store is not open','Buy milk',3,1,NULL,3,5,NULL,NULL),(19,106,1,'Blocked, \'cause the store is not open','Buy milk',3,1,NULL,1,5,NULL,NULL),(19,108,1,'Blocked, \'cause the store is not open','Buy milk',3,1,NULL,2,5,NULL,NULL),(20,82,0,NULL,'Get a new car',0,NULL,NULL,0,5,NULL,NULL),(20,83,1,NULL,'Get a new car',1,NULL,NULL,0,5,NULL,NULL),(20,84,1,NULL,'Get a new car',1,NULL,NULL,1,5,NULL,NULL),(20,85,1,NULL,'Get a new car',1,NULL,NULL,0,5,NULL,NULL),(20,88,1,NULL,'Get a new car',2,NULL,NULL,0,5,NULL,NULL),(20,89,1,'<i>Pending for a raise</i>','Get a new car',2,NULL,NULL,0,5,NULL,NULL),(20,92,1,'<i>Pending for a raise</i>','Get a new car',2,NULL,NULL,1,5,NULL,NULL),(20,99,1,'<i>Pending for a raise</i>','Get a new car',2,NULL,40,1,5,NULL,NULL),(20,101,1,'<i>Pending for a raise</i>','Get a new car',2,NULL,25,1,5,NULL,NULL),(20,103,1,'<i>Pending for a raise</i>','Get a new car',2,NULL,9,1,5,NULL,NULL),(20,104,1,'<i>Pending for a raise</i>','Get a new car',2,NULL,0,1,5,NULL,NULL),(20,105,1,'<i>Pending for a raise</i>','Get a new car',2,40,0,1,5,NULL,NULL),(20,106,1,'<i>Pending for a raise</i>','Get a new car',2,40,0,2,5,NULL,NULL),(20,107,1,'<i>Pending for a raise</i>','Get a new car',2,40,0,3,5,NULL,NULL),(21,84,0,NULL,'Get more beer',0,NULL,NULL,0,5,NULL,NULL),(21,85,1,NULL,'Get more beer',0,NULL,NULL,3,5,NULL,NULL),(21,93,1,NULL,'Get more beer',0,NULL,NULL,0,5,19,NULL),(21,94,1,NULL,'Get more beer',0,NULL,NULL,2,5,NULL,NULL),(21,95,1,NULL,'Get more beer',6,NULL,NULL,2,5,NULL,NULL),(21,96,1,'Deferred, don\'t need more beer today','Get more beer',6,NULL,NULL,2,5,NULL,NULL),(21,97,1,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,2,5,NULL,NULL),(21,106,1,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,3,5,NULL,NULL),(21,107,1,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,2,5,NULL,NULL),(21,108,1,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,1,5,NULL,NULL),(21,292,1,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,1,5,NULL,9),(21,300,1,'Deferred, don\'t need more beer today','Get more beer',2,3,NULL,1,5,NULL,9),(21,301,1,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,1,5,NULL,9),(22,135,0,'','The Agilefant Co-op is coming on strong',0,NULL,NULL,0,3,NULL,NULL),(22,139,1,'','The Agilefant Co-op is coming on strong',0,NULL,NULL,1,3,3,NULL),(22,140,1,'','The Agilefant Co-op is coming on strong',0,NULL,NULL,0,3,8,NULL),(22,141,1,'','The Agilefant Co-op is coming on strong',0,NULL,NULL,1,3,8,NULL),(22,152,1,'','The Agilefant Co-op is coming on strong',0,NULL,NULL,2,3,8,NULL),(22,188,1,'','Agilefant Inc. has been launched',0,NULL,NULL,2,3,8,NULL),(22,191,1,'','Agilefant Inc. has been launched',0,NULL,NULL,2,1,8,NULL),(22,234,1,'','Agilefant Inc. has been launched',1,NULL,NULL,2,1,8,NULL),(23,144,0,NULL,'Recognized as the one with (etc.)',0,NULL,NULL,0,1,1,NULL),(23,145,1,NULL,'Recognized as the one with (etc.)',1,NULL,NULL,0,1,1,NULL),(23,149,1,NULL,'Recognized as the one with (etc.)',1,NULL,NULL,1,1,1,NULL),(23,174,1,NULL,'Recognized as the meanest machine',1,NULL,NULL,1,1,1,NULL),(23,175,1,NULL,'Agilefant has been recognized as the meanest machine',1,NULL,NULL,1,1,1,NULL),(24,172,0,NULL,'3.0 alpha 2',0,NULL,NULL,2,7,14,NULL),(24,187,1,NULL,'3.0 alpha 2',1,NULL,NULL,2,7,14,NULL),(24,235,1,NULL,'3.0 alpha 2',5,NULL,NULL,2,7,14,NULL),(24,237,1,NULL,'3.0 alpha 2',5,NULL,NULL,3,7,14,NULL),(24,238,1,NULL,'3.0 alpha 2',5,NULL,NULL,2,7,14,NULL),(24,240,1,NULL,'3.0 alpha 2',5,NULL,NULL,3,7,14,NULL),(24,241,1,NULL,'3.0 alpha 2',5,NULL,NULL,2,7,14,NULL),(25,178,0,NULL,'Instances 1-13',0,NULL,NULL,0,2,13,NULL),(25,179,1,NULL,'Instances 1-13',5,NULL,NULL,0,2,13,NULL),(25,180,1,NULL,'Instances 1-13',5,NULL,NULL,0,6,13,NULL),(26,181,0,NULL,'Instances 14-30',0,NULL,NULL,1,6,13,NULL),(26,184,1,NULL,'Instances 14-30',0,NULL,NULL,1,7,13,NULL),(26,185,1,NULL,'Instances 14-30',4,NULL,NULL,1,7,13,NULL),(26,256,1,NULL,'Instances 14-30',5,NULL,NULL,1,7,13,NULL),(27,189,0,NULL,'Business models and prel. biz plan prepared at the EIT ICT labs Helsinki Summer School',0,NULL,NULL,0,3,22,NULL),(27,192,1,NULL,'Business models and prel. biz plan prepared at the EIT ICT labs Helsinki Summer School',0,NULL,NULL,1,3,22,NULL),(27,195,1,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',0,NULL,NULL,1,3,22,NULL),(27,196,1,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',0,NULL,NULL,2,3,22,NULL),(27,199,1,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',0,NULL,NULL,3,3,22,NULL),(27,207,1,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',0,NULL,NULL,2,3,22,NULL),(27,227,1,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',5,NULL,NULL,2,3,22,NULL),(27,233,1,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',5,NULL,NULL,2,8,22,NULL),(28,190,0,NULL,'Set Agilefant as one of the topics',0,NULL,NULL,0,3,27,NULL),(28,192,1,NULL,'Set Agilefant as one of the topics',0,NULL,NULL,0,3,22,NULL),(28,193,1,NULL,'Set Agilefant as one of the topics at EIT ICT labs Helsinki Summer School',0,NULL,NULL,0,6,22,NULL),(28,194,1,NULL,'Set Agilefant as one of the topics at EIT ICT labs Helsinki Summer School',5,NULL,NULL,0,6,22,NULL),(28,196,1,NULL,'Set Agilefant as one of the topics at EIT ICT labs Helsinki Summer School',5,NULL,NULL,1,6,22,NULL),(28,199,1,NULL,'Set Agilefant as one of the topics at EIT ICT labs Helsinki Summer School',5,NULL,NULL,2,6,22,NULL),(28,203,1,NULL,'Set Agilefant as one of the topics at EIT ICT labs Helsinki Summer School',5,NULL,NULL,0,6,22,NULL),(29,196,0,NULL,'Killer team recruited',0,NULL,NULL,0,1,22,NULL),(29,197,1,NULL,'Killer team recruited',0,NULL,NULL,0,7,22,NULL),(29,198,1,NULL,'Killer team recruited',4,NULL,NULL,0,7,22,NULL),(29,199,1,NULL,'Killer team recruited',4,NULL,NULL,1,7,22,NULL),(29,202,1,NULL,'Killer team recruited',4,NULL,NULL,0,7,22,NULL),(29,203,1,NULL,'Killer team recruited',4,NULL,NULL,1,7,22,NULL),(29,257,1,NULL,'Killer team recruited',5,NULL,NULL,1,7,22,NULL),(30,199,0,NULL,'Pre-course coding sessions',0,NULL,NULL,0,1,22,NULL),(30,200,1,NULL,'Pre-course coding sessions',2,NULL,NULL,0,1,22,NULL),(30,201,1,NULL,'Pre-course coding sessions',2,NULL,NULL,0,7,22,NULL),(30,202,1,NULL,'Pre-course coding sessions',2,NULL,NULL,1,7,22,NULL),(30,203,1,NULL,'Pre-course coding sessions',2,NULL,NULL,2,7,22,NULL),(30,207,1,NULL,'Pre-course coding sessions',2,NULL,NULL,3,7,3,NULL),(30,208,1,NULL,'Pre-course coding sessions',2,NULL,NULL,0,7,7,NULL),(30,209,1,NULL,'Pre-course coding sessions',2,NULL,NULL,1,7,7,NULL),(30,243,1,NULL,'Pre-course coding sessions',1,NULL,NULL,1,7,7,NULL),(30,244,1,NULL,'Pre-course coding sessions',5,NULL,NULL,1,7,7,NULL),(31,237,0,NULL,'3.0 alpha 3..7',0,NULL,NULL,0,1,14,NULL),(31,238,1,NULL,'3.0 alpha 3..7',0,NULL,NULL,3,1,14,NULL),(31,239,1,NULL,'3.0 alpha 3..7',5,NULL,NULL,3,1,14,NULL),(31,240,1,NULL,'3.0 alpha 3..7',5,NULL,NULL,4,1,14,NULL),(31,241,1,NULL,'3.0 alpha 3..7',5,NULL,NULL,3,1,14,NULL),(32,240,0,NULL,'3.0 alpha 8',0,NULL,NULL,0,1,14,NULL),(32,241,1,NULL,'3.0 alpha 8',0,NULL,NULL,4,1,14,NULL),(32,242,1,NULL,'3.0 alpha 8',1,NULL,NULL,4,1,14,NULL),(33,245,0,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',0,NULL,NULL,0,1,3,NULL),(33,246,1,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',4,NULL,NULL,0,1,3,NULL),(33,247,1,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',4,NULL,NULL,0,8,3,NULL),(33,248,1,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',4,NULL,NULL,1,8,3,NULL),(33,278,1,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',5,NULL,NULL,1,3,3,8),(33,306,1,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',5,NULL,NULL,2,3,3,8),(33,307,1,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',5,NULL,NULL,3,3,3,8),(34,249,0,NULL,'Agilefant has 100 registered user organizations',0,NULL,NULL,0,1,6,NULL),(34,250,1,NULL,'Agilefant has 100 registered user organizations',4,NULL,NULL,0,1,6,NULL),(34,251,1,NULL,'Agilefant has 100 registered user organizations',4,NULL,NULL,0,8,6,NULL),(34,252,1,NULL,'Agilefant has 100 registered user organizations',4,NULL,NULL,1,8,6,NULL),(34,295,1,NULL,'Agilefant has 100 registered user organizations',5,NULL,NULL,1,3,6,8),(35,252,0,NULL,'Agilefant has 150 registered user organizations',0,NULL,NULL,0,1,6,NULL),(35,253,1,NULL,'Agilefant has 150 registered user organizations',0,NULL,NULL,0,3,6,NULL),(35,254,1,NULL,'Agilefant has 150 registered user organizations',1,NULL,NULL,0,3,6,NULL),(36,265,0,NULL,'Agilefant Cloud',0,NULL,NULL,0,1,2,NULL),(36,270,1,NULL,'Agilefant Cloud',1,NULL,NULL,0,1,2,NULL),(37,267,0,NULL,'\"Unlimited\" supply of readily available 30-day evaluation instances',0,NULL,NULL,0,1,36,NULL),(37,271,1,NULL,'\"Unlimited\" supply of readily available 30-day evaluation instances',1,NULL,NULL,0,1,36,NULL),(37,273,1,NULL,'\"Unlimited\" supply of readily available 30-day evaluation instances',1,NULL,NULL,1,1,36,NULL),(37,274,1,NULL,'\"Unlimited\" supply of readily available 30-day evaluation instances',1,NULL,NULL,1,3,36,NULL),(38,269,0,NULL,'Cloudification of evaluation \'fants',0,NULL,NULL,0,1,37,NULL),(38,272,1,NULL,'Cloudification of evaluation \'fants',1,NULL,NULL,0,1,37,NULL),(38,274,1,NULL,'Cloudification of evaluation \'fants',1,NULL,NULL,0,3,37,NULL),(38,275,1,NULL,'Cloudification of evaluation \'fants',1,NULL,NULL,0,8,37,NULL),(38,294,1,NULL,'Cloudification of evaluation \'fants',1,NULL,NULL,0,3,37,9),(38,299,1,NULL,'Cloudification of evaluation \'fants',6,NULL,NULL,0,3,37,9),(38,302,1,NULL,'Cloudification of evaluation \'fants',1,NULL,NULL,0,3,37,9),(39,303,0,'','Celebrate the release of Agilefant 3.0 Beta 1',0,NULL,NULL,0,3,NULL,8),(39,304,1,'','Celebrate the release of Agilefant 3.0 Beta 1',1,NULL,NULL,0,3,NULL,8),(39,305,1,'','Celebrate the release of Agilefant 3.0 Beta 1',1,NULL,NULL,3,3,5,8),(39,306,1,'','Celebrate the release of Agilefant 3.0 Beta 1',1,NULL,NULL,0,3,3,8),(39,307,1,'','Celebrate the release of Agilefant 3.0 Beta 1',1,NULL,NULL,1,3,3,8),(39,311,1,'','Celebrate the release of Agilefant 3.0 Beta 1',1,NULL,NULL,1,3,3,9);
/*!40000 ALTER TABLE `stories_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story_access`
--

DROP TABLE IF EXISTS `story_access`;
CREATE TABLE `story_access` (
  `id` int(11) NOT NULL auto_increment,
  `date` datetime default NULL,
  `story_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK44C5ABEEE0E4BFA2` (`story_id`),
  KEY `FK44C5ABEEC1610AD2` (`user_id`),
  CONSTRAINT `FK44C5ABEEC1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK44C5ABEEE0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `story_access`
--

LOCK TABLES `story_access` WRITE;
/*!40000 ALTER TABLE `story_access` DISABLE KEYS */;
INSERT INTO `story_access` VALUES (1,'2012-04-10 22:48:10',2,1),(2,'2012-04-10 22:48:25',1,1),(3,'2012-04-10 22:50:28',4,1),(4,'2012-04-10 22:51:43',2,1),(5,'2012-04-10 22:52:06',3,1),(6,'2012-04-10 23:06:09',3,1),(7,'2012-04-10 23:06:52',3,1),(8,'2012-04-10 23:06:58',3,1),(9,'2012-04-10 23:08:31',8,1),(10,'2012-04-10 23:09:07',8,1),(11,'2012-04-10 23:09:18',7,1),(12,'2012-04-10 23:09:22',7,1),(13,'2012-04-10 23:09:47',3,1),(14,'2012-04-10 23:10:22',3,1),(15,'2012-04-10 23:11:29',8,1),(16,'2012-04-10 23:12:10',8,1),(17,'2012-04-10 23:12:43',7,1),(18,'2012-04-10 23:16:49',12,1),(19,'2012-04-10 23:17:28',6,1),(20,'2012-04-10 23:17:49',4,1),(21,'2012-04-10 23:17:58',4,1),(22,'2012-04-10 23:18:02',14,1),(23,'2012-04-10 23:18:14',14,1),(24,'2012-04-10 23:18:25',14,1),(25,'2012-04-10 23:18:41',17,1),(26,'2012-04-10 23:26:50',18,1),(27,'2012-04-10 23:26:58',20,1),(28,'2012-04-10 23:27:20',19,1),(29,'2012-04-10 23:27:48',20,1),(30,'2012-04-10 23:28:01',18,1),(31,'2012-04-10 23:28:17',19,1),(32,'2012-04-10 23:28:22',21,1),(33,'2012-04-10 23:28:52',18,1),(34,'2012-04-10 23:28:59',21,1),(35,'2012-04-10 23:29:05',19,1),(36,'2012-04-10 23:29:13',20,1),(37,'2012-04-10 23:29:20',18,1),(38,'2012-04-10 23:29:36',20,1),(39,'2012-04-10 23:29:47',20,1),(40,'2012-04-10 23:29:56',18,1),(41,'2012-04-10 23:30:02',20,1),(42,'2012-04-10 23:30:15',20,1),(43,'2012-04-10 23:35:53',15,1),(44,'2012-04-10 23:43:18',9,1),(45,'2012-04-10 23:43:51',4,1),(46,'2012-04-10 23:44:13',1,1),(47,'2012-04-10 23:44:26',23,1),(48,'2012-04-10 23:44:35',4,1),(49,'2012-04-11 13:56:32',18,2),(50,'2012-04-12 22:29:55',12,1),(51,'2012-04-12 22:30:15',13,1),(52,'2012-05-11 23:05:42',14,1),(53,'2012-05-14 09:30:49',15,1),(54,'2012-05-14 09:30:54',16,1),(55,'2012-05-14 09:31:02',15,1),(56,'2012-05-14 09:31:12',16,1),(57,'2012-05-14 09:31:28',16,1),(58,'2012-05-14 09:31:35',16,1),(59,'2012-05-14 09:31:45',24,1),(60,'2012-05-14 09:31:58',17,1),(61,'2012-05-14 09:32:23',23,1),(62,'2012-05-14 09:32:48',4,1),(63,'2012-05-14 09:33:13',13,1),(64,'2012-05-14 09:33:23',13,1),(65,'2012-05-14 09:33:34',25,1),(66,'2012-05-14 09:33:42',13,1),(67,'2012-05-14 09:33:47',25,1),(68,'2012-05-14 09:33:56',13,1),(69,'2012-05-14 09:34:08',26,1),(70,'2012-05-14 09:34:16',26,1),(71,'2012-05-14 09:36:33',17,1),(72,'2012-05-14 09:36:41',24,1),(73,'2012-05-14 09:37:16',22,1),(74,'2012-05-14 09:37:44',22,1),(75,'2012-05-14 09:38:28',27,1),(76,'2012-05-14 09:38:41',28,1),(77,'2012-05-14 09:39:06',22,1),(78,'2012-05-14 09:39:28',27,1),(79,'2012-05-14 09:39:38',28,1),(80,'2012-05-14 09:40:05',27,1),(81,'2012-05-14 09:40:31',22,1),(82,'2012-05-14 09:40:47',29,1),(83,'2012-05-14 09:41:06',29,1),(84,'2012-05-14 09:41:13',22,1),(85,'2012-05-14 09:41:24',30,1),(86,'2012-05-14 09:41:47',7,1),(87,'2012-05-14 09:42:05',12,1),(88,'2012-05-14 09:42:46',7,1),(89,'2012-06-19 14:58:12',3,2),(90,'2012-06-19 14:59:27',22,2),(91,'2012-06-19 15:00:01',24,2),(92,'2012-06-19 15:00:08',16,2),(93,'2012-06-19 15:00:13',14,2),(94,'2012-06-19 15:00:35',31,2),(95,'2012-06-19 15:00:39',14,2),(96,'2012-06-19 15:00:49',32,2),(97,'2012-06-19 15:01:21',30,2),(98,'2012-06-19 15:01:36',30,2),(99,'2012-06-19 15:02:10',3,2),(100,'2012-06-19 15:02:31',33,2),(101,'2012-06-19 15:02:34',33,2),(102,'2012-06-19 15:03:02',6,2),(103,'2012-06-19 15:03:20',34,2),(104,'2012-06-19 15:03:28',6,2),(105,'2012-06-19 15:03:42',35,2),(106,'2012-06-19 15:03:54',35,2),(107,'2012-06-19 15:04:02',26,2),(108,'2012-06-19 15:04:04',13,2),(109,'2012-06-19 15:04:07',26,2),(110,'2012-06-19 15:04:15',29,2),(111,'2012-06-19 15:06:45',2,2),(112,'2012-06-19 15:07:01',36,2),(113,'2012-06-19 15:07:35',37,2),(114,'2012-06-19 15:07:48',37,2),(115,'2012-06-19 15:07:49',36,2),(116,'2012-06-19 15:07:52',37,2),(117,'2012-06-19 15:07:56',38,2),(118,'2012-06-19 15:08:01',38,2),(119,'2012-06-19 15:08:04',37,2),(120,'2012-06-19 15:08:26',37,2),(121,'2012-06-19 15:08:40',38,2),(122,'2012-08-15 10:54:24',33,1),(123,'2012-08-15 10:54:37',33,1),(124,'2012-08-15 10:54:52',17,1),(125,'2012-08-15 10:54:57',38,1),(126,'2012-08-15 10:54:59',11,1),(127,'2012-08-15 10:55:02',34,1),(128,'2012-08-15 10:55:03',27,1),(129,'2012-08-15 10:55:14',10,1),(130,'2012-08-15 10:55:14',24,1),(131,'2012-08-15 10:55:15',30,1),(132,'2012-08-15 10:55:17',12,1),(133,'2012-08-15 10:55:18',16,1),(134,'2012-08-15 10:55:19',29,1),(135,'2012-08-15 10:55:29',3,1),(136,'2012-08-15 10:55:43',28,1),(137,'2012-08-15 10:56:07',28,1),(138,'2012-08-15 10:56:37',26,1),(139,'2012-08-15 11:02:24',26,1),(140,'2012-08-15 11:12:07',28,1),(141,'2012-08-15 11:20:00',28,1),(142,'2012-08-15 11:20:39',5,1),(143,'2012-08-15 11:20:42',5,1),(144,'2012-08-15 11:20:46',6,1),(145,'2012-08-15 11:20:49',6,1),(146,'2012-08-15 11:21:00',6,1),(147,'2012-08-15 11:24:30',26,1),(148,'2012-08-15 11:24:39',26,1),(149,'2012-08-15 11:24:51',25,1),(150,'2012-08-15 11:24:57',25,1),(151,'2012-08-15 11:26:20',36,1),(152,'2012-08-15 11:26:27',36,1),(153,'2012-08-15 11:26:35',25,1),(154,'2012-08-15 11:26:40',25,1),(155,'2012-08-15 11:26:59',25,1),(156,'2012-08-15 11:29:00',28,1),(157,'2012-08-15 11:29:20',28,1),(158,'2012-08-15 11:29:23',28,1),(159,'2012-08-15 11:29:34',28,1),(160,'2012-08-15 11:39:35',3,1),(161,'2012-08-15 11:39:44',25,1),(162,'2012-08-15 11:39:48',25,1),(163,'2012-08-15 11:40:13',25,1),(164,'2012-08-15 11:45:34',1,1),(165,'2012-08-15 11:45:38',25,1),(166,'2012-08-15 11:45:42',25,1),(167,'2012-08-15 12:39:40',13,1),(168,'2012-08-15 12:42:15',28,1),(169,'2012-08-15 12:42:23',28,1),(170,'2012-08-15 15:10:22',21,1),(171,'2012-08-15 15:13:40',21,1),(172,'2012-08-15 15:15:51',7,1),(173,'2012-08-15 15:15:58',12,1),(174,'2012-08-15 15:16:13',39,1),(175,'2012-08-15 15:18:44',18,1),(176,'2012-08-15 15:19:09',19,1),(177,'2012-08-15 15:19:13',20,1);
/*!40000 ALTER TABLE `story_access` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story_user`
--

DROP TABLE IF EXISTS `story_user`;
CREATE TABLE `story_user` (
  `Story_id` int(11) NOT NULL,
  `User_id` int(11) NOT NULL,
  PRIMARY KEY  (`Story_id`,`User_id`),
  KEY `FK5914FCD5E0E4BFA2` (`Story_id`),
  KEY `FK5914FCD5C1610AD2` (`User_id`),
  CONSTRAINT `FK5914FCD5C1610AD2` FOREIGN KEY (`User_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK5914FCD5E0E4BFA2` FOREIGN KEY (`Story_id`) REFERENCES `stories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `story_user`
--

LOCK TABLES `story_user` WRITE;
/*!40000 ALTER TABLE `story_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `story_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story_user_AUD`
--

DROP TABLE IF EXISTS `story_user_AUD`;
CREATE TABLE `story_user_AUD` (
  `REV` int(11) NOT NULL,
  `Story_id` int(11) NOT NULL,
  `User_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  PRIMARY KEY  (`REV`,`Story_id`,`User_id`),
  KEY `FK9A1CB82620258526` (`REV`),
  CONSTRAINT `FK9A1CB82620258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `story_user_AUD`
--

LOCK TABLES `story_user_AUD` WRITE;
/*!40000 ALTER TABLE `story_user_AUD` DISABLE KEYS */;
/*!40000 ALTER TABLE `story_user_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storyrank`
--

DROP TABLE IF EXISTS `storyrank`;
CREATE TABLE `storyrank` (
  `id` int(11) NOT NULL auto_increment,
  `rank` int(11) NOT NULL,
  `backlog_id` int(11) NOT NULL,
  `story_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `backlog_id` (`backlog_id`,`story_id`),
  KEY `FK6600C2A1E0E4BFA2` (`story_id`),
  KEY `FK6600C2A1F63400A2` (`backlog_id`),
  CONSTRAINT `FK6600C2A1F63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK6600C2A1E0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `storyrank`
--

LOCK TABLES `storyrank` WRITE;
/*!40000 ALTER TABLE `storyrank` DISABLE KEYS */;
INSERT INTO `storyrank` VALUES (9,0,2,15),(10,0,3,17),(11,1,2,16),(15,2,2,12),(16,6,7,10),(17,3,2,10),(18,1,3,11),(19,0,4,9),(21,4,2,4),(22,1,6,15),(23,2,7,16),(24,5,7,24),(25,5,2,24),(26,6,2,25),(27,2,6,25),(29,7,2,26),(30,0,7,26),(33,2,3,27),(34,0,6,28),(35,8,2,28),(36,1,7,29),(37,9,2,29),(38,4,7,30),(39,10,2,30),(40,3,7,12),(42,4,9,11),(43,3,9,17),(44,5,9,27),(45,2,9,33),(46,3,3,33),(47,6,9,34),(48,4,3,34),(49,5,3,35),(50,6,3,38),(51,7,3,37),(53,0,9,21),(54,1,9,38),(55,8,3,39),(56,7,9,39);
/*!40000 ALTER TABLE `storyrank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storyrank_AUD`
--

DROP TABLE IF EXISTS `storyrank_AUD`;
CREATE TABLE `storyrank_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  `rank` int(11) default NULL,
  `backlog_id` int(11) default NULL,
  `story_id` int(11) default NULL,
  PRIMARY KEY  (`id`,`REV`),
  KEY `FK1CD8B7F220258526` (`REV`),
  CONSTRAINT `FK1CD8B7F220258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `storyrank_AUD`
--

LOCK TABLES `storyrank_AUD` WRITE;
/*!40000 ALTER TABLE `storyrank_AUD` DISABLE KEYS */;
INSERT INTO `storyrank_AUD` VALUES (1,6,0,0,1,1),(1,7,1,1,1,1),(1,8,1,2,1,1),(1,12,1,3,1,1),(2,7,0,0,1,2),(2,8,1,1,1,2),(2,12,1,2,1,2),(3,8,0,0,1,3),(3,12,1,1,1,3),(4,12,0,0,1,5),(5,80,0,0,5,18),(5,81,1,1,5,18),(5,82,1,2,5,18),(5,84,1,3,5,18),(6,81,0,0,5,19),(6,82,1,1,5,19),(6,84,1,2,5,19),(7,82,0,0,5,20),(7,84,1,1,5,20),(8,84,0,0,5,21),(9,117,0,0,2,15),(9,126,1,1,2,15),(9,178,1,0,2,15),(10,118,0,0,3,17),(10,135,1,1,3,17),(10,136,1,0,3,17),(11,119,0,0,2,16),(11,125,1,4,2,16),(11,127,1,3,2,16),(11,128,1,4,2,16),(11,129,1,2,2,16),(11,178,1,1,2,16),(12,120,0,0,6,13),(12,177,2,0,6,13),(13,120,0,0,2,13),(13,125,1,3,2,13),(13,126,1,0,2,13),(13,178,2,0,2,13),(14,121,0,0,6,12),(14,205,2,0,6,12),(15,121,0,0,2,12),(15,125,1,2,2,12),(15,126,1,3,2,12),(15,127,1,4,2,12),(15,128,1,2,2,12),(15,129,1,3,2,12),(15,178,1,2,2,12),(16,122,0,0,7,10),(16,211,1,1,7,10),(16,212,1,2,7,10),(16,213,1,3,7,10),(16,215,1,6,7,10),(17,122,0,0,2,10),(17,125,1,1,2,10),(17,126,1,2,2,10),(17,128,1,3,2,10),(17,129,1,4,2,10),(17,178,1,3,2,10),(18,123,0,0,3,11),(18,130,1,1,3,11),(18,135,1,2,3,11),(18,137,1,1,3,11),(19,124,0,0,4,9),(20,135,0,0,3,22),(20,136,1,1,3,22),(20,137,1,2,3,22),(20,189,2,2,3,22),(21,148,0,5,2,4),(21,178,1,4,2,4),(22,169,0,0,6,15),(22,177,1,1,6,15),(22,205,1,0,6,15),(22,210,1,1,6,15),(23,170,0,0,7,16),(23,211,1,2,7,16),(23,212,1,0,7,16),(23,213,1,1,7,16),(23,216,1,2,7,16),(24,172,0,0,7,24),(24,211,1,3,7,24),(24,213,1,4,7,24),(24,215,1,3,7,24),(24,216,1,4,7,24),(24,217,1,5,7,24),(25,172,0,6,2,24),(25,178,1,5,2,24),(26,178,0,6,2,25),(27,180,0,2,6,25),(27,205,1,1,6,25),(27,210,1,2,6,25),(28,181,0,3,6,26),(28,184,2,3,6,26),(29,181,0,7,2,26),(30,184,0,0,7,26),(30,211,1,4,7,26),(30,213,1,0,7,26),(31,189,0,2,3,27),(31,190,2,2,3,27),(32,190,0,2,3,28),(32,193,2,2,3,28),(33,192,0,3,3,27),(33,193,1,2,3,27),(34,193,0,3,6,28),(34,205,1,2,6,28),(34,210,1,0,6,28),(35,193,0,8,2,28),(36,197,0,0,7,29),(36,211,1,5,7,29),(36,215,1,4,7,29),(36,216,1,1,7,29),(37,197,0,9,2,29),(38,201,0,0,7,30),(38,211,1,6,7,30),(38,215,1,5,7,30),(38,217,1,4,7,30),(39,201,0,10,2,30),(40,205,0,0,7,12),(40,212,1,1,7,12),(40,213,1,2,7,12),(40,216,1,3,7,12),(41,228,0,0,8,11),(41,231,2,0,8,11),(42,231,0,0,8,11),(42,258,1,1,8,11),(42,259,1,2,8,11),(42,260,1,3,8,11),(42,261,1,1,8,11),(42,263,1,2,8,11),(42,276,1,3,8,11),(42,294,1,2,8,11),(42,303,1,3,8,11),(42,311,1,4,9,11),(43,232,0,0,8,17),(43,260,1,1,8,17),(43,261,1,2,8,17),(43,262,1,3,8,17),(43,263,1,1,8,17),(43,303,1,2,8,17),(43,311,1,3,9,17),(44,233,0,0,8,27),(44,258,1,2,8,27),(44,259,1,1,8,27),(44,260,1,2,8,27),(44,261,1,3,8,27),(44,262,1,2,8,27),(44,263,1,3,8,27),(44,276,1,4,8,27),(44,294,1,3,8,27),(44,303,1,4,8,27),(44,311,1,5,9,27),(45,247,0,0,8,33),(45,258,1,3,8,33),(45,260,1,0,8,33),(45,303,1,1,8,33),(45,311,1,2,9,33),(46,247,0,3,3,33),(47,251,0,0,8,34),(47,258,1,4,8,34),(47,276,1,5,8,34),(47,294,1,4,8,34),(47,303,1,5,8,34),(47,311,1,6,9,34),(48,251,0,4,3,34),(49,253,0,5,3,35),(50,274,0,6,3,38),(51,274,0,7,3,37),(52,275,0,5,8,38),(52,276,1,2,8,38),(52,294,2,2,8,38),(53,292,0,0,9,21),(54,294,0,0,9,38),(54,311,1,1,9,38),(55,303,0,8,3,39),(56,303,0,0,8,39),(56,311,1,7,9,39);
/*!40000 ALTER TABLE `storyrank_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_user`
--

DROP TABLE IF EXISTS `task_user`;
CREATE TABLE `task_user` (
  `tasks_id` int(11) NOT NULL,
  `responsibles_id` int(11) NOT NULL,
  PRIMARY KEY  (`tasks_id`,`responsibles_id`),
  KEY `FKAC91A45B1C109E9` (`tasks_id`),
  KEY `FKAC91A4527F4B120` (`responsibles_id`),
  CONSTRAINT `FKAC91A4527F4B120` FOREIGN KEY (`responsibles_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKAC91A45B1C109E9` FOREIGN KEY (`tasks_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `task_user`
--

LOCK TABLES `task_user` WRITE;
/*!40000 ALTER TABLE `task_user` DISABLE KEYS */;
INSERT INTO `task_user` VALUES (1,2),(2,1);
/*!40000 ALTER TABLE `task_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_user_AUD`
--

DROP TABLE IF EXISTS `task_user_AUD`;
CREATE TABLE `task_user_AUD` (
  `REV` int(11) NOT NULL,
  `tasks_id` int(11) NOT NULL,
  `responsibles_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  PRIMARY KEY  (`REV`,`tasks_id`,`responsibles_id`),
  KEY `FKF0A49D9620258526` (`REV`),
  CONSTRAINT `FKF0A49D9620258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `task_user_AUD`
--

LOCK TABLES `task_user_AUD` WRITE;
/*!40000 ALTER TABLE `task_user_AUD` DISABLE KEYS */;
INSERT INTO `task_user_AUD` VALUES (156,1,1,0),(157,1,2,0),(158,1,2,0),(159,1,2,0),(160,1,2,0),(161,1,2,0),(162,2,1,0);
/*!40000 ALTER TABLE `task_user_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL auto_increment,
  `description` longtext,
  `effortleft` bigint(20) default NULL,
  `name` varchar(255) default NULL,
  `originalestimate` bigint(20) default NULL,
  `rank` int(11) NOT NULL default '0',
  `state` int(11) NOT NULL,
  `iteration_id` int(11) default NULL,
  `story_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK6907B8E4157D2A2` (`iteration_id`),
  KEY `FK6907B8EE0E4BFA2` (`story_id`),
  CONSTRAINT `FK6907B8EE0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`),
  CONSTRAINT `FK6907B8E4157D2A2` FOREIGN KEY (`iteration_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (1,NULL,0,'The online demo has been set up to run Agilefant 2.5.0',120,1,5,NULL,13),(2,NULL,180,'Some ~20 7-days / evaluator instances have been set up',600,2,1,NULL,13);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks_AUD`
--

DROP TABLE IF EXISTS `tasks_AUD`;
CREATE TABLE `tasks_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  `description` longtext,
  `effortleft` bigint(20) default NULL,
  `name` varchar(255) default NULL,
  `originalestimate` bigint(20) default NULL,
  `rank` int(11) default '0',
  `state` int(11) default NULL,
  `iteration_id` int(11) default NULL,
  `story_id` int(11) default NULL,
  PRIMARY KEY  (`id`,`REV`),
  KEY `FKBD8325F20258526` (`REV`),
  CONSTRAINT `FKBD8325F20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tasks_AUD`
--

LOCK TABLES `tasks_AUD` WRITE;
/*!40000 ALTER TABLE `tasks_AUD` DISABLE KEYS */;
INSERT INTO `tasks_AUD` VALUES (1,156,0,NULL,NULL,'The online demo has been set up to run Agilefant 2.5.0',NULL,1,4,NULL,13),(1,157,1,NULL,NULL,'The online demo has been set up to run Agilefant 2.5.0',NULL,1,4,NULL,13),(1,158,1,NULL,0,'The online demo has been set up to run Agilefant 2.5.0',NULL,1,5,NULL,13),(1,159,1,NULL,0,'The online demo has been set up to run Agilefant 2.5.0',0,1,4,NULL,13),(1,160,1,NULL,0,'The online demo has been set up to run Agilefant 2.5.0',120,1,4,NULL,13),(1,161,1,NULL,0,'The online demo has been set up to run Agilefant 2.5.0',120,1,5,NULL,13),(2,162,0,NULL,NULL,'Some ~20 7-days / evaluator instances have been set up',NULL,2,1,NULL,13),(2,163,1,NULL,600,'Some ~20 7-days / evaluator instances have been set up',600,2,1,NULL,13),(2,164,1,NULL,180,'Some ~20 7-days / evaluator instances have been set up',600,2,1,NULL,13);
/*!40000 ALTER TABLE `tasks_AUD` ENABLE KEYS */;


UNLOCK TABLES;

--
-- Table structure for table `team_iteration`
--

DROP TABLE IF EXISTS `team_iteration`;
CREATE TABLE `team_iteration` (
  `Team_id` int(11) NOT NULL,
  `Iteration_id` int(11) NOT NULL,
  KEY `FKF2269B7B4157D2A2` (`Iteration_id`),
  KEY `FKF2269B7B745BA992` (`Team_id`),
  CONSTRAINT `FKF2269B7B745BA992` FOREIGN KEY (`Team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FKF2269B7B4157D2A2` FOREIGN KEY (`Iteration_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `team_iteration`
--

LOCK TABLES `team_iteration` WRITE;
/*!40000 ALTER TABLE `team_iteration` DISABLE KEYS */;
INSERT INTO `team_iteration` VALUES (1,9);
/*!40000 ALTER TABLE `team_iteration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_iteration_AUD`
--

DROP TABLE IF EXISTS `team_iteration_AUD`;
CREATE TABLE `team_iteration_AUD` (
  `REV` int(11) NOT NULL,
  `Team_id` int(11) NOT NULL,
  `Iteration_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  PRIMARY KEY  (`REV`,`Team_id`,`Iteration_id`),
  KEY `FKFE5293CC20258526` (`REV`),
  CONSTRAINT `FKFE5293CC20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `team_iteration_AUD`
--

LOCK TABLES `team_iteration_AUD` WRITE;
/*!40000 ALTER TABLE `team_iteration_AUD` DISABLE KEYS */;
/*!40000 ALTER TABLE `team_iteration_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_product`
--

DROP TABLE IF EXISTS `team_product`;
CREATE TABLE `team_product` (
  `Team_id` int(11) NOT NULL,
  `Product_id` int(11) NOT NULL,
  KEY `FK65CE090D745BA992` (`Team_id`),
  KEY `FK65CE090DA7FE2362` (`Product_id`),
  CONSTRAINT `FK65CE090DA7FE2362` FOREIGN KEY (`Product_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK65CE090D745BA992` FOREIGN KEY (`Team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `team_product`
--

LOCK TABLES `team_product` WRITE;
/*!40000 ALTER TABLE `team_product` DISABLE KEYS */;
INSERT INTO `team_product` VALUES (1,5),(1,1);
/*!40000 ALTER TABLE `team_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_product_AUD`
--

DROP TABLE IF EXISTS `team_product_AUD`;
CREATE TABLE `team_product_AUD` (
  `REV` int(11) NOT NULL,
  `Team_id` int(11) NOT NULL,
  `Product_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  PRIMARY KEY  (`REV`,`Team_id`,`Product_id`),
  KEY `FK4E84E85E20258526` (`REV`),
  CONSTRAINT `FK4E84E85E20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `team_product_AUD`
--

LOCK TABLES `team_product_AUD` WRITE;
/*!40000 ALTER TABLE `team_product_AUD` DISABLE KEYS */;
INSERT INTO `team_product_AUD` VALUES (277,1,1,0),(277,1,5,0);
/*!40000 ALTER TABLE `team_product_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_user`
--

DROP TABLE IF EXISTS `team_user`;
CREATE TABLE `team_user` (
  `User_id` int(11) NOT NULL,
  `Team_id` int(11) NOT NULL,
  KEY `FKF587546DC1610AD2` (`User_id`),
  KEY `FKF587546D745BA992` (`Team_id`),
  CONSTRAINT `FKF587546D745BA992` FOREIGN KEY (`Team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FKF587546DC1610AD2` FOREIGN KEY (`User_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `team_user`
--

LOCK TABLES `team_user` WRITE;
/*!40000 ALTER TABLE `team_user` DISABLE KEYS */;
INSERT INTO `team_user` VALUES (1,1),(2,1);
/*!40000 ALTER TABLE `team_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_user_AUD`
--

DROP TABLE IF EXISTS `team_user_AUD`;
CREATE TABLE `team_user_AUD` (
  `REV` int(11) NOT NULL,
  `Team_id` int(11) NOT NULL,
  `User_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  PRIMARY KEY  (`REV`,`Team_id`,`User_id`),
  KEY `FK7FE983BE20258526` (`REV`),
  CONSTRAINT `FK7FE983BE20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `team_user_AUD`
--

LOCK TABLES `team_user_AUD` WRITE;
/*!40000 ALTER TABLE `team_user_AUD` DISABLE KEYS */;
INSERT INTO `team_user_AUD` VALUES (277,1,1,0);
/*!40000 ALTER TABLE `team_user_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams` (
  `id` int(11) NOT NULL auto_increment,
  `description` longtext,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (1,NULL,'Admin team');
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams_AUD`
--

DROP TABLE IF EXISTS `teams_AUD`;
CREATE TABLE `teams_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  `description` longtext,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`id`,`REV`),
  KEY `FKF6966C8720258526` (`REV`),
  CONSTRAINT `FKF6966C8720258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `teams_AUD`
--

LOCK TABLES `teams_AUD` WRITE;
/*!40000 ALTER TABLE `teams_AUD` DISABLE KEYS */;
INSERT INTO `teams_AUD` VALUES (1,277,0,NULL,'Admin team');
/*!40000 ALTER TABLE `teams_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL auto_increment,
  `admin` bit(1) default '',
  `autoassignToStories` bit(1) default '\0',
  `autoassignToTasks` bit(1) default '',
  `email` varchar(255) default NULL,
  `enabled` bit(1) NOT NULL,
  `fullName` varchar(255) default NULL,
  `initials` varchar(255) default NULL,
  `loginName` varchar(255) default NULL,
  `markStoryStarted` int(11) default '1',
  `password` varchar(255) default NULL,
  `recentItemsNumberOfWeeks` int(11) NOT NULL,
  `weekEffort` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `loginName` (`loginName`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'','\0','',NULL,'','Administrator','Admin','admin',1,'5ebe2294ecd0e0f08eab7690d2a6ee69',16,NULL),(2,'','','','jarno@agilefant.org','','Jarno Vähäniitty (you may want to disable this user, as well as change the admin user\'s password!!!)','Jarno','jvahanii',1,'418a358a37b34ce4afe16893f4547330',4,2235),(3,'\0','\0','',NULL,'','readonly',NULL,'readonly',1,'5ebe2294ecd0e0f08eab7690d2a6ee69',0,NULL),(4,'\0','','','nonadmin@gmail.com','','Non-admin user that has access to nothing (password: nonadmin - log in with this user and see what happens)','NonAdmin','nonadmin',1,'9b4a061e33aceff57eee1429404cf716',16,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_AUD`
--

DROP TABLE IF EXISTS `users_AUD`;
CREATE TABLE `users_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) default NULL,
  `email` varchar(255) default NULL,
  `enabled` bit(1) default NULL,
  `fullName` varchar(255) default NULL,
  `initials` varchar(255) default NULL,
  `loginName` varchar(255) default NULL,
  `weekEffort` bigint(20) default NULL,
  PRIMARY KEY  (`id`,`REV`),
  KEY `FK154C77D920258526` (`REV`),
  CONSTRAINT `FK154C77D920258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users_AUD`
--

LOCK TABLES `users_AUD` WRITE;
/*!40000 ALTER TABLE `users_AUD` DISABLE KEYS */;
INSERT INTO `users_AUD` VALUES (2,1,0,'jarno@agilefant.org','','Jarno Vähäniitty','Jarno','jvahanii',0),(2,219,1,'jarno@agilefant.org','','Agilefant Dev Team','Jarno','jvahanii',0),(2,220,1,'team@agilefant.org','','Agilefant Dev Team','Jarno','jvahanii',0),(2,221,1,'team@agilefant.org','','Agilefant Dev Team','Jarno','team',0),(2,222,1,'team@agilefant.org','','Agilefant Dev Team','A-team','team',0),(2,283,1,'team@agilefant.org','','Jarno Vähäniitty','A-team','team',0),(2,284,1,'team@agilefant.org','','Jarno Vähäniitty','A-team','Jarno',0),(2,285,1,'team@agilefant.org','','Jarno Vähäniitty','Jarno','Jarno',0),(2,286,1,'jarno@agilefant.org','','Jarno Vähäniitty','Jarno','jvahanii',0),(2,287,1,'jarno@agilefant.org','','Jarno Vähäniitty','Jarno','jvahanii',2235),(2,297,1,'jarno@agilefant.org','','Jarno Vähäniitty (you may want to disable this user)','Jarno','jvahanii',2235),(2,298,1,'jarno@agilefant.org','','Jarno Vähäniitty (you may want to disable this user, as well as change the admin user\'s password!!!)','Jarno','jvahanii',2235),(4,314,0,'nonadmin@gmail.com','','Non-admin user that has access to nothing (password: nonadmin - log in with this user and see what happens)','NonAdmin','nonadmin',0);
/*!40000 ALTER TABLE `users_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whatsnextentry`
--

DROP TABLE IF EXISTS `whatsnextentry`;
CREATE TABLE `whatsnextentry` (
  `id` int(11) NOT NULL auto_increment,
  `rank` int(11) NOT NULL default '0',
  `task_id` int(11) default NULL,
  `user_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `task_id` (`task_id`,`user_id`),
  KEY `FK4B213410C1610AD2` (`user_id`),
  KEY `FK4B2134106E84F892` (`task_id`),
  CONSTRAINT `FK4B2134106E84F892` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`),
  CONSTRAINT `FK4B213410C1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `whatsnextentry`
--

LOCK TABLES `whatsnextentry` WRITE;
/*!40000 ALTER TABLE `whatsnextentry` DISABLE KEYS */;
/*!40000 ALTER TABLE `whatsnextentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgetcollections`
--

DROP TABLE IF EXISTS `widgetcollections`;
CREATE TABLE `widgetcollections` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `user_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK26C78D1C1610AD2` (`user_id`),
  CONSTRAINT `FK26C78D1C1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `widgetcollections`
--

LOCK TABLES `widgetcollections` WRITE;
/*!40000 ALTER TABLE `widgetcollections` DISABLE KEYS */;
/*!40000 ALTER TABLE `widgetcollections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgets`
--

DROP TABLE IF EXISTS `widgets`;
CREATE TABLE `widgets` (
  `id` int(11) NOT NULL auto_increment,
  `listNumber` int(11) default NULL,
  `objectId` int(11) default NULL,
  `position` int(11) default NULL,
  `type` varchar(255) NOT NULL,
  `widgetCollection_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK4FE3EEAF8BACA792` (`widgetCollection_id`),
  CONSTRAINT `FK4FE3EEAF8BACA792` FOREIGN KEY (`widgetCollection_id`) REFERENCES `widgetcollections` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `widgets`
--

LOCK TABLES `widgets` WRITE;
/*!40000 ALTER TABLE `widgets` DISABLE KEYS */;
/*!40000 ALTER TABLE `widgets` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-08-15 12:24:44
