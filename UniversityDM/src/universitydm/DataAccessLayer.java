package universitydm;

import dto.CourseDTO;
import dto.DepartmentCourseDTO;
import dto.DepartmentDTO;
import dto.GradeDTO;
import dto.StudentCourseDTO;
import dto.StudentDTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

public class DataAccessLayer {

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public Connection ConnectDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/dm_case_study", "root", "root");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connect;
    }

    public int login(String username, String password) {
        try {
            connect = ConnectDb();
            System.out.println(username + password);
            String sql = "select * from admin where username = ? and password = ?;";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username);
            prepare.setString(2, password);

            result = prepare.executeQuery();
            if (result.next()) {
                return 1;

            } else {
                return 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<StudentDTO> getAllStudents() {
        StudentDTO student;
        List<StudentDTO> studentList = new ArrayList<StudentDTO>();
        try {
            connect = ConnectDb();
            String sql = "SELECT * FROM student";
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                //int id, String fname, String lname, String email, String gender, int departmentId, Date dob
                student = new StudentDTO(
                        result.getInt("id"),
                        result.getString("fname"),
                        result.getString("lname"),
                        result.getString("email"),
                        result.getString("gender"),
                        result.getInt("department_id"),
                        result.getDate("dob")
                );
                studentList.add(student);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentList;
    }

    public boolean addStudent(String fname, String lname, String email, String gender, int department_id, LocalDate dob) {
        try {
            connect = ConnectDb();

            // Check if the student with the same details already exists
            String checkStatement = "SELECT * FROM student WHERE fname = ? AND lname = ? AND email = ? AND gender = ? AND department_id = ? AND dob = ?";

            // Insert the student
            String insertStatement = "INSERT INTO student (fname, lname, email, gender, department_id, dob) VALUES (?, ?, ?, ?, ?, ?);";

            try {
                // Check if the student already exists
                prepare = connect.prepareStatement(checkStatement);
                prepare.setString(1, fname);
                prepare.setString(2, lname);
                prepare.setString(3, email);
                prepare.setString(4, gender);
                prepare.setInt(5, department_id);
                prepare.setDate(6, Date.valueOf(dob));
                result = prepare.executeQuery();
                if (result.next()) {
                    return false; // Student with the same details already exists
                }

                prepare = connect.prepareStatement(insertStatement);
                prepare.setString(1, fname);
                prepare.setString(2, lname);
                prepare.setString(3, email);
                prepare.setString(4, gender);
                prepare.setInt(5, department_id);
                prepare.setDate(6, Date.valueOf(dob));

                int rowsAffected = prepare.executeUpdate();

                // Return true if the insertion was successful
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStudent(int studentId, String fname, String lname, String email, String gender, LocalDate dob, int departmentId) {
        try {
            connect = ConnectDb();

            // Call the MySQL stored procedure
            String callStatement = "{CALL update_student(?, ?, ?, ?, ?, ?, ?)}";

            try {
                // Update the student information using the stored procedure
                prepare = connect.prepareCall(callStatement);
                prepare.setInt(1, studentId);
                prepare.setString(2, fname);
                prepare.setString(3, lname);
                prepare.setString(4, email);
                prepare.setString(5, gender);
                prepare.setInt(6, departmentId);
                prepare.setDate(7, Date.valueOf(dob));

                int rowsAffected = prepare.executeUpdate();

                return rowsAffected > 0;
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CourseDTO> getAllCourses() {
        CourseDTO course;
        List<CourseDTO> courseList = new ArrayList<CourseDTO>();
        try {
            connect = ConnectDb();
            String sql = "SELECT c.id, c.name, c.hours, c.code AS course_code, d.id as dep_id\n"
                    + "FROM course c\n"
                    + "JOIN department_course dc ON c.id = dc.course_id\n"
                    + "JOIN department d ON dc.department_id = d.id;";
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                //int id, String name, int hours, String code, int departmentId
                course = new CourseDTO(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("hours"),
                        result.getString("course_code"),
                        result.getInt("dep_id")
                );
                courseList.add(course);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return courseList;
    }

    public boolean addCourse(String name, String code, int hours, int department_id) {
        try {
            connect = ConnectDb();

            // Check if the course with the same details already exists
            String checkStatement = "SELECT * FROM course WHERE code = ? ";
            String checkDepartments = "select id from department where id = ?";

            // Insert the course, associating it with a department
            String insertStatement = "INSERT INTO course (name, code, hours) VALUES (?, ?, ?);";
            String assignToDepartmentStatement = "INSERT INTO department_course (department_id, course_id) VALUES (?, LAST_INSERT_ID());";
            String insertToDepartmentOnly = "insert into department_course (course_id , department_id)\n"
                    + "VALUES ( (select id from course where code = ?) , ?);";
            try {
                // Check if the course already exists
                prepare = connect.prepareStatement(checkStatement);
                prepare.setString(1, code);
                result = prepare.executeQuery();
                if (result.next()) {
                    prepare = connect.prepareStatement(checkDepartments);
                    prepare.setInt(1, department_id);
                    result = prepare.executeQuery();
                    if (result.next()) {
                        // Associate the course with the specified department
                        prepare = connect.prepareStatement(insertToDepartmentOnly);
                        prepare.setString(1, code);
                        prepare.setInt(2, department_id);
                        int rowsAffected = prepare.executeUpdate();

                        // Return true if the assignment was successful
                        return rowsAffected > 0;

                    }
                    return false; // Course with the same details already exists
                }
                prepare = connect.prepareStatement(checkDepartments);
                prepare.setInt(1, department_id);
                result = prepare.executeQuery();
                if (result.next()) {// department with the same details already exists
                    // Insert the new course
                    prepare = connect.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
                    prepare.setString(1, name);
                    prepare.setString(2, code);
                    prepare.setInt(3, hours);
                    int rowsAffected = prepare.executeUpdate();

                    // Check if the insertion was successful
                    if (rowsAffected > 0) {
                        // Retrieve the auto-generated course_id
                        ResultSet generatedKeys = prepare.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int courseId = generatedKeys.getInt(1);

                            // Associate the course with the specified department
                            prepare = connect.prepareStatement(assignToDepartmentStatement);
                            prepare.setInt(1, department_id);
                            rowsAffected = prepare.executeUpdate();

                            // Return true if the assignment was successful
                            return rowsAffected > 0;
                        }

                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCourse(int courseId, String name, String code, int hours, int departmentId) {
        try {
            connect = ConnectDb();

            // Check if the course exists
            String checkStatement = "SELECT * FROM course WHERE id = ?";
            String checkDepartmetnt = "Select * from department_course WHERE course_id = ? "
                    + "and department_id = ?";
            // Update the course information
            String updateStatement = "UPDATE course SET name = ?, code = ?, hours = ? WHERE id = ?";
            String updateDepartmentStatement = "UPDATE department_course SET department_id = ? WHERE course_id = ?";

            try {
                // Check if the course exists
                prepare = connect.prepareStatement(checkStatement);
                prepare.setInt(1, courseId);
                result = prepare.executeQuery();
                if (result.next()) {
                    prepare = connect.prepareStatement(checkDepartmetnt);
                    prepare.setInt(1, courseId);
                    prepare.setInt(2, departmentId);
                    result = prepare.executeQuery();
                    if (result.next()) {
                        return false;
                    }
// Update the department association
                    prepare = connect.prepareStatement(updateDepartmentStatement);
                    prepare.setInt(1, departmentId);
                    prepare.setInt(2, courseId);
                    int departmentRowsAffected = prepare.executeUpdate();

                    // Return true if both updates were successful
                    return departmentRowsAffected > 0;

                }

                // Update the course information
                prepare = connect.prepareStatement(updateStatement);
                prepare.setString(1, name);
                prepare.setString(2, code);
                prepare.setInt(3, hours);
                prepare.setInt(4, courseId);

                int rowsAffected = prepare.executeUpdate();

                // Check if the update was successful
                if (rowsAffected > 0) {
                    // Update the department association
                    prepare = connect.prepareStatement(updateDepartmentStatement);
                    prepare.setInt(1, departmentId);
                    prepare.setInt(2, courseId);
                    int departmentRowsAffected = prepare.executeUpdate();

                    // Return true if both updates were successful
                    return departmentRowsAffected > 0;
                }
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                // Handle the case of duplicate email entry

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCourse(int courseId) {
        try {
            connect = ConnectDb();

            String checkStatement = "SELECT * FROM course WHERE id = ?";

            String deleteStatement = "DELETE FROM course WHERE id = ?";

            String deleteDepartmentStatement = "DELETE FROM department_course WHERE course_id = ?";

            try {
                // Check if the course exists
                connect.setAutoCommit(false); // Start a transaction
                prepare = connect.prepareStatement(checkStatement);
                prepare.setInt(1, courseId);
                result = prepare.executeQuery();

                if (!result.next()) {
                    connect.rollback(); // Rollback the transaction
                    return false; // Course does not exist
                }

                // Delete the course
                prepare = connect.prepareStatement(deleteDepartmentStatement);
                prepare.setInt(1, courseId);
                int rowsAffected = prepare.executeUpdate();

                // Check if the deletion was successful
                if (rowsAffected > 0) {
                    // Delete the department association
                    prepare = connect.prepareStatement(deleteStatement);
                    prepare.setInt(1, courseId);
                    int departmentRowsAffected = prepare.executeUpdate();

                    // Check if both deletions were successful
                    if (departmentRowsAffected > 0) {
                        connect.commit(); // Commit the transaction
                        return true;
                    } else {
                        connect.rollback(); // Rollback the transaction
                    }
                }
            } catch (SQLException ex) {
                try {
                    connect.rollback(); // Rollback the transaction on exception
                } catch (SQLException ex1) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex1);
                }
                ex.printStackTrace();
            } finally {
                try {
                    connect.setAutoCommit(true); // Reset auto-commit to true
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<GradeDTO> getAllGrades() {
        GradeDTO grade;
        List<GradeDTO> gradeList = new ArrayList<GradeDTO>();
        try {
            connect = ConnectDb();
            String sql = "select g.*,concat(s.fname, ' ' , s.lname) as student_name , c.name as course_name \n"
                    + "from grade g join student s on g.student_id = s.id \n"
                    + "join course c on g.course_id = c.id;";

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                //int id, int studentId, int courseId, int grade, String studentName, String courseName
                grade = new GradeDTO(
                        result.getInt("id"),
                        result.getInt("student_id"),
                        result.getInt("course_id"),
                        result.getInt("grade"),
                        result.getString("student_name"),
                        result.getString("course_name")
                );
                gradeList.add(grade);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gradeList;
    }

    public boolean addGrade(int studentId, int courseId, int grade) {
        try {
            connect = ConnectDb();

            // Check if the grade with the same details already exists
            String checkStatement = "SELECT * FROM grade WHERE student_id = ? AND course_id = ?";

            // Insert the grade
            String insertStatement = "INSERT INTO grade (student_id, course_id, grade) VALUES (?, ?, ?);";

            try {
                // Check if the grade already exists
                prepare = connect.prepareStatement(checkStatement);
                prepare.setInt(1, studentId);
                prepare.setInt(2, courseId);
                result = prepare.executeQuery();
                if (result.next()) {
                    return false; // Grade for the same student and course already exists
                }

                // Insert the new grade
                prepare = connect.prepareStatement(insertStatement);
                prepare.setInt(1, studentId);
                prepare.setInt(2, courseId);
                prepare.setInt(3, grade);

                int rowsAffected = prepare.executeUpdate();

                // Return true if the insertion was successful
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateGrade(int gradeId, int studentId, int courseId, int gradeValue) {
        try {
            connect = ConnectDb();

            // Check if the grade exists
            String checkStatement = "SELECT * FROM grade WHERE id = ?";

            // Update the grade information
            String updateStatement = "UPDATE grade SET student_id = ?, course_id = ?, grade = ? WHERE id = ?";

            try {
                // Check if the grade exists
                prepare = connect.prepareStatement(checkStatement);
                prepare.setInt(1, gradeId);
                result = prepare.executeQuery();
                if (!result.next()) {
                    return false; // Grade does not exist
                }

                // Update the grade information
                prepare = connect.prepareStatement(updateStatement);
                prepare.setInt(1, studentId);
                prepare.setInt(2, courseId);
                prepare.setInt(3, gradeValue);
                prepare.setInt(4, gradeId);

                int rowsAffected = prepare.executeUpdate();

                // Return true if the update was successful
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<StudentCourseDTO> getAllStudentCourses() {
        StudentCourseDTO studentCourse;
        List<StudentCourseDTO> studentCourseList = new ArrayList<StudentCourseDTO>();
        try {
            connect = ConnectDb();
            String sql = "SELECT \n"
                    + "    s.id AS student_id,\n"
                    + "    c.id AS course_id,\n"
                    + "    CONCAT(s.fname, ' ', s.lname) AS student_name,\n"
                    + "    c.name AS course_name\n"
                    + "FROM \n"
                    + "    student s\n"
                    + "JOIN \n"
                    + "    student_course sc ON s.id = sc.student_id\n"
                    + "JOIN \n"
                    + "    course c ON sc.course_id = c.id\n"
                    + "LEFT JOIN \n"
                    + "    grade g ON s.id = g.student_id AND c.id = g.course_id\n"
                    + "WHERE \n"
                    + "    g.grade IS NULL;";

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                //int studentId, int courseId, String studentName, String courseName
                studentCourse = new StudentCourseDTO(
                        result.getInt("student_id"),
                        result.getInt("course_id"),
                        result.getString("student_name"),
                        result.getString("course_name")
                );
                studentCourseList.add(studentCourse);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentCourseList;
    }

    public List<GradeDTO> getStudentGrades(int id) {
        GradeDTO grade;
        List<GradeDTO> gradeList = new ArrayList<GradeDTO>();
        try {
            connect = ConnectDb();
            String sql = "select g.*,concat(s.fname, ' ' , s.lname) as student_name , c.name as course_name \n"
                    + "from grade g join student s on g.student_id = s.id \n"
                    + "join course c on g.course_id = c.id"
                    + " where g.student_id = ?";

            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, id);
            result = prepare.executeQuery();
            while (result.next()) {
                //int id, int studentId, int courseId, int grade, String studentName, String courseName
                grade = new GradeDTO(
                        result.getInt("id"),
                        result.getInt("student_id"),
                        result.getInt("course_id"),
                        result.getInt("grade"),
                        result.getString("student_name"),
                        result.getString("course_name")
                );
                gradeList.add(grade);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gradeList;
    }

    public List<DepartmentCourseDTO> getDepartmentCourses(int id) {
        List<DepartmentCourseDTO> departmentCourses = new ArrayList<DepartmentCourseDTO>();;
        DepartmentCourseDTO departmentCourse;
        try {
            connect = ConnectDb();
            String sql = "select c.id , c.name , c.code , calculate_course_avg_gpa(c.id)as avg_gpa , count( sc.student_id) as num_students , dc.department_id as dep_id\n"
                    + "from course c join student_course sc \n"
                    + "on c.id = sc.course_id \n"
                    + "join department_course dc on c.id = dc.course_id\n"
                    + "where dc.department_id =?\n"
                    + "GROUP BY c.id;";

            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, id);
            result = prepare.executeQuery();
            while (result.next()) {

                departmentCourse = new DepartmentCourseDTO(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("code"),
                        result.getDouble("avg_gpa"),
                        result.getInt("num_students"),
                        result.getInt("num_students")
                );
                departmentCourses.add(departmentCourse);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return departmentCourses;

    }

    public String studentGpa(int id) {
        String gpa = "";
        try {
            connect = ConnectDb();
            String sql = "SELECT CALCULATE_STUDENT_GPA(id) AS student_gpa\n"
                    + "FROM student\n"
                    + "where id = ?;";

            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, id);
            result = prepare.executeQuery();
            if (result.next()) {
                gpa = result.getString("student_gpa");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gpa;
    }

    public String studentLevel(int id) {
        String level = "";
        try {
            connect = ConnectDb();
            String sql = "select calculate_level(id) as level \n"
                    + "from student where id = ?;";

            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, id);
            result = prepare.executeQuery();
            if (result.next()) {
                level = result.getString("level");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return level;
    }

    public int getTotalNumberOfStudents() {
        int totalStudents = 0;
        try {
            connect = ConnectDb();
            String sql = "SELECT COUNT(*) AS total_students FROM student;";

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                totalStudents = result.getInt("total_students");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalStudents;
    }

    public int getTotalNumberOfCourses() {
        int totalCourses = 0;
        try {
            connect = ConnectDb();
            String sql = "SELECT COUNT(*) AS total_courses FROM course;";

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                totalCourses = result.getInt("total_courses");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalCourses;
    }

    public int getTotalNumberOfDepartments() {
        int totalDepartments = 0;
        try {
            connect = ConnectDb();
            String sql = "SELECT COUNT(*) AS total_departments FROM department;";

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                totalDepartments = result.getInt("total_departments");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalDepartments;
    }

    public List<StudentDTO> getCourseStudents(int id) {
        List<StudentDTO> courseStudents = new ArrayList<StudentDTO>();
        StudentDTO student;

        try {
            connect = ConnectDb();
            String sql = "select * \n"
                    + "from student s join student_course sc\n"
                    + "on s.id = sc.student_id \n"
                    + "where sc.course_id = ?;";

            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, id);
            result = prepare.executeQuery();
            while (result.next()) {
                //int id, int studentId, int courseId, int grade, String studentName, String courseName
                student = new StudentDTO(
                        result.getInt("id"),
                        result.getString("fname"),
                        result.getString("lname"),
                        result.getString("email"),
                        result.getString("gender"),
                        result.getInt("department_id"),
                        result.getDate("dob")
                );
                courseStudents.add(student);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return courseStudents;
    }

    public List<DepartmentDTO> getAllDepartments() {
        DepartmentDTO department;
        List<DepartmentDTO> DepartmentList = new ArrayList<DepartmentDTO>();
        try {
            connect = ConnectDb();
            String sql = "select d.* , count(distinct s.id) as num_students , count(distinct c.id) as num_courses\n"
                    + "from department d left join department_course dc on d.id = dc.department_id \n"
                    + "left join course c on dc.course_id = c.id \n"
                    + "left join student_course sc on c.id = sc.course_id \n"
                    + "left join student s on d.id = s.department_id\n"
                    + "group by d.id;";
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                // int id, String name, String code, int numberOfStudents, int numberOfCourses
                department = new DepartmentDTO(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("code"),
                        result.getInt("num_students"),
                        result.getInt("num_courses")
                );
                DepartmentList.add(department);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DepartmentList;
    }

    public boolean addDepartment(String name, String code) {
        try {
            connect = ConnectDb();

            String checkStatement = "SELECT * FROM department WHERE code = ?";

            String insertStatement = "INSERT INTO department (name, code) VALUES (?, ?)";

            try {
                prepare = connect.prepareStatement(checkStatement);
                prepare.setString(1, code);
                result = prepare.executeQuery();
                if (result.next()) {
                    return false;
                }

                prepare = connect.prepareStatement(insertStatement);
                prepare.setString(1, name);
                prepare.setString(2, code);

                int rowsAffected = prepare.executeUpdate();

                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDepartment(int departmentId, String name, String code) {
        try {
            connect = ConnectDb();

            // Check if the department exists
            String checkStatement = "SELECT * FROM department WHERE id = ?";

            // Update the department information
            String updateStatement = "UPDATE department SET name = ?, code = ? WHERE id = ?";

            try {
                // Check if the department exists
                prepare = connect.prepareStatement(checkStatement);
                prepare.setInt(1, departmentId);
                result = prepare.executeQuery();
                if (!result.next()) {
                    return false; // Department does not exist
                }

                // Update the department information
                prepare = connect.prepareStatement(updateStatement);
                prepare.setString(1, name);
                prepare.setString(2, code);
                prepare.setInt(3, departmentId);

                int rowsAffected = prepare.executeUpdate();

                // Return true if the update was successful
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CourseDTO> getStudentAvailableCourses(int studentId) {
        CourseDTO course;
        List<CourseDTO> availableCoursesList = new ArrayList<>();

        try {
            connect = ConnectDb();
            String sql = "SELECT c.id, c.name, c.hours, c.code\n"
                    + "FROM course c\n"
                    + "WHERE NOT EXISTS (\n"
                    + "    SELECT 1\n"
                    + "    FROM student_course sc\n"
                    + "    WHERE sc.student_id = ?\n"
                    + "      AND sc.course_id = c.id\n"
                    + ");";

            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, studentId);
            result = prepare.executeQuery();

            while (result.next()) {
                // int id, String name, int hours, String code
                course = new CourseDTO(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("hours"),
                        result.getString("code")
                );
                availableCoursesList.add(course);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return availableCoursesList;
    }

    public boolean assignCourse(int studentId, int courseId) {
        try {
            connect = ConnectDb();

            // Check if the assignment already exists
            String checkStatement = "SELECT * FROM student_course WHERE student_id = ? AND course_id = ?";

            // Insert the assignment
            String insertStatement = "INSERT INTO student_course (student_id, course_id) VALUES (?, ?)";

            try {
                // Check if the assignment already exists
                prepare = connect.prepareStatement(checkStatement);
                prepare.setInt(1, studentId);
                prepare.setInt(2, courseId);
                result = prepare.executeQuery();
                if (result.next()) {
                    return false; // Assignment already exists
                }

                // Insert the new assignment
                prepare = connect.prepareStatement(insertStatement);
                prepare.setInt(1, studentId);
                prepare.setInt(2, courseId);
                int rowsAffected = prepare.executeUpdate();

                // Return true if the insertion was successful
                return rowsAffected > 0;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> studentsAvgAge() {
        List<Integer> ageDist = new ArrayList<Integer>();
        Integer x;

        try {
            connect = ConnectDb();
            String sql = "SELECT\n"
                    + "    FLOOR(DATEDIFF(CURDATE(), dob) / 365) AS age, COUNT(*) AS number_of_students\n"
                    + "FROM student GROUP BY age ORDER BY age;";

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                //ageDist.add();

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ageDist;
    }

    public List<StudentDTO> getStudentsByDepartment(int departmentId) {
        StudentDTO student;
        List<StudentDTO> studentList = new ArrayList<>();
        try {
            connect = ConnectDb();
            String sql = "SELECT id, email FROM student WHERE department_id = ?";
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, departmentId);
            result = prepare.executeQuery();

            while (result.next()) {
                student = new StudentDTO(
                        result.getInt("id"),
                        result.getString("email"),
                        departmentId
                );
                studentList.add(student);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentList;
    }

    public List<CourseDTO> getCoursesByDepartment(int departmentId) {
        List<CourseDTO> departmentCourses = new ArrayList<>();
        try {
            connect = ConnectDb();
            String sql = "SELECT c.id, c.name, c.code FROM course c "
                    + "JOIN department_course dc ON dc.course_id = c.id "
                    + "WHERE dc.department_id = ?";
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, departmentId);
            result = prepare.executeQuery();

            while (result.next()) {
                CourseDTO course = new CourseDTO(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("code")
                );
                departmentCourses.add(course);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return departmentCourses;
    }

}
