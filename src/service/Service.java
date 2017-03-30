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
	
	public User getActiveUser() {
		return activeUser;
	}
	
	public User createUser(String username, String password) {
		User u = new User(username, password);
		
		storage.addUser(u);
		
		return u;
	}
	
	public void initStorage() {
		createUser("test", "test");
	}
	
	public static Service getInstance() {
		return instance;
	}
}
