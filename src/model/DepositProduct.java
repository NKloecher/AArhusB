package model;

public class DepositProduct extends Product {
    private double deposit;

    public DepositProduct(String name, Integer clips, String category, String image, double deposit) {
        super(name, clips, category, image);
        this.deposit = deposit;
    }
}
