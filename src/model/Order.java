package model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<ProductOrder> products = new ArrayList<>();
    private User user;
    private Pricelist pricelist;

    public Order(User user, Pricelist pricelist) {
        this.user = user;
        this.pricelist = pricelist;
    }
}
