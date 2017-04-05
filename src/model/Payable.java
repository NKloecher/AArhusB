package model;

import exceptions.DiscountParseException;
import exceptions.InvaildPaymentAmount;

public interface Payable {
	void pay(Payment payment);

	PaymentStatus paymentStatus() throws DiscountParseException, InvaildPaymentAmount;
	
	double totalPayment();
}
