-- Updating student information
DELIMITER //
CREATE PROCEDURE update_student(
    IN student_id INT,
    IN new_fname VARCHAR(40),
    IN new_lname VARCHAR(40),
    IN new_email VARCHAR(40),
    IN new_gender ENUM('male', 'female'),
    IN new_department_id INT,
    IN new_dob DATE
)
BEGIN
    UPDATE student
    SET fname = new_fname,
        lname = new_lname,
        email = new_email,
        gender = new_gender,
        department_id = new_department_id,
        dob = new_dob
    WHERE id = student_id;
END //
DELIMITER ;
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+--+-+-+-+-+
DROP FUNCTION IF EXISTS calculate_student_gpa;

DELIMITER //
CREATE FUNCTION calculate_student_gpa(student_id INT) RETURNS DECIMAL(5, 2) DETERMINISTIC
BEGIN
    DECLARE total_credits INT DEFAULT 0;
    DECLARE total_grade_points DECIMAL(5, 2) DEFAULT 0;
    DECLARE student_gpa DECIMAL(5, 2) DEFAULT 0;
    SELECT SUM(CASE
					WHEN g.grade >= 90 THEN 4.0
                    WHEN g.grade >= 85 THEN 3.7
                    WHEN g.grade >= 80 THEN 3.3
                    WHEN g.grade >= 75 THEN 3.0
                    WHEN g.grade >= 70 THEN 2.7
                    WHEN g.grade >= 65 THEN 2.3
                    WHEN g.grade >= 60 THEN 2.0
                    WHEN g.grade >= 55 THEN 1.7
                    WHEN g.grade >= 50 THEN 1.3
                    ELSE 0.0
                END * c.hours),
           SUM(c.hours) INTO total_grade_points, total_credits
    FROM grade g
    JOIN course c ON g.course_id = c.id
    WHERE g.student_id = student_id;

    IF total_credits > 0 THEN
        SET student_gpa = total_grade_points / total_credits;
    END IF;

    RETURN student_gpa;
END //
DELIMITER ;


-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
drop trigger before_insert_grade;
DELIMITER //
CREATE TRIGGER before_insert_grade
BEFORE INSERT ON grade
FOR EACH ROW
BEGIN
    DECLARE existing_grade INT;

    SELECT grade INTO existing_grade
    FROM grade
    WHERE student_id = NEW.student_id AND course_id = NEW.course_id and grade >= 50 ;

    IF existing_grade IS NOT NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot insert a new grade. Student has already succeeded in the course.';
    END IF;
END;
//
DELIMITER ;

delete from grade where student_id =1 and course_id = 2;
INSERT INTO grade (student_id, course_id, grade) VALUES 
(1,2,60);



-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+

-- the used one
drop function calculate_course_avg_gpa;
DELIMITER //
CREATE FUNCTION calculate_course_avg_gpa(course_id INT) 
RETURNS DECIMAL(5, 2) DETERMINISTIC
BEGIN
    DECLARE avg_gpa DECIMAL(5, 2) DEFAULT 0;

    SELECT AVG(CASE
                    WHEN g.grade >= 90 THEN 4.0
                    WHEN g.grade >= 85 THEN 3.7
                    WHEN g.grade >= 80 THEN 3.3
                    WHEN g.grade >= 75 THEN 3.0
                    WHEN g.grade >= 70 THEN 2.7
                    WHEN g.grade >= 65 THEN 2.3
                    WHEN g.grade >= 60 THEN 2.0
                    WHEN g.grade >= 55 THEN 1.7
                    WHEN g.grade >= 50 THEN 1.3
                    ELSE 0.0
                END) INTO avg_gpa
    FROM grade g
    WHERE g.course_id = course_id;

    RETURN avg_gpa;
END //
DELIMITER ;





-- select the students of a specific course in a specific department 
select * from student s join student_course sc on s.id = sc.student_id 
join course c on sc.course_id = c.id
where c.id = 1;

select c.code , COUNT(s.id) as total_students
from student s join student_course sc on s.id = sc.student_id 
join course c on sc.course_id = c.id
group by c.id;

select *, calculate_course_avg_gpa(id) as avg_gpa
from course ;



-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
-- +-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-++-+--++-+-+-+-++--++++-+-+-+-+-+
drop function calculate_level;

DELIMITER //
CREATE FUNCTION calculate_level(studentId INT)
RETURNS VARCHAR(10) DETERMINISTIC
BEGIN
    DECLARE totalHours INT;
    DECLARE studentLevel VARCHAR(10);

    -- Calculate total hours of courses passed by the student
    SELECT SUM(c.hours) INTO totalHours
    FROM grade g
    JOIN course c ON g.course_id = c.id
    WHERE g.student_id = studentId
    and g.grade >= 50;

    -- Determine the level based on total hours
    IF totalHours < 10 THEN
        SET studentLevel = 'Level 1';
    ELSEIF totalHours < 20 THEN
        SET studentLevel = 'Level 2';
    ELSEIF totalHours < 30 THEN
        SET studentLevel = 'Level 3';
    ELSEIF totalHours < 40 THEN
        SET studentLevel = 'Level 4';
    END IF;

    RETURN studentLevel;
END //

DELIMITER ;
-- select students and the number of hours they passed along with thier level 
select s.email , sum(c.hours) as total_hours , calculate_level(s.id) as level
from student s join  grade sc on s.id = sc.student_id join course c on sc.course_id = c.id  
group by s.id;

select calculate_level(id) as level 
from student where id = 1;






