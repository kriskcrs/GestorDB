package com.umg.gestordbbackend.Entity;

public class UserDB  extends ConnectionDB{

    private String username;
    private String password;
    private String UsernameToCheck;
    private String table;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernameToCheck() {
        return UsernameToCheck;
    }

    public void setUsernameToCheck(String usernameToCheck) {
        UsernameToCheck = usernameToCheck;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
