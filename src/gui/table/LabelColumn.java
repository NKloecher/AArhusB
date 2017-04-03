package gui.table;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class LabelColumn<A> extends Column<A> {
	private Getter<A, String> getter;
	private List<Label> children = new ArrayList<>();
	
	public LabelColumn(String name, Getter<A, String> getter) {
		super(name);
		
		this.getter = getter;
	}

	public void updateChild(A owner) {
		for (Label l : children) {
			if (l.getUserData().equals(owner)) {
				setValue(l, owner);
			}
		}
	}
	
	private void setValue(Label l, A owner) {
		String value = getter.get(owner);
		l.setText(value);
	}
	
	@Override
	public Node getNode(A owner) {
		Label l = new Label();
		
		setValue(l, owner);
		
		l.setUserData(owner);
		
		children.add(l);
		
		return l;
	}
}
