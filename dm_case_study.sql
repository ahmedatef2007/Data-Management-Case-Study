-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dm_case_study
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `username` varchar(40) DEFAULT NULL,
  `password` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('1','1');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `code` varchar(10) NOT NULL,
  `hours` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code_constraint` (`code`),
  CONSTRAINT `course_chk_1` CHECK ((`hours` in (2,3,6)))
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'Introduction to Programming','CS101',3),(2,'Database Management','CS201',6),(3,'Web Development','IT101',3),(4,'Data Structures','CS301',6),(5,'Mobile App Development','IT202',3),(6,'Software Design Patterns','SE101',2),(7,'Algorithms and Complexity','CS401',6),(8,'Computer Networks','CS501',3),(9,'Database Security','IT301',2),(10,'Software Testing','SE201',3),(11,'Machine Learning','CS601',6),(12,'Web Security','IT302',2),(13,'Agile Development','SE102',3),(14,'Data Mining','CS701',6),(15,'Cloud Computing','IT401',3),(16,'Mobile Game Development','SE202',2),(17,'Network Security','IT303',3),(18,'E-commerce Systems','IT304',2);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Computer Science','CS'),(2,'Information Technology','IT'),(3,'Computer Science','CS'),(4,'Data Science','DS'),(5,'Software Engineering','SE');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department_course`
--

DROP TABLE IF EXISTS `department_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department_course` (
  `department_id` int NOT NULL,
  `course_id` int NOT NULL,
  PRIMARY KEY (`department_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `department_course_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `department_course_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department_course`
--

LOCK TABLES `department_course` WRITE;
/*!40000 ALTER TABLE `department_course` DISABLE KEYS */;
INSERT INTO `department_course` VALUES (1,1),(4,2),(2,3),(1,4),(2,5),(3,6),(4,6),(1,7),(1,8),(2,9),(3,10),(1,11),(2,12),(3,13),(1,14),(2,15),(3,16),(2,17),(2,18);
/*!40000 ALTER TABLE `department_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grade`
--

DROP TABLE IF EXISTS `grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grade` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `course_id` int NOT NULL,
  `grade` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `student_id` (`student_id`,`course_id`),
  CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`student_id`, `course_id`) REFERENCES `student_course` (`student_id`, `course_id`) ON DELETE CASCADE,
  CONSTRAINT `grade_chk_1` CHECK (((`grade` >= 0) and (`grade` <= 100)))
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grade`
--

LOCK TABLES `grade` WRITE;
/*!40000 ALTER TABLE `grade` DISABLE KEYS */;
INSERT INTO `grade` VALUES (1,1,1,85),(2,2,2,92),(3,3,3,78),(4,7,1,90),(5,8,2,82),(6,9,3,75),(7,10,4,88),(8,11,5,94),(9,1,7,85),(10,2,8,92),(11,3,9,78),(12,4,10,88),(13,5,11,95),(14,6,12,82),(15,7,13,60),(16,8,14,76),(17,9,15,85),(18,10,16,94),(19,1,17,88),(20,2,18,75),(21,3,17,92),(22,4,18,80),(23,3,1,65),(24,5,1,90),(25,1,4,60),(26,2,4,60);
/*!40000 ALTER TABLE `grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fname` varchar(40) NOT NULL,
  `lname` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `gender` enum('male','female') NOT NULL,
  `department_id` int DEFAULT NULL,
  `dob` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email_constraint` (`email`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,'John','Doe','john.doe@example.com','male',1,'1995-05-15'),(2,'Jane','Smith','jane.smith@example.com','female',1,'1998-08-20'),(3,'Mike','Johnson','mike.johnson@example.com','male',4,'1997-03-10'),(4,'Emily','Williams','emily.williams@example.com','female',4,'1998-12-12'),(5,'Alex','Miller','alex.miller@example.com','male',4,'1999-04-25'),(6,'Sophia','Jones','sophia.jones@example.com','female',3,'1998-07-18'),(7,'Ahmed','Mohamed','ahmed.mohamed@example.com','male',1,'1997-09-08'),(8,'Fatima','Ali','fatima.ali@example.com','female',2,'1998-11-15'),(9,'Omar','Abdullah','omar.abdullah@example.com','male',1,'1996-03-20'),(10,'Nour','Hassan','nour.hassan@example.com','female',4,'1999-05-25'),(11,'Youssef','Ibrahim','youssef.ibrahim@example.com','male',1,'1997-07-12');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_course`
--

DROP TABLE IF EXISTS `student_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_course` (
  `student_id` int NOT NULL,
  `course_id` int NOT NULL,
  PRIMARY KEY (`student_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `student_course_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `student_course_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_course`
--

LOCK TABLES `student_course` WRITE;
/*!40000 ALTER TABLE `student_course` DISABLE KEYS */;
INSERT INTO `student_course` VALUES (1,1),(3,1),(4,1),(5,1),(7,1),(1,2),(2,2),(5,2),(8,2),(3,3),(6,3),(9,3),(1,4),(2,4),(10,4),(11,5),(1,7),(2,8),(3,9),(4,10),(5,11),(6,12),(7,13),(8,14),(9,15),(10,16),(1,17),(3,17),(2,18),(4,18);
/*!40000 ALTER TABLE `student_course` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-25  2:24:53
