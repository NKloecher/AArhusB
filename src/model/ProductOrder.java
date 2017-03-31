package model;

import exceptions.DiscountParseException;

public class ProductOrder {
    private Discount discount = null;
    private int amount = 1;
    private Product product;
    private Pricelist pricelist;

    public ProductOrder(Product product, Pricelist pricelist) {
        this.product = product;
        this.pricelist = pricelist;
    }

    public double individualPrice() throws DiscountParseException {
        double total = pricelist.getPrice(product);
        if (discount != null) {
            total = discount.getPrice(total);
        }
        return total;
    }

    public double price() throws DiscountParseException {
        return amount * individualPrice();
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
