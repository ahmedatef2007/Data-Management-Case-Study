/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.sql.Date;

/**
 *
 * @author ahmed
 */
public class StudentDTO {

    private int id;
    private String fname;
    private String lname;
    private String email;
    private String gender;
    private int departmentId;
    private Date dob;

    public StudentDTO() {
    }

    public StudentDTO(int id, String email, int departmentId) {
        this.id = id;
        this.email = email;
        this.departmentId = departmentId;

    }

    public StudentDTO(int id, String fname, String lname, String email, String gender, int departmentId, Date dob) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.gender = gender;
        this.departmentId = departmentId;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
