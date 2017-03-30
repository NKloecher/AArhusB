package test;

import model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderTest {
	Pricelist pl;
	Product product100kr4clip;
	Product product100kr3clip;
	Product product50kr0clip;
	DepositProduct depositProduct500kr100rent;
	User user;

	public OrderTest() {
		user = new User("test", "test");
		pl = new Pricelist("Pricelist");
		product100kr4clip = new Product("100kr, 4clip", 4, "test", null);
		pl.addProduct(product100kr4clip, 100);
		product100kr3clip = new Product("100kr, 3clip", 3, "test", null);
		pl.addProduct(product100kr3clip, 100);
		product50kr0clip = new Product("50kr, 0clip", null, "test", null);
		pl.addProduct(product50kr0clip, 50);
		depositProduct500kr100rent = new DepositProduct("500kr, 100rent", null, "test", null, 100);
		pl.addProduct(depositProduct500kr100rent, 500);
	}

	@Test
	public void orderTotalPriceOneProduct(){
		Order order = new Order(user, pl);
		order.createProductOrder(product100kr4clip);
		assertEquals(100, order.totalPrice(), 0.1);
	}

	@Test
	public void orderTotalPriceTwoProduct(){
		Order order = new Order(user, pl);
		order.createProductOrder(product100kr4clip);
		order.createProductOrder(product100kr3clip);
		assertEquals(200, order.totalPrice(), 0.1);
	}


	@Test
	public void orderTotalPriceOneProductTwoItems(){
		Order order = new Order(user, pl);
		ProductOrder productOrder = order.createProductOrder(product100kr4clip);
		productOrder.setAmount(2);
		assertEquals(200, order.totalPrice(), 0.1);
	}

	@Test
	public void orderTotalPriceOneDepositProduct(){
		Order order = new Order(user, pl);
		order.createProductOrder(depositProduct500kr100rent);
		assertEquals(500, order.totalPrice(), 0.1);
	}

	@Test
	public void orderTotalDepositOneProduct(){
		Order order = new Order(user, pl);
		order.createRentalProductOrder(depositProduct500kr100rent);
		assertEquals(100, order.totalDeposit(), 0.1);
	}


	@Test
	public void orderTotalDepositOneProductTwoItems(){
		Order order = new Order(user, pl);
		RentalProductOrder rentalProductOrder = order.createRentalProductOrder(depositProduct500kr100rent);
		rentalProductOrder.setAmount(2);
		assertEquals(200, order.totalDeposit(), 0.1);
	}
}