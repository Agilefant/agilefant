-- 250-300

INSERT INTO settings (`name`, `value`, `description`) VALUES ('AgilefantDatabaseVersion', '300', 'Agilefant database version') ON DUPLICATE KEY UPDATE `value`="300";
ALTER TABLE users ADD COLUMN admin bit DEFAULT 1 AFTER id;
create table team_product (Team_id integer not null, Product_id integer not null) ENGINE=InnoDB;
alter table team_product add index FK65CE090D745BA992 (Team_id), add constraint FK65CE090D745BA992 foreign key (Team_id) references teams (id);
alter table team_product add index FK65CE090DA7FE2362 (Product_id), add constraint FK65CE090DA7FE2362 foreign key (Product_id) references backlogs (id);
ALTER TABLE backlogs ADD COLUMN readonlyToken varchar(255) unique;
INSERT INTO users (admin, fullName, password, loginName, enabled, recentItemsNumberOfWeeks) VALUES (0, "readonly", "5ebe2294ecd0e0f08eab7690d2a6ee69", "readonly", 1, 0);

-- Give all teams access to all products
INSERT INTO team_product (Team_id, Product_id)
SELECT A.id, B.id
FROM (SELECT id FROM teams) A,
     (SELECT id FROM backlogs WHERE backlogtype='Product') B;

-- Access rights for stand alone iterations
create table team_iteration (Team_id integer not null, Iteration_id integer not null) ENGINE=InnoDB;
alter table team_iteration add index FKF2269B7B4157D2A2 (Iteration_id), add constraint FKF2269B7B4157D2A2 foreign key (Iteration_id) references backlogs (id);
alter table team_iteration add index FKF2269B7B745BA992 (Team_id), add constraint FKF2269B7B745BA992 foreign key (Team_id) references teams (id);

-- Give all teams access to all standalone iterations
INSERT INTO team_iteration (Team_id, Iteration_id)
SELECT A.id, B.id
FROM (SELECT id FROM teams) A,
     (SELECT id FROM backlogs WHERE backlogtype='Iteration' AND parent_id IS NULL) B;


-- 300-301

     alter table stories add column iteration_id integer DEFAULT NULL;

alter table stories add CONSTRAINT `FK_iteration_id` FOREIGN KEY (`iteration_id`) REFERENCES `backlogs` (`id`);

alter table stories_AUD add column iteration_id integer DEFAULT NULL;

alter table stories modify column backlog_id integer DEFAULT NULL;

INSERT INTO settings (`name`, `value`, `description`) VALUES ('AgilefantDatabaseVersion', '301', 'Agilefant database version') ON DUPLICATE KEY UPDATE `value`="301";

DELIMITER //

DROP PROCEDURE IF EXISTS UpdateStoryIterationRefs //

CREATE PROCEDURE UpdateStoryIterationRefs()
BEGIN
  DECLARE stories_loop_done BOOL DEFAULT FALSE;
  DECLARE story_id INT;
  DECLARE backlog_id INT;
  DECLARE project_id INT;
  DECLARE cur_story
    CURSOR FOR
    SELECT stories.id, stories.backlog_id FROM stories, backlogs WHERE backlogs.id = stories.backlog_id AND backlogs.backlogtype = 'Iteration';

  DECLARE CONTINUE HANDLER FOR SQLSTATE '02000'
    SET stories_loop_done = TRUE;

  OPEN cur_story;

  storyLoop: LOOP
    FETCH cur_story INTO story_id, backlog_id;

    SELECT parent_id INTO project_id FROM backlogs WHERE id = backlog_id;

    IF stories_loop_done THEN
      CLOSE cur_story;
      LEAVE storyLoop;
    END IF;

    UPDATE stories SET iteration_id = backlog_id WHERE id = story_id;
    UPDATE stories SET backlog_id = project_id WHERE id = story_id;
  END LOOP;
END //

DELIMITER ;

CALL UpdateStoryIterationRefs();

DROP PROCEDURE IF EXISTS UpdateStoryIterationRefs;


-- 301-302

INSERT INTO settings (`name`, `value`, `description`) VALUES ('AgilefantDatabaseVersion', '302', 'Agilefant database version') ON DUPLICATE KEY UPDATE `value`="302";
DELETE storyrank FROM storyrank INNER JOIN backlogs ON storyrank.backlog_id = backlogs.id AND backlogs.backlogtype = 'Product';


-- 302-303

INSERT INTO settings (`name`, `value`, `description`) VALUES ('AgilefantDatabaseVersion', '303', 'Agilefant database version') ON DUPLICATE KEY UPDATE `value`="303";
create table team_user_AUD (REV integer not null, Team_id integer not null, User_id integer not null, REVTYPE tinyint, primary key (REV, Team_id, User_id)) ENGINE=InnoDB;
alter table team_user_AUD add index FK7FE983BE20258526 (REV), add constraint FK7FE983BE20258526 foreign key (REV) references agilefant_revisions (id);
create table teams_AUD (id integer not null, REV integer not null, REVTYPE tinyint, description longtext, name varchar(255), primary key (id, REV)) ENGINE=InnoDB;
alter table teams_AUD add index FKF6966C8720258526 (REV), add constraint FKF6966C8720258526 foreign key (REV) references agilefant_revisions (id);
create table team_iteration_AUD (REV integer not null, Team_id integer not null, Iteration_id integer not null, REVTYPE tinyint, primary key (REV, Team_id, Iteration_id)) ENGINE=InnoDB;
alter table team_iteration_AUD add index FKFE5293CC20258526 (REV), add constraint FKFE5293CC20258526 foreign key (REV) references agilefant_revisions (id);
create table team_product_AUD (REV integer not null, Team_id integer not null, Product_id integer not null, REVTYPE tinyint, primary key (REV, Team_id, Product_id)) ENGINE=InnoDB;
alter table team_product_AUD add index FK4E84E85E20258526 (REV), add constraint FK4E84E85E20258526 foreign key (REV) references agilefant_revisions (id);


-- 303-304

INSERT INTO settings (`name`, `value`, `description`) VALUES ('AgilefantDatabaseVersion', '304', 'Agilefant database version') ON DUPLICATE KEY UPDATE `value`="304";
DELETE history_backlogs FROM history_backlogs INNER JOIN backlogs ON history_backlogs.backlog_id = backlogs.id AND backlogs.backlogtype <> 'Project';


-- 304-311

-- Create a new team --
INSERT INTO teams (name, description) VALUES ('ALL-USERS', 'Automatically generated team used to grant all users access to all existing products when upgrading from 2.5 to 3.x');

-- Add all users to created team --
INSERT INTO team_user (Team_id, User_id)
SELECT A.id, B.id
FROM (SELECT id FROM teams WHERE name='ALL-USERS') A, (SELECT id FROM users ) B;

DELETE FROM team_user WHERE 
	User_id = (SELECT id FROM users WHERE loginName='readonly') AND
	Team_id = (SELECT id FROM teams WHERE name='ALL-USERS');

-- Give access to all products --
INSERT INTO team_product (Team_id, Product_id)
SELECT A.id, B.id
FROM (SELECT id FROM teams WHERE name='ALL-USERS') A,
     (SELECT id FROM backlogs WHERE backlogtype='Product') B;
     