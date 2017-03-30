package model;

public class Customer extends Person {
    private String address;
    private String phone;
    private String email;

    public Customer(String name, String address, String phone, String email) {
    	super(name);
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}
