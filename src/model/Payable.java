package model;

import exceptions.DiscountParseException;

public interface Payable {
	void pay(Payment payment);

	PaymentStatus paymentStatus() throws DiscountParseException;
}
