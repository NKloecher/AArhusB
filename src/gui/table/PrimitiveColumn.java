package gui.table;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class PrimitiveColumn<A, B> extends Column<A> {
	private final Type<B> type;
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
	public PrimitiveColumn(String name, Type<B> type, Getter<A, B> getter, Setter<A, B> setter, Validator<A> validator) {
		super(name);
		
		assert getter != null;
		assert setter != null;
		
		this.type = type;
		this.getter = getter;
		this.setter = setter;
		this.validator = validator;
	}
	
	public PrimitiveColumn(String name, Type<B> type, Getter<A, B> getter, Setter<A, B> setter) {
		this(name, type, getter, setter, null);
	}
	
	@Override
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
				setter.set(item, type.parse(text));
				
				if (validationHandler != null) {
					validationHandler.onValidate(null, true);					
				}
			}
			else {
				if (validationHandler != null) {
					validationHandler.onValidate(error, false);
				}
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
	
	public interface Type<C> {
		public static String String = new String();
		public static Integer Integer = new Integer();
		public static Double Double = new Double();
		
		public C parse(java.lang.String text);
		
		class String implements Type<java.lang.String> {
			private String() {}
			public java.lang.String parse(java.lang.String text) { return text; }
		}
		
		class Integer implements Type<java.lang.Integer> {
			private Integer() {}
			public java.lang.Integer parse(java.lang.String text) {
				if (!text.isEmpty()) return java.lang.Integer.parseInt(text);
				else return null;
			}
		}
		class Double implements Type<java.lang.Double> {
			private Double() {}
			public java.lang.Double parse(java.lang.String text) {
				if (!text.isEmpty()) return java.lang.Double.parseDouble(text);
				else return null;
			}
		}
	}
}
