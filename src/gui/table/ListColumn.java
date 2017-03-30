package gui.table;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class ListColumn<A, B> extends PrimitiveColumn<A, B> {
	private B[] values;
	private ListSetter<A, B> setter;

	public ListColumn(String name, Getter<A, B> getter, ListSetter<A, B> setter, B[] values) {
		super(name, getter, null);
		this.setter = setter;
		this.values = values;
	}

	@Override
	public Node getNode(A owner) {
		ComboBox<B> cb = new ComboBox<>();
		B value = getter.get(owner);

		cb.getItems().setAll(values);
		System.out.println(value);
		cb.setValue(value);
		cb.setOnAction(e -> setter.set(owner, cb.getSelectionModel().getSelectedItem()));

		return cb;
	}

}
