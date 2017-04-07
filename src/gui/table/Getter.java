package gui.table;

@FunctionalInterface
public interface Getter<A, B> {
	public B get(A item);
}
