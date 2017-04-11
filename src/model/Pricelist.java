package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pricelist implements Serializable {
    private String name;
    private Map<Product, Double> products = new HashMap<>();

    public Pricelist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addProduct(Product product, double price) {
        products.put(product, price);
    }

    /**
     * Returns the price of a product
     */
    public double getPrice(Product key) {
        return products.get(key);
    }

    public void setPrice(Product product, double price) {
        products.put(product, price);
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products.keySet());
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    @Override
    public String toString() {
        return name;
    }
}
