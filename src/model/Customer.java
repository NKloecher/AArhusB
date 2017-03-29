package model;

public class Customer extends Person {
    private String address;
    private String phone;
    private String email;

    public Customer(String address, String phone, String email) {
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}
