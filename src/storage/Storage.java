package storage;

import java.util.ArrayList;
import java.util.List;

import model.*;

public class Storage {
	private final static Storage instance = new Storage();
	
	private final List<User> users = new ArrayList<>();
	private final List<Product> products = new ArrayList<>();
	private final List<Pricelist> pricelists = new ArrayList<>();
	private final List<Payment> payments = new ArrayList<>();
	private final List<Order> orders = new ArrayList<>();

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

	public List<Payment> getPayments() {
		return new ArrayList<>(payments);
	}
	public void addPayment(Payment payment) {
		payments.add(payment);
	}

	public List<Order> getOrders() {
		return new ArrayList<>(orders);
	}
	public void addOrder(Order order) {
		orders.add(order);
	}
}
