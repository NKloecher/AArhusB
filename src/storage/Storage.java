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
    private final List<Tour> tours = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();

    private Storage() {
    }

    public static Storage getInstance() {
        return instance;
    }

    public void addCategory(String category) {
    	categories.add(category);
    }
    public List<String> getCategories() {
    	return new ArrayList<>(categories);
    }
    
    public List<Tour> getTours() {
        return new ArrayList<>(tours);
    }

    public void addTour(Tour tour) {
        tours.add(tour);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void addUser(User u) {
        users.add(u);
    }

    public void deleteUser(User u) {
        users.remove(u);
    }

    public List<Product> getProducts() {
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
