package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pricelist {
    private String name;
    private Map<Product, Double> products = new HashMap<>();

    public Pricelist(String name) {
        this.name = name;
    }

    public void addProduct(Product product, double price) {
        products.put(product, price);
    }

    public double getPrice(Product key) {
        return products.get(key);
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<Product>(products.keySet());
    }

}
