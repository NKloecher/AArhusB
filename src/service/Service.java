package service;

import java.util.List;

import javax.security.sasl.AuthenticationException;

import model.*;
import storage.Storage;

public class Service {
	private final static Service instance = new Service();
	private User activeUser;
	private Storage storage = Storage.getInstance();
	
	private Service() {}
	
	/*
<<<<<<< HEAD
	 * returns a activeUser if the username and password is corrent
	 * if username or password is not correct it returns null
=======
	 * sets serivce.user if username and passowr dis correct
	 * if username or password is not correct it throws an error
>>>>>>> 4a2419a336fa20b656e8b0e69f0528769576db5f
	 */
	public void login(String username, String password) throws AuthenticationException {
    	List<User> users = storage.getUsers();
    	
    	for (User u : users) {
    		if (u.getUsername().equals(username)) {
    			if (u.checkPassword(password)) {
<<<<<<< HEAD
    				activeUser = u;
=======
    				user = u;
    				return;
>>>>>>> 4a2419a336fa20b656e8b0e69f0528769576db5f
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
