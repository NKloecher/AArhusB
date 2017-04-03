package model;

import exceptions.DiscountParseException;

public class ProductOrder {
    private Discount discount = null;
    private int amount = 1;
    private Product product;
    private double price;

    public ProductOrder(Product product, Pricelist pricelist) {
        this.product = product;
        this.price = pricelist.getPrice(product);
    }

    public double individualPrice() throws DiscountParseException {
        return price() / amount;
    }

    public double price() throws DiscountParseException {
    	double p = price * amount;
    	
    	if (discount != null) p = discount.getPrice(p);
    	
        return p;
    }

    public String getDiscount() {
    	if (discount == null) return "";
    	
    	return discount.getValue();
    }
    public void setDiscount(String str) throws DiscountParseException {
        if (discount == null) {
            discount = new Discount();
        }
        discount.setDiscount(str);
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
