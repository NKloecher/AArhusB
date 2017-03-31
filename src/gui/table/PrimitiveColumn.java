package gui.table;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class PrimitiveColumn<A,B> extends Column<A> {
	protected final Getter<A,B> getter;
	protected final Setter<A> setter;
	
	public PrimitiveColumn(String name, Getter<A, B> getter, Setter<A> setter) {
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
			tf.setOnKeyTyped(e -> {
				System.out.println(tf.getText());
				setter.set(owner, tf.getText());
			});
			
			return tf;
		}
		
		return null;
	}
}
