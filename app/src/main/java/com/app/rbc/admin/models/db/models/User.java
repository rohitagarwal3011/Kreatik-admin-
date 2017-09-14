package com.app.rbc.admin.models.db.models;

/**
 * Created by jeet on 5/9/17.
 */

public class User {

    public User() {

    }
    private String name;
    private String email;
    private String pwd;
    private String role;
    private int file_present; // 1 : image present  , 0; not
    private String myfile;
    private String admin_user_id;
    private String user_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getFile_present() {
        return file_present;
    }

    public void setFile_present(int file_present) {
        this.file_present = file_present;
    }

    public String getMyfile() {
        return myfile;
    }

    public void setMyfile(String myfile) {
        this.myfile = myfile;
    }

    public String getAdmin_user_id() {
        return admin_user_id;
    }

    public void setAdmin_user_id(String admin_user_id) {
        this.admin_user_id = admin_user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
