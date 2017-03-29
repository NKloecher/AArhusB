package model;

public class ProductOrder {
    private double discount = 0;
    private int amount = 1;
    private PaymentType paymentType;
    private Product product;
    private Pricelist pricelist;

    public ProductOrder(Product product, Pricelist pricelist) {
        this.product = product;
        this.pricelist = pricelist;
    }
}
