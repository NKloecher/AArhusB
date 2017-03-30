package model;

public class ProductOrder {
    private Discount discount = null;
    private int amount = 1;
    private Product product;
    private Pricelist pricelist;

    public ProductOrder(Product product, Pricelist pricelist) {
        this.product = product;
        this.pricelist = pricelist;
    }

    public double price() throws Exception {
        double total = amount * pricelist.getPrice(product);
        if (discount != null) {
            total = discount.getPrice(total);
        }
        return total;
    }

    public void setDiscount(String str) throws Exception {
        if (discount == null) {
            discount = new Discount();
        }
        discount.setDiscount(str);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
