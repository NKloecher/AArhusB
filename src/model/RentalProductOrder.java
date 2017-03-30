package model;

public class RentalProductOrder extends ProductOrder {
    private int unused;
    private int notReturned;

    public RentalProductOrder(Product product, Pricelist pricelist) {
        super(product, pricelist);
    }
}
