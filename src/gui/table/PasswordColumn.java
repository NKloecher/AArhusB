package gui.table;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;

public class PasswordColumn<A> extends Column<A> {
	private String name;
	private Setter<A, String> setter;
	private HBox h = new HBox();
	private Button button = new Button("Sæt kode");
	private PasswordField pf = new PasswordField();
	private A owner;

	public PasswordColumn(String name, Setter<A, String> setter) {
		super(name);
		this.setter = setter;
		this.name = name;

		button.setText(name);
		button.setMinWidth(90);
		button.setOnAction(e -> onPressButton());

		pf.setPromptText("Indtast ny kode");
		pf.textProperty().addListener(e -> onPressPasswordField());

		h.getChildren().add(button);
	}

	private void onPressPasswordField(){
		if (pf.getText().length() == 0){
			button.setText("Annuller");
		} else {
			button.setText("Gem kode");
		}
	}

	private void onPressButton(){
		if (h.getChildren().contains(pf)){
			button.setText(this.name);
			h.getChildren().remove(pf);
			this.setter.set(owner, pf.getText());
		} else {
			h.getChildren().add(0, pf);
			pf.setMaxWidth(300);
			button.setText("Annuller");
		}
	}

	@Override
	public Node getNode(A owner) {
		this.owner = owner;
		return h;
	}
}
