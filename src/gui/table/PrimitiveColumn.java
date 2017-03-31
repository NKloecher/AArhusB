package gui.table;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class PrimitiveColumn<A> extends Column<A> {
	protected final Getter<A,Object> getter;
	protected final Setter<A> setter;
	
	public PrimitiveColumn(String name, Getter<A, Object> getter, Setter<A> setter) {
		super(name);
		
		this.getter = getter;
		this.setter = setter;
	}
	
	@Override
	public Node getNode(A owner) {
		TextField tf = new TextField();
		
		Object o = getter.get(owner);
		
		if (o != null) tf.setText(o.toString());
		
		tf.setOnKeyReleased(e -> {
            setter.set(owner, tf.getText());
        });
		
		return tf;
	}
}
