package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainMenu extends GridPane {
	private Handler<Pane> selectHandler;
	private final Controller controller = new Controller();

	public MainMenu() {
		setHgap(10);
		setVgap(10);
		setAlignment(Pos.TOP_CENTER);

		Button sale = getBigButton("Lav salg");
		add(sale, 0, 0);

		Button statistics = getBigButton("Statestik");
		add(statistics, 0, 1);

		Button rentals = getBigButton("Udlejninger");
		add(rentals, 0, 2);

		Button tours = getBigButton("Rundvisninger");
		add(tours, 0, 3);

		Button products = getBigButton("Produkter");
		products.setOnAction(e -> controller.select(new Products()));
		add(products, 0, 4);

		Button pricelists = getBigButton("Prislister");
		add(pricelists, 0, 5);
		pricelists.setOnAction(e -> controller.select(new Pricelists()));

		Button users = getBigButton("Brugere");
		users.setOnAction(e -> controller.select(new Users()));
		add(users, 0, 6);
	}

	public void setOnSelect(Handler<Pane> handler) {
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
			if (selectHandler == null)
				return;

			selectHandler.exec(pane);
		}
	}
}
