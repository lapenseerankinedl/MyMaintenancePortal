package com.example.mymaintenancenportal;

public class User {
    private String id;
    private String userName;
    private String name;
    private String password;
    private String email;
    private String landlordEmail;

    public String getName()
    {
        return name;
    }

    public String getUserName() { return userName; }

    public String getPassword() { return password; }

    public String getEmail()
    {
        return email;
    }

    public String getId()
    {
        return id;
    }

    public String getLandlordEmail() { return landlordEmail; }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public void setUserName(String userName) { this.userName = userName; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLandlordEmail(String landlordEmail) { this.landlordEmail = landlordEmail; }

    public User(String id, String name, String userName, String email, String password)
    {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.landlordEmail = "";
        this.password = password;
    }

    public User()
    {

    }
}