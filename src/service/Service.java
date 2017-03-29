package service;

import java.util.List;

import javax.security.sasl.AuthenticationException;

import model.*;
import storage.Storage;

public class Service {
	private final static Service instance = new Service();
	private User user;
	private Storage storage = Storage.getInstance();
	
	private Service() {}
	
	/*
	 * sets serivce.user if username and passowr dis correct
	 * if username or password is not correct it throws an error
	 */
	public void login(String username, String password) throws AuthenticationException {
    	List<User> users = storage.getUsers();
    	
    	for (User u : users) {
    		if (u.getUsername().equals(username)) {
    			if (u.checkPassword(password)) {
    				user = u;
    				return;
    			}
    		}
    	}
    	
    	throw new AuthenticationException("wrong username or password");
    }
	
	public User getUser() {
		return user;
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
