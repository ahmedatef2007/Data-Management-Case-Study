/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author ahmed
 */
public class DepartmentDTO {
    private int id;
    private String name;
    private String code;
    private int numberOfStudents;
    private int numberOfCourses;


    public DepartmentDTO() {
    }

    public DepartmentDTO(int id, String name, String code, int numberOfStudents, int numberOfCourses) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.numberOfStudents = numberOfStudents;
        this.numberOfCourses = numberOfCourses;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getNumberOfCourses() {
        return numberOfCourses;
    }

    public void setNumberOfCourses(int numberOfCourses) {
        this.numberOfCourses = numberOfCourses;
    }
}
