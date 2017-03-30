package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MainMenu extends GridPane {
	public MainMenu() {
		this.add(this.getBigButton("Lav salg"), 0, 0);
		this.add(this.getBigButton("Statestik"), 0, 1);
		this.add(this.getBigButton("Udlejninger"), 0, 2);
		this.add(this.getBigButton("Produkter"), 0, 3);
		this.add(this.getBigButton("Prisliter"), 0, 4);
		this.add(this.getBigButton("Brugere"), 0, 5);
	}
	
	private Button getBigButton(String text) {
		Button b = new Button(text);
		
		b.setStyle("-fx-font-size: 24px");
		b.setPrefWidth(300);
		b.setPrefHeight(170);
		
		return b;
	}
}
