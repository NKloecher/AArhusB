package gui.table;

@FunctionalInterface
public interface Setter<A, B> {
	public void set(A item, B value);
}