package model;

public class RentalProductOrder extends ProductOrder {
    private int unused = 0;
    private int returned = 0;

    public RentalProductOrder(Product product, Pricelist pricelist) {
        super(product, pricelist);
    }

    public boolean rentalComplete(){
        assert unused + returned <= getAmount();
        return unused + returned == getAmount();
    }
}
