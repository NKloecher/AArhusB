package gui.table;

import javafx.scene.Node;

public abstract class Column<A> {
	private final String name;

	public Column(String name) {
		super();
		this.name = name;
	}
	
	public abstract Node getNode(A owner);

	public String getName() {
		return name;
	}
}
