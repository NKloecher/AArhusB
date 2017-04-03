package gui.table;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class DateColumn<A> extends Column<A> {
	private Getter<A, LocalDate> getter;
	private Setter<A, LocalDate> setter;

	public DateColumn(String name, Getter<A, LocalDate> getter, Setter<A, LocalDate> setter) {
		super(name);
		
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public Node getNode(A owner) {
		LocalDate value = getter.get(owner);
		DatePicker datePicker = new DatePicker(value);

		datePicker.setOnAction(e -> setter.set(owner, datePicker.getValue()));

		return datePicker;
	}
}
