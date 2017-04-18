package model;

import exceptions.DiscountParseException;
import exceptions.InvalidPaymentAmount;
import javafx.util.Pair;

public interface Payable {
    void pay(Payment payment);

    PaymentStatus paymentStatus() throws DiscountParseException, InvalidPaymentAmount;

    double totalPayment();

    double getPrice();

    Pair<Integer, Double> totalClipCardPrice();
}
