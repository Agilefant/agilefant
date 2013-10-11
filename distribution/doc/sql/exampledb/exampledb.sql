-- MySQL dump 10.13  Distrib 5.5.29, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: exampledbtest
-- ------------------------------------------------------
-- Server version	5.5.29-0ubuntu0.12.04.1

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
-- Table structure for table `Holiday`
--

DROP TABLE IF EXISTS `Holiday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Holiday` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `endDate` datetime DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA44DC038C1610AD2` (`user_id`),
  CONSTRAINT `FKA44DC038C1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HolidayAnomaly` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK267567ABC1610AD2` (`user_id`),
  CONSTRAINT `FK267567ABC1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HolidayAnomaly`
--

LOCK TABLES `HolidayAnomaly` WRITE;
/*!40000 ALTER TABLE `HolidayAnomaly` DISABLE KEYS */;
/*!40000 ALTER TABLE `HolidayAnomaly` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agilefant_revisions`
--

DROP TABLE IF EXISTS `agilefant_revisions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agilefant_revisions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` bigint(20) NOT NULL,
  `userId` int(11) NOT NULL,
  `userName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agilefant_revisions`
--

LOCK TABLES `agilefant_revisions` WRITE;
/*!40000 ALTER TABLE `agilefant_revisions` DISABLE KEYS */;
INSERT INTO `agilefant_revisions` VALUES (12,1369925667265,3,'Administrator'),(13,1369925825692,3,'Administrator'),(14,1369925853767,3,'Administrator original'),(15,1369925865431,3,'Administrator original'),(16,1369990668961,5,'Administrator'),(17,1369990813264,5,'Administrator'),(18,1369990855207,5,'Administrator'),(19,1369990855458,5,'Administrator'),(20,1369990893520,5,'Administrator'),(21,1369990899423,5,'Administrator'),(22,1369990917113,5,'Administrator'),(23,1369990930736,5,'Administrator'),(24,1369990935625,5,'Administrator'),(25,1369990953443,5,'Administrator'),(26,1369990989675,5,'Administrator'),(27,1369990994132,5,'Administrator'),(28,1369990997631,5,'Administrator'),(29,1369991000455,5,'Administrator'),(30,1369991005822,5,'Administrator'),(31,1369991011993,5,'Administrator'),(32,1369991034569,5,'Administrator'),(33,1369991045739,5,'Administrator'),(34,1369991064753,5,'Administrator'),(35,1369991081881,5,'Administrator'),(36,1369991109030,5,'Administrator'),(37,1369991121957,5,'Administrator'),(38,1369991125864,5,'Administrator'),(39,1369991134700,5,'Administrator'),(40,1369991140951,5,'Administrator'),(41,1369991159886,5,'Administrator'),(42,1369991165423,5,'Administrator'),(43,1369991184551,5,'Administrator'),(44,1369991301620,5,'Administrator'),(45,1369991357374,5,'Administrator'),(46,1370000406600,5,'Administrator'),(47,1370000426575,5,'Administrator'),(48,1370000444135,5,'Administrator'),(49,1370000447030,5,'Administrator'),(50,1370000450030,5,'Administrator'),(51,1370000468000,5,'Administrator'),(52,1370000483192,5,'Administrator'),(53,1370000487943,5,'Administrator'),(54,1370000499653,5,'Administrator'),(55,1370000504524,5,'Administrator'),(56,1370001480806,5,'Administrator'),(57,1370001505882,5,'Administrator'),(58,1370001521063,5,'Administrator'),(59,1370001628246,5,'Administrator'),(60,1370001668144,5,'Administrator'),(61,1370001906079,5,'Administrator'),(62,1370001946817,5,'Administrator'),(63,1370001954594,5,'Administrator'),(64,1370002032868,5,'Administrator'),(65,1370092236585,5,'Administrator'),(66,1370092238286,5,'Administrator'),(67,1370092239484,5,'Administrator'),(68,1370092243446,5,'Administrator'),(69,1370092245180,5,'Administrator'),(70,1371118207470,5,'Administrator'),(71,1371118233377,5,'Administrator'),(72,1371118257928,5,'Administrator'),(73,1371118385347,5,'Administrator');
/*!40000 ALTER TABLE `agilefant_revisions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignment`
--

DROP TABLE IF EXISTS `assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `availability` int(11) NOT NULL,
  `delta_personal_load` bigint(20) DEFAULT NULL,
  `backlog_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3D2B86CDF8762ABE` (`backlog_id`),
  KEY `FK3D2B86CDE537EC82` (`backlog_id`),
  KEY `FK3D2B86CDC1610AD2` (`user_id`),
  KEY `FK3D2B86CDF63400A2` (`backlog_id`),
  CONSTRAINT `FK3D2B86CDC1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK3D2B86CDE537EC82` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK3D2B86CDF63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK3D2B86CDF8762ABE` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignment`
--

LOCK TABLES `assignment` WRITE;
/*!40000 ALTER TABLE `assignment` DISABLE KEYS */;
INSERT INTO `assignment` VALUES (1,100,0,7,6),(2,100,0,8,6),(3,100,0,9,6),(4,100,0,13,6),(5,100,0,15,6),(6,100,0,16,6),(7,100,0,14,5),(8,100,0,14,6),(9,100,0,17,6),(10,100,0,10,6),(11,100,0,10,5),(12,100,0,18,6),(13,100,0,19,6),(14,100,0,20,6),(15,100,0,21,6),(16,100,0,21,5);
/*!40000 ALTER TABLE `assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignment_AUD`
--

DROP TABLE IF EXISTS `assignment_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assignment_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `availability` int(11) DEFAULT NULL,
  `delta_personal_load` bigint(20) DEFAULT NULL,
  `backlog_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK1B6D861E20258526` (`REV`),
  CONSTRAINT `FK1B6D861E20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignment_AUD`
--

LOCK TABLES `assignment_AUD` WRITE;
/*!40000 ALTER TABLE `assignment_AUD` DISABLE KEYS */;
INSERT INTO `assignment_AUD` VALUES (1,12,0,100,0,7,6),(2,12,0,100,0,8,6),(3,12,0,100,0,9,6),(4,12,0,100,0,13,6),(5,12,0,100,0,15,6),(6,12,0,100,0,16,6),(7,12,0,100,0,14,5),(8,12,0,100,0,14,6),(9,12,0,100,0,17,6),(10,12,0,100,0,10,6),(11,12,0,100,0,10,5),(12,12,0,100,0,18,6),(13,12,0,100,0,19,6),(14,12,0,100,0,20,6),(15,17,0,100,0,21,6),(16,17,0,100,0,21,5);
/*!40000 ALTER TABLE `assignment_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backlogs`
--

DROP TABLE IF EXISTS `backlogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `backlogs` (
  `backlogtype` varchar(31) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `backlogSize` bigint(20) DEFAULT NULL,
  `baselineLoad` bigint(20) DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `readonlyToken` varchar(255) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `readonlyToken` (`readonlyToken`),
  KEY `FK7E75FF16B8A85DD5` (`parent_id`),
  CONSTRAINT `FK7E75FF16B8A85DD5` FOREIGN KEY (`parent_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backlogs`
--

LOCK TABLES `backlogs` WRITE;
/*!40000 ALTER TABLE `backlogs` DISABLE KEYS */;
INSERT INTO `backlogs` VALUES ('Product',5,'<div><b>Welcome, dear evaluator!</b></div><div><br></div>This example product is (a heavily pruned version of) Agilefant\'s actual roadmap, it is also a simple example of how you might want to plan and work on a major product - or a business using Agilefant. It illustrates hierarchical stories (=goals), and how the steps for advancing them have been refined into iteration-sized chunks.&nbsp;<div><br>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>In your activities, use the degree of accuracy that fits you: right tool for the right job!</div><div><br></div>You can find Agilefant\'s full <b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">product vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u><br>+ Agilefant\'s built-in help page (see top right corner!)<br>+ A <a href=\"http://agilefant.freeforums.org/post1637.html\">list of all topics</a> in the forum\'s User Guide section - this is probably the easiest place to find your answer!<br></div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a><br><br><b>Also, do not miss <a href=\"http://agilefant.freeforums.org/how-to-enable-the-keyboard-shortcuts-t346.html\">Agilefant\'s Jedi Keyboard Shortcuts</a>.</b> Give you unparalleled speed in keeping your backlogs up-to-date, they do!<br></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',6,'Creating a bunch of stories in the story tree view is the simplest way to create a to-do list.&nbsp;<div><br></div><div>With Agilefant you can create such - as well plan full-fledged business, product and development efforts (click on the \'product\' Agilefant in the left hand tree)</div>','ToDo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Project',7,'','2012 Spring',0,0,'2012-05-31 18:00:00',NULL,'2012-04-10 08:00:00',0,0,5),('Project',8,'','2012 Summer',0,NULL,'2012-08-31 18:00:00',NULL,'2012-06-01 08:00:00',0,4,5),('Project',9,'','2012 Fall',0,0,'2012-12-31 18:00:00',NULL,'2012-09-01 08:00:00',0,1,5),('Project',10,'','2013 Q1-Q3 TUTL',0,0,'2013-09-27 18:00:00',NULL,'2013-01-06 08:00:00',0,0,5),('Iteration',11,NULL,'April',0,0,'2012-04-30 18:00:00',NULL,'2012-04-10 08:00:00',NULL,NULL,7),('Iteration',12,NULL,'May',0,0,'2012-05-31 18:00:00',NULL,'2012-05-01 08:00:00',NULL,NULL,7),('Iteration',13,NULL,'June-Aug',0,0,'2012-08-15 23:59:00',NULL,'2012-06-01 08:00:00',NULL,NULL,8),('Iteration',14,'This is an example standalone iteration. Standalone iterations can contain stories from many products.','A standalone iteration',NULL,NULL,'2020-09-27 15:09:00',NULL,'2012-08-15 15:09:00',NULL,NULL,NULL),('Iteration',15,NULL,'Sep-Oct',0,0,'2012-11-02 18:00:00',NULL,'2012-09-02 08:00:00',NULL,NULL,9),('Iteration',16,NULL,'Nov-Dec',0,0,'2012-12-31 18:00:00',NULL,'2012-11-04 08:00:00',NULL,NULL,9),('Iteration',17,NULL,'January',0,0,'2013-01-31 18:00:00',NULL,'2013-01-01 08:00:00',NULL,NULL,10),('Iteration',18,NULL,'Feb',0,0,'2013-02-28 18:00:00',NULL,'2013-02-01 08:00:00',NULL,NULL,10),('Iteration',19,NULL,'March',0,0,'2013-03-31 18:00:00',NULL,'2013-03-01 08:00:00',NULL,NULL,10),('Iteration',20,NULL,'April',0,0,'2013-04-30 18:00:00',NULL,'2013-04-01 08:00:00',NULL,NULL,10),('Iteration',21,NULL,'May-June-July',0,0,'2013-07-31 18:00:00',NULL,'2013-05-01 08:00:00',NULL,NULL,10);
/*!40000 ALTER TABLE `backlogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backlogs_AUD`
--

DROP TABLE IF EXISTS `backlogs_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `backlogs_AUD` (
  `backlogtype` varchar(31) NOT NULL,
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `backlogSize` bigint(20) DEFAULT NULL,
  `baselineLoad` bigint(20) DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK46C0B1E720258526` (`REV`),
  CONSTRAINT `FK46C0B1E720258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backlogs_AUD`
--

LOCK TABLES `backlogs_AUD` WRITE;
/*!40000 ALTER TABLE `backlogs_AUD` DISABLE KEYS */;
INSERT INTO `backlogs_AUD` VALUES ('Product',5,12,0,'While this product is (a pruned version of) Agilefant\'s actual roadmap, it is also a simple example of how you might want to plan and work on a major product - or a business using Agilefant. Illustrating how hierarchical goals, and how the steps for advancing them has been refined into iteration-sized chunks.&nbsp;<div>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>In your activities, use the degree of accuracy that fits you: right tool for the right job!</div><div><br></div>You can find Agilefant\'s full <b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">product vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u><br>+ Agilefant\'s built-in help page (see top right corner!)<br>+ A <a href=\"http://agilefant.freeforums.org/post1637.html\">list of all topics</a> in the forum\'s User Guide section - this is probably the easiest place to find your answer!<br></div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a><br><br><b>Also, do not miss <a href=\"http://agilefant.freeforums.org/how-to-enable-the-keyboard-shortcuts-t346.html\">Agilefant\'s Jedi Keyboard Shortcuts</a>.</b> Give you unparalleled speed in keeping your backlogs up-to-date, they do!<br></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,61,1,'This product is (a heavily pruned version of) Agilefant\'s actual roadmap, it is also a simple example of how you might want to plan and work on a major product - or a business using Agilefant. It illustrates hierarchical stories (=goals), and how the steps for advancing them have been refined into iteration-sized chunks.&nbsp;<div><br>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>In your activities, use the degree of accuracy that fits you: right tool for the right job!</div><div><br></div>You can find Agilefant\'s full <b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">product vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u><br>+ Agilefant\'s built-in help page (see top right corner!)<br>+ A <a href=\"http://agilefant.freeforums.org/post1637.html\">list of all topics</a> in the forum\'s User Guide section - this is probably the easiest place to find your answer!<br></div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a><br><br><b>Also, do not miss <a href=\"http://agilefant.freeforums.org/how-to-enable-the-keyboard-shortcuts-t346.html\">Agilefant\'s Jedi Keyboard Shortcuts</a>.</b> Give you unparalleled speed in keeping your backlogs up-to-date, they do!<br></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,62,1,'<div><b>Welcome, dear evaluator!</b></div><div><br></div>This product is (a heavily pruned version of) Agilefant\'s actual roadmap, it is also a simple example of how you might want to plan and work on a major product - or a business using Agilefant. It illustrates hierarchical stories (=goals), and how the steps for advancing them have been refined into iteration-sized chunks.&nbsp;<div><br>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>In your activities, use the degree of accuracy that fits you: right tool for the right job!</div><div><br></div>You can find Agilefant\'s full <b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">product vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u><br>+ Agilefant\'s built-in help page (see top right corner!)<br>+ A <a href=\"http://agilefant.freeforums.org/post1637.html\">list of all topics</a> in the forum\'s User Guide section - this is probably the easiest place to find your answer!<br></div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a><br><br><b>Also, do not miss <a href=\"http://agilefant.freeforums.org/how-to-enable-the-keyboard-shortcuts-t346.html\">Agilefant\'s Jedi Keyboard Shortcuts</a>.</b> Give you unparalleled speed in keeping your backlogs up-to-date, they do!<br></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',5,63,1,'<div><b>Welcome, dear evaluator!</b></div><div><br></div>This example product is (a heavily pruned version of) Agilefant\'s actual roadmap, it is also a simple example of how you might want to plan and work on a major product - or a business using Agilefant. It illustrates hierarchical stories (=goals), and how the steps for advancing them have been refined into iteration-sized chunks.&nbsp;<div><br>But, Agilefant scales down into simple <a href=\"http://cloud.agilefant.org/evaluator4/editProduct.action?productId=5\">to-do</a> -lists as well.&nbsp;</div><div><br></div><div>In your activities, use the degree of accuracy that fits you: right tool for the right job!</div><div><br></div>You can find Agilefant\'s full <b><u><a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">product vision online at Agilefant.org</a></u></b><div>(that\'s&nbsp;<a href=\"http://www.agilefant.org/wiki/display/AEF/Vision\">http://www.agilefant.org/wiki/display/AEF/Vision</a>)</div><div><br></div><div><u>For more info on how to use Agilefant, the ideas behind it, and the criteria for selecting a backlog management tool, check out</u><br>+ Agilefant\'s built-in help page (see top right corner!)<br>+ A <a href=\"http://agilefant.freeforums.org/post1637.html\">list of all topics</a> in the forum\'s User Guide section - this is probably the easiest place to find your answer!<br></div><div>+ <a href=\"http://www.soberit.hut.fi/~jvahanii/\">Jarno</a>\'s Ph.D.: \"<a href=\"http://lib.tkk.fi/Diss/2012/isbn9789526045061/isbn9789526045061.pdf\">Towards Agile Product and Portfolio Management</a>\"</div><div>+ A <a href=\"http://www.soberit.hut.fi/sprg/projects/atman/TowardsAgileProductandPortfolioManagement.pdf\">practitioner-oriented book</a> written by Jarno and his colleagues by the same title</div><div>+ A short <a href=\"http://www.methodsandtools.com/tools/tools.php?agilefant\">online guide</a> describing how to use Agilefant</div><div>+ Agilefant.org\'s <a href=\"http://www.agilefant.org/wiki/display/AEF/Agilefant+knowledge+hub\">knowledge hub</a><br><br><b>Also, do not miss <a href=\"http://agilefant.freeforums.org/how-to-enable-the-keyboard-shortcuts-t346.html\">Agilefant\'s Jedi Keyboard Shortcuts</a>.</b> Give you unparalleled speed in keeping your backlogs up-to-date, they do!<br></div>','Agilefant',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Product',6,12,0,'Creating a bunch of stories in the story tree view is the simplest way to create a to-do list.&nbsp;<div><br></div><div>With Agilefant you can create such - as well plan full-fledged business, product and development efforts (click on the \'product\' Agilefant in the left hand tree)</div>','ToDo-list',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('Project',7,12,0,'','2012 Spring',5,0,0,'2012-05-31 18:00:00',0,'2012-04-10 08:00:00',0),('Project',8,12,0,'','2012 Summer',5,0,NULL,'2012-08-31 18:00:00',0,'2012-06-01 08:00:00',4),('Project',9,12,0,'','2012 Fall',5,0,0,'2012-12-31 18:00:00',0,'2012-09-01 08:00:00',1),('Project',10,12,0,'','2013 Q1-Q3 TUTL',5,0,0,'2013-09-27 18:00:00',0,'2013-01-06 08:00:00',0),('Iteration',11,12,0,NULL,'April',7,0,0,'2012-04-30 18:00:00',NULL,'2012-04-10 08:00:00',NULL),('Iteration',12,12,0,NULL,'May',7,0,0,'2012-05-31 18:00:00',NULL,'2012-05-01 08:00:00',NULL),('Iteration',13,12,0,NULL,'June-Aug',8,0,0,'2012-08-15 23:59:00',NULL,'2012-06-01 08:00:00',NULL),('Iteration',14,12,0,'This is an example standalone iteration. Standalone iterations can contain stories from many products.','A standalone iteration',NULL,NULL,NULL,'2020-09-27 15:09:00',NULL,'2012-08-15 15:09:00',NULL),('Iteration',15,12,0,NULL,'Sep-Oct',9,0,0,'2012-11-02 18:00:00',NULL,'2012-09-02 08:00:00',NULL),('Iteration',16,12,0,NULL,'Nov-Dec',9,0,0,'2012-12-31 18:00:00',NULL,'2012-11-04 08:00:00',NULL),('Iteration',17,12,0,NULL,'January',10,0,0,'2013-01-31 18:00:00',NULL,'2013-01-01 08:00:00',NULL),('Iteration',18,12,0,NULL,'Feb',10,0,0,'2013-02-28 18:00:00',NULL,'2013-02-01 08:00:00',NULL),('Iteration',19,12,0,NULL,'March',10,0,0,'2013-03-31 18:00:00',NULL,'2013-03-01 08:00:00',NULL),('Iteration',20,12,0,NULL,'April',10,0,0,'2013-04-30 18:00:00',NULL,'2013-04-01 08:00:00',NULL),('Iteration',21,17,0,NULL,'May-June-July',10,0,0,'2013-07-31 18:00:00',NULL,'2013-05-01 08:00:00',NULL);
/*!40000 ALTER TABLE `backlogs_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history_backlogs`
--

DROP TABLE IF EXISTS `history_backlogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history_backlogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branchMax` bigint(20) NOT NULL,
  `doneSum` bigint(20) NOT NULL,
  `estimateSum` bigint(20) NOT NULL,
  `rootSum` bigint(20) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
  `backlog_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA6B13261F8762ABE` (`backlog_id`),
  CONSTRAINT `FKA6B13261F8762ABE` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history_backlogs`
--

LOCK TABLES `history_backlogs` WRITE;
/*!40000 ALTER TABLE `history_backlogs` DISABLE KEYS */;
INSERT INTO `history_backlogs` VALUES (2,0,0,0,0,'2012-04-10 23:44:46',7),(3,0,0,0,0,'2012-04-10 23:42:37',8),(4,0,0,0,0,'2012-04-10 23:43:21',9),(5,0,0,0,0,'2012-05-14 09:44:14',7),(6,0,0,0,0,'2012-05-14 09:40:20',8),(7,0,0,0,0,'2012-06-19 15:08:45',8),(8,0,0,0,0,'2012-06-19 15:04:16',7),(9,0,0,0,0,'2012-08-15 15:16:20',8),(10,0,0,0,0,'2012-08-15 15:16:00',7),(11,0,0,0,0,'2012-10-18 15:37:11',8),(12,0,0,0,0,'2012-10-18 15:44:12',7),(13,0,0,0,0,'2012-10-18 15:48:29',9),(14,0,0,0,0,'2012-10-18 15:47:08',10),(15,0,0,0,0,'2012-10-26 09:31:40',9),(16,0,0,0,0,'2012-10-26 09:32:22',8),(17,0,0,0,0,'2012-11-07 23:42:32',8),(18,0,0,0,0,'2012-11-07 23:48:29',9),(19,0,0,0,0,'2012-12-04 11:09:52',9),(20,0,0,0,0,'2012-12-21 11:33:16',10),(21,0,0,0,0,'2012-12-21 11:42:38',9),(22,0,0,0,0,'2012-12-21 11:41:25',8),(23,0,0,0,0,'2013-01-31 10:39:10',9),(24,0,0,0,0,'2013-01-31 10:41:42',10),(25,14,2,14,14,'2013-03-08 12:46:31',10),(26,13,11,13,13,'2013-03-08 12:38:20',9),(27,5,5,5,5,'2013-03-08 12:36:29',7),(28,14,14,14,14,'2013-04-17 16:16:56',10),(29,13,11,13,13,'2013-04-17 16:16:49',9),(30,30,14,30,14,'2013-05-31 12:06:24',10),(31,35,14,35,19,'2013-05-31 15:07:12',10),(32,35,14,35,19,'2013-06-13 13:13:05',10);
/*!40000 ALTER TABLE `history_backlogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history_iterations`
--

DROP TABLE IF EXISTS `history_iterations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history_iterations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deltaOriginalEstimate` bigint(20) NOT NULL,
  `effortLeftSum` bigint(20) NOT NULL,
  `originalEstimateSum` bigint(20) NOT NULL,
  `timestamp` date NOT NULL,
  `iteration_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `iteration_id` (`iteration_id`,`timestamp`),
  KEY `FK9DD479C14157D2A2` (`iteration_id`),
  CONSTRAINT `FK9DD479C14157D2A2` FOREIGN KEY (`iteration_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history_iterations`
--

LOCK TABLES `history_iterations` WRITE;
/*!40000 ALTER TABLE `history_iterations` DISABLE KEYS */;
INSERT INTO `history_iterations` VALUES (2,0,0,0,'2012-04-10',11),(3,720,180,720,'2012-04-12',11),(4,-720,0,0,'2012-05-14',11),(5,0,0,0,'2012-10-18',11),(6,0,0,0,'2012-04-10',12),(7,0,0,0,'2012-05-14',12),(8,0,0,0,'2012-06-19',12),(9,0,0,0,'2012-10-18',12),(10,0,0,0,'2012-06-19',13),(11,0,0,0,'2012-08-15',13),(12,0,0,0,'2012-11-07',13),(13,0,0,0,'2012-08-15',14),(14,0,0,0,'2012-10-18',14),(15,0,0,0,'2012-10-26',14),(16,600,600,600,'2012-12-21',14),(17,0,0,600,'2013-03-08',14),(18,0,0,0,'2012-10-18',15),(19,0,0,0,'2012-10-26',15),(20,0,0,0,'2012-11-07',15),(21,0,0,0,'2012-10-18',16),(22,0,0,0,'2012-10-26',16),(23,0,0,0,'2012-11-07',16),(24,0,0,0,'2012-12-04',16),(25,0,0,0,'2012-12-21',16),(26,0,0,0,'2013-01-31',17),(27,0,0,0,'2013-03-08',18),(28,0,0,0,'2013-03-08',19),(29,0,0,0,'2013-04-17',20),(30,0,0,0,'2013-05-31',21),(31,0,0,600,'2013-05-31',14);
/*!40000 ALTER TABLE `history_iterations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hourentries`
--

DROP TABLE IF EXISTS `hourentries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hourentries` (
  `DTYPE` varchar(31) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `description` longtext,
  `minutesSpent` bigint(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `backlog_id` int(11) DEFAULT NULL,
  `story_id` int(11) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF6FE62CE0E4BFA2` (`story_id`),
  KEY `FKF6FE62CF63400A2` (`backlog_id`),
  KEY `FKF6FE62CC1610AD2` (`user_id`),
  KEY `FKF6FE62C6E84F892` (`task_id`),
  CONSTRAINT `FKF6FE62C6E84F892` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`),
  CONSTRAINT `FKF6FE62CC1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKF6FE62CE0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`),
  CONSTRAINT `FKF6FE62CF63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hourentries`
--

LOCK TABLES `hourentries` WRITE;
/*!40000 ALTER TABLE `hourentries` DISABLE KEYS */;
INSERT INTO `hourentries` VALUES ('TaskHourEntry',1,'2012-04-12 22:32:00','',60,5,NULL,NULL,2),('TaskHourEntry',2,'2012-04-12 22:33:00','',300,5,NULL,NULL,3),('StoryHourEntry',3,'2013-05-31 15:03:00','',600,5,NULL,51,NULL),('StoryHourEntry',4,'2013-05-31 15:03:00','',300,5,NULL,23,NULL),('StoryHourEntry',5,'2013-05-31 15:03:00','',30,5,NULL,21,NULL),('StoryHourEntry',6,'2013-05-31 15:03:00','',1800,5,NULL,3,NULL);
/*!40000 ALTER TABLE `hourentries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labels`
--

DROP TABLE IF EXISTS `labels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `displayName` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `story_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `label_name` (`name`),
  KEY `FKBDD05FFFE0E4BFA2` (`story_id`),
  KEY `FKBDD05FFF1C5D0ED1` (`creator_id`),
  CONSTRAINT `FKBDD05FFF1C5D0ED1` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKBDD05FFFE0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labels`
--

LOCK TABLES `labels` WRITE;
/*!40000 ALTER TABLE `labels` DISABLE KEYS */;
/*!40000 ALTER TABLE `labels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logins`
--

DROP TABLE IF EXISTS `logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4B213410C1610AD4` (`user_id`),
  CONSTRAINT `FK4B213410C1610AD4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logins`
--

LOCK TABLES `logins` WRITE;
/*!40000 ALTER TABLE `logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `logins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schema_version`
--

DROP TABLE IF EXISTS `schema_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_version` (
  `version_rank` int(11) NOT NULL,
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(30) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  KEY `schema_version_vr_idx` (`version_rank`),
  KEY `schema_version_ir_idx` (`installed_rank`),
  KEY `schema_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schema_version`
--

LOCK TABLES `schema_version` WRITE;
/*!40000 ALTER TABLE `schema_version` DISABLE KEYS */;
INSERT INTO `schema_version` VALUES (1,1,'0','Initial version','SQL','V0__Initial_version.sql',-1085754636,'exampledbtest','2013-05-30 14:53:16',8654,1),(2,2,'1','updated version','SQL','V1__updated_version.sql',-733178282,'exampledbtest','2013-05-30 14:53:16',276,1);
/*!40000 ALTER TABLE `schema_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (13,'Agilefant database version','AgilefantDatabaseVersion','304'),(14,NULL,'RangeLow','0'),(15,NULL,'RangeHigh','120'),(16,NULL,'OptimalLow','70'),(17,NULL,'OptimalHigh','85'),(18,NULL,'CriticalLow','100'),(19,NULL,'HourReporting','true'),(20,NULL,'DevPortfolio','true'),(21,NULL,'DailyWork','true'),(22,NULL,'StoryTreeFieldOrder','state,storyPoints,labels,name,backlog,breadcrumb'),(23,NULL,'branchMetricsType','estimate'),(24,NULL,'labelsInStoryList','true'),(25,NULL,'weekendsInBurndown','true');
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stories`
--

DROP TABLE IF EXISTS `stories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `name` varchar(255) NOT NULL,
  `state` int(11) NOT NULL,
  `storyPoints` int(11) DEFAULT NULL,
  `storyValue` int(11) DEFAULT NULL,
  `treeRank` int(11) NOT NULL DEFAULT '0',
  `backlog_id` int(11) DEFAULT NULL,
  `iteration_id` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8FB060534157D2A2` (`iteration_id`),
  KEY `FK8FB06053F63400A2` (`backlog_id`),
  KEY `FK8FB06053F5E897CD` (`parent_id`),
  CONSTRAINT `FK8FB060534157D2A2` FOREIGN KEY (`iteration_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK8FB06053F5E897CD` FOREIGN KEY (`parent_id`) REFERENCES `stories` (`id`),
  CONSTRAINT `FK8FB06053F63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stories`
--

LOCK TABLES `stories` WRITE;
/*!40000 ALTER TABLE `stories` DISABLE KEYS */;
INSERT INTO `stories` VALUES (3,NULL,'FREE, OPEN SOURCE MARKET LEADER OF AGILE MGMT TOOLS',1,NULL,NULL,1,5,NULL,NULL),(4,NULL,'MEANEST MACHINE',1,NULL,NULL,0,5,NULL,3),(5,NULL,'MOST USERS',1,NULL,NULL,1,5,NULL,3),(6,NULL,'FULL POWER COMMUNITY',1,NULL,NULL,2,5,NULL,3),(7,'Agilefant has the conceptually best fit for real/world agile/lean development','The best model',5,NULL,NULL,0,7,NULL,4),(8,NULL,'Agilefant has gained the market leadership of free tools',1,NULL,NULL,4,5,NULL,5),(9,NULL,'Community is submitting in more new features than can be included in releases',1,NULL,NULL,0,5,NULL,6),(10,'(Includes Jarno, Petteri and Benjamin)','At least 10 people are contributing to Agilefant\'s development',5,NULL,NULL,1,5,NULL,9),(11,NULL,'Efforts to launch Agilefant are fully funded',5,NULL,NULL,4,5,NULL,6),(12,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and won the pitching competition',5,NULL,NULL,0,8,13,11),(13,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',5,NULL,NULL,0,7,12,10),(14,NULL,'Enough evaluation instances readily available online',5,NULL,NULL,2,5,NULL,5),(15,NULL,'Agilefant has been recognized as the meanest machine',1,200,NULL,3,5,NULL,5),(16,NULL,'Releases',1,NULL,NULL,1,5,NULL,15),(17,NULL,'2.5.0',5,5,NULL,5,7,11,16),(18,NULL,'3.0 alpha 1',5,NULL,NULL,6,7,12,16),(19,NULL,'3.0 beta released',5,NULL,NULL,10,8,13,16),(20,'Blocked. No better free tool than Agilefant exists.','Find a better backlog management tool, one that is free as well as scales!',5,5,NULL,1,6,NULL,NULL),(21,'Blocked, \'cause the store is not open','Buy milk',3,1,NULL,3,6,NULL,NULL),(22,'<i>Pending for a raise</i>','Get a new car',2,40,0,4,6,NULL,NULL),(23,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,2,6,14,NULL),(24,'Agilefant HQ has enough funding to&nbsp; provide and develop the best free open source backlog management tool. For this, commercial activities are in place.','Agilefant Inc. has been launched',1,NULL,NULL,3,5,NULL,6),(25,NULL,'3.0 alpha 2',5,NULL,NULL,7,7,12,16),(26,NULL,'Instances 1-13',5,5,NULL,1,9,NULL,14),(27,NULL,'Instances 14-30',5,3,NULL,2,9,NULL,14),(28,NULL,'First steps',5,NULL,NULL,3,8,NULL,24),(29,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',5,NULL,NULL,0,8,13,28),(30,NULL,'Set Agilefant as one of the topics at EIT ICT labs Helsinki Summer School',5,NULL,NULL,2,8,NULL,28),(31,NULL,'Killer team recruited',5,NULL,NULL,1,8,NULL,28),(32,NULL,'Pre-course coding sessions',5,NULL,NULL,1,7,12,10),(33,NULL,'3.0 alpha 3..7',5,NULL,NULL,8,7,NULL,16),(34,NULL,'3.0 alpha 8',5,NULL,NULL,9,7,NULL,16),(35,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',5,NULL,NULL,5,8,13,6),(36,NULL,'Agilefant has 100 registered user organizations',5,NULL,NULL,3,8,13,8),(37,NULL,'Agilefant has 200+ registered user organizations',4,NULL,NULL,2,9,NULL,8),(38,'','Celebrate the release of Agilefant 3.0.0',5,NULL,NULL,2,8,14,6),(39,'Pending for:<br>- most important bugs in 3.0.0 have been discovered<br>- a core team member has started working on 3.0.0','3.0.1 released',5,NULL,NULL,11,9,16,16),(40,NULL,'Instances 31-100',5,3,NULL,3,9,NULL,14),(41,NULL,'Agilefant has 2000 registered user organizations',5,NULL,NULL,1,10,20,8),(42,NULL,'Got 311ke from Tekes\' TUTL program to prepare the launch',5,NULL,NULL,1,9,16,24),(43,NULL,'Market leader of free open source tools',1,NULL,NULL,1,5,NULL,5),(44,NULL,'Cloud ver 2 set up',4,NULL,NULL,0,9,16,43),(45,NULL,'10 000 hosted users',1,NULL,NULL,4,10,NULL,43),(46,NULL,'3.0.0 released',5,NULL,NULL,12,9,15,16),(47,'','Submit the best application of TUTL funding round #3',5,NULL,0,2,9,15,24),(48,NULL,'3.0.2 released',4,2,NULL,4,9,16,16),(49,NULL,'2 seconds to Agilefant trial',1,NULL,NULL,0,9,NULL,5),(50,NULL,'Open just-email registration form for single-product trials in the community fant',5,NULL,NULL,0,9,16,49),(51,NULL,'Get Christmas presents for everyone',5,NULL,NULL,0,6,NULL,NULL),(52,'','Avoid the end of the world',5,NULL,NULL,0,NULL,14,NULL),(53,'','Celebrate Christmas',5,NULL,NULL,1,NULL,14,6),(54,NULL,'Gathered a rockstar team for TUTL',5,NULL,NULL,0,10,NULL,24),(55,'','Recruited a full time CTO w/ full power tech savvy',5,NULL,NULL,1,10,17,54),(56,NULL,'Recruited three part time junior but xtremely skilled developers',5,NULL,NULL,2,10,17,54),(57,NULL,'Recuited a full time star developer',5,NULL,NULL,0,10,18,54),(58,NULL,'Instances 101-131',5,2,NULL,4,10,17,14),(59,NULL,'3.0.3 released',5,3,NULL,3,10,17,16),(60,NULL,'Semi-automatic evaluation and trial instance creation',5,2,NULL,7,10,19,14),(61,NULL,'Instances 131-145',5,2,NULL,5,10,18,14),(62,NULL,'3.1.1 released',5,5,NULL,2,10,19,16),(63,NULL,'Scripted the single-product account creation',5,NULL,NULL,6,10,18,14),(64,NULL,'Cloud ver 3 set up',5,NULL,NULL,1,10,19,43),(65,NULL,'Cloud ver 4 set up',1,NULL,NULL,3,10,20,43),(66,NULL,'200 hosted users',5,NULL,NULL,2,10,18,43),(67,NULL,'Each new release includes a community contribution',5,NULL,NULL,0,10,19,9),(68,NULL,'Evangelism tour',1,NULL,NULL,0,10,NULL,15),(69,NULL,'Tell to people @ AgileJkl',5,NULL,NULL,2,10,20,68),(70,NULL,'Pitch @ Tivit Results Seminar Finlandia Hall',5,NULL,NULL,3,10,20,68),(71,NULL,'Get hot local references',1,NULL,NULL,4,10,NULL,68),(72,NULL,'Pitch @ Nitor Creations',1,NULL,NULL,0,10,20,71),(73,NULL,'Scripted the new instance creation',4,NULL,NULL,0,10,20,14),(74,NULL,'3.2 released',4,NULL,NULL,1,10,20,16),(75,NULL,'3.3 released',1,NULL,NULL,0,10,20,16),(76,NULL,'Tell to people & survey for the most popular tools @ Turku Agile Day',4,8,NULL,1,10,21,68),(77,NULL,'Attended XP2013 Full Power',1,NULL,NULL,0,10,NULL,68),(78,NULL,'Got practice pitching Agilefant Pecha Kucha -style',1,4,NULL,2,10,21,77),(79,'','Done the sessions',2,NULL,NULL,1,10,21,77),(80,NULL,'Surveyed participants about their tool of choice',2,4,NULL,0,10,21,77),(81,NULL,'Automatic sign-up of new trials (with captcha) activated',4,5,NULL,0,10,21,8);
/*!40000 ALTER TABLE `stories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stories_AUD`
--

DROP TABLE IF EXISTS `stories_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stories_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `storyPoints` int(11) DEFAULT NULL,
  `storyValue` int(11) DEFAULT NULL,
  `treeRank` int(11) DEFAULT '0',
  `backlog_id` int(11) DEFAULT NULL,
  `iteration_id` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK853E2CA420258526` (`REV`),
  CONSTRAINT `FK853E2CA420258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stories_AUD`
--

LOCK TABLES `stories_AUD` WRITE;
/*!40000 ALTER TABLE `stories_AUD` DISABLE KEYS */;
INSERT INTO `stories_AUD` VALUES (3,12,0,NULL,'FREE, OPEN SOURCE MARKET LEADER OF AGILE MGMT TOOLS',1,NULL,NULL,1,5,NULL,NULL),(4,12,0,NULL,'MEANEST MACHINE',1,NULL,NULL,0,5,NULL,3),(5,12,0,NULL,'MOST USERS',1,NULL,NULL,1,5,NULL,3),(6,12,0,NULL,'FULL POWER COMMUNITY',1,NULL,NULL,2,5,NULL,3),(7,12,0,'Agilefant has the conceptually best fit for real/world agile/lean development','The best model',5,NULL,NULL,0,7,NULL,4),(8,12,0,NULL,'Agilefant has gained the market leadership of free tools',1,NULL,NULL,4,5,NULL,5),(9,12,0,NULL,'Community is submitting in more new features than can be included in releases',1,NULL,NULL,0,5,NULL,6),(10,12,0,'(Includes Jarno, Petteri and Benjamin)','At least 10 people are contributing to Agilefant\'s development',5,NULL,NULL,1,5,NULL,9),(11,12,0,NULL,'Efforts to launch Agilefant are fully funded',5,NULL,NULL,4,5,NULL,6),(12,12,0,NULL,'An Agilefant Team has taken part in the EIT 2012 Cloud Summer School and won the pitching competition',5,NULL,NULL,0,8,13,11),(13,12,0,NULL,'The 3.0 beta backlog has been prioritized, published,drummed about, and ready to be taken apart by the coding-hungry community!',5,NULL,NULL,0,7,12,10),(14,12,0,NULL,'Enough evaluation instances readily available online',4,NULL,NULL,2,5,NULL,5),(14,46,1,NULL,'Enough evaluation instances readily available online',5,NULL,NULL,2,5,NULL,5),(15,12,0,NULL,'Agilefant has been recognized as the meanest machine',1,200,NULL,3,5,NULL,5),(16,12,0,NULL,'Releases',1,NULL,NULL,1,5,NULL,15),(17,12,0,NULL,'2.5.0',5,5,NULL,5,7,11,16),(18,12,0,NULL,'3.0 alpha 1',5,NULL,NULL,6,7,12,16),(19,12,0,NULL,'3.0 beta released',5,NULL,NULL,10,8,13,16),(20,12,0,'Blocked. No better free tool than Agilefant exists.','Find a better backlog management tool, one that is free as well as scales!',5,5,NULL,1,6,NULL,NULL),(21,12,0,'Blocked, \'cause the store is not open','Buy milk',3,1,NULL,3,6,NULL,NULL),(22,12,0,'<i>Pending for a raise</i>','Get a new car',2,40,0,4,6,NULL,NULL),(23,12,0,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,2,6,14,NULL),(23,48,1,'Deferred, don\'t need more beer today','Get more beer',1,3,NULL,2,6,14,NULL),(23,49,1,'Deferred, don\'t need more beer today','Get more beer',5,3,NULL,2,6,14,NULL),(23,50,1,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,2,6,14,NULL),(23,57,1,'Deferred, don\'t need more beer today','Get more beer',6,3,NULL,2,6,14,NULL),(24,12,0,'Agilefant HQ has enough funding to&nbsp; provide and develop the best free open source backlog management tool. For this, commercial activities are in place.','Agilefant Inc. has been launched',1,NULL,NULL,3,5,NULL,6),(25,12,0,NULL,'3.0 alpha 2',5,NULL,NULL,7,7,12,16),(26,12,0,NULL,'Instances 1-13',5,5,NULL,1,9,NULL,14),(27,12,0,NULL,'Instances 14-30',5,3,NULL,2,9,NULL,14),(28,12,0,NULL,'First steps',5,NULL,NULL,3,8,NULL,24),(29,12,0,NULL,'Prel. business models plan prepared at the EIT ICT labs Helsinki Summer School',5,NULL,NULL,0,8,13,28),(30,12,0,NULL,'Set Agilefant as one of the topics at EIT ICT labs Helsinki Summer School',5,NULL,NULL,2,8,NULL,28),(31,12,0,NULL,'Killer team recruited',5,NULL,NULL,1,8,NULL,28),(32,12,0,NULL,'Pre-course coding sessions',5,NULL,NULL,1,7,12,10),(33,12,0,NULL,'3.0 alpha 3..7',5,NULL,NULL,8,7,NULL,16),(34,12,0,NULL,'3.0 alpha 8',5,NULL,NULL,9,7,NULL,16),(35,12,0,NULL,'A full-time engineer has been recruited to work on Agilefant for the Summer',5,NULL,NULL,5,8,13,6),(36,12,0,NULL,'Agilefant has 100 registered user organizations',5,NULL,NULL,2,8,13,8),(36,43,1,NULL,'Agilefant has 100 registered user organizations',5,NULL,NULL,3,8,13,8),(37,12,0,NULL,'Agilefant has 200+ registered user organizations',4,NULL,NULL,1,9,NULL,8),(37,43,1,NULL,'Agilefant has 200+ registered user organizations',4,NULL,NULL,2,9,NULL,8),(38,12,0,'','Celebrate the release of Agilefant 3.0.0',5,NULL,NULL,2,8,14,6),(39,12,0,'Pending for:<br>- most important bugs in 3.0.0 have been discovered<br>- a core team member has started working on 3.0.0','3.0.1 released',5,NULL,NULL,11,9,16,16),(40,12,0,NULL,'Instances 31-100',5,3,NULL,3,9,NULL,14),(41,12,0,NULL,'Agilefant has 500 registered user organizations',1,NULL,NULL,0,10,20,8),(41,41,1,NULL,'Agilefant has 500 registered user organizations',5,NULL,NULL,0,10,20,8),(41,42,1,NULL,'Agilefant has 2000 registered user organizations',5,NULL,NULL,0,10,20,8),(41,43,1,NULL,'Agilefant has 2000 registered user organizations',5,NULL,NULL,1,10,20,8),(42,12,0,NULL,'Got 311ke from Tekes\' TUTL program to prepare the launch',5,NULL,NULL,1,9,16,24),(43,12,0,NULL,'Market leader of free open source tools',1,NULL,NULL,1,5,NULL,5),(44,12,0,NULL,'Cloud ver 2 set up',4,NULL,NULL,0,9,16,43),(45,12,0,NULL,'10 000 hosted users',1,NULL,NULL,4,10,NULL,43),(46,12,0,NULL,'3.0.0 released',5,NULL,NULL,12,9,15,16),(47,12,0,'','Submit the best application of TUTL funding round #3',5,NULL,0,2,9,15,24),(48,12,0,NULL,'3.0.2 released',4,2,NULL,4,9,16,16),(49,12,0,NULL,'2 seconds to Agilefant trial',1,NULL,NULL,0,9,NULL,5),(50,12,0,NULL,'Open just-email registration form for single-product trials in the community fant',5,NULL,NULL,0,9,16,49),(51,12,0,NULL,'Get Christmas presents for everyone',5,NULL,NULL,0,6,NULL,NULL),(52,12,0,'','Avoid the end of the world',5,NULL,NULL,0,NULL,14,NULL),(53,12,0,'','Celebrate Christmas',1,NULL,NULL,1,NULL,14,6),(53,16,1,'','Celebrate Christmas',5,NULL,NULL,1,NULL,14,6),(54,12,0,NULL,'Gathered a rockstar team for TUTL',5,NULL,NULL,0,10,NULL,24),(55,12,0,'','Recruited a full time CTO w/ full power tech savvy',5,NULL,NULL,1,10,17,54),(56,12,0,NULL,'Recruited three part time junior but xtremely skilled developers',5,NULL,NULL,2,10,17,54),(57,12,0,NULL,'Recuited a full time star developer',5,NULL,NULL,0,10,18,54),(58,12,0,NULL,'Instances 101-131',5,2,NULL,4,10,17,14),(59,12,0,NULL,'3.0.3 released',5,3,NULL,3,10,17,16),(59,73,1,NULL,'3.0.3 released',5,3,NULL,3,10,17,16),(60,12,0,NULL,'Semi-automatic evaluation and trial instance creation',5,2,NULL,7,10,19,14),(61,12,0,NULL,'Instances 131-145',5,2,NULL,5,10,18,14),(62,12,0,NULL,'3.1.1 released',5,5,NULL,2,10,19,16),(63,12,0,NULL,'Scripted the single-product account creation',5,NULL,NULL,6,10,18,14),(64,12,0,NULL,'Cloud ver 3 set up',5,NULL,NULL,1,10,19,43),(65,12,0,NULL,'Cloud ver 4 set up',1,NULL,NULL,3,10,20,43),(66,12,0,NULL,'200 hosted users',5,NULL,NULL,2,10,18,43),(67,12,0,NULL,'Each new release includes a community contribution',5,NULL,NULL,0,10,19,9),(68,12,0,NULL,'Evangelism tour',1,NULL,NULL,0,10,NULL,15),(69,12,0,NULL,'Tell to people @ AgileJkl',1,NULL,NULL,0,10,20,68),(69,18,1,NULL,'Tell to people @ AgileJkl',5,NULL,NULL,0,10,20,68),(69,20,1,NULL,'Tell to people @ AgileJkl',5,NULL,NULL,1,10,20,68),(69,25,1,NULL,'Tell to people @ AgileJkl',5,NULL,NULL,2,10,20,68),(70,12,0,NULL,'Pitch @ Tivit Results Seminar Finlandia Hall',1,NULL,NULL,1,10,20,68),(70,19,1,NULL,'Pitch @ Tivit Results Seminar Finlandia Hall',5,NULL,NULL,1,10,20,68),(70,20,1,NULL,'Pitch @ Tivit Results Seminar Finlandia Hall',5,NULL,NULL,2,10,20,68),(70,25,1,NULL,'Pitch @ Tivit Results Seminar Finlandia Hall',5,NULL,NULL,3,10,20,68),(71,12,0,NULL,'Get hot local references',1,NULL,NULL,2,10,NULL,68),(71,20,1,NULL,'Get hot local references',1,NULL,NULL,3,10,NULL,68),(71,25,1,NULL,'Get hot local references',1,NULL,NULL,4,10,NULL,68),(72,12,0,NULL,'Pitch @ Nitor Creations',1,NULL,NULL,0,10,20,71),(73,12,0,NULL,'Scripted the new instance creation',4,NULL,NULL,0,10,20,14),(74,12,0,NULL,'3.2 released',4,NULL,NULL,1,10,20,16),(75,12,0,NULL,'3.3 released',1,NULL,NULL,0,10,20,16),(76,20,0,NULL,'Tell to people & survey for the most popular tools @ Turku Agile Day',0,NULL,NULL,0,10,NULL,68),(76,21,1,NULL,'Tell to people & survey for the most popular tools @ Turku Agile Day',4,NULL,NULL,0,10,NULL,68),(76,22,1,NULL,'Tell to people & survey for the most popular tools @ Turku Agile Day',4,NULL,NULL,0,10,21,68),(76,23,1,NULL,'Tell to people & survey for the most popular tools @ Turku Agile Day',4,NULL,NULL,0,10,21,68),(76,24,1,NULL,'Tell to people & survey for the most popular tools @ Turku Agile Day',4,8,NULL,0,10,21,68),(76,25,1,NULL,'Tell to people & survey for the most popular tools @ Turku Agile Day',4,8,NULL,1,10,21,68),(77,25,0,NULL,'Attended XP2013 Full Power',0,NULL,NULL,0,10,NULL,68),(77,27,1,NULL,'Attended XP2013 Full Power',1,NULL,NULL,0,10,NULL,68),(78,26,0,NULL,'Got practice pitching Agilefant Pecha Kucha -style',0,NULL,NULL,0,10,NULL,77),(78,27,1,NULL,'Got practice pitching Agilefant Pecha Kucha -style',1,NULL,NULL,0,10,NULL,77),(78,28,1,NULL,'Got practice pitching Agilefant Pecha Kucha -style',1,3,NULL,0,10,NULL,77),(78,29,1,NULL,'Got practice pitching Agilefant Pecha Kucha -style',1,4,NULL,0,10,NULL,77),(78,30,1,NULL,'Got practice pitching Agilefant Pecha Kucha -style',1,4,NULL,0,10,21,77),(78,31,1,NULL,'Got practice pitching Agilefant Pecha Kucha -style',1,4,NULL,0,10,21,77),(78,32,1,NULL,'Got practice pitching Agilefant Pecha Kucha -style',1,4,NULL,1,10,21,77),(78,36,1,NULL,'Got practice pitching Agilefant Pecha Kucha -style',1,4,NULL,2,10,21,77),(79,32,0,'','Done the sessions',0,NULL,NULL,0,10,NULL,77),(79,33,1,'','Done the sessions',0,NULL,NULL,0,10,21,77),(79,34,1,'','Done the sessions',0,NULL,NULL,0,10,21,77),(79,35,1,'','Done the sessions',2,NULL,NULL,0,10,21,77),(79,36,1,'','Done the sessions',2,NULL,NULL,1,10,21,77),(80,36,0,NULL,'Surveyed participants about their tool of choice',0,NULL,NULL,0,10,NULL,77),(80,37,1,NULL,'Surveyed participants about their tool of choice',2,NULL,NULL,0,10,NULL,77),(80,38,1,NULL,'Surveyed participants about their tool of choice',2,NULL,NULL,0,10,21,77),(80,39,1,NULL,'Surveyed participants about their tool of choice',2,4,NULL,0,10,21,77),(80,40,1,NULL,'Surveyed participants about their tool of choice',2,4,NULL,0,10,21,77),(81,43,0,NULL,'Automatic sign-up of new trials activated',0,NULL,NULL,0,10,NULL,8),(81,52,1,NULL,'Automatic sign-up of new trials activated',4,NULL,NULL,0,10,NULL,8),(81,53,1,NULL,'Automatic sign-up of new trials activated',4,NULL,NULL,0,10,21,8),(81,54,1,NULL,'Automatic sign-up of new trials activated',4,NULL,NULL,0,10,21,8),(81,55,1,NULL,'Automatic sign-up of new trials activated',4,5,NULL,0,10,21,8),(81,64,1,NULL,'Automatic sign-up of new trials (with captcha) activated',4,5,NULL,0,10,21,8),(81,72,1,NULL,'Automatic sign-up of new trials (with captcha) activated',4,5,NULL,0,10,21,8);
/*!40000 ALTER TABLE `stories_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story_access`
--

DROP TABLE IF EXISTS `story_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `story_access` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `story_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK44C5ABEEE0E4BFA2` (`story_id`),
  KEY `FK44C5ABEEC1610AD2` (`user_id`),
  CONSTRAINT `FK44C5ABEEC1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK44C5ABEEE0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=381 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `story_access`
--

LOCK TABLES `story_access` WRITE;
/*!40000 ALTER TABLE `story_access` DISABLE KEYS */;
INSERT INTO `story_access` VALUES (2,'2012-04-10 22:48:25',4,5),(3,'2012-04-10 23:44:13',4,5),(4,'2012-08-15 11:45:34',4,5),(5,'2012-04-10 22:48:10',5,5),(6,'2012-04-10 22:51:43',5,5),(7,'2012-06-19 15:06:45',5,6),(8,'2012-10-18 15:43:47',5,6),(9,'2012-10-18 15:46:17',5,6),(10,'2012-12-04 11:09:00',5,6),(11,'2012-04-10 22:52:06',6,5),(12,'2012-04-10 23:06:09',6,5),(13,'2012-04-10 23:06:52',6,5),(14,'2012-04-10 23:06:58',6,5),(15,'2012-04-10 23:09:47',6,5),(16,'2012-04-10 23:10:22',6,5),(17,'2012-06-19 14:58:12',6,6),(18,'2012-06-19 15:02:10',6,6),(19,'2012-08-15 10:55:29',6,5),(20,'2012-08-15 11:39:35',6,5),(21,'2013-03-08 12:45:24',6,5),(22,'2012-04-10 22:50:28',7,5),(23,'2012-04-10 23:17:49',7,5),(24,'2012-04-10 23:17:58',7,5),(25,'2012-04-10 23:43:51',7,5),(26,'2012-04-10 23:44:35',7,5),(27,'2012-05-14 09:32:48',7,5),(28,'2012-10-18 15:29:05',7,6),(29,'2012-08-15 11:20:39',3,5),(30,'2012-08-15 11:20:42',3,5),(31,'2012-10-18 15:42:32',3,6),(32,'2012-04-10 23:17:28',8,5),(33,'2012-06-19 15:03:02',8,6),(34,'2012-06-19 15:03:28',8,6),(35,'2012-08-15 11:20:46',8,5),(36,'2012-08-15 11:20:49',8,5),(37,'2012-08-15 11:21:00',8,5),(38,'2012-10-18 15:34:09',8,6),(39,'2012-10-18 15:34:27',8,6),(40,'2012-04-10 23:09:18',10,5),(41,'2012-04-10 23:09:22',10,5),(42,'2012-04-10 23:12:43',10,5),(43,'2012-05-14 09:41:47',10,5),(44,'2012-05-14 09:42:46',10,5),(45,'2012-08-15 15:15:51',10,5),(46,'2012-04-10 23:08:31',11,5),(47,'2012-04-10 23:09:07',11,5),(48,'2012-04-10 23:11:29',11,5),(49,'2012-04-10 23:12:10',11,5),(50,'2012-12-21 11:43:20',11,6),(51,'2012-12-21 11:43:33',11,6),(52,'2013-01-31 10:40:37',11,5),(53,'2012-08-15 10:54:59',12,5),(54,'2012-11-07 23:39:16',12,6),(55,'2012-04-10 23:16:49',13,5),(56,'2012-04-12 22:29:55',13,5),(57,'2012-05-14 09:42:05',13,5),(58,'2012-08-15 10:55:17',13,5),(59,'2012-08-15 15:15:58',13,5),(60,'2012-04-12 22:30:15',14,5),(61,'2012-05-14 09:33:13',14,5),(62,'2012-05-14 09:33:23',14,5),(63,'2012-05-14 09:33:42',14,5),(64,'2012-05-14 09:33:56',14,5),(65,'2012-06-19 15:04:04',14,6),(66,'2012-08-15 12:39:40',14,5),(67,'2012-10-18 15:31:56',14,6),(68,'2012-10-18 15:31:59',14,6),(69,'2012-10-18 15:32:04',14,6),(70,'2012-10-18 15:32:14',14,6),(71,'2012-10-18 15:33:19',14,6),(72,'2012-10-18 15:45:03',14,6),(73,'2013-01-31 10:38:11',14,5),(74,'2013-01-31 10:38:43',14,5),(75,'2013-01-31 10:38:58',14,5),(76,'2013-03-08 12:29:48',14,5),(77,'2013-03-08 12:34:43',14,5),(78,'2013-04-17 16:12:25',14,5),(79,'2013-04-17 16:13:06',14,5),(80,'2012-04-10 23:18:02',16,5),(81,'2012-04-10 23:18:14',16,5),(82,'2012-04-10 23:18:25',16,5),(83,'2012-05-11 23:05:42',16,5),(84,'2012-06-19 15:00:13',16,6),(85,'2012-06-19 15:00:39',16,6),(86,'2012-10-18 15:30:25',16,6),(87,'2012-10-18 15:31:01',16,6),(88,'2012-10-18 15:31:06',16,6),(89,'2012-10-18 15:47:35',16,6),(90,'2012-12-04 11:08:18',16,6),(91,'2012-12-04 11:08:41',16,6),(92,'2013-01-31 10:39:21',16,5),(93,'2013-01-31 10:39:27',16,5),(94,'2013-03-08 12:35:28',16,5),(95,'2013-03-08 12:36:35',16,5),(96,'2013-04-17 16:13:28',16,5),(97,'2013-04-17 16:13:50',16,5),(98,'2012-04-10 23:35:53',17,5),(99,'2012-05-14 09:30:49',17,5),(100,'2012-05-14 09:31:02',17,5),(101,'2013-03-08 12:36:27',17,5),(102,'2012-05-14 09:30:54',18,5),(103,'2012-05-14 09:31:12',18,5),(104,'2012-05-14 09:31:28',18,5),(105,'2012-05-14 09:31:35',18,5),(106,'2012-06-19 15:00:08',18,6),(107,'2012-08-15 10:55:18',18,5),(108,'2013-03-08 12:36:31',18,5),(109,'2012-04-10 23:18:41',19,5),(110,'2012-05-14 09:31:58',19,5),(111,'2012-05-14 09:36:33',19,5),(112,'2012-08-15 10:54:52',19,5),(113,'2012-10-18 15:29:49',19,6),(114,'2012-10-18 15:30:20',19,6),(115,'2012-04-10 23:26:50',20,5),(116,'2012-04-10 23:28:01',20,5),(117,'2012-04-10 23:28:52',20,5),(118,'2012-04-10 23:29:20',20,5),(119,'2012-04-10 23:29:56',20,5),(120,'2012-04-11 13:56:32',20,6),(121,'2012-08-15 15:18:44',20,5),(122,'2012-12-21 11:41:47',20,6),(123,'2012-12-21 11:41:55',20,6),(124,'2012-04-10 23:27:20',21,5),(125,'2012-04-10 23:28:17',21,5),(126,'2012-04-10 23:29:05',21,5),(127,'2012-08-15 15:19:09',21,5),(128,'2012-04-10 23:26:58',22,5),(129,'2012-04-10 23:27:48',22,5),(130,'2012-04-10 23:29:13',22,5),(131,'2012-04-10 23:29:36',22,5),(132,'2012-04-10 23:29:47',22,5),(133,'2012-04-10 23:30:02',22,5),(134,'2012-04-10 23:30:15',22,5),(135,'2012-08-15 15:19:13',22,5),(136,'2012-12-21 11:42:07',22,6),(137,'2012-04-10 23:28:22',23,5),(138,'2012-04-10 23:28:59',23,5),(139,'2012-08-15 15:10:22',23,5),(140,'2012-08-15 15:13:40',23,5),(141,'2012-11-08 00:07:00',23,6),(142,'2012-12-21 11:42:03',23,6),(143,'2012-05-14 09:37:16',24,5),(144,'2012-05-14 09:37:44',24,5),(145,'2012-05-14 09:39:06',24,5),(146,'2012-05-14 09:40:31',24,5),(147,'2012-05-14 09:41:13',24,5),(148,'2012-06-19 14:59:27',24,6),(149,'2012-10-18 15:36:07',24,6),(150,'2012-10-18 15:37:24',24,6),(151,'2012-10-26 09:30:59',24,6),(152,'2012-10-26 09:31:41',24,6),(153,'2012-11-07 23:46:35',24,6),(154,'2012-12-21 11:43:41',24,6),(155,'2013-01-31 10:31:59',24,5),(156,'2013-01-31 10:37:52',24,5),(157,'2012-04-10 23:44:26',15,5),(158,'2012-05-14 09:32:23',15,5),(159,'2013-01-31 10:39:06',15,5),(160,'2013-03-08 12:36:37',15,5),(161,'2013-03-08 12:36:39',15,5),(162,'2013-04-17 16:09:40',15,5),(163,'2012-05-14 09:31:45',25,5),(164,'2012-05-14 09:36:41',25,5),(165,'2012-06-19 15:00:01',25,6),(166,'2012-08-15 10:55:14',25,5),(167,'2012-05-14 09:33:34',26,5),(168,'2012-05-14 09:33:47',26,5),(169,'2012-08-15 11:24:51',26,5),(170,'2012-08-15 11:24:57',26,5),(171,'2012-08-15 11:26:35',26,5),(172,'2012-08-15 11:26:40',26,5),(173,'2012-08-15 11:26:59',26,5),(174,'2012-08-15 11:39:44',26,5),(175,'2012-08-15 11:39:48',26,5),(176,'2012-08-15 11:40:13',26,5),(177,'2012-08-15 11:45:38',26,5),(178,'2012-08-15 11:45:42',26,5),(179,'2013-03-08 12:34:10',26,5),(180,'2012-05-14 09:34:08',27,5),(181,'2012-05-14 09:34:16',27,5),(182,'2012-06-19 15:04:02',27,6),(183,'2012-06-19 15:04:07',27,6),(184,'2012-08-15 10:56:37',27,5),(185,'2012-08-15 11:02:24',27,5),(186,'2012-08-15 11:24:30',27,5),(187,'2012-08-15 11:24:39',27,5),(188,'2013-03-08 12:34:14',27,5),(189,'2012-05-14 09:38:28',29,5),(190,'2012-05-14 09:39:28',29,5),(191,'2012-05-14 09:40:05',29,5),(192,'2012-08-15 10:55:03',29,5),(193,'2012-11-07 23:41:24',29,6),(194,'2012-05-14 09:38:41',30,5),(195,'2012-05-14 09:39:38',30,5),(196,'2012-08-15 10:55:43',30,5),(197,'2012-08-15 10:56:07',30,5),(198,'2012-08-15 11:12:07',30,5),(199,'2012-08-15 11:20:00',30,5),(200,'2012-08-15 11:29:00',30,5),(201,'2012-08-15 11:29:20',30,5),(202,'2012-08-15 11:29:23',30,5),(203,'2012-08-15 11:29:34',30,5),(204,'2012-08-15 12:42:15',30,5),(205,'2012-08-15 12:42:23',30,5),(206,'2012-10-18 15:35:59',30,6),(207,'2012-10-18 15:36:49',30,6),(208,'2012-10-18 15:36:51',30,6),(209,'2012-05-14 09:40:47',31,5),(210,'2012-05-14 09:41:06',31,5),(211,'2012-06-19 15:04:15',31,6),(212,'2012-08-15 10:55:19',31,5),(213,'2012-10-18 15:37:07',31,6),(214,'2012-05-14 09:41:24',32,5),(215,'2012-06-19 15:01:21',32,6),(216,'2012-06-19 15:01:36',32,6),(217,'2012-08-15 10:55:15',32,5),(218,'2012-06-19 15:00:35',33,6),(219,'2012-10-18 15:29:57',33,6),(220,'2012-06-19 15:00:49',34,6),(221,'2012-10-18 15:29:50',34,6),(222,'2012-10-18 15:29:56',34,6),(223,'2012-10-18 15:30:10',34,6),(224,'2012-06-19 15:02:31',35,6),(225,'2012-06-19 15:02:34',35,6),(226,'2012-08-15 10:54:24',35,5),(227,'2012-08-15 10:54:37',35,5),(228,'2012-06-19 15:03:20',36,6),(229,'2012-08-15 10:55:02',36,5),(230,'2012-06-19 15:03:42',37,6),(231,'2012-06-19 15:03:54',37,6),(232,'2012-10-18 15:34:35',37,6),(233,'2012-10-18 15:34:39',37,6),(234,'2012-10-18 15:34:53',37,6),(235,'2012-08-15 15:16:13',38,5),(236,'2012-10-18 15:35:28',38,6),(237,'2012-10-18 15:31:22',39,6),(238,'2012-10-18 15:41:58',39,6),(239,'2012-10-18 15:47:40',39,6),(240,'2012-10-26 09:29:51',39,6),(241,'2012-10-18 15:32:28',40,6),(242,'2012-10-18 15:32:47',40,6),(243,'2012-10-18 15:33:45',40,6),(244,'2013-01-31 10:38:25',40,5),(245,'2013-03-08 12:34:19',40,5),(246,'2012-10-18 15:34:59',41,6),(247,'2012-10-18 15:36:18',28,6),(248,'2012-10-18 15:37:59',42,6),(249,'2012-10-18 15:38:05',42,6),(250,'2012-10-26 09:30:27',42,6),(251,'2012-10-26 09:31:02',42,6),(252,'2012-10-26 09:31:34',42,6),(253,'2012-11-07 23:44:54',42,6),(254,'2012-11-07 23:45:04',42,6),(255,'2012-12-04 11:09:58',42,6),(256,'2013-01-31 10:31:38',42,5),(257,'2012-10-18 15:43:40',44,6),(258,'2012-10-18 15:44:17',44,6),(259,'2012-12-21 11:32:47',44,6),(260,'2012-12-21 11:32:55',44,6),(261,'2012-12-21 11:33:09',44,6),(262,'2012-10-18 15:44:02',45,6),(263,'2012-10-18 15:46:09',45,6),(264,'2012-10-18 15:46:32',45,6),(265,'2012-12-21 11:33:35',45,6),(266,'2013-03-08 12:44:33',45,5),(267,'2012-10-18 15:46:42',43,6),(268,'2012-12-21 11:46:30',43,6),(269,'2013-03-08 12:43:31',43,5),(270,'2013-03-08 12:44:02',43,5),(271,'2012-10-26 09:30:03',46,6),(272,'2012-10-26 09:30:09',46,6),(273,'2012-11-07 23:48:23',47,6),(274,'2012-12-04 11:08:26',48,6),(275,'2012-12-04 11:08:45',48,6),(276,'2013-01-31 10:39:24',48,5),(277,'2013-03-08 12:36:23',48,5),(278,'2012-12-04 11:09:16',49,6),(279,'2012-12-04 11:09:20',49,6),(280,'2013-03-08 12:37:08',49,5),(281,'2012-12-04 11:09:46',50,6),(282,'2012-12-21 11:39:29',51,6),(283,'2012-12-21 11:39:35',51,6),(284,'2012-12-21 11:41:38',51,6),(285,'2012-12-21 11:47:19',52,5),(286,'2013-01-31 10:32:14',54,5),(287,'2013-01-31 10:34:19',54,5),(288,'2013-01-31 10:36:01',54,5),(289,'2013-01-31 10:36:19',54,5),(290,'2013-01-31 10:36:56',54,5),(291,'2013-01-31 10:37:31',54,5),(292,'2013-03-08 12:42:54',54,5),(293,'2013-01-31 10:37:14',55,5),(294,'2013-01-31 10:41:32',55,5),(295,'2013-01-31 10:35:55',56,5),(296,'2013-01-31 10:41:39',56,5),(297,'2013-01-31 10:37:45',57,5),(298,'2013-01-31 10:38:33',58,5),(299,'2013-01-31 10:38:48',58,5),(300,'2013-01-31 10:39:07',58,5),(301,'2013-03-08 12:30:01',58,5),(302,'2013-03-08 12:34:23',58,5),(303,'2013-01-31 10:39:34',59,5),(304,'2013-03-08 12:35:14',59,5),(305,'2013-03-08 12:36:19',59,5),(306,'2013-04-17 16:11:55',59,5),(307,'2013-03-08 12:33:33',60,5),(308,'2013-03-08 12:34:37',60,5),(309,'2013-03-08 12:38:06',60,5),(310,'2013-03-08 12:38:10',60,5),(311,'2013-03-08 12:38:30',60,5),(312,'2013-03-08 12:30:14',61,5),(313,'2013-03-08 12:30:27',61,5),(314,'2013-03-08 12:34:27',61,5),(315,'2013-04-17 16:12:14',61,5),(316,'2013-03-08 12:35:36',62,5),(317,'2013-03-08 12:35:45',62,5),(318,'2013-03-08 12:35:52',62,5),(319,'2013-03-08 12:36:12',62,5),(320,'2013-03-08 12:37:32',63,5),(321,'2013-03-08 12:38:03',63,5),(322,'2013-03-08 12:38:14',63,5),(323,'2013-03-08 12:38:25',63,5),(324,'2013-04-17 16:12:20',63,5),(325,'2013-04-17 16:12:43',63,5),(326,'2013-03-08 12:43:39',64,5),(327,'2013-03-08 12:44:14',65,5),(328,'2013-03-08 12:44:22',65,5),(329,'2013-04-17 16:15:53',65,5),(330,'2013-03-08 12:44:49',66,5),(331,'2013-04-17 16:12:01',66,5),(332,'2013-04-17 16:12:09',66,5),(333,'2013-03-08 12:45:45',9,5),(334,'2013-03-08 12:45:54',9,5),(335,'2013-03-08 12:46:00',9,5),(336,'2013-03-08 12:46:17',67,5),(337,'2013-04-17 16:09:56',68,5),(338,'2013-04-17 16:10:11',69,5),(339,'2013-04-17 16:10:25',69,5),(340,'2013-04-17 16:10:51',70,5),(341,'2013-04-17 16:11:01',70,5),(342,'2013-04-17 16:11:23',71,5),(343,'2013-04-17 16:11:44',71,5),(344,'2013-04-17 16:11:32',72,5),(345,'2013-04-17 16:11:39',72,5),(346,'2013-04-17 16:12:34',73,5),(347,'2013-04-17 16:12:50',73,5),(348,'2013-04-17 16:12:59',73,5),(349,'2013-04-17 16:13:13',73,5),(350,'2013-04-17 16:13:34',74,5),(351,'2013-04-17 16:13:42',74,5),(352,'2013-04-17 16:13:59',75,5),(353,'2013-05-31 11:59:25',68,5),(354,'2013-05-31 12:00:56',68,5),(355,'2013-05-31 12:00:57',69,5),(356,'2013-05-31 12:01:09',68,5),(357,'2013-05-31 12:01:36',76,5),(358,'2013-05-31 12:01:45',68,5),(359,'2013-05-31 12:01:47',76,5),(360,'2013-05-31 12:02:21',68,5),(361,'2013-05-31 12:02:35',77,5),(362,'2013-05-31 12:03:12',78,5),(363,'2013-05-31 12:03:38',77,5),(364,'2013-05-31 12:03:56',79,5),(365,'2013-05-31 12:04:38',79,5),(366,'2013-05-31 12:04:48',77,5),(367,'2013-05-31 12:05:12',80,5),(368,'2013-05-31 12:05:57',41,5),(369,'2013-05-31 12:06:10',8,5),(370,'2013-05-31 12:06:26',81,5),(371,'2013-05-31 14:41:21',81,5),(372,'2013-05-31 14:57:55',14,5),(373,'2013-05-31 14:59:02',60,5),(374,'2013-05-31 14:59:07',81,5),(375,'2013-05-31 15:03:19',51,5),(376,'2013-05-31 15:03:28',23,5),(377,'2013-05-31 15:03:32',23,5),(378,'2013-05-31 15:03:37',21,5),(379,'2013-05-31 15:03:47',3,5),(380,'2013-05-31 15:07:05',81,5);
/*!40000 ALTER TABLE `story_access` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story_user`
--

DROP TABLE IF EXISTS `story_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `story_user` (
  `Story_id` int(11) NOT NULL,
  `User_id` int(11) NOT NULL,
  PRIMARY KEY (`Story_id`,`User_id`),
  KEY `FK5914FCD5E0E4BFA2` (`Story_id`),
  KEY `FK5914FCD5C1610AD2` (`User_id`),
  CONSTRAINT `FK5914FCD5C1610AD2` FOREIGN KEY (`User_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK5914FCD5E0E4BFA2` FOREIGN KEY (`Story_id`) REFERENCES `stories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `story_user`
--

LOCK TABLES `story_user` WRITE;
/*!40000 ALTER TABLE `story_user` DISABLE KEYS */;
INSERT INTO `story_user` VALUES (20,5),(22,5),(22,6),(23,5),(23,6),(24,6),(38,5),(38,6),(39,5),(39,6),(42,5),(42,6),(44,5),(44,6),(45,5),(47,6),(48,5),(48,6),(50,5),(50,6),(51,5),(52,5),(52,6),(53,5),(53,6),(55,5),(55,6),(56,5),(57,5),(58,5),(59,6),(60,5),(61,5),(64,5),(67,5),(76,5),(76,6),(78,5),(78,6),(79,5),(79,6),(80,5),(80,6),(81,5),(81,6);
/*!40000 ALTER TABLE `story_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `story_user_AUD`
--

DROP TABLE IF EXISTS `story_user_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `story_user_AUD` (
  `REV` int(11) NOT NULL,
  `Story_id` int(11) NOT NULL,
  `User_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`Story_id`,`User_id`),
  KEY `FK9A1CB82620258526` (`REV`),
  CONSTRAINT `FK9A1CB82620258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `story_user_AUD`
--

LOCK TABLES `story_user_AUD` WRITE;
/*!40000 ALTER TABLE `story_user_AUD` DISABLE KEYS */;
INSERT INTO `story_user_AUD` VALUES (12,20,5,0),(12,22,5,0),(12,22,6,0),(12,23,5,0),(12,24,6,0),(12,38,5,0),(12,38,6,0),(12,39,5,0),(12,39,6,0),(12,42,5,0),(12,42,6,0),(12,44,5,0),(12,44,6,0),(12,45,5,0),(12,47,6,0),(12,48,5,0),(12,48,6,0),(12,50,5,0),(12,50,6,0),(12,51,5,0),(12,52,5,0),(12,52,6,0),(12,53,5,0),(12,53,6,0),(12,55,5,0),(12,55,6,0),(12,56,5,0),(12,57,5,0),(12,58,5,0),(12,59,6,0),(12,59,11,0),(12,60,5,0),(12,61,5,0),(12,64,5,0),(12,67,5,0),(23,76,5,0),(23,76,6,0),(31,78,5,0),(31,78,6,0),(34,79,5,0),(34,79,6,0),(40,80,5,0),(40,80,6,0),(54,81,5,0),(54,81,6,0),(54,81,12,0),(57,23,6,0),(72,81,12,2),(73,59,11,2);
/*!40000 ALTER TABLE `story_user_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storyrank`
--

DROP TABLE IF EXISTS `storyrank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storyrank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rank` int(11) NOT NULL,
  `backlog_id` int(11) NOT NULL,
  `story_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `backlog_id` (`backlog_id`,`story_id`),
  KEY `FK6600C2A1E0E4BFA2` (`story_id`),
  KEY `FK6600C2A1F63400A2` (`backlog_id`),
  CONSTRAINT `FK6600C2A1E0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`),
  CONSTRAINT `FK6600C2A1F63400A2` FOREIGN KEY (`backlog_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storyrank`
--

LOCK TABLES `storyrank` WRITE;
/*!40000 ALTER TABLE `storyrank` DISABLE KEYS */;
INSERT INTO `storyrank` VALUES (3,3,7,7),(4,2,7,13),(5,0,7,17),(6,1,7,18),(7,4,7,25),(8,5,7,32),(9,6,7,33),(10,7,7,34),(11,1,8,12),(12,0,8,19),(13,2,8,29),(14,6,8,30),(15,7,8,31),(16,3,8,35),(17,4,8,36),(18,5,8,38),(19,3,9,26),(20,4,9,27),(21,0,9,37),(22,1,9,39),(23,5,9,40),(24,2,9,42),(25,10,9,44),(26,6,9,46),(27,7,9,47),(28,8,9,48),(29,9,9,50),(30,0,11,17),(31,1,12,13),(32,0,12,18),(33,3,12,25),(34,2,12,32),(35,1,13,12),(36,2,13,19),(37,3,13,29),(38,4,13,35),(39,5,13,36),(40,2,14,23),(41,3,14,38),(42,1,14,52),(43,0,14,53),(44,1,15,46),(45,0,15,47),(46,0,16,39),(47,1,16,42),(48,4,16,44),(49,2,16,48),(50,3,16,50),(51,20,10,41),(52,0,10,45),(53,2,10,55),(54,1,10,56),(55,3,10,57),(56,4,10,58),(57,5,10,59),(58,7,10,60),(59,6,10,61),(60,8,10,62),(61,9,10,63),(62,10,10,64),(63,11,10,65),(64,12,10,66),(65,13,10,67),(66,14,10,69),(67,15,10,70),(68,16,10,72),(69,17,10,73),(70,18,10,74),(71,19,10,75),(72,2,17,55),(73,3,17,56),(74,0,17,58),(75,1,17,59),(76,0,18,57),(77,1,18,61),(78,2,18,63),(79,3,18,66),(80,0,19,60),(81,1,19,62),(82,2,19,64),(83,3,19,67),(84,6,20,41),(85,5,20,65),(86,0,20,69),(87,1,20,70),(88,2,20,72),(89,7,20,73),(90,3,20,74),(91,4,20,75),(92,21,10,76),(94,1,21,76),(96,22,10,78),(98,3,21,78),(99,23,10,79),(101,4,21,79),(102,24,10,80),(104,2,21,80),(105,25,10,81),(107,0,21,81);
/*!40000 ALTER TABLE `storyrank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storyrank_AUD`
--

DROP TABLE IF EXISTS `storyrank_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storyrank_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `backlog_id` int(11) DEFAULT NULL,
  `story_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK1CD8B7F220258526` (`REV`),
  CONSTRAINT `FK1CD8B7F220258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storyrank_AUD`
--

LOCK TABLES `storyrank_AUD` WRITE;
/*!40000 ALTER TABLE `storyrank_AUD` DISABLE KEYS */;
INSERT INTO `storyrank_AUD` VALUES (3,12,0,3,7,7),(4,12,0,2,7,13),(5,12,0,0,7,17),(6,12,0,1,7,18),(7,12,0,4,7,25),(8,12,0,5,7,32),(9,12,0,6,7,33),(10,12,0,7,7,34),(11,12,0,1,8,12),(12,12,0,0,8,19),(13,12,0,2,8,29),(14,12,0,6,8,30),(15,12,0,7,8,31),(16,12,0,3,8,35),(17,12,0,4,8,36),(18,12,0,5,8,38),(19,12,0,3,9,26),(20,12,0,4,9,27),(21,12,0,0,9,37),(22,12,0,1,9,39),(23,12,0,5,9,40),(24,12,0,2,9,42),(25,12,0,10,9,44),(26,12,0,6,9,46),(27,12,0,7,9,47),(28,12,0,8,9,48),(29,12,0,9,9,50),(30,12,0,0,11,17),(31,12,0,1,12,13),(32,12,0,0,12,18),(33,12,0,3,12,25),(34,12,0,2,12,32),(35,12,0,1,13,12),(36,12,0,2,13,19),(37,12,0,3,13,29),(38,12,0,4,13,35),(39,12,0,5,13,36),(40,12,0,2,14,23),(41,12,0,3,14,38),(42,12,0,1,14,52),(43,12,0,0,14,53),(44,12,0,1,15,46),(45,12,0,0,15,47),(46,12,0,0,16,39),(47,12,0,1,16,42),(48,12,0,4,16,44),(49,12,0,2,16,48),(50,12,0,3,16,50),(51,12,0,20,10,41),(52,12,0,0,10,45),(53,12,0,2,10,55),(54,12,0,1,10,56),(55,12,0,3,10,57),(56,12,0,4,10,58),(57,12,0,5,10,59),(58,12,0,7,10,60),(59,12,0,6,10,61),(60,12,0,8,10,62),(61,12,0,9,10,63),(62,12,0,10,10,64),(63,12,0,11,10,65),(64,12,0,12,10,66),(65,12,0,13,10,67),(66,12,0,14,10,69),(67,12,0,15,10,70),(68,12,0,16,10,72),(69,12,0,17,10,73),(70,12,0,18,10,74),(71,12,0,19,10,75),(72,12,0,2,17,55),(73,12,0,3,17,56),(74,12,0,0,17,58),(75,12,0,1,17,59),(76,12,0,0,18,57),(77,12,0,1,18,61),(78,12,0,2,18,63),(79,12,0,3,18,66),(80,12,0,0,19,60),(81,12,0,1,19,62),(82,12,0,2,19,64),(83,12,0,3,19,67),(84,12,0,6,20,41),(85,12,0,5,20,65),(86,12,0,0,20,69),(87,12,0,1,20,70),(88,12,0,2,20,72),(89,12,0,7,20,73),(90,12,0,3,20,74),(91,12,0,4,20,75),(92,20,0,21,10,76),(94,22,0,0,21,76),(94,58,1,1,21,76),(95,25,0,22,10,77),(95,26,2,22,10,77),(96,26,0,22,10,78),(98,30,0,0,21,78),(98,33,1,1,21,78),(98,51,1,2,21,78),(98,58,1,3,21,78),(99,32,0,23,10,79),(101,33,0,2,21,79),(101,51,1,3,21,79),(101,58,1,4,21,79),(102,36,0,24,10,80),(104,38,0,3,21,80),(104,51,1,1,21,80),(104,58,1,2,21,80),(105,43,0,25,10,81),(107,53,0,4,21,81),(107,58,1,0,21,81);
/*!40000 ALTER TABLE `storyrank_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_user`
--

DROP TABLE IF EXISTS `task_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_user` (
  `tasks_id` int(11) NOT NULL,
  `responsibles_id` int(11) NOT NULL,
  PRIMARY KEY (`tasks_id`,`responsibles_id`),
  KEY `FKAC91A45B1C109E9` (`tasks_id`),
  KEY `FKAC91A4527F4B120` (`responsibles_id`),
  CONSTRAINT `FKAC91A4527F4B120` FOREIGN KEY (`responsibles_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKAC91A45B1C109E9` FOREIGN KEY (`tasks_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_user`
--

LOCK TABLES `task_user` WRITE;
/*!40000 ALTER TABLE `task_user` DISABLE KEYS */;
INSERT INTO `task_user` VALUES (2,6),(3,5),(4,5),(4,6),(5,5),(6,5);
/*!40000 ALTER TABLE `task_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_user_AUD`
--

DROP TABLE IF EXISTS `task_user_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_user_AUD` (
  `REV` int(11) NOT NULL,
  `tasks_id` int(11) NOT NULL,
  `responsibles_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`tasks_id`,`responsibles_id`),
  KEY `FKF0A49D9620258526` (`REV`),
  CONSTRAINT `FKF0A49D9620258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_user_AUD`
--

LOCK TABLES `task_user_AUD` WRITE;
/*!40000 ALTER TABLE `task_user_AUD` DISABLE KEYS */;
INSERT INTO `task_user_AUD` VALUES (12,2,6,0),(12,3,5,0),(12,4,5,0),(12,4,6,0),(12,5,5,0),(12,6,5,0);
/*!40000 ALTER TABLE `task_user_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `effortleft` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `originalestimate` bigint(20) DEFAULT NULL,
  `rank` int(11) NOT NULL DEFAULT '0',
  `state` int(11) NOT NULL,
  `iteration_id` int(11) DEFAULT NULL,
  `story_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6907B8E4157D2A2` (`iteration_id`),
  KEY `FK6907B8EE0E4BFA2` (`story_id`),
  CONSTRAINT `FK6907B8E4157D2A2` FOREIGN KEY (`iteration_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FK6907B8EE0E4BFA2` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (2,NULL,0,'The online demo has been set up to run Agilefant 2.5.0',120,1,5,NULL,14),(3,NULL,0,'Some ~20 7-days / evaluator instances have been set up',600,2,5,NULL,14),(4,NULL,NULL,'RECURRING, Daily: answer customer emails and post to the forums',NULL,1,4,14,NULL),(5,NULL,0,'Tell Chuck Norris to deal with it',0,1,5,NULL,52),(6,NULL,0,'Get Chuck enough beer as the reward',600,2,5,NULL,52);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks_AUD`
--

DROP TABLE IF EXISTS `tasks_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` longtext,
  `effortleft` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `originalestimate` bigint(20) DEFAULT NULL,
  `rank` int(11) DEFAULT '0',
  `state` int(11) DEFAULT NULL,
  `iteration_id` int(11) DEFAULT NULL,
  `story_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKBD8325F20258526` (`REV`),
  CONSTRAINT `FKBD8325F20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks_AUD`
--

LOCK TABLES `tasks_AUD` WRITE;
/*!40000 ALTER TABLE `tasks_AUD` DISABLE KEYS */;
INSERT INTO `tasks_AUD` VALUES (2,12,0,NULL,0,'The online demo has been set up to run Agilefant 2.5.0',120,1,5,NULL,14),(3,12,0,NULL,180,'Some ~20 7-days / evaluator instances have been set up',600,2,1,NULL,14),(3,56,1,NULL,0,'Some ~20 7-days / evaluator instances have been set up',600,2,5,NULL,14),(4,12,0,NULL,NULL,'RECURRING, Daily: Keep the evaluation instances running, deal out new ones and format old ones',NULL,1,4,14,NULL),(4,47,1,NULL,NULL,'RECURRING, Daily: answer customer emails and post to the forums',NULL,1,4,14,NULL),(5,12,0,NULL,0,'Tell Chuck Norris to deal with it',0,1,5,NULL,52),(6,12,0,NULL,0,'Get Chuck enough beer as the reward',600,2,5,NULL,52);
/*!40000 ALTER TABLE `tasks_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_iteration`
--

DROP TABLE IF EXISTS `team_iteration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team_iteration` (
  `Team_id` int(11) NOT NULL,
  `Iteration_id` int(11) NOT NULL,
  PRIMARY KEY (`Team_id`,`Iteration_id`),
  KEY `FKF2269B7B4157D2A2` (`Iteration_id`),
  KEY `FKF2269B7B745BA992` (`Team_id`),
  CONSTRAINT `FKF2269B7B4157D2A2` FOREIGN KEY (`Iteration_id`) REFERENCES `backlogs` (`id`),
  CONSTRAINT `FKF2269B7B745BA992` FOREIGN KEY (`Team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_iteration`
--

LOCK TABLES `team_iteration` WRITE;
/*!40000 ALTER TABLE `team_iteration` DISABLE KEYS */;
INSERT INTO `team_iteration` VALUES (2,14);
/*!40000 ALTER TABLE `team_iteration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_iteration_AUD`
--

DROP TABLE IF EXISTS `team_iteration_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team_iteration_AUD` (
  `REV` int(11) NOT NULL,
  `Team_id` int(11) NOT NULL,
  `Iteration_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`Team_id`,`Iteration_id`),
  KEY `FKFE5293CC20258526` (`REV`),
  CONSTRAINT `FKFE5293CC20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_iteration_AUD`
--

LOCK TABLES `team_iteration_AUD` WRITE;
/*!40000 ALTER TABLE `team_iteration_AUD` DISABLE KEYS */;
INSERT INTO `team_iteration_AUD` VALUES (12,2,14,0),(12,3,14,0),(71,3,14,2);
/*!40000 ALTER TABLE `team_iteration_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_product`
--

DROP TABLE IF EXISTS `team_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team_product` (
  `Team_id` int(11) NOT NULL,
  `Product_id` int(11) NOT NULL,
  PRIMARY KEY (`Team_id`,`Product_id`),
  KEY `FK65CE090D745BA992` (`Team_id`),
  KEY `FK65CE090DA7FE2362` (`Product_id`),
  CONSTRAINT `FK65CE090D745BA992` FOREIGN KEY (`Team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FK65CE090DA7FE2362` FOREIGN KEY (`Product_id`) REFERENCES `backlogs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_product`
--

LOCK TABLES `team_product` WRITE;
/*!40000 ALTER TABLE `team_product` DISABLE KEYS */;
INSERT INTO `team_product` VALUES (2,5),(2,6);
/*!40000 ALTER TABLE `team_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_product_AUD`
--

DROP TABLE IF EXISTS `team_product_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team_product_AUD` (
  `REV` int(11) NOT NULL,
  `Team_id` int(11) NOT NULL,
  `Product_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`Team_id`,`Product_id`),
  KEY `FK4E84E85E20258526` (`REV`),
  CONSTRAINT `FK4E84E85E20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_product_AUD`
--

LOCK TABLES `team_product_AUD` WRITE;
/*!40000 ALTER TABLE `team_product_AUD` DISABLE KEYS */;
INSERT INTO `team_product_AUD` VALUES (12,2,5,0),(12,2,6,0),(12,3,5,0),(71,3,5,2);
/*!40000 ALTER TABLE `team_product_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_user`
--

DROP TABLE IF EXISTS `team_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team_user` (
  `User_id` int(11) NOT NULL,
  `Team_id` int(11) NOT NULL,
  KEY `FKF587546DC1610AD2` (`User_id`),
  KEY `FKF587546D745BA992` (`Team_id`),
  CONSTRAINT `FKF587546D745BA992` FOREIGN KEY (`Team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FKF587546DC1610AD2` FOREIGN KEY (`User_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_user`
--

LOCK TABLES `team_user` WRITE;
/*!40000 ALTER TABLE `team_user` DISABLE KEYS */;
INSERT INTO `team_user` VALUES (5,2),(6,2);
/*!40000 ALTER TABLE `team_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_user_AUD`
--

DROP TABLE IF EXISTS `team_user_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team_user_AUD` (
  `REV` int(11) NOT NULL,
  `Team_id` int(11) NOT NULL,
  `User_id` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`Team_id`,`User_id`),
  KEY `FK7FE983BE20258526` (`REV`),
  CONSTRAINT `FK7FE983BE20258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_user_AUD`
--

LOCK TABLES `team_user_AUD` WRITE;
/*!40000 ALTER TABLE `team_user_AUD` DISABLE KEYS */;
INSERT INTO `team_user_AUD` VALUES (12,2,5,0),(12,2,6,0),(12,3,6,0),(12,3,9,0),(12,3,10,0),(12,3,11,0),(12,3,12,0),(70,3,6,0);
/*!40000 ALTER TABLE `team_user_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teams` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES (2,NULL,'Admin team');
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams_AUD`
--

DROP TABLE IF EXISTS `teams_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teams_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKF6966C8720258526` (`REV`),
  CONSTRAINT `FKF6966C8720258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams_AUD`
--

LOCK TABLES `teams_AUD` WRITE;
/*!40000 ALTER TABLE `teams_AUD` DISABLE KEYS */;
INSERT INTO `teams_AUD` VALUES (2,12,0,NULL,'Admin team'),(3,12,0,NULL,'Team Agilefant'),(3,70,1,NULL,'Team Agilefant'),(3,71,2,NULL,'Team Agilefant');
/*!40000 ALTER TABLE `teams_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin` bit(1) DEFAULT b'1',
  `autoassignToStories` bit(1) DEFAULT b'0',
  `autoassignToTasks` bit(1) DEFAULT b'1',
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `initials` varchar(255) DEFAULT NULL,
  `loginName` varchar(255) DEFAULT NULL,
  `markStoryStarted` int(11) DEFAULT '1',
  `password` varchar(255) DEFAULT NULL,
  `recentItemsNumberOfWeeks` int(11) NOT NULL,
  `weekEffort` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginName` (`loginName`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (5,'','\0','',NULL,'','Administrator','Admin','admin',1,'5ebe2294ecd0e0f08eab7690d2a6ee69',16,NULL),(6,'','','','j@aa.com','\0','Jarno Vähäniitty','Jarno','jvahanii',1,'5ebe2294ecd0e0f08eab7690d2a6ee69',4,2235);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_AUD`
--

DROP TABLE IF EXISTS `users_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `initials` varchar(255) DEFAULT NULL,
  `loginName` varchar(255) DEFAULT NULL,
  `weekEffort` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK154C77D920258526` (`REV`),
  CONSTRAINT `FK154C77D920258526` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_AUD`
--

LOCK TABLES `users_AUD` WRITE;
/*!40000 ALTER TABLE `users_AUD` DISABLE KEYS */;
INSERT INTO `users_AUD` VALUES (3,13,1,NULL,'','Administrator','Admin','adminoriginal',NULL),(3,14,1,NULL,'','Administrator original','Admin','adminoriginal',NULL),(5,12,0,NULL,'','Administrator','Admin','admin1369925666014',NULL),(5,15,1,NULL,'','Administrator','Admin','admin',NULL),(6,12,0,'jarno@agilefant.org','','Jarno Vähäniitty','Jarno','jvahanii',2235),(6,59,1,'j@aa.com','','Jarno Vähäniitty','Jarno','jvahanii',2235),(6,69,1,'j@aa.com','\0','Jarno Vähäniitty','Jarno','jvahanii',2235),(7,12,0,NULL,'','readonly',NULL,'readonly1369925666021',NULL),(7,65,1,NULL,'\0','readonly',NULL,'readonly',NULL),(8,12,0,'nonadmin@gmail.com','','Non-admin user that has access to nothing (password: nonadmin - log in with this user and see what happens)','NonAdmin','nonadmin',0),(8,60,1,'nonadmin@gmail.com','','Non-admin user that has access to nothing (password: secret - log in with this user and see what happens)','NonAdmin','nonadmin',0),(8,68,1,'nonadmin@gmail.com','\0','Non-admin user that has access to nothing (password: secret - log in with this user and see what happens)','NonAdmin','nonadmin',0),(9,12,0,'petteri@petteri.fi','\0','Petteri Räty','Petteri','praty',0),(10,12,0,'j@j.com','\0','Joonas Javanainen','Javis','javis',0),(11,12,0,'v@v.com','\0','Ville Seppä','Ville','ville',0),(11,45,1,'v@v.com','','Ville Seppä','Ville','ville',0),(11,66,1,'v@v.com','\0','Ville Seppä','Ville','ville',0),(12,12,0,'s@s.com','\0','Santeri Korri','Santeri','santeri',0),(12,44,1,'s@s.com','','Santeri Korri','Santeri','santeri',0),(12,67,1,'s@s.com','\0','Santeri Korri','Santeri','santeri',0);
/*!40000 ALTER TABLE `users_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whatsnextentry`
--

DROP TABLE IF EXISTS `whatsnextentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `whatsnextentry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rank` int(11) NOT NULL DEFAULT '0',
  `task_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_id` (`task_id`,`user_id`),
  KEY `FK4B213410C1610AD2` (`user_id`),
  KEY `FK4B2134106E84F892` (`task_id`),
  CONSTRAINT `FK4B2134106E84F892` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`),
  CONSTRAINT `FK4B213410C1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whatsnextentry`
--

LOCK TABLES `whatsnextentry` WRITE;
/*!40000 ALTER TABLE `whatsnextentry` DISABLE KEYS */;
INSERT INTO `whatsnextentry` VALUES (1,1,4,6),(2,1,4,5);
/*!40000 ALTER TABLE `whatsnextentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whatsnextstoryentry`
--

DROP TABLE IF EXISTS `whatsnextstoryentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `whatsnextstoryentry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rank` int(11) NOT NULL DEFAULT '0',
  `story_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `story_id` (`story_id`,`user_id`),
  KEY `FK4B213410C1610AD3` (`user_id`),
  KEY `FK4B2134106E84F893` (`story_id`),
  CONSTRAINT `FK4B2134106E84F893` FOREIGN KEY (`story_id`) REFERENCES `stories` (`id`),
  CONSTRAINT `FK4B213410C1610AD3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whatsnextstoryentry`
--

LOCK TABLES `whatsnextstoryentry` WRITE;
/*!40000 ALTER TABLE `whatsnextstoryentry` DISABLE KEYS */;
INSERT INTO `whatsnextstoryentry` VALUES (3,2,45,5),(4,3,78,5),(5,4,80,5),(6,0,81,5),(7,1,76,5);
/*!40000 ALTER TABLE `whatsnextstoryentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgetcollections`
--

DROP TABLE IF EXISTS `widgetcollections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `widgetcollections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK26C78D1C1610AD2` (`user_id`),
  CONSTRAINT `FK26C78D1C1610AD2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `widgetcollections`
--

LOCK TABLES `widgetcollections` WRITE;
/*!40000 ALTER TABLE `widgetcollections` DISABLE KEYS */;
INSERT INTO `widgetcollections` VALUES (1,'Example custom portfolio view',NULL);
/*!40000 ALTER TABLE `widgetcollections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `widgets`
--

DROP TABLE IF EXISTS `widgets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `widgets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `listNumber` int(11) DEFAULT NULL,
  `objectId` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `widgetCollection_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4FE3EEAF8BACA792` (`widgetCollection_id`),
  CONSTRAINT `FK4FE3EEAF8BACA792` FOREIGN KEY (`widgetCollection_id`) REFERENCES `widgetcollections` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `widgets`
--

LOCK TABLES `widgets` WRITE;
/*!40000 ALTER TABLE `widgets` DISABLE KEYS */;
INSERT INTO `widgets` VALUES (1,1,23,0,'story',1),(2,0,21,1,'iterationMetrics',1),(3,1,10,1,'projectMetrics',1),(4,0,5,0,'userLoad',1);
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

-- Dump completed on 2013-08-15 14:57:27
