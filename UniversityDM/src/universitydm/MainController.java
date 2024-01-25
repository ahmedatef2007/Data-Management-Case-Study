/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universitydm;

import dto.CourseDTO;
import dto.DepartmentCourseDTO;
import dto.DepartmentDTO;
import dto.GradeDTO;
import dto.StudentCourseDTO;
import dto.StudentDTO;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class MainController implements Initializable {

    @FXML
    private Button logout_btn;
    @FXML
    private Button home_btn;
    @FXML
    private Button courses_btn;
    @FXML
    private Button departments_btn;
    @FXML
    private Button grades_btn;
    @FXML
    private Button student_btn;
    @FXML
    private Label coursesNum_label;
    @FXML
    private Label studentNum_label;
    @FXML
    private Label departmentsNum_label;
    @FXML
    private BarChart<String, Number> studentAgeAVG_chart;
    @FXML
    private BarChart<String, Number> courseGradeAvg_chart;
    @FXML
    private BarChart<String, Number> departmentGradeAvg_chart;
    @FXML
    private TextField students_id_tf;
    @FXML
    private TextField students_fname_tf;
    @FXML
    private ComboBox<String> student_gender_cbox;
    @FXML
    private TextField students_lname_tf;
    @FXML
    private TextField students_email_tf;
    @FXML
    private DatePicker student_dob_dp;
    @FXML
    private Button addStudent_btn;
    @FXML
    private Button updateStudent_btn;
    @FXML
    private Button clearStudent_btn;
    @FXML
    private TableView<GradeDTO> student_courseGrade_tableview;
    @FXML
    private TableColumn<GradeDTO, String> student_courseGrade_tableview_course;
    @FXML
    private TableColumn<GradeDTO, Integer> student_courseGrade_tableview_grade;
    @FXML
    private Label gpa_label;
    @FXML
    private Label level_label;
    @FXML
    private TextField studentSearch_tf;
    @FXML
    private TableView<StudentDTO> students_tableview;
    @FXML
    private TableColumn<StudentDTO, Integer> students_tableview_id;
    @FXML
    private TableColumn<StudentDTO, String> students_tableview_fname;
    @FXML
    private TableColumn<StudentDTO, String> students_tableview_lname;
    @FXML
    private TableColumn<StudentDTO, String> students_tableview_email;
    @FXML
    private TableColumn<StudentDTO, String> students_tableview_gender;
    @FXML
    private TableColumn<StudentDTO, String> students_tableview_dob;
    @FXML
    private TableColumn<StudentDTO, Integer> students_tableview_depId;
    @FXML
    private TextField courses_id_tf;
    @FXML
    private TextField courses_name_tf;
    @FXML
    private ComboBox<Integer> courses_numHours_cbox;
    @FXML
    private TextField courses_depId_tf;
    @FXML
    private Button courseAdd_btn;
    @FXML
    private Button courseUpdate_btn;
    @FXML
    private Button courseClear_btn;
    @FXML
    private Button courseDelete_btn;

    @FXML
    private TableView<CourseDTO> courses_tableview;
    @FXML
    private TableColumn<CourseDTO, Integer> courses_tableview_id;
    @FXML
    private TableColumn<CourseDTO, String> courses_tableview_code;
    @FXML
    private TableColumn<CourseDTO, String> courses_tableview_name;
    @FXML
    private TableColumn<CourseDTO, Integer> courses_tableview_numHours;
    @FXML
    private TableColumn<CourseDTO, Integer> courses_tableview_depId;

    @FXML
    private TableView<StudentDTO> courses_enrolledStudents_tableview;
    @FXML
    private TableColumn<StudentDTO, Integer> courses_enrolledStudents_tableview_sid;
    @FXML
    private TableColumn<StudentDTO, String> courses_enrolledStudents_tableview_semail;
    @FXML
    private TextField studentGrade_studentId_tf;
    @FXML
    private TextField studentGrade_courseId_tf;
    @FXML
    private TextField studentGrade_grade_tf;
    @FXML
    private TableView<StudentCourseDTO> studentCourses_tableview;
    @FXML
    private TableColumn<StudentCourseDTO, String> studentCourses_studentName;
    @FXML
    private TableColumn<StudentCourseDTO, String> studentCourses_courseName;
    @FXML
    private TableColumn<StudentCourseDTO, Integer> studentCourses_studentID;
    @FXML
    private TableColumn<StudentCourseDTO, Integer> studentCourses_CourseID;

    @FXML
    private TextField studentCourses_search;
    @FXML
    private Button insertGrade_btn;
    @FXML
    private Button updateGrade_btn;
    @FXML
    private TableView<GradeDTO> studentGrade_tableview;
    @FXML
    private TableColumn<GradeDTO, String> studentGrade_studentName;
    @FXML
    private TableColumn<GradeDTO, String> studentGrade_courseName;
    @FXML
    private TableColumn<GradeDTO, Integer> studentGrade_grade;
    @FXML
    private TableColumn<GradeDTO, Integer> studentGrade_studentID;
    @FXML
    private TableColumn<GradeDTO, Integer> studentGrade_courseID;
    @FXML
    private TextField studentGrade_search;
    @FXML
    private TableView<DepartmentDTO> departments_tableview;
    @FXML
    private TableColumn<DepartmentDTO, Integer> departments_tableview_id;
    @FXML
    private TableColumn<DepartmentDTO, String> departments_tableview_code;
    @FXML
    private TableColumn<DepartmentDTO, String> departments_tableview_name;
    @FXML
    private TableColumn<DepartmentDTO, Integer> departments_tableview_numStudents;
    @FXML
    private TableColumn<DepartmentDTO, Integer> departments_tableview_numCourses;
    @FXML
    private TextField departments_depId_tf;
    @FXML
    private TextField departments_depCode_tf;
    @FXML
    private TextField departments_depName_tf;
    @FXML
    private AnchorPane home_form;
    @FXML
    private AnchorPane students_form;
    @FXML
    private TextField students_depID_tf;
    @FXML
    private Button Refresh_btn;
    @FXML
    private AnchorPane courses_form;
    @FXML
    private AnchorPane grades_form;
    @FXML
    private AnchorPane departments_form;

    @FXML
    private TableView<DepartmentCourseDTO> departments_courses_tableview;
    @FXML
    private TableColumn<DepartmentCourseDTO, Integer> departments_courses_tableview_id;
    @FXML
    private TableColumn<DepartmentCourseDTO, String> departments_courses_tableview_code;
    @FXML
    private TableColumn<DepartmentCourseDTO, String> departments_courses_tableview_name;
    @FXML
    private TableColumn<DepartmentCourseDTO, Double> departments_courses_tableview_avgGpa;
    @FXML
    private TableColumn<DepartmentCourseDTO, Integer> departments_courses_tableview_numStudents;

    public DataAccessLayer dal = new DataAccessLayer();
    @FXML
    private TextField courses_code_tf;
    Alert alert;
    @FXML
    private Button assignCourses_btn;
    @FXML
    private Button addDep_btn;
    @FXML
    private Button updateDep_btn;
    @FXML
    private Button clearDep_btn;
    @FXML
    private AnchorPane assignCourses_form;
    @FXML
    private TextField studentSearch_tf1;
    @FXML
    private TableView<StudentDTO> ac_students_tableview;
    @FXML
    private TableColumn<StudentDTO, Integer> ac_students_tableview_id;
    @FXML
    private TableColumn<StudentDTO, String> ac_students_tableview_fname;
    @FXML
    private TableColumn<StudentDTO, String> ac_students_tableview_lname;
    @FXML
    private TableColumn<StudentDTO, String> ac_students_tableview_email;
    @FXML
    private TableColumn<StudentDTO, String> ac_students_tableview_gender;
    @FXML
    private TableColumn<StudentDTO, String> ac_students_tableview_dob;
    @FXML
    private TableColumn<StudentDTO, Integer> ac_students_tableview_depId;
    @FXML
    private TableView<CourseDTO> studentAvailableCourses_tableview;
    @FXML
    private TableColumn<CourseDTO, Integer> studentAvailableCourses_CourseID;
    @FXML
    private TableColumn<CourseDTO, String> studentAvailableCourses_courseName;
    @FXML
    private TextField studentAvailableCorses_sid_tf;
    @FXML
    private TextField studentAvailableCorses_cid_tf;
    @FXML
    private Button assignCourse_btn;
    @FXML
    private TableView<StudentDTO> department_students_tableview;
    @FXML
    private TableColumn<StudentDTO, Integer> department_students_sid;
    @FXML
    private TableColumn<StudentDTO, String> department_students_sname;
    @FXML
    private TableView<CourseDTO> department_courses_tableview;
    @FXML
    private TableColumn<CourseDTO, Integer> department_courses_cid;
    @FXML
    private TableColumn<CourseDTO, String> department_courses_cname;
    @FXML
    private TableColumn<CourseDTO, String> department_courses_ccode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentNum_label.setText(String.valueOf(dal.getTotalNumberOfStudents()));
        coursesNum_label.setText(String.valueOf(dal.getTotalNumberOfCourses()));
        departmentsNum_label.setText(String.valueOf(dal.getTotalNumberOfDepartments()));
        showAllStudents();
        showAllCourses();
        showAllGrades();
        showAllDepartments();
        showAllStudentCourses();
        addStudentsGenderList();
        addNumHoursList();
        showAllStudents2();
        studentAgeChart();
        averageGradePerCourseChart();
        averageGradePerDepartmentChart();
    }

    public void refreshData() {

        studentNum_label.setText(String.valueOf(dal.getTotalNumberOfStudents()));
        coursesNum_label.setText(String.valueOf(dal.getTotalNumberOfCourses()));
        departmentsNum_label.setText(String.valueOf(dal.getTotalNumberOfDepartments()));
        showAllStudents();
        showAllCourses();
        showAllGrades();
        showAllDepartments();
        showAllStudentCourses();
        addStudentsGenderList();
        addNumHoursList();
        showAllStudents2();
        studentAgeChart();
        averageGradePerCourseChart();
        averageGradePerDepartmentChart();
    }

    public ObservableList<StudentDTO> getAllStudents() {
        ObservableList<StudentDTO> allStudents = FXCollections.observableArrayList();
        allStudents.addAll(dal.getAllStudents());
        return allStudents;
    }
    private ObservableList<StudentDTO> allStudents;

    public void showAllStudents() {
        allStudents = getAllStudents();
        students_tableview_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        students_tableview_fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
        students_tableview_lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
        students_tableview_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        students_tableview_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        students_tableview_depId.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        students_tableview_dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        students_tableview.setItems(allStudents);
    }

    public void showAllStudents2() {
        allStudents = getAllStudents();
        ac_students_tableview_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ac_students_tableview_fname.setCellValueFactory(new PropertyValueFactory<>("fname"));
        ac_students_tableview_lname.setCellValueFactory(new PropertyValueFactory<>("lname"));
        ac_students_tableview_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        ac_students_tableview_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ac_students_tableview_depId.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        ac_students_tableview_dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        ac_students_tableview.setItems(allStudents);
    }

    public ObservableList<CourseDTO> getAllCourses() {
        ObservableList<CourseDTO> allCourses = FXCollections.observableArrayList();
        allCourses.addAll(dal.getAllCourses());
        return allCourses;
    }
    private ObservableList<CourseDTO> allCourses;

    public void showAllCourses() {
        allCourses = getAllCourses();
        courses_tableview_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        courses_tableview_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        courses_tableview_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        courses_tableview_numHours.setCellValueFactory(new PropertyValueFactory<>("hours"));
        courses_tableview_depId.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        courses_tableview.setItems(allCourses);
    }

    public ObservableList<GradeDTO> getAllGrades() {
        ObservableList<GradeDTO> allGrades = FXCollections.observableArrayList();
        allGrades.addAll(dal.getAllGrades());
        return allGrades;
    }
    private ObservableList<GradeDTO> allGrades;

    public void showAllGrades() {
        allGrades = getAllGrades();
        studentGrade_studentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentGrade_courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        studentGrade_studentID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentGrade_courseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        studentGrade_grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        studentGrade_tableview.setItems(allGrades);
    }

    @FXML
    public void addGrade() {
        try {
            // Retrieve values from your UI components
            int studentId = Integer.parseInt(studentGrade_studentId_tf.getText());
            int courseId = Integer.parseInt(studentGrade_courseId_tf.getText());
            int gradeValue = Integer.parseInt(studentGrade_grade_tf.getText());

            // Call the data access layer to add the grade
            boolean result = dal.addGrade(studentId, courseId, gradeValue);

            if (result) {
                showAllGrades();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Grade Inserted Successfully");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error Inserting Grade, Please Revise the values");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Log the exception for debugging

        }
        showAllStudentCourses();
    }

    @FXML
    public void updateGrade() {
        GradeDTO grade = studentGrade_tableview.getSelectionModel().getSelectedItem();
        int num = studentGrade_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        int studentId = Integer.parseInt(studentGrade_studentId_tf.getText());
        int courseId = Integer.parseInt(studentGrade_courseId_tf.getText());
        int gradeValue = Integer.parseInt(studentGrade_grade_tf.getText());

        boolean result = dal.updateGrade(grade.getId(), studentId, courseId, gradeValue);

        if (result) {
            showAllGrades();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Grade Updated Successfully");
            alert.showAndWait();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error Updating Grade, Please Revise the values");
            alert.showAndWait();
        }
    }

    public ObservableList<StudentCourseDTO> getAllStudentCourses() {
        ObservableList<StudentCourseDTO> allStudentCourses = FXCollections.observableArrayList();
        allStudentCourses.addAll(dal.getAllStudentCourses());
        return allStudentCourses;
    }
    private ObservableList<StudentCourseDTO> allStudentCourses;

    public void showAllStudentCourses() {
        allStudentCourses = getAllStudentCourses();
        studentCourses_studentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentCourses_courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        studentCourses_studentID.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentCourses_CourseID.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        studentCourses_tableview.setItems(allStudentCourses);
    }

    public ObservableList<DepartmentDTO> getAllDepartments() {
        ObservableList<DepartmentDTO> allDepartments = FXCollections.observableArrayList();
        allDepartments.addAll(dal.getAllDepartments());
        return allDepartments;
    }
    private ObservableList<DepartmentDTO> allDepartments;

    public void showAllDepartments() {
        allDepartments = getAllDepartments();
        departments_tableview_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        departments_tableview_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        departments_tableview_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        departments_tableview_numStudents.setCellValueFactory(new PropertyValueFactory<>("numberOfStudents"));
        departments_tableview_numCourses.setCellValueFactory(new PropertyValueFactory<>("numberOfCourses"));
        departments_tableview.setItems(allDepartments);
    }

    @FXML
    public void selectStudent() {

        StudentDTO student = students_tableview.getSelectionModel().getSelectedItem();
        int num = students_tableview.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) {
            return;
        }
        students_id_tf.setText(String.valueOf(student.getId()));
        showStudentGrades(student.getId());
        gpa_label.setText(dal.studentGpa(student.getId()));
        level_label.setText(dal.studentLevel(student.getId()));
        students_fname_tf.setText(student.getFname());
        students_lname_tf.setText(student.getLname());
        students_email_tf.setText(student.getEmail());
        students_depID_tf.setText(String.valueOf(student.getDepartmentId()));
        student_gender_cbox.setValue(student.getGender());
        student_dob_dp.setValue(dateToLocalDate(student.getDob()));

    }

    @FXML
    public void addStudent() {
        String studentIdText = students_id_tf.getText();
        String depIdText = students_depID_tf.getText();

        if (depIdText.isEmpty() || student_gender_cbox.getSelectionModel().getSelectedItem() == null
                || students_fname_tf.getText().isEmpty() || students_lname_tf.getText().isEmpty()
                || students_email_tf.getText().isEmpty() || student_dob_dp.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();

            return;
        }

        try {
            //int studentId = Integer.valueOf(studentIdText);
            int depId = Integer.valueOf(depIdText);

            Boolean result = dal.addStudent(
                    students_fname_tf.getText(),
                    students_lname_tf.getText(),
                    students_email_tf.getText(),
                    student_gender_cbox.getSelectionModel().getSelectedItem(),
                    depId,
                    student_dob_dp.getValue()
            );

            if (result) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Student Inserted Successfully");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error Inserting Student, Please Revise the values");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        showAllStudents();
    }

    @FXML
    public void updateStudent() {
        String studentIdText = students_id_tf.getText();

        if (studentIdText.isEmpty() || student_gender_cbox.getSelectionModel().getSelectedItem() == null
                || students_fname_tf.getText().isEmpty() || students_lname_tf.getText().isEmpty()
                || students_email_tf.getText().isEmpty() || student_dob_dp.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();

            return;
        }

        try {
            int studentId = Integer.valueOf(studentIdText);
            int departmentId = Integer.valueOf(students_depID_tf.getText()); // Assuming you have a TextField for departmentId

            Boolean result = dal.updateStudent(
                    studentId,
                    students_fname_tf.getText(),
                    students_lname_tf.getText(),
                    students_email_tf.getText(),
                    student_gender_cbox.getSelectionModel().getSelectedItem(),
                    student_dob_dp.getValue(),
                    departmentId
            );

            if (result) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Student Updated Successfully");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error Updating Student, Please Revise the values");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        showAllStudents();
    }

    @FXML
    public void clearStudentSelection() {
        //students_tableview.getSelectionModel().clearSelection();
        students_id_tf.setText("");
        students_depID_tf.setText("");
        students_fname_tf.setText("");
        students_lname_tf.setText("");
        students_email_tf.setText("");
        student_gender_cbox.getSelectionModel().clearSelection();
        student_dob_dp.setValue(null);
        gpa_label.setText("");
        level_label.setText("");
    }

    @FXML
    public void clearDepartmentSelection() {
        // departments_tableview.getSelectionModel().clearSelection();
        departments_depId_tf.setText("");
        departments_depCode_tf.setText("");
        departments_depName_tf.setText("");
    }

    public ObservableList<GradeDTO> getStudentGrades(int id) {
        ObservableList<GradeDTO> allGrades = FXCollections.observableArrayList();
        allGrades.addAll(dal.getStudentGrades(id));

        return allGrades;
    }
    private ObservableList<GradeDTO> studentGrades;

    public void showStudentGrades(int id) {
        studentGrades = getStudentGrades(id);
        student_courseGrade_tableview_course.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        student_courseGrade_tableview_grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        student_courseGrade_tableview.setItems(studentGrades);
    }

    @FXML
    public void selectCourse() {

        CourseDTO course = courses_tableview.getSelectionModel().getSelectedItem();
        int num = courses_tableview.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) {
            return;
        }
        courses_id_tf.setText(String.valueOf(course.getId()));
        showCourseStudents(course.getId());
        courses_code_tf.setText(course.getCode());
        courses_name_tf.setText(course.getName());
        courses_depId_tf.setText(String.valueOf(course.getDepartmentId()));
        courses_numHours_cbox.setValue(course.getHours());

    }

    @FXML
    public void addCourse() {
        String courseIdText = courses_id_tf.getText();
        String depIdText = courses_depId_tf.getText();
        if (courses_numHours_cbox.getSelectionModel().getSelectedItem() == null) {
            // Handle the case where input is empty
            // You might want to show an alert or take appropriate action
            return;
        }

        try {
            int numHours = courses_numHours_cbox.getSelectionModel().getSelectedItem();
            int dep_id = Integer.valueOf(depIdText);
            boolean result = dal.addCourse(courses_name_tf.getText(),
                    courses_code_tf.getText(), numHours,
                    dep_id
            );

            if (result) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Course Inserted Successfully");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error Inserting Course, Please Revise the values");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        showAllCourses();
    }

    @FXML
    public void clearCourseSelection() {
        //courses_tableview.getSelectionModel().clearSelection();
        courses_id_tf.setText("");
        courses_numHours_cbox.getSelectionModel().clearSelection();
        courses_code_tf.setText("");
        courses_name_tf.setText("");
        courses_depId_tf.setText("");
    }

    @FXML
    public void updateCourse() {
        String courseIdText = courses_id_tf.getText();
        String depIdText = courses_depId_tf.getText();

        if (courses_numHours_cbox.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();
            return;
        }

        try {
            int courseId = Integer.valueOf(courseIdText);
            int departmentId = Integer.valueOf(depIdText);

            boolean result = dal.updateCourse(
                    courseId,
                    courses_name_tf.getText(),
                    courses_code_tf.getText(),
                    courses_numHours_cbox.getSelectionModel().getSelectedItem(),
                    departmentId
            );

            if (result) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Course Updated Successfully");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error Updating Course, Please Revise the values");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        showAllCourses();
    }

    @FXML
    public void deleteCourse() {
        String courseIdText = courses_id_tf.getText();

        if (courseIdText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course to delete.");
            alert.showAndWait();
            return;
        }

        try {
            int courseId = Integer.valueOf(courseIdText);

            boolean result = dal.deleteCourse(courseId);

            if (result) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Course Deleted Successfully");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error Deleting Course, Please try again");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        showAllCourses();
    }

    public ObservableList<StudentDTO> getCourseStudents(int id) {
        ObservableList<StudentDTO> allCourseStudents = FXCollections.observableArrayList();
        allCourseStudents.addAll(dal.getCourseStudents(id));

        return allCourseStudents;
    }
    private ObservableList<StudentDTO> allCourseStudents;

    public void showCourseStudents(int id) {
        allCourseStudents = getCourseStudents(id);
        courses_enrolledStudents_tableview_sid.setCellValueFactory(new PropertyValueFactory<>("id"));
        courses_enrolledStudents_tableview_semail.setCellValueFactory(new PropertyValueFactory<>("email"));
        courses_enrolledStudents_tableview.setItems(allCourseStudents);
    }

    @FXML
    public void selectGrade() {

        GradeDTO grade = studentGrade_tableview.getSelectionModel().getSelectedItem();
        System.out.println(grade.toString());
        studentGrade_studentId_tf.setText(String.valueOf(grade.getStudentId()));
        studentGrade_courseId_tf.setText(String.valueOf(grade.getCourseId()));
        studentGrade_grade_tf.setText(String.valueOf(grade.getGrade()));

    }

    @FXML
    public void addGradeSelect() {

        StudentCourseDTO student = studentCourses_tableview.getSelectionModel().getSelectedItem();
        int num = studentCourses_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        studentGrade_studentId_tf.setText(String.valueOf(student.getStudentId()));
        studentGrade_courseId_tf.setText(String.valueOf(student.getCourseId()));
        studentGrade_grade_tf.setText("");

    }

    /**
     * ************************************************************************************************************************
     */
    @FXML
    public void selectDepartment() {

        DepartmentDTO department = departments_tableview.getSelectionModel().getSelectedItem();
        int num = departments_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        showDepartmentCourses(department.getId());
        showDepartmentStudents(department.getId());
        showDepartmentAllCourses(department.getId());
        departments_depId_tf.setText(String.valueOf(department.getId()));
        departments_depCode_tf.setText(department.getCode());
        departments_depName_tf.setText(department.getName());

    }

    public ObservableList<DepartmentCourseDTO> getDepartmentCourses(int id) {
        ObservableList<DepartmentCourseDTO> allDepartmentCourses = FXCollections.observableArrayList();
        allDepartmentCourses.addAll(dal.getDepartmentCourses(id));

        return allDepartmentCourses;
    }

    @FXML
    public void addDepartment() {
        String depName = departments_depName_tf.getText();
        String depCode = departments_depCode_tf.getText();

        if (depName.isEmpty() || depCode.isEmpty()) {
            // Handle the case where input is empty
            // You might want to show an alert or take appropriate action
            return;
        }

        boolean result = dal.addDepartment(depName, depCode);

        if (result) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Department Inserted Successfully");
            alert.showAndWait();
            showAllDepartments();
        } else {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error Inserting Department, Please Revise the values");
            alert.showAndWait();
            showAllDepartments();
        }

        showAllDepartments();
    }

    @FXML
    public void updateDepartment() {
        String depIdText = departments_depId_tf.getText();
        String depName = departments_depName_tf.getText();
        String depCode = departments_depCode_tf.getText();

        if (depIdText.isEmpty() || depName.isEmpty() || depCode.isEmpty()) {
            // Handle the case where input is empty
            // You might want to show an alert or take appropriate action
            return;
        }

        try {
            int depId = Integer.valueOf(depIdText);

            boolean result = dal.updateDepartment(depId, depName, depCode);

            if (result) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Department Updated Successfully");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Error Updating Department, Please Revise the values");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        showAllDepartments();
    }

    private ObservableList<DepartmentCourseDTO> allDepartmentCourses;

    public void showDepartmentCourses(int id) {
        allDepartmentCourses = getDepartmentCourses(id);
        departments_courses_tableview_id.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        departments_courses_tableview_name.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        departments_courses_tableview_code.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        departments_courses_tableview_avgGpa.setCellValueFactory(new PropertyValueFactory<>("avgGpa"));
        departments_courses_tableview_numStudents.setCellValueFactory(new PropertyValueFactory<>("numStudents"));

        departments_courses_tableview.setItems(allDepartmentCourses);
    }

    private LocalDate dateToLocalDate(Date date) {
        return date.toLocalDate();
    }

    @FXML
    public void switchForm(ActionEvent event) {
        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            students_form.setVisible(false);
            courses_form.setVisible(false);
            grades_form.setVisible(false);
            departments_form.setVisible(false);
            assignCourses_form.setVisible(false);

            student_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:#fff;\n"
                    + "    -fx-text-fill:#7C98B3;");
            courses_btn.setStyle("-fx-background-color:transparent");
            grades_btn.setStyle("-fx-background-color:transparent");
            departments_btn.setStyle("-fx-background-color:transparent");
            assignCourses_btn.setStyle("-fx-background-color:transparent");

            studentAgeChart();
            averageGradePerCourseChart();
            averageGradePerDepartmentChart();
        } else if (event.getSource() == student_btn) {
            home_form.setVisible(false);
            students_form.setVisible(true);
            courses_form.setVisible(false);
            grades_form.setVisible(false);
            departments_form.setVisible(false);
            assignCourses_form.setVisible(false);

            student_btn.setStyle("-fx-background-color:#fff;\n"
                    + "    -fx-text-fill:#7C98B3;");

            home_btn.setStyle("-fx-background-color:transparent");
            courses_btn.setStyle("-fx-background-color:transparent");
            grades_btn.setStyle("-fx-background-color:transparent");
            departments_btn.setStyle("-fx-background-color:transparent");
            assignCourses_btn.setStyle("-fx-background-color:transparent");
            showAllStudents();

        } else if (event.getSource() == courses_btn) {
            home_form.setVisible(false);
            students_form.setVisible(false);
            courses_form.setVisible(true);
            grades_form.setVisible(false);
            departments_form.setVisible(false);
            assignCourses_form.setVisible(false);

            student_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");
            courses_btn.setStyle("-fx-background-color:#fff;\n"
                    + "    -fx-text-fill:#7C98B3;");
            grades_btn.setStyle("-fx-background-color:transparent");
            departments_btn.setStyle("-fx-background-color:transparent");
            assignCourses_btn.setStyle("-fx-background-color:transparent");
            showAllCourses();

        } else if (event.getSource() == grades_btn) {
            home_form.setVisible(false);
            students_form.setVisible(false);
            courses_form.setVisible(false);
            grades_form.setVisible(true);
            departments_form.setVisible(false);
            assignCourses_form.setVisible(false);

            student_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");
            courses_btn.setStyle("-fx-background-color:transparent");
            grades_btn.setStyle("-fx-background-color:#fff;\n"
                    + "    -fx-text-fill:#7C98B3;");
            departments_btn.setStyle("-fx-background-color:transparent");
            assignCourses_btn.setStyle("-fx-background-color:transparent");

            showAllGrades();

        } else if (event.getSource() == departments_btn) {
            home_form.setVisible(false);
            students_form.setVisible(false);
            courses_form.setVisible(false);
            grades_form.setVisible(false);
            departments_form.setVisible(true);
            assignCourses_form.setVisible(false);

            student_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");
            courses_btn.setStyle("-fx-background-color:transparent");
            grades_btn.setStyle("-fx-background-color:transparent");
            departments_btn.setStyle("-fx-background-color:#fff;\n"
                    + "    -fx-text-fill:#7C98B3;");
            assignCourses_btn.setStyle("-fx-background-color:transparent");

            showAllDepartments();

        } else if (event.getSource() == assignCourses_btn) {
            home_form.setVisible(false);
            students_form.setVisible(false);
            courses_form.setVisible(false);
            grades_form.setVisible(false);
            departments_form.setVisible(false);
            assignCourses_form.setVisible(true);

            student_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");
            courses_btn.setStyle("-fx-background-color:transparent");
            grades_btn.setStyle("-fx-background-color:transparent");
            departments_btn.setStyle("-fx-background-color:transparent");
            assignCourses_btn.setStyle("-fx-background-color:#fff;\n"
                    + "    -fx-text-fill:#7C98B3;");
            showAllStudents();
            showAllStudentCourses();

            showAllStudents2();

        }
    }

    private String[] genderList = {"Male", "Female"};

    public void addStudentsGenderList() {
        List<String> genderL = new ArrayList<>();

        for (String data : genderList) {
            genderL.add(data);
        }

        ObservableList ObList = FXCollections.observableArrayList(genderL);
        student_gender_cbox.setItems(ObList);
    }

    public void addNumHoursList() {
        List<Integer> numHoursList = Arrays.asList(2, 3, 6);

        ObservableList<Integer> obList = FXCollections.observableArrayList(numHoursList);
        courses_numHours_cbox.setItems(obList);
    }
    private double x = 0;
    private double y = 0;

    @FXML
    public void logout() {

        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.setScene(scene);
                stage.show();

            } else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void selectStudentAvalableCourses() {
        showStudentAvailableCourses();
    }

    public ObservableList<CourseDTO> getStudentAvailableCourses(int studentId) {
        ObservableList<CourseDTO> availableCourses = FXCollections.observableArrayList();
        availableCourses.addAll(dal.getStudentAvailableCourses(studentId));
        return availableCourses;
    }

    private ObservableList<CourseDTO> availableCourses;

    public void showStudentAvailableCourses() {
        int studentId = ac_students_tableview.getSelectionModel().getSelectedItem().getId();
        int num = ac_students_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        studentAvailableCorses_sid_tf.setText(String.valueOf(studentId));

        availableCourses = getStudentAvailableCourses(studentId);
        populateAvailableCoursesTable();
    }

    private void populateAvailableCoursesTable() {
        studentAvailableCourses_CourseID.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentAvailableCourses_courseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentAvailableCourses_tableview.setItems(availableCourses);
    }

    @FXML
    public void selectCourseToAssign() {
        int courseID = studentAvailableCourses_tableview.getSelectionModel().getSelectedItem().getId();
        int num = studentAvailableCourses_tableview.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        studentAvailableCorses_cid_tf.setText(String.valueOf(courseID));

    }

    @FXML
    public void assignCourse() {
        String courseIdText = studentAvailableCorses_cid_tf.getText();
        String studentIdText = studentAvailableCorses_sid_tf.getText();

        if (courseIdText.isEmpty() || studentIdText.isEmpty()) {
            // Handle the case where input is empty
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the required fields");
            alert.showAndWait();
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdText);
            int studentId = Integer.parseInt(studentIdText);

            boolean result = dal.assignCourse(studentId, courseId);

            if (result) {
                showStudentAvailableCourses();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Course Assigned Successfully");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Failed to assign course. Please check your input or try again later.");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid input for course ID or student ID");
            alert.showAndWait();
        }
    }
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void studentAgeChart() {
        studentAgeAVG_chart.getData().clear();
        String sql = "SELECT\n"
                + "    FLOOR(DATEDIFF(CURDATE(), dob) / 365) AS age, COUNT(*) AS number_of_students\n"
                + "FROM student GROUP BY age ORDER BY age;";
        connect = dal.ConnectDb();
        try {
            XYChart.Series<String, Number> chart = new XYChart.Series<>(); // Explicit type arguments

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                String age = String.valueOf(result.getInt(1));
                Number numberOfStudents = result.getInt(2);

                chart.getData().add(new XYChart.Data<>(age, numberOfStudents));
            }
            studentAgeAVG_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void averageGradePerCourseChart() {
        courseGradeAvg_chart.getData().clear();
        String sql = "SELECT c.code AS course_name, AVG(g.grade) AS average_grade\n"
                + "FROM course c\n"
                + "LEFT JOIN grade g ON c.id = g.course_id\n"
                + "GROUP BY c.id, c.name\n"
                + "HAVING AVG(g.grade) IS NOT NULL;";
        connect = dal.ConnectDb();
        try {
            XYChart.Series<String, Number> chart = new XYChart.Series<>();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                String courseName = result.getString("course_name");
                double averageGrade = result.getDouble("average_grade");
                chart.getData().add(new XYChart.Data<>(courseName, averageGrade));
            }

            NumberAxis yAxis = (NumberAxis) courseGradeAvg_chart.getYAxis();
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(100);

            courseGradeAvg_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void averageGradePerDepartmentChart() {
        departmentGradeAvg_chart.getData().clear();
        String sql = "SELECT d.code AS department_name, AVG(g.grade) AS average_grade\n"
                + "FROM department d\n"
                + "JOIN student s ON d.id = s.department_id\n"
                + "JOIN grade g ON s.id = g.student_id\n"
                + "GROUP BY d.id, d.name;";
        connect = dal.ConnectDb();
        try {
            XYChart.Series<String, Number> chart = new XYChart.Series<>();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                String departmentName = result.getString("department_name");
                double averageGrade = result.getDouble("average_grade");

                chart.getData().add(new XYChart.Data<>(departmentName, averageGrade));
            }
// Set y-axis range from 0 to 100
            NumberAxis yAxis = (NumberAxis) departmentGradeAvg_chart.getYAxis();
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(100);
            departmentGradeAvg_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<StudentDTO> getStudentsByDepartment(int departmentId) {
        ObservableList<StudentDTO> departmentStudents = FXCollections.observableArrayList();
        departmentStudents.addAll(dal.getStudentsByDepartment(departmentId));
        return departmentStudents;
    }

    private ObservableList<StudentDTO> departmentStudents;

    public void showDepartmentStudents(int departmentId) {
        departmentStudents = getStudentsByDepartment(departmentId);
        department_students_sid.setCellValueFactory(new PropertyValueFactory<>("id"));

        department_students_sname.setCellValueFactory(new PropertyValueFactory<>("email"));

        department_students_tableview.setItems(departmentStudents);
    }

    public ObservableList<CourseDTO> getCoursesByDepartment(int departmentId) {
        ObservableList<CourseDTO> departmentCourses = FXCollections.observableArrayList();
        departmentCourses.addAll(dal.getCoursesByDepartment(departmentId));
        return departmentCourses;
    }

    private ObservableList<CourseDTO> departmentCourses;

    public void showDepartmentAllCourses(int departmentId) {
        departmentCourses = getCoursesByDepartment(departmentId);
        department_courses_cid.setCellValueFactory(new PropertyValueFactory<>("id"));
        department_courses_cname.setCellValueFactory(new PropertyValueFactory<>("name"));
        department_courses_ccode.setCellValueFactory(new PropertyValueFactory<>("code"));
        department_courses_tableview.setItems(departmentCourses);
    }

}
