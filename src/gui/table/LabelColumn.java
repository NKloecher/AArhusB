package gui.table;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class LabelColumn<A> extends Column<A> {
	private Getter<A, String> getter;
	
	public LabelColumn(String name, Getter<A, String> getter) {
		super(name);
		
		this.getter = getter;
	}

	@Override
	public Node getNode(A owner) {
		String value = getter.get(owner);
		
		return new Label(value);
	}
	
}
