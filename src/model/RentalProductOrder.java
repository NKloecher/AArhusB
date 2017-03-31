package model;

public class RentalProductOrder extends ProductOrder {
    private int unused = 0;
    private int returned = 0;
    private int notReturned = 0;

    public RentalProductOrder(Product product, Pricelist pricelist) {
        super(product, pricelist);
    }

    public boolean rentalComplete(){
        assert unused + returned <= getAmount();
        return unused + returned == getAmount();
    }

    public int getUnused() {
        return unused;
    }

    public int getReturned() {
        return returned;
    }

    public int getNotReturned() {
        return notReturned;
    }

    public void setUnused(int unused) {
        this.unused = unused;
    }

    public void setReturned(int returned) {
        this.returned = returned;
    }

    public void setNotReturned(int notReturned) {
        this.notReturned = notReturned;
    }
}
