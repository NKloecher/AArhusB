package model;

import exceptions.DiscountParseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tour implements Payable{
    private List<Payment> payments = new ArrayList<>();
    private int persons;
    private LocalDateTime date;
    private double price;
    private double duration;
    private User user;

    public Tour(int persons, LocalDateTime date, double price, double duration, User user) {
        this.persons = persons;
        this.date = date;
        this.price = price;
        this.duration = duration;
        this.user = user;
    }

    @Override
    public void pay(Payment payment) {
		payments.add(payment);
    }

	@Override
	public PaymentStatus paymentStatus() throws DiscountParseException {
		return null;
	}
}
