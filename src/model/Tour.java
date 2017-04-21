package model;

import exceptions.DiscountParseException;
import exceptions.InvalidPaymentAmount;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tour implements Payable, Serializable {
    private List<Payment> payments = new ArrayList<>();
    private int persons;
    private LocalDateTime date;
    private double price;
    private Duration duration;
    private User user;

    public Tour(int persons, LocalDateTime date, double price, Duration duration, User user) {
        this.persons = persons;
        this.date = date;
        this.price = price;
        this.duration = duration;
        this.user = user;
    }

    /**
     * Adds a payment
     */
    @Override
    public void pay(Payment payment) {
        payments.add(payment);
    }

    /**
     * Calculates the current payment status
     */
    @Override
    public PaymentStatus paymentStatus() throws DiscountParseException {
        if (totalPayment() >= price) {
            return PaymentStatus.ORDERPAID;
        }
        return PaymentStatus.UNPAID;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double totalPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * Calculate the total payment
     */
    @Override
    public double totalPayment() {
        double sum = 0;
        for (Payment payment : payments) {
            sum += payment.getAmount();
        }
        return sum;
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public Pair<Integer, Double> totalClipCardPrice() {
        return null;
    }

    @Override
    public boolean tryPay(Payment payment) {
        pay(payment);
        try {
            paymentStatus();
            return true;
        }
        catch (InvalidPaymentAmount e) {
            payments.remove(payment);
            return false;
        }
    }
}
