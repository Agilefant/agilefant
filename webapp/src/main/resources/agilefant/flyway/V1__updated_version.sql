
CREATE TABLE `logins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK4B213410C1610AD4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

