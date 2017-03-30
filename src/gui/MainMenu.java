package gui;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MainMenu extends GridPane {
	private SelectHandler selectHandler;
	private final Controller controller = new Controller();
	
	public MainMenu() {
		Button sale = getBigButton("Lav salg");
		add(sale, 0, 0);
		
		Button statistics = getBigButton("Statestik");
		add(statistics, 0, 1);
		
		Button rentals = getBigButton("Udlejninger");
		add(rentals, 0, 2);
		
		Button products = getBigButton("Produkter");
		add(products, 0, 3);
		
		Button pricelists = getBigButton("Prislister");
		add(pricelists, 0, 4);
		
		Button users = getBigButton("Brugere");
		users.setOnAction(e -> controller.select(new Users()));
		add(users, 0, 5);
	}
	
	public void setOnSelect(SelectHandler handler) {
		selectHandler = handler;
	}
	
	private Button getBigButton(String text) {
		Button b = new Button(text);
		
		b.setStyle("-fx-font-size: 24px");
		b.setPrefWidth(300);
		b.setPrefHeight(170);
		
		return b;
	}
	
	private class Controller {
		private void select(GridPane pane) {
			if (selectHandler == null) return;
			
			selectHandler.exec(pane);
		}
	}
}
