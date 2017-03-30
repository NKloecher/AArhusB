package gui;

import gui.table.ButtonColumn;
import gui.table.Column;
import gui.table.Table;
import gui.table.ValueColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import storage.Storage;
import service.Service;

import model.User;

public class Users extends GridPane {
	private final Storage storage = Storage.getInstance();
	private final Service service = Service.getInstance();
	private final Controller controller = new Controller();
	private final Label lError = new Label();
	private final Table<User> table = new Table<>();
	private final TextField tfName = new TextField();
	private final TextField tfUsername = new TextField();
	private final TextField tfPassword = new TextField();
	
	public Users() {
		table.addColumn(new ValueColumn<User, String>("Navn", u -> u.getName(), controller::updateName));
		table.addColumn(new ValueColumn<User, String>("Brugernavn", u -> u.getUsername(), controller::updateUsername));
		table.addColumn(new ButtonColumn<User>("Delete", controller::deleteUser));
		table.setItems(storage.getUsers());
		add(table, 0, 0);
		
		HBox hbAdd = new HBox();
		
		tfName.setPromptText("Navn");
		hbAdd.getChildren().add(tfName);
		
		tfUsername.setPromptText("Brugernavn");
		hbAdd.getChildren().add(tfUsername);
		
		tfPassword.setPromptText("Kodeord");
		hbAdd.getChildren().add(tfPassword);
		
		Button bAdd = new Button("Tilføj");
		bAdd.setOnAction(e -> controller.addUser());
		hbAdd.getChildren().add(bAdd);
		
		add(hbAdd, 0, 1);
		
		lError.setStyle("-fx-text-fill: red");
		add(lError, 0, 2);
	}
	
	private class Controller {
		public boolean validateName(String name) {
			if (name.isEmpty()) {
				lError.setText("navn er påkrævet");
				return false;
			}
			
			return true;
		}
		
		public boolean validateUsername(String username) {
			if (username.isEmpty()) {
				lError.setText("brugernavn er påkrævet");
				return false;
			}
			if (!service.usernameIsUnique(username)) {
				lError.setText("brugernavn er allerede brugt");
				return false;
			}
			
			return true;
		}
		
		public void updateName(User user, String name) {
			if (!validateName(name))
				return;
			
			service.updateUserName(user, name);
		}
		
		public void updateUsername(User user, String username) {
			if (!validateUsername(username))
				return;
			
			service.updateUserUsername(user, username);
		}
		
		public void deleteUser(User user) {
			if (service.getActiveUser().equals(user)) {
				lError.setText("du kan ikke slette den bruger du er logget ind som");
				return;
			}
			
			table.removeItem(user);
			service.deleteUser(user);
		}
		
		public void addUser() {
			String name = tfName.getText();
			String username = tfUsername.getText();
			String password = tfPassword.getText();
			
			if (!validateName(name) || !validateUsername(username))
				return;
			
			if (password.isEmpty()) {
				lError.setText("kodeord er påkrævet");
				return;
			}
			
			if (password.length() < 8) {
				lError.setText("koden skal være mindst 8 karakterer langt");
				return;
			}
			
			User user = service.createUser(name, username, password);
			table.addItem(user);
		}
	}
}
