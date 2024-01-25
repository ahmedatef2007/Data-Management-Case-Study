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
public class CourseDTO {

    private int id;
    private String name;
    private int hours;
    private String code;
    private int departmentId;

    // Constructors, getters, and setters
    public CourseDTO() {
    }

    public CourseDTO(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public CourseDTO(int id, String name, int hours, String code, int departmentId) {
        this.id = id;
        this.name = name;
        this.hours = hours;
        this.code = code;
        this.departmentId = departmentId;
    }

    public CourseDTO(int id, String name, int hours, String code) {
        this.id = id;
        this.name = name;
        this.hours = hours;
        this.code = code;

    }
    // Getters and setters for each field

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

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
