package service;

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
        product.setName(name);
    }

    public void updateProductClips(Product product, Integer clips) {
        product.setClips(clips);
    }

    public void updateProductCategory(Product product, String category) {
        product.setCategory(category);
    }

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
     * if category is "All" every category will be selected
     */
    public List<Product> getMatchingProducts(String query, String category,
        List<Product> products) {
        List<Product> selected = new ArrayList<>();

        for (Product p : products) {
            if (p.getName().toLowerCase().contains(query.toLowerCase())
                && (category == "All" || p.getCategory().equals(category))) {
                selected.add(p);
            }
        }

        return selected;
    }

    /**
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

    public void logout() {
        assert activeUser != null;
        activeUser = null;
    }

    public void setSelectedPricelist(Pricelist pricelist) {
        selectedPricelist = pricelist;
    }

    public Pricelist getSelectedPricelist() {
        return selectedPricelist;
    }

    public void setPricelistPrice(Pricelist pricelist, Product product, double price) {
        pricelist.setPrice(product, price);
    }

    /**
     * is null if no user is logged in
     */
    public User getActiveUser() {
        return activeUser;
    }

    public void addCategory(String category) {
        storage.addCategory(category);
    }

    public User createUser(String name, String username, String password, Permission permission) {
        User u = new User(name, username, password, permission);

        storage.addUser(u);

        return u;
    }

    public void updateUserPassword(User user, String password){
        user.setPassword(password);
    }

    public void deleteUser(User user) {
        storage.deleteUser(user);
    }

    public void updateUserName(User user, String name) {
        user.setName(name);
    }

    public void updateUserUsername(User user, String username) {
        user.setUsername(username);
    }

    public void updateUserPermission(User user, Permission permission) {
        user.setPermission(permission);
    }

    public boolean usernameIsUnique(String username, User user) {
        for (User u : storage.getUsers()) {
            if (!u.equals(user) && u.getUsername().equals(username)) {
                return false;
            }
        }

        return true;
    }

    public Tour createTour(int persons, LocalDateTime date, double price, Duration duration){
        Tour tour = new Tour(persons, date, price, duration, activeUser);
        storage.addTour(tour);
        return tour;
    }

    public List<Tour> getTours(){
        return storage.getTours();
    }

    public List<Tour> getTours(LocalDate date){
        List<Tour> tours = new ArrayList<>();
        for (Tour tour : getTours()){
            if (tour.getDate().toLocalDate().equals(date)){
                tours.add(tour);
            }
        }
        return tours;
    }

    public Set<LocalDate> getTourDates(){
        Set<LocalDate> dates = new HashSet<>();
        for (Tour tour : getTours()){
            dates.add(tour.getDate().toLocalDate());
        }
        return dates;
    }

    public void updateTourPersons(Tour tour, int persons){
        tour.setPersons(persons);
    }

    public void updateTourDate(Tour tour, LocalDateTime date){
        tour.setDate(date);
    }

    public void updateTourPrice(Tour tour, double price){
        tour.setPrice(price);
    }

    public void updateTourDuration(Tour tour, Duration duration){
        tour.setDuration(duration);
    }

    public void updateProductOrderAmount(ProductOrder productOrder, int amount) {
    	productOrder.setAmount(amount);
    }
    
    public Product createProduct(String name, Integer clips, String category, String image) {
        Product product = new Product(name, clips, category, image);
        storage.addProduct(product);
        return product;
    }

    public DepositProduct createDepositProduct(String name, Integer clips, String category,
        String image, double deposit) {
        DepositProduct depositProduct = new DepositProduct(name, clips, category, image, deposit);
        storage.addProduct(depositProduct);
        return depositProduct;
    }

    public Pricelist createPricelist(String name) {
        Pricelist pricelist = new Pricelist(name);
        storage.addPricelist(pricelist);
        return pricelist;
    }

    public Payment createPayment(Payable payable, double amount, PaymentType paymentType) {
        Payment payment = new Payment(paymentType, amount);
        payable.pay(payment);
        storage.addPayment(payment);
        return payment;
    }

    public void removeProductFromPricelist(Product product, Pricelist pricelist){
        pricelist.removeProduct(product);
    }

    public void addProductToPricelist(Product product, Pricelist pricelist, double price) {
        pricelist.addProduct(product, price);
    }

    public List<Product> getProducts() {
        return storage.getProducts();
    }

    public Order createOrder() {
    	return createOrder(activeUser, selectedPricelist);
    }

    public Order createOrder(User user, Pricelist pricelist) {
        Order order = new Order(user, pricelist);
        storage.addOrder(order);
        return order;
    }

    public ProductOrder createProductOrder(Order order, Product product) {
        return order.createProductOrder(product);
    }

    public RentalProductOrder createRentalProductOrder(Order order, DepositProduct product) {
        return order.createRentalProductOrder(product);
    }

    public Customer createCustomer(String name, String address, String phone, String email) {
        Customer c = new Customer(name, address, phone, email);
        storage.addCustomer(c);
        return c;
    }

    public void initStorage() throws DiscountParseException {
        User test = createUser("John", "test", "test", Permission.ADMIN);

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
        Product productFlaskeSweetGeorgiaBrown =
            createProduct("Sweet Georgia Brown", 2, "flaske", null);
        addProductToPricelist(productFlaskeSweetGeorgiaBrown, pl1, 50);
        addProductToPricelist(productFlaskeSweetGeorgiaBrown, pl2, 36);
        Product productFlaskeExtraPilsner = createProduct("Extra Pilsner", 2, "flaske", "extra-pilsner.png");
        addProductToPricelist(productFlaskeExtraPilsner, pl1, 50);
        addProductToPricelist(productFlaskeExtraPilsner, pl2, 36);

        Product productFadolKlosterbryg = createProduct("Klosterbryg", 2, "fadøl", null);
        addProductToPricelist(productFadolKlosterbryg, pl1, 30);
        Product productFadolSweetGeorgiaBrown =
            createProduct("Sweet Georgia Brown", 2, "fadøl", null);
        addProductToPricelist(productFadolSweetGeorgiaBrown, pl1, 30);
        Product productFadolExtraPilsner = createProduct("Extra Pilsner", 2, "fadøl", null);
        addProductToPricelist(productFadolExtraPilsner, pl1, 30);

        DepositProduct depositProductKlosterbryg =
            createDepositProduct("Klosterbryg, 20 liter", null, "fustage", null, 200);
        addProductToPricelist(depositProductKlosterbryg, pl2, 775);
        DepositProduct depositProductJazzClassic =
            createDepositProduct("Jazz Classic, 25 liter", null, "fustage", null, 200);
        addProductToPricelist(depositProductJazzClassic, pl2, 625);
        DepositProduct depositProductExtraPilsner =
            createDepositProduct("Extra Pilsner, 25 liter", null, "fustage", null, 200);
        addProductToPricelist(depositProductExtraPilsner, pl2, 575);

        createTour(5, LocalDateTime.now(), 1000, Duration.of(1, ChronoUnit.HOURS));
        // Customers
        createCustomer("Hans Hansen", "Vestervej 38", "35698457", "somewhere@somethere.dk");
        createCustomer("Hans Jensen", "Østervej 38", "35864557", "somehere@somethere.dk");
        createCustomer("Østerli Nielsen", "Søndenvej 38", "8979854", "where@somethere.dk");
        createCustomer("Person 2.0", "Nordenvej 38", "39875557", "here@somethere.dk");
        createCustomer("Niels Sommer", "Åkæret 1", "35634687", "there@somethere.dk");
        Customer uno =
            createCustomer("Jens-Peter Petersen", "Nyborg", "878788457", "somehow@somethere.dk");

        //Order
        Order order1 = createOrder(test, pl1);
        ProductOrder test2 = createProductOrder(order1, productFadolKlosterbryg);
        createProductOrder(order1, productFadolSweetGeorgiaBrown);
        ProductOrder test3 = createProductOrder(order1, productFlaskeExtraPilsner);
        test3.setDiscount("-10");
        test2.setAmount(5);
        order1.setCustomer(uno);
        
        Order order2 = createOrder(test, pl2);
        createRentalProductOrder(order2, depositProductKlosterbryg);
        createPayment(order2, order2.totalPrice() + order2.totalDeposit(), PaymentType.CASH);
        
    }

    public static Service getInstance() {
        return instance;
    }
}
