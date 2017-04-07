package test;

import model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderTest {
	User user;
	Pricelist pricelist;
	Product simpleProduct;
	DepositProduct simpleDepositProduct;

	public OrderTest() {
		user = new User("test", "test", "test", Permission.NORMAL);
		pricelist = new Pricelist("test");

		simpleProduct = new Product("Simple Product", null, "test", null);
		pricelist.addProduct(simpleProduct, 100);

		simpleDepositProduct = new DepositProduct("Simple Deposit Product", null, "test", null, 10);
		pricelist.addProduct(simpleDepositProduct, 100);
	}

	@Before
	public void before(){}

	@Test
	public void orderHasRentalOrderTS1None() throws Exception {
		Order order = new Order(user, pricelist);

		assertEquals(false, order.hasRentalOrder());
	}

	@Test
	public void orderHasRentalOrderTS2DepositProduct() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleDepositProduct);

		assertEquals(true, order.hasRentalOrder());
	}

	@Test
	public void orderHasRentalOrderTS3Product() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleProduct);

		assertEquals(false, order.hasRentalOrder());
	}

	@Test
	public void orderTotalPriceTS2OneProductOrder() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleProduct);

		assertEquals(100, order.totalPrice(), 0.01);
	}

	@Test
	public void orderTotalPriceTS3OneRentalProductOrder() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleDepositProduct);

		assertEquals(100, order.totalPrice(), 0.01);
	}

	@Test
	public void orderTotalPriceTS4OneRentalProductOrderOneProductOrder() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleDepositProduct);
		order.addProduct(simpleProduct);

		assertEquals(200, order.totalPrice(), 0.01);
	}

	@Test
	public void orderTotalPriceTS4OneRentalProductOrderOneProductOrderDiscount() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleDepositProduct);
		order.addProduct(simpleProduct);
		order.setDiscount("40%");

		assertEquals(120, order.totalPrice(), 0.01);
	}

	@Test
	public void orderAllRentalsReturnedTS1NoRentals() throws Exception {
		Order order = new Order(user, pricelist);
		assertEquals(true, order.allRentalsReturned());
	}


	@Test
	public void orderAllRentalsReturnedTS2OneRentalsReturned() throws Exception {
		Order order = new Order(user, pricelist);
		RentalProductOrder rentalProductOrder = (RentalProductOrder) order.addProduct(simpleDepositProduct);
		rentalProductOrder.setReturned(1);

		assertEquals(true, order.allRentalsReturned());
	}

	@Test
	public void orderAllRentalsReturnedTS3OneRentalsNotReturned() throws Exception {
		Order order = new Order(user, pricelist);
		RentalProductOrder rentalProductOrder = (RentalProductOrder) order.addProduct(simpleDepositProduct);
		rentalProductOrder.setReturned(0);

		assertEquals(false, order.allRentalsReturned());
	}

	@Test
	public void orderAllRentalsReturnedTS4TwoRentalsOneReturned() throws Exception {
		Order order = new Order(user, pricelist);
		RentalProductOrder rentalProductOrder1 = (RentalProductOrder) order.addProduct(simpleDepositProduct);
		RentalProductOrder rentalProductOrder2 = (RentalProductOrder) order.addProduct(simpleDepositProduct);
		rentalProductOrder1.setReturned(1);
		rentalProductOrder2.setReturned(0);

		assertEquals(false, order.allRentalsReturned());
	}

	@Test
	public void orderTotalDepositTS1NoProducts() throws Exception {
		Order order = new Order(user, pricelist);

		assertEquals(null, order.totalDeposit());
	}

	@Test
	public void orderTotalDepositTS2OneProductsOrder() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleProduct);

		assertEquals(null, order.totalDeposit());
	}

	@Test
	public void orderTotalDepositTS3OneDepositProductsOrder() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleDepositProduct);

		assertEquals(10, order.totalDeposit(), 0.01);
	}

	@Test
	public void orderTotalDepositTS4TwoDepositProductsOrder() throws Exception {
		Order order = new Order(user, pricelist);
		order.addProduct(simpleDepositProduct);
		DepositProduct dp = new DepositProduct("Test desposit", null, "test", null, 20);
		pricelist.addProduct(dp, 100);
		order.addProduct(dp);

		assertEquals(30, order.totalDeposit(), 0.01);
	}

}