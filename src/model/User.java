package model;

public class User extends Person {
    private String username;
    private String passwordHash;
    private String salt;
    private Permission permission;
}
