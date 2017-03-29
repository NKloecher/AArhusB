package storage;

import java.util.ArrayList;
import java.util.List;

import model.*;

public class Storage {
	private final static Storage instance = new Storage();
	
	private final List<User> users = new ArrayList<>();
	
	private Storage() {}
	
	public List<User> getUsers() {
		return new ArrayList<>(users);
	}
	public void addUser(User u) {
		users.add(u);
	}
	
	public static Storage getInstance() {
		return instance;
	}
}
