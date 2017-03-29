package model;

public class User extends Person {
    private String username;
    private String passwordHash;
    private String salt;
    private Permission permission;

    public User(String username, String password) {
        this.username = username;
        // TODO: set password
    }
    
    public String getUsername() {
    	return username;
    }
}
