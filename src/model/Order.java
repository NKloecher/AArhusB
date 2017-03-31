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

    public void setDiscount(String str) throws Exception {
        if (discount == null) {
            discount = new Discount();
        }
        discount.setDiscount(str);
    }

    private List<ProductOrder> getAllProducts(){
        List<ProductOrder> allProducts = new ArrayList<>(products);
        allProducts.addAll(productsRental);
        return allProducts;
    }

    /**
     * Doesn't include deposit
     * @return
     */
    public double totalPrice() throws Exception{
        double sum = 0;
        for (ProductOrder productOrder : getAllProducts()){
            sum += productOrder.price();
        }

        if (discount != null) {
            sum = discount.getPrice(sum);
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

    public double totalPayment(){
        double sum = 0;
        for (Payment payment : payments){
            sum += payment.getAmount();
        }
        return sum;
    }

    @Override
    public void pay(Payment payment) {
        payments.add(payment);
    }

    @Override
    public PaymentStatus paymentStatus() throws Exception{
        if (getAllProducts().size() == 0){
            return PaymentStatus.UNPAID;
        }

        if (totalPrice() > totalPayment()) {
            return PaymentStatus.UNPAID;
        }
        return null;
    }
}
