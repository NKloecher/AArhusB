package gui.table;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class ValueColumn<A,B> extends Column<A> {
	private final Getter<A,B> getter;
	private final Setter<A,B> setter;
	
	public ValueColumn(String name, Getter<A, B> getter, Setter<A, B> setter) {
		super(name);
		
		this.getter = getter;
		this.setter = setter;
	}
	
	@Override
	public Node getNode(A owner) {
		B value = this.getter.get(owner);
		
		if (value instanceof String || value instanceof Integer) {
			TextField tf = new TextField();
			
			tf.setText(getter.get(owner).toString());
			
			return tf;
		}
		
		return null;
	}
}
