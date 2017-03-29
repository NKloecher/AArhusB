package model;

public class RentalProductOrder extends ProductOrder {
    private int unused;
    private int notReturned;
    private PaymentType depositPaymentType;

    public RentalProductOrder(Product product, Pricelist pricelist) {
        super(product, pricelist);
    }
}
