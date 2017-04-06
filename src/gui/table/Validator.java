package gui.table;

@FunctionalInterface
public interface Validator<A> {
	/**
	 * returns null if ok, and an error message if not
	 */
	public String validate(A item, String value);
}