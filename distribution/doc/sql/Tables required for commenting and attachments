--
-- Table structure for table `story_comments`
-- story_comments table used for storing comments were added to a story
--

CREATE TABLE `story_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `storyId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `comment` varchar(10000) DEFAULT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_USER_COMMENT_CONSTRAINT` (`userId`),
  KEY `FK_STORY_CONSTRAINT` (`storyId`),
  CONSTRAINT `FK_STORY_CONSTRAINT` FOREIGN KEY (`storyId`) REFERENCES `stories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_USER_COMMENT_CONSTRAINT` FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=436 DEFAULT CHARSET=utf8;


--
-- Table structure for table `story_comments_AUD`
-- story_comments_AUD table used for auditing comments added to a story
--

CREATE TABLE `story_comments_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `storyId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `comment` varchar(10000) DEFAULT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK_comments_AUD` (`REV`),
  CONSTRAINT `FK_comments_AUD` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `task_comments`
-- task_comments table used for storing comments were added to a task
--

CREATE TABLE `task_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `comment` varchar(10000) DEFAULT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_USER_TASK_COMMENT_CONSTRAINT` (`userId`),
  KEY `FK_TASK_CONSTRAINT` (`taskId`),
  CONSTRAINT `FK_TASK_CONSTRAINT` FOREIGN KEY (`taskId`) REFERENCES `tasks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_USER_TASK_COMMENT_CONSTRAINT` FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;



--
-- Table structure for table `task_comments_AUD`
-- task_comments_AUD table used for auditing comments added to a story
--

CREATE TABLE `task_comments_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `taskId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `comment` varchar(10000) DEFAULT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK_task_comments_AUD` (`REV`),
  CONSTRAINT `FK_task_comments_AUD` FOREIGN KEY (`REV`) REFERENCES `agilefant_revisions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `attachments`
-- Attachment table used for keeping attachments information
--

CREATE TABLE `attachments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `storyCommentId` int(11) DEFAULT NULL,
  `taskCommentId` int(11) DEFAULT NULL,
  `attachmentLocation` varchar(1024) NOT NULL,
  `orginalFileName` varchar(250) NOT NULL,
  `fileContentType` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_STORY_COMMENT_ATTACHMENT` (`storyCommentId`),
  KEY `FK_TASK_COMMENT_ATTACHMENT` (`taskCommentId`)
) ENGINE=InnoDB AUTO_INCREMENT=358 DEFAULT CHARSET=utf8;



--
-- Table structure for table `attachments_AUD`
-- Attachment_AUD table used for keeping the logs of attachments
--


CREATE TABLE `attachments_AUD` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `storyCommentId` int(11) DEFAULT NULL,
  `taskCommentId` int(11) DEFAULT NULL,
  `attachmentLocation` varchar(1024) NOT NULL,
  `orginalFileName` varchar(250) NOT NULL,
  `fileContentType` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=358 DEFAULT CHARSET=utf8;


--
--Insert attachment configuration into settings table 
--
--
-- Attachment location config

Insert into settings (description,name,value) values ('Location for saving attachments','AttachmentLocation','/export/agilefant_attachments');

--Attachment MaxSize

Insert into settings (description,name,value) values ('Maximum size for all attachments','MaxSizeOfAllAttachment','25');

--Number of attachment per comment

Insert into settings (description,name,value) values ('Maximum Number of attachment per comment','MaxNumberOfAttachment','5');
