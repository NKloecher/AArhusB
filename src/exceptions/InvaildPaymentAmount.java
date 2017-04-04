package exceptions;

public class InvaildPaymentAmount extends RuntimeException {
	public InvaildPaymentAmount(String message) {
		super(message);
	}
}
