package gui.table;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class ButtonColumn<A> extends Column<A> {
	private final ColumnActionHandler<A> actionHandler;
	
	public ButtonColumn(String name, ColumnActionHandler<A> actionHandler) {
		super(name);
		
		this.actionHandler = actionHandler;
	}

	@Override
	public Node getNode(A owner) {
		Button b = new Button(getName());
		
		b.setOnAction(e -> actionHandler.exec(owner));
		
		return b;
	}

}
