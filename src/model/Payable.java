package model;

public interface Payable {
	void pay(Payment payment);

	PaymentStatus paymentStatus() throws Exception;
}
