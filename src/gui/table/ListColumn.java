package gui.table;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class ListColumn<A, B> extends PrimitiveColumn<A> {
	private B[] values;
	private Setter<A, B> setter;
	private Getter<A, B> getter;

	public ListColumn(String name, Getter<A, B> getter, Setter<A, B> setter, B[] values) {
		super(name, null, null);
		this.setter = setter;
		this.getter = getter;
		this.values = values;
	}

	@Override
	public Node getNode(A owner) {
		ComboBox<B> cb = new ComboBox<>();
		B value = getter.get(owner);

		cb.getItems().setAll(values);
		cb.setValue(value);
		cb.valueProperty().addListener(e -> setter.set(owner, cb.getSelectionModel().getSelectedItem()));

		return cb;
	}

}
