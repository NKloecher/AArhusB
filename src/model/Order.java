package model;

import java.util.ArrayList;
import java.util.List;

public class Order implements Payable{
    private List<ProductOrder> products = new ArrayList<>();
    private List<RentalProductOrder> productsRental = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private User user;
    private Pricelist pricelist;
    private Discount discount;

    public Order(User user, Pricelist pricelist) {
        this.user = user;
        this.pricelist = pricelist;
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

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    /**
     * Doesn't include deposit
     * @return
     */
    public double totalPrice() throws Exception{
        List<ProductOrder> allProducts = new ArrayList<>(products);
        allProducts.addAll(productsRental);

        double sum = 0;
        for (ProductOrder productOrder : allProducts){
            sum += productOrder.price();
        }
        return sum;
    }

    public double totalDeposit(){
        double sum = 0;
        for (RentalProductOrder productOrder : productsRental){
            sum += ((DepositProduct) productOrder.getProduct()).getDeposit() * productOrder.getAmount();
        }
        return sum;
    }

    @Override
    public void pay(Payment payment) {
        payments.add(payment);
    }
}
