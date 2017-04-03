package gui.table;

import java.util.ArrayList;
import java.util.List;

import gui.Handler;
import javafx.scene.Node;

public abstract class Column<A> {
	private final String name;
	protected List<Node> nodes = new ArrayList<>(); 
	
	public Column(String name) {
		super();
		this.name = name;
	}
	
	public abstract Node getNode(A owner);

	public String getName() {
		return name;
	}
	
	public void updateNodes(Handler<Node> handler) {
		for (Node n : nodes) {
			handler.exec(n);
		}
	}
}
