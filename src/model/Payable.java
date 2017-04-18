package model;

import exceptions.DiscountParseException;
import exceptions.InvaildPaymentAmount;
import javafx.util.Pair;

public interface Payable {
	void pay(Payment payment);

	PaymentStatus paymentStatus() throws DiscountParseException, InvaildPaymentAmount;
	
	double totalPayment();
	
	double getPrice();
	
	Pair<Integer, Double> totalClipCardPrice();
}
