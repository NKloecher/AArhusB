package gui.table;

@FunctionalInterface
public interface ValidationHandler {
	public void onValidate(String error, boolean isValid);
}