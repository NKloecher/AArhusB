package model;

import exceptions.DiscountParseException;
import exceptions.InvaildPaymentAmount;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order implements Payable, Serializable {
    private List<ProductOrder> products = new ArrayList<>();
    private List<RentalProductOrder> productsRental = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private User user;
    private Pricelist pricelist;
    private Discount discount;
    private Customer customer;
    private LocalDate date = LocalDate.now();

    public Order(User user, Pricelist pricelist) {
        this.user = user;
        this.pricelist = pricelist;
    }

	public ProductOrder addProduct(Product product) {
		if (product instanceof DepositProduct) {
			return createRentalProductOrder((DepositProduct)product);
		}
		else {
			return createProductOrder(product);
		}
	}
	public ProductOrder removeProduct(Product product) {
		for (int i = 0; i < products.size(); i++) {
			ProductOrder po = products.get(i);
			
			if (po.getProduct().equals(product)) {
				products.remove(po);
				
				return po;
			}
		}
			
		for (int i = 0; i < productsRental.size(); i++) {
			ProductOrder po = productsRental.get(i);
			
			if (po.getProduct().equals(product)) {
				productsRental.remove(i);
				
				return po;
			}
		}
		
		return null;
	}
	
	public User getUser() {
		return user;
	}
	
	public Pricelist getPricelist() {
		return pricelist;
	}
    
    public ProductOrder createProductOrder(Product product) {
        ProductOrder productOrder = new ProductOrder(product, this.pricelist);
        products.add(productOrder);
        return productOrder;
    }

    public RentalProductOrder createRentalProductOrder(DepositProduct product) {
        RentalProductOrder rentalProductOrder = new RentalProductOrder(product, this.pricelist);
        productsRental.add(rentalProductOrder);
        return rentalProductOrder;
    }

    public void setDiscount(String str) throws DiscountParseException {
        if (discount == null) {
            discount = new Discount();
        }
        discount.setDiscount(str);
    }

    public List<ProductOrder> getProductOrders() {
    	return new ArrayList<>(products);
    }
    
    public List<RentalProductOrder> getRentalProductOrders() {
    	return new ArrayList<>(productsRental);
    }
    
    public List<ProductOrder> getAllProducts(){
        List<ProductOrder> allProducts = new ArrayList<>(products);
        allProducts.addAll(productsRental);
        return allProducts;
    }

    /**
     * Checks if order has any rental orders
     */
    public boolean hasRentalOrder() {
        return productsRental.size() > 0;
    }

    /**
     * Calculate the total price
     * NOTE: Doesn't include deposit
     */
    public double totalPrice() throws DiscountParseException {
        double sum = 0;
        for (ProductOrder productOrder : getAllProducts()) {
            sum += productOrder.price();
        }

        if (discount != null) {
            sum = discount.getPrice(sum);
        }
        return sum;
    }

    /**
     * Checks if all products are returned
     */
    public boolean allRentalsReturned() {
        for (RentalProductOrder order : productsRental) {
            if (!order.isReturned()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the total deposit after the products are returned
     */
    public double totalDepositAfterReturn() throws DiscountParseException {
        double sum = 0;
        for (RentalProductOrder productOrder : productsRental) {
            sum += productOrder.getDepositAfterReturn();
        }
        return sum;

    }

    /**
     * Calculate the total deposit
     */
    public Double totalDeposit() {
        double sum = 0;
        for (RentalProductOrder productOrder : productsRental) {
            sum += productOrder.getDeposit();
        }
        
        if (sum == 0) return null;
        
        return sum;
    }

    /**
     * Calculate the amount of clip card paid
     */
    private int totalClipCardPaid(){
        int sum = 0;
        for (Payment payment : payments) {
            if (payment.getPaymentType() == PaymentType.CLIP_CARD){
                sum += payment.getAmount();
            }
        }
        return sum;
    }

    /**
     * Calculate the how much the clip card payments are worth
     * NOTE: It will always try to maximize the value of each clip card
     */
    private double totalPaymentClipCard(){
        double sum = 0;
        int clips = totalClipCardPaid();

        if (clips == 0){
            return 0;
        }
        List<ProductOrder> productOrders = new ArrayList<>(this.products);

        productOrders.sort((ProductOrder p1, ProductOrder p2) -> {
            double p1ClipRatio = p1.individualPrice() / (double) p1.getProduct().getClips();
            double p2ClipRatio = p2.individualPrice() / (double) p2.getProduct().getClips();
            return Double.compare(p2ClipRatio, p1ClipRatio);
        });

        for (ProductOrder productOrder : productOrders) {
            int orderClips = productOrder.getProduct().getClips() * productOrder.getAmount();
            if (clips > orderClips){
                clips -= orderClips;
                sum += productOrder.price();
            } else {
                sum += (productOrder.individualPrice() / (double) productOrder.getProduct().getClips()) * clips;
                return sum;
            }
        }
        throw new InvaildPaymentAmount("");
    }

    /**
     * Calculate the total of all payments, including clip cards
     */
    public double totalPayment() {
        double sum = 0;
        for (Payment payment : payments) {
            if (payment.getPaymentType() != PaymentType.CLIP_CARD){
                sum += payment.getAmount();
            }
        }
        sum += totalPaymentClipCard();
        return sum;
    }

    /**
     * Add a payment to the order
     */
    @Override
    public void pay(Payment payment) {
        payments.add(payment);
    }
    
    public List<Payment> getPayments() {
    	return new ArrayList<>(payments);
    }

    /**
     * Calculates the current status of an order
     */
    @Override
    public PaymentStatus paymentStatus() throws DiscountParseException, InvaildPaymentAmount {
        if (getAllProducts().size() == 0) {
            return PaymentStatus.UNPAID;
        }
        if (hasRentalOrder()) {
            if (allRentalsReturned()) {
                double returnPrice = totalDepositAfterReturn();
                if (totalPayment() == totalPrice() + returnPrice) {
                    return PaymentStatus.ORDERPAID;
                }
                else if (totalPayment() < totalPrice() + returnPrice) {
                    return PaymentStatus.UNPAID;
                }
                else if (totalPayment() > totalPrice() + returnPrice) {
                    return PaymentStatus.DEPOSITNOTPAIDBACK;
                }
            }
            else if (totalPayment() < totalDeposit()) {
                return PaymentStatus.UNPAID;
            }
            else if (totalPayment() >= totalDeposit()
                && totalPayment() <= totalPrice() + totalDeposit()) {
                return PaymentStatus.DEPOSITPAID;
            }
            else {
                throw new InvaildPaymentAmount("The order was overpaid");
            }
        }
        else {
            if (totalPayment() < totalPrice()) {
                return PaymentStatus.UNPAID;
            }
            else if (totalPayment() == totalPrice()) {
                return PaymentStatus.ORDERPAID;
            }
            else {
                throw new InvaildPaymentAmount("The order was overpaid");
            }
        }

        return null;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer c) {
        customer = c;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        try {
            return totalPrice() + "kr " + date.toString();
        }
        catch (DiscountParseException e) {
            return date.toString();
        }
    }
}
