package model;

import exceptions.DiscountParseException;
import exceptions.InvalidPaymentAmount;

public interface Payable {
	void pay(Payment payment);

	PaymentStatus paymentStatus() throws DiscountParseException, InvalidPaymentAmount;
	
	double totalPayment();
}
