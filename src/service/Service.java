package service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.sasl.AuthenticationException;

import exceptions.DiscountParseException;
import model.*;
import storage.Storage;

public class Service {

	private final static Service instance = new Service();
	private User activeUser;
	private Storage storage = Storage.getInstance();
	private Pricelist selectedPricelist;

	private Service() {
	}

	public void updateProductName(Product product, String name) {
		assert product != null;
		assert name != null && !name.isEmpty();

		product.setName(name);
	}

	public void updateProductClips(Product product, Integer clips) {
		assert product != null;

		product.setClips(clips);
	}

	public void updateProductCategory(Product product, String category) {
		assert product != null;
		assert category != null && !category.isEmpty();

		product.setCategory(category);
	}

	/**
	 * returns all orders that are not returned or paid
	 */
	public List<Order> getRentals() {
		List<Order> rentals = new ArrayList<>();

		for (Order o : storage.getOrders()) {
			PaymentStatus s = o.paymentStatus();

			if (s == PaymentStatus.DEPOSITPAID) {
				rentals.add(o);
			}
		}

		return rentals;
	}

	/**
	 * returns all product where the query is contained in the name and the
	 * products category equals category parameter if category is "All" every
	 * category will be selected
	 */
	public List<Product> getMatchingProducts(String query, String category, List<Product> products) {
		assert query != null && !query.isEmpty();
		assert category != null && !category.isEmpty();
		assert products != null;

		List<Product> selected = new ArrayList<>();

		for (Product p : products) {
			if (p.getName().toLowerCase().contains(query.toLowerCase())
					&& (category.equals("All") || p.getCategory().equals(category))) {
				selected.add(p);
			}
		}

		return selected;
	}

	/**
	 * sets serivce.user if username and password is correct if username or
	 * password is not correct it throws an error
	 */
	public void login(String username, String password) throws AuthenticationException {
		assert username != null && !username.isEmpty();
		assert password != null && !password.isEmpty();

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

	/**
	 * sets activeUser to null
	 */
	public void logout() {
		assert activeUser != null;
		activeUser = null;
	}

	public void setSelectedPricelist(Pricelist pricelist) {
		assert pricelist != null;

		selectedPricelist = pricelist;
	}

	public Pricelist getSelectedPricelist() {
		return selectedPricelist;
	}

	public void setPricelistPrice(Pricelist pricelist, Product product, double price) {
		assert pricelist != null;
		assert product != null;

		pricelist.setPrice(product, price);
	}

	/**
	 * is null if no user is logged in
	 */
	public User getActiveUser() {
		return activeUser;
	}

	public void addCategory(String category) {
		assert category != null && !category.isEmpty();

		storage.addCategory(category);
	}

	public User createUser(String name, String username, String password, Permission permission) {
		assert name != null && !name.isEmpty();
		assert username != null && !username.isEmpty();
		assert password != null && !password.isEmpty();
		assert permission != null;

		User u = new User(name, username, password, permission);

		storage.addUser(u);

		return u;
	}

	public void updateUserPassword(User user, String password) {
		assert user != null;
		assert password != null && !password.isEmpty();

		user.setPassword(password);
	}

	public void deleteUser(User user) {
		assert user != null;

		user.setDeleted();
	}

	public void updateUserName(User user, String name) {
		assert user != null;
		assert name != null && !name.isEmpty();

		user.setName(name);
	}

	public void updateUserUsername(User user, String username) {
		assert user != null;
		assert username != null && !username.isEmpty();

		user.setUsername(username);
	}

	public void updateUserPermission(User user, Permission permission) {
		assert user != null;
		assert permission != null;

		user.setPermission(permission);
	}

	public boolean usernameIsUnique(String username, User user) {
		assert user != null;
		assert username != null && !username.isEmpty();

		for (User u : storage.getUsers()) {
			if (!u.equals(user) && u.getUsername().equals(username)) {
				return false;
			}
		}

		return true;
	}

	public Tour createTour(int persons, LocalDateTime date, double price, Duration duration) {
		assert date != null;
		assert duration != null;

		Tour tour = new Tour(persons, date, price, duration, activeUser);
		storage.addTour(tour);
		return tour;
	}

	public List<Tour> getTours() {
		return storage.getTours();
	}

	public List<Tour> getTours(LocalDate date) {
		assert date != null;

		List<Tour> tours = new ArrayList<>();
		for (Tour tour : getTours()) {
			if (tour.getDate().toLocalDate().equals(date)) {
				tours.add(tour);
			}
		}
		return tours;
	}

	public Set<LocalDate> getTourDates() {
		Set<LocalDate> dates = new HashSet<>();
		for (Tour tour : getTours()) {
			dates.add(tour.getDate().toLocalDate());
		}
		return dates;
	}

	public void updateTourPersons(Tour tour, int persons) {
		assert tour != null;

		tour.setPersons(persons);
	}

	public void updateTourDate(Tour tour, LocalDateTime date) {
		assert tour != null;
		assert date != null;

		tour.setDate(date);
	}

	public void updateTourPrice(Tour tour, double price) {
		assert tour != null;

		tour.setPrice(price);
	}

	public void updateTourDuration(Tour tour, Duration duration) {
		assert tour != null;
		assert duration != null;

		tour.setDuration(duration);
	}

	public void updateProductOrderAmount(ProductOrder productOrder, int amount) {
		assert productOrder != null;

		productOrder.setAmount(amount);
	}

	public void updateOrderCutsomer(Order order, Customer customer) {
		assert order != null;
		assert customer != null;

		order.setCustomer(customer);
	}

	public void removeProduct(Product p) {
		assert p != null;

		storage.removeProduct(p);
	}

	public Product createProduct(String name, Integer clips, String category, String image) {
		assert name != null;
		assert clips != null;
		assert category != null;
		assert image != null;

		Product product = new Product(name, clips, category, image);
		storage.addProduct(product);

		return product;
	}

	public DepositProduct createDepositProduct(String name, Integer clips, String category, String image,
			double deposit) {
		assert name != null;
		assert clips != null;
		assert category != null;
		assert image != null;

		DepositProduct depositProduct = new DepositProduct(name, clips, category, image, deposit);
		storage.addProduct(depositProduct);
		return depositProduct;
	}

	public List<Pricelist> getPricelists() {
		return storage.getPricelists();
	}

	public void removePricelist(Pricelist pricelist) {
		assert pricelist != null;

		storage.removePricelist(pricelist);
		setSelectedPricelist(storage.getPricelists().get(0));
	}

	public Pricelist createPricelist(String name) {
		assert name != null && !name.isEmpty();

		Pricelist pricelist = new Pricelist(name);
		storage.addPricelist(pricelist);
		return pricelist;
	}

	public Payment createPayment(Payable payable, double amount, PaymentType paymentType) {
		assert payable != null;
		assert paymentType != null;

		Payment payment = new Payment(paymentType, amount);
		payable.pay(payment);
		storage.addPayment(payment);
		return payment;
	}

	public void removeProductFromPricelist(Product product, Pricelist pricelist) {
		assert product != null;
		assert pricelist != null;

		pricelist.removeProduct(product);
	}

	public void addProductToPricelist(Product product, Pricelist pricelist, double price) {
		assert product != null;
		assert pricelist != null;
		
		pricelist.addProduct(product, price);
	}

	public List<Product> getProducts() {
		return storage.getProducts();
	}

	public Order createOrder() {
		return createOrder(activeUser, selectedPricelist);
	}

	public Order createOrder(User user, Pricelist pricelist) {
		assert user != null;
		assert pricelist != null;
		
		Order order = new Order(user, pricelist);
		storage.addOrder(order);
		return order;
	}

	public ProductOrder createProductOrder(Order order, Product product) {
		assert order != null;
		assert product != null;
		
		return order.createProductOrder(product);
	}

	public RentalProductOrder createRentalProductOrder(Order order, DepositProduct product) {
		assert order != null;
		assert product != null;
		
		return order.createRentalProductOrder(product);
	}

	public Customer createCustomer(String name, String address, String phone, String email) {
		assert name != null && !name.isEmpty();
		assert address != null && !address.isEmpty();
		assert phone != null && !phone.isEmpty();
		assert email != null && !email.isEmpty();
		
		Customer c = new Customer(name, address, phone, email);
		storage.addCustomer(c);
		return c;
	}

	public void removeCustomer(Customer c) throws Exception {
		assert c != null;
		
		for (Order o : storage.getOrders()) {
			if (o.getCustomer() != null && o.getCustomer().equals(c))
				throw new Exception("Customer has orders");
		}

		storage.removeCustomer(c);
	}

	public Storage loadStorage() throws IOException, ClassNotFoundException {
		return Storage.loadStorage();
	}

	public void saveStorage() throws IOException {
		Storage.saveStorage();
	}

	public void initStorage() throws DiscountParseException {
		try {
			storage = loadStorage();
			System.out.println("Loaded pricelists" + storage.getPricelists());
			setSelectedPricelist(storage.getPricelists().get(0));
			return;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Could not load storage, generating data from initStorage");
		}

		User user = createUser("John", "test", "test", Permission.ADMIN);
		User user1 = createUser("John Johnson", "test1", "test", Permission.NORMAL);

		Pricelist pl1 = createPricelist("Fredagsbar");
		setSelectedPricelist(pl1);
		Pricelist pl2 = createPricelist("Butik");

		addCategory("flaske");
		addCategory("fadøl");
		addCategory("fustage");
		addCategory("andet");

		Product productKlippekort = createProduct("Klippekort, 4 klip", 4, "andet", null);
		addProductToPricelist(productKlippekort, pl1, 100);
		addProductToPricelist(productKlippekort, pl2, 100);

		Product productFlaskeKlosterbryg = createProduct("Klosterbryg", 2, "flaske", null);
		addProductToPricelist(productFlaskeKlosterbryg, pl1, 50);
		addProductToPricelist(productFlaskeKlosterbryg, pl2, 36);
		Product productFlaskeSweetGeorgiaBrown = createProduct("Sweet Georgia Brown", 2, "flaske", null);
		addProductToPricelist(productFlaskeSweetGeorgiaBrown, pl1, 50);
		addProductToPricelist(productFlaskeSweetGeorgiaBrown, pl2, 36);
		Product productFlaskeExtraPilsner = createProduct("Extra Pilsner", 2, "flaske", "extra-pilsner.png");
		addProductToPricelist(productFlaskeExtraPilsner, pl1, 50);
		addProductToPricelist(productFlaskeExtraPilsner, pl2, 36);

		Product productFadolKlosterbryg = createProduct("Klosterbryg", 2, "fadøl", null);
		addProductToPricelist(productFadolKlosterbryg, pl1, 30);
		Product productFadolSweetGeorgiaBrown = createProduct("Sweet Georgia Brown", 2, "fadøl", null);
		addProductToPricelist(productFadolSweetGeorgiaBrown, pl1, 30);
		Product productFadolExtraPilsner = createProduct("Extra Pilsner", 2, "fadøl", null);
		addProductToPricelist(productFadolExtraPilsner, pl1, 30);

		DepositProduct depositProductKlosterbryg = createDepositProduct("Klosterbryg, 20 liter", null, "fustage", null,
				200);
		addProductToPricelist(depositProductKlosterbryg, pl2, 775);
		DepositProduct depositProductJazzClassic = createDepositProduct("Jazz Classic, 25 liter", null, "fustage", null,
				200);
		addProductToPricelist(depositProductJazzClassic, pl2, 625);
		DepositProduct depositProductExtraPilsner = createDepositProduct("Extra Pilsner, 25 liter", null, "fustage",
				null, 200);
		addProductToPricelist(depositProductExtraPilsner, pl2, 575);

		createTour(5, LocalDateTime.now(), 1000, Duration.of(1, ChronoUnit.HOURS));
		// Customers
		createCustomer("Hans Hansen", "Vestervej 38", "35698457", "somewhere@somethere.dk");
		createCustomer("Hans Jensen", "Østervej 38", "35864557", "somehere@somethere.dk");
		createCustomer("Østerli Nielsen", "Søndenvej 38", "8979854", "where@somethere.dk");
		Customer dos = createCustomer("Person 2.0", "Nordenvej 38", "39875557", "here@somethere.dk");
		createCustomer("Niels Sommer", "Åkæret 1", "35634687", "there@somethere.dk");
		Customer uno = createCustomer("Jens-Peter Petersen", "Nyborg", "878788457", "somehow@somethere.dk");

		// Order
		Order order1 = createOrder(user, pl1);
		ProductOrder po1 = createProductOrder(order1, productFadolKlosterbryg);
		createProductOrder(order1, productFadolSweetGeorgiaBrown);
		ProductOrder po2 = createProductOrder(order1, productFlaskeExtraPilsner);
		po1.setAmount(5);
		po2.setDiscount("-10");
		order1.setCustomer(uno);

		Order order2 = createOrder(user, pl2);
		createRentalProductOrder(order2, depositProductKlosterbryg);
		createPayment(order2, order2.totalPrice() + order2.totalDeposit(), PaymentType.CASH);
		order2.setCustomer(uno);

		Order order3 = createOrder(user1, pl1);
		createProductOrder(order3, productFadolKlosterbryg);
		createProductOrder(order3, productFadolSweetGeorgiaBrown);
		ProductOrder po4 = createProductOrder(order3, productFlaskeExtraPilsner);
		po4.setAmount(5);
		order3.setCustomer(dos);

	}

	public List<Order> getOrdersInPeriod(TimePeriod timePeriod) {
		List<Order> selected = new ArrayList<Order>();
		LocalDate fromDate = LocalDate.now();

		switch (timePeriod) {
		case DAY:
			fromDate = fromDate.minus(1, ChronoUnit.DAYS);
		case WEEK:
			fromDate = fromDate.minus(1, ChronoUnit.WEEKS);
		case MONTH:
			fromDate = fromDate.minus(1, ChronoUnit.MONTHS);
		case YEAR:
			fromDate = fromDate.minus(1, ChronoUnit.YEARS);
		case FOREVER:
			fromDate = LocalDate.ofYearDay(1970, 1);
		default:
			break;
		}

		for (Order o : storage.getOrders()) {
			if (o.getDate().isAfter(fromDate)) {
				selected.add(o);
			}
		}

		return selected;
	}

	public static Service getInstance() {
		return instance;
	}
}
