package storage;

import java.util.ArrayList;
import java.util.List;

import model.*;

public class Storage {
	private final static Storage instance = new Storage();
	
	private final List<User> users = new ArrayList<>();
	private final List<Product> products = new ArrayList<>();
	private final List<Pricelist> pricelists = new ArrayList<>();

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

	public List<Product> getProduct() {
		return new ArrayList<>(products);
	}
	public void addProduct(Product p) {
		products.add(p);
	}

	public List<Pricelist> getPricelists() {
		return new ArrayList<>(pricelists);
	}
	public void addPricelist(Pricelist pricelist) {
		pricelists.add(pricelist);
	}
}
