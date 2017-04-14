package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.Service;
import storage.Storage;

public class Statistics {
	private final Service service = Service.getInstance();
	private final Storage storage = Storage.getInstance();
	private List<Order> orders;
	
	public Statistics() {
		
	}
	
	public Map<Pricelist, Double> getSalesPrPricelist() {
		Map<Pricelist, Double> sales = new HashMap<>();
		
		for (Order o : orders) {
			Double currentTotal = sales.get(o.getPricelist());
			
			if (currentTotal == null) {
				currentTotal = 0.0;
			}
			
			currentTotal += o.totalPrice();
			
			sales.put(o.getPricelist(), currentTotal);
		}
		
		return sales;
	}

	public Map<String, Double> getSalesPrBeer() {
		Map<String, Double> sales = new HashMap<>();
		
		for (Order o : orders) {
			for (ProductOrder po : o.getAllProducts()) {
				Product p = po.getProduct();
				String category = po.getProduct().getCategory();
				
				if (category.equals("fadøl") || category.equals("flaske") || category.equals("fustage")) {
					
					String name = p.getName().split(",")[0].trim();
					Double currentTotal = sales.get(name);
					
					if (currentTotal == null) currentTotal = 0.0;
					
					currentTotal += po.price();
					
					sales.put(name, currentTotal);
				}
			}
		}
		
		return sales;
	}
	
	public double getClipCardSales() {
		double d = 0;
		
		for (Order o : orders) {
			for (ProductOrder po : o.getProductOrders()) {
				if (po.getProduct().getName().startsWith("Klippekort")) {
					d += po.price();
				}
			}
		}
		
		return d;
	}
	
	public int getClipCardUses() {
		int d = 0;
		
		for (Order o : orders) {
			for (Payment p : o.getPayments()) {
				if (p.getPaymentType() == PaymentType.CLIP_CARD) {
					d += p.getAmount();
				}	
			}
		}
		
		return d;
	}
	
	public Map<String, Double> getSalesPrCategory() {
		Map<String, Double> sales = new HashMap<>();
		
		for (Order o : orders) {
			for (ProductOrder po : o.getProductOrders()) {
				String category = po.getProduct().getCategory();
				Double currentTotal = sales.get(category);
				
				if (currentTotal == null) currentTotal = 0.0;
				
				currentTotal += po.price();
				
				sales.put(category, currentTotal);
			}
		}
		
		return sales;
	}
	
	public Map<User, Double> getSalesPrUser() {
		Map<User, Double> sales = new HashMap<>();
		
		for (Order o : orders) {
			Double currentTotal = sales.get(o.getUser());
			
			if (currentTotal == null) currentTotal = 0.0;
			
			currentTotal += o.totalPrice();
			
			sales.put(o.getUser(), currentTotal);
		}
		
		return sales;
	}
	
	public double getTotalSales() {
		double total = 0;
		
		for (Order o : orders) {
			total += o.totalPrice();
		}
		
		return total;
	}
	
	public void setTimePeriod(TimePeriod timePeriod) {
		orders = service.getOrdersInPeriod(timePeriod);
	}
}
