package service;

import javax.security.sasl.AuthenticationException;

import model.*;
import storage.Storage;

public class Service {
	private final static Service instance = new Service();
	private User activeUser;
	private Storage storage = Storage.getInstance();
	
	private Service() {}
	
	/*
	 * sets serivce.user if username and password is correct
	 * if username or password is not correct it throws an error
	 */
	public void login(String username, String password) throws AuthenticationException {
		for (User u : storage.getUsers()) {
    		if (u.getUsername().equals(username)) {
    			if (u.checkPassword(password)) {
    				activeUser = u;
    				return;
    			}
    		}
    	}
    	
    	throw new AuthenticationException("wrong username or password");
    }

    public void logout(){
		assert activeUser != null;
		activeUser = null;
	}
	
    /*
     * is null if no user is logged in
     */
	public User getActiveUser() {
		return activeUser;
	}
	
	public User createUser(String name, String username, String password) {
		User u = new User(name, username, password);
		
		storage.addUser(u);
		
		return u;
	}
	public void deleteUser(User user) {
		storage.deleteUser(user);
	}
	public void updateUser(User user) {
		
	}
	public void setUserName(User user, String name) {
		user.setName(name);
	}
	public void setUserUsername(User user, String username) {
		user.setUsername(username);
	}
	public void setUserPassword(User user, String password) {
		user.setPassword(password);
	}
	public boolean usernameIsUnique(String username) {
		for (User u : storage.getUsers()) {
			if (u.getUsername().equals(username)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void initStorage() {
		createUser("John", "test", "test");
	}
	
	public static Service getInstance() {
		return instance;
	}
}
