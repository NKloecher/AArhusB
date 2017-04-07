package gui.table;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class PrimitiveColumn<A, B> extends Column<A> {
	private final Object type;
	private final Getter<A, B> getter;
	private final Setter<A, B> setter;
	private final Validator<A> validator;
	private List<Boolean> validity = new ArrayList<>();
	
	/**
	 * @param type
	 * must be one of String.class, Integer.class or Double.class
	 * @param getter
	 * getter gets a value from the item in the current row of the table and populates the TextField with it.
	 * @param setter
	 * gets called, with the current item and the new value, when the text of the TextField gets updated and the validator function returns true.
	 * @param validator
	 * should return true when the value of the textfield should be set. 
	 * can be null
	 */
	public PrimitiveColumn(String name, Object type, Getter<A, B> getter, Setter<A, B> setter, Validator<A> validator) {
		super(name);
		
		assert type == String.class || type == Integer.class || type == Double.class;
		assert getter != null;
		assert setter != null;
		
		this.type = type;
		this.getter = getter;
		this.setter = setter;
		this.validator = validator;
	}
	
	public PrimitiveColumn(String name, Object type, Getter<A, B> getter, Setter<A, B> setter) {
		this(name, type, getter, setter, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Node getNode(A item) {
		final int id = validity.size();
		final TextField tf = new TextField(); 
		final B value = getter.get(item);
		
		validity.add(true);
		
		if (value != null) {
			tf.setText(value.toString());			
		}
		else {
			tf.setText("");
		}
		
		tf.setOnKeyReleased(e -> {
			final String text = tf.getText();
			final String error = ((validator == null) ? null : validator.validate(item, text));
			
			validity.set(id, error == null);
			
			if (error == null) {
				if (type.equals(String.class)) {
					((Setter<A, String>)setter).set(item, text);
				}
				else if (type.equals(Integer.class)) {
					Integer i = null;
					if (!text.isEmpty()) i = Integer.parseInt(text); 
					
					((Setter<A, Integer>)setter).set(item, i);
				}
				else if (type.equals(Double.class)) {
					Double d = null;
					if (!text.isEmpty()) d = Double.parseDouble(text);
					
					((Setter<A, Double>)setter).set(item, d);
				}
				validationHandler.onValidate(null, true);
			}
			else {
				validationHandler.onValidate(error, false);
			}
		});

		return tf;
	}

	@Override
	public boolean isValid() {
		for (boolean validity : this.validity) {
			if (!validity) return false;
		}
		
		return true;
	}
}
