package gui.table;


import javafx.scene.Node;
import javafx.scene.control.Button;

public class ButtonColumn<A> extends Column<A> {
	private final ActionHandler<A> actionHandler;
	
	public ButtonColumn(String name, ActionHandler<A> actionHandler) {
		super(name);
		
		this.actionHandler = actionHandler;
	}

	@Override
	public Node getNode(A item) {
		Button b = new Button(getName());
		
		b.setOnAction(e -> actionHandler.exec(item));
		
		return b;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
