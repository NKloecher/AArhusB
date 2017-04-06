package gui.table;

@FunctionalInterface
public interface ActionHandler<A> {
	public void exec(A item);
}