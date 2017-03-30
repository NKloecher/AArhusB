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
	 * sets serivce.user if username and password is correct
	 * if username or password is not correct it throws an error
	 */
	public void login(String username, String password) throws AuthenticationException {
    	for (User u : storage.getUsers()) {
    		if (u.getUsername().equals(username)) {
    			if (u.checkPassword(password)) {
    				activeUser = u;
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

	public Product createProduct(String name, Integer clips, String category, String image) {
		Product product = new Product(name, clips, category, image);
		storage.addProduct(product);
		return product;
	}

	public DepositProduct createDepositProduct(String name, Integer clips, String category, String image, double deposit){
    	DepositProduct depositProduct = new DepositProduct(name, clips, category, image, deposit);
    	storage.addProduct(depositProduct);
    	return depositProduct;
	}

	public Pricelist createPricelist(String name){
    	Pricelist pricelist = new Pricelist(name);
    	storage.addPricelist(pricelist);
    	return pricelist;
	}

	public void addProductToPricelist(Product product, Pricelist pricelist, double price){
		pricelist.addProduct(product, price);
	}

	public void initStorage() {
		createUser("test", "test");

		Pricelist pl1 = createPricelist("Fredagsbar");
		Pricelist pl2 = createPricelist("Butik");

		Product productKlippekort = createProduct("Klippekort, 4 klip", 4, "andet", null);
		addProductToPricelist(productKlippekort, pl1, 100);
		addProductToPricelist(productKlippekort, pl2, 100);

		Product productFlaskeKlosterbryg = createProduct("Klosterbryg", 2, "flaske", null);
		addProductToPricelist(productFlaskeKlosterbryg, pl1, 50);
		addProductToPricelist(productFlaskeKlosterbryg, pl2, 36);
		Product productFlaskeSweetGeorgiaBrown = createProduct("Sweet Georgia Brown", 2, "flaske", null);
		addProductToPricelist(productFlaskeSweetGeorgiaBrown, pl1, 50);
		addProductToPricelist(productFlaskeSweetGeorgiaBrown, pl2, 36);
		Product productFlaskeExtraPilsner = createProduct("Extra Pilsner", 2, "flaske", null);
		addProductToPricelist(productFlaskeExtraPilsner, pl1, 50);
		addProductToPricelist(productFlaskeExtraPilsner, pl2, 36);

		Product productFadolKlosterbryg = createProduct("Klosterbryg", 2, "fadøl", null);
		addProductToPricelist(productFadolKlosterbryg, pl1, 30);
		Product productFadolSweetGeorgiaBrown = createProduct("Sweet Georgia Brown", 2, "fadøl", null);
		addProductToPricelist(productFadolSweetGeorgiaBrown, pl1, 30);
		Product productFadolExtraPilsner = createProduct("Extra Pilsner", 2, "fadøl", null);
		addProductToPricelist(productFadolExtraPilsner, pl1, 30);

		DepositProduct depositProductKlosterbryg = createDepositProduct("Klosterbryg, 20 liter", null, "fustage", null, 200);
		addProductToPricelist(depositProductKlosterbryg, pl2, 775);
		DepositProduct depositProductJazzClassic = createDepositProduct("Jazz Classic, 25 liter", null, "fustage", null, 200);
		addProductToPricelist(depositProductJazzClassic, pl2, 625);
		DepositProduct depositProductExtraPilsner = createDepositProduct("Extra Pilsner, 25 liter", null, "fustage", null, 200);
		addProductToPricelist(depositProductExtraPilsner, pl2, 575);

	}
	
	public static Service getInstance() {
		return instance;
	}
}
