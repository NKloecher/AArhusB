package model;

import java.util.ArrayList;
import java.util.List;

public class Order {
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

    public RentalProductOrder createRentalProductOrder(Product product) {
        RentalProductOrder rentalProductOrder = new RentalProductOrder(product, this.pricelist);
        products.add(rentalProductOrder);
        return rentalProductOrder;
    }

    public void addPayment(Payment payment){
        payments.add(payment);
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }


}
