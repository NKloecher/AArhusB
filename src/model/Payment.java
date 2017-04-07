package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Payment implements Serializable {
    private PaymentType paymentType;
    private double amount;
    private LocalDateTime date;

    public Payment(PaymentType paymentType, double amount) {
        this.paymentType = paymentType;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public double getAmount() {
        return amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }
}
