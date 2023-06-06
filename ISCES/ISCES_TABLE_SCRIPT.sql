CREATE DATABASE  IF NOT EXISTS `isces_directory`;
USE `isces_directory`;


DROP TABLE IF EXISTS `folder`;
DROP TABLE IF EXISTS `files`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `student`;
DROP TABLE IF EXISTS `candidate`;
DROP TABLE IF EXISTS `admin`;
DROP TABLE IF EXISTS `election`;
DROP TABLE IF EXISTS `department`;
DROP TABLE IF EXISTS `delegate`;



--
-- Table structure for ısces
--


CREATE TABLE `user` (
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `student` (
  `student_number` BIGINT NOT NULL,
  `department_id` BIGINT NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `grade` FLOAT NOT NULL,
  `term` BIGINT NOT NULL,
  `is_voted` TINYINT NOT NULL,
  `email` varchar(50) NOT NULL,
  `is_applied` TINYINT,
  PRIMARY KEY (`student_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `candidate` (
  `candidate_id` BIGINT NOT NULL,
  `votes` BIGINT NOT NULL,
  `student_number` BIGINT NOT NULL,
  PRIMARY KEY (`candidate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `admin` (
  `admin_id` BIGINT NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `department_id` BIGINT,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `election` (
  `election_id` BIGINT NOT NULL,
  `start_date` DATETIME NOT NULL,
  `end_date` DATETIME NOT NULL,
  `is_finished` TINYINT NOT NULL,
  PRIMARY KEY (`election_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `folder` (
  `folder_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `student_number` BIGINT NOT NULL,
  `folder_directory` VARCHAR(255)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `files` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `file_path` VARCHAR(255),
  `folder_id` BIGINT
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `department` (
  `department_id` BIGINT NOT NULL,
  `department_name` varchar(50) NOT NULL,
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `delegate` (
  `delegate_id` BIGINT NOT NULL,
  `candidate_id` BIGINT NOT NULL,
  PRIMARY KEY (`delegate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



--
-- Inserting data for tables
--

INSERT INTO `user`
VALUES
('ahmetozdemir@std.iyte.edu.tr', 'test123', 'student'),
('emrekaraduman@std.iyte.edu.tr', 'test123', 'student'),
('keremyasar@std.iyte.edu.tr', 'test123', 'student'),
('mehmetsonmez@std.iyte.edu.tr', 'test123', 'student'),
('ecesavran@std.iyte.edu.tr', 'test123', 'student'),
('bilgehanay@std.iyte.edu.tr', 'test123', 'student'),
('betulsoylek@std.iyte.edu.tr', 'test123', 'student'),
('gencayturgut@std.iyte.edu.tr', 'test123', 'student'),
('kaganyalim@std.iyte.edu.tr', 'test123', 'student'),
('cuneytalim@std.iyte.edu.tr', 'test123', 'student'),
('yusufolmez@std.iyte.edu.tr', 'test123', 'student'),
('hilmicem@std.iyte.edu.tr', 'test123', 'student'),
('husnudeler@std.iyte.edu.tr', 'test123', 'student'),
('baharcandan@std.iyte.edu.tr', 'test123', 'student'),
('silabozkurt@std.iyte.edu.tr', 'test123', 'student'),
('mesuttomay@std.iyte.edu.tr', 'test123', 'student'),
('alibicim@std.iyte.edu.tr', 'test123', 'student'),
('umutvarol@std.iyte.edu.tr', 'test123', 'student'),
('sevincaykurt@std.iyte.edu.tr', 'test123', 'student'),
('haliluyanik@std.iyte.edu.tr', 'test123', 'student'),
('officer1@ofc.iyte.edu.tr','test123','officer'),
('officer2@ofc.iyte.edu.tr','test123','officer'),
('officer3@ofc.iyte.edu.tr','test123','officer'),
('officer4@ofc.iyte.edu.tr','test123','officer'),
('rector@rct.iyte.edu.tr','test123','rector');





INSERT INTO `student`
VALUES
(270201051, 1, 'Ahmet', 'Ozdemir', 3.98, 3, 0, 'ahmetozdemir@std.iyte.edu.tr', false),
(270201052, 1, 'Emre', 'Karaduman', 3.99, 3, 0, 'emrekaraduman@std.iyte.edu.tr', false),
(270201053, 1, 'Kerem', 'Yasar', 2.22, 3, 0, 'keremyasar@std.iyte.edu.tr', false),
(270201054, 1, 'Mehmet', 'Sonmez', 2.43, 3, 0, 'mehmetsonmez@std.iyte.edu.tr', false),
(270201055, 1, 'Ece', 'Savran', 2.54, 3, 0, 'ecesavran@std.iyte.edu.tr', false),
(270201021, 2, 'Bilgehan', 'Ay', 3.51, 3, 0, 'bilgehanay@std.iyte.edu.tr', false),
(270201022, 2, 'Betul', 'Soylek', 3.41, 3, 0, 'betulsoylek@std.iyte.edu.tr', false),
(270201023, 2, 'Gencay', 'Turgut', 3.13, 3, 0, 'gencayturgut@std.iyte.edu.tr', false),
(270201024, 2, 'Kagan', 'Yalim', 3.23, 3, 0, 'kaganyalim@std.iyte.edu.tr', false),
(270201025, 2, 'Cuneyt', 'Alim', 3.14, 3, 0, 'cuneytalim@std.iyte.edu.tr', false),
(270201031, 3, 'Yusuf', 'Olmez', 3.32, 3, 0, 'yusufolmez@std.iyte.edu.tr', false),
(270201032, 3, 'Hilmi', 'Cem', 3.11, 3, 0, 'hilmicem@std.iyte.edu.tr', false),
(270201033, 3, 'Husnu', 'Deler', 3.41, 3, 0, 'husnudeler@std.iyte.edu.tr', false),
(270201034, 3, 'Bahar', 'Candan', 3.12, 3, 0, 'baharcandan@std.iyte.edu.tr', false),
(270201035, 3, 'Sila', 'Bozkurt', 3.05, 3, 0, 'silabozkurt@std.iyte.edu.tr', false),
(270201071, 4, 'Mesut', 'Tomay', 3.50, 3, 0, 'mesuttomay@std.iyte.edu.tr', false),
(270201072, 4, 'Ali', 'Bicim', 3.21, 3, 0, 'alibicim@std.iyte.edu.tr', false),
(270201073, 4, 'Umut', 'Varol', 1.59, 3, 0, 'umutvarol@std.iyte.edu.tr', false),
(270201074, 4, 'SevinÃ§', 'Aykurt', 2.22, 3, 0, 'sevincaykurt@std.iyte.edu.tr', false),
(270201075, 4, 'Halil', 'Uyanik', 3.41, 3, 0, 'haliluyanik@std.iyte.edu.tr', false);




INSERT INTO `admin`
VALUES
(1,'officer1firstname','officer1lastname','officer1@ofc.iyte.edu.tr',1),
(2,'officer2firstname','officer2lastname','officer2@ofc.iyte.edu.tr',2),
(3,'officer3firstname','officer3lastname','officer3@ofc.iyte.edu.tr',3),
(4,'officer4firstname','officer4lastname','officer4@ofc.iyte.edu.tr',4),
(5,'rectorfirstname','rectorlastname','rector@rct.iyte.edu.tr',null);

INSERT INTO `department`
VALUES
(1,'1'),
(2,'2'),
(3,'3'),
(4,'4');





