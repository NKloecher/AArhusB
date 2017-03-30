package gui;

import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
	
	private final ListView<User> lwUsers = new ListView<>();
	private final TextField tfName = new TextField();
	private final TextField tfUsername = new TextField();
	private final TextField tfPassword = new TextField();
	private final Label lError = new Label();
	private final Button saveUser = new Button("Gem");
	private final Button addUser = new Button("Tilføj");
	private final Button deleteUser = new Button("Fjern");
	
	public Users() {
		lwUsers.getItems().setAll(storage.getUsers());
		lwUsers.getSelectionModel().selectedItemProperty()
			.addListener(e -> controller.selectUser());
		add(lwUsers, 0, 0, 1, 4);
		
		controller.selectUser();
		
		tfName.setPromptText("Navn");
		add(tfName, 1, 0);
		
		tfUsername.setPromptText("Brugernavn");
		add(tfUsername, 1, 1);
		
		tfPassword.setPromptText("Kodeord");
		add(tfPassword, 1, 2);
		
		HBox hbButtons = new HBox();
		add(hbButtons, 1, 3);
		hbButtons.setAlignment(Pos.TOP_RIGHT);
		
		saveUser.setOnAction(e -> controller.saveUser());
		hbButtons.getChildren().add(saveUser);
		
		addUser.setOnAction(e -> controller.addUser());
		hbButtons.getChildren().add(addUser);
		
		deleteUser.setOnAction(e -> controller.deleteUser());
		hbButtons.getChildren().add(deleteUser);
		
		lError.setStyle("-fx-text-fill: red");
		add(lError, 1, 4);
	}
	
	private class Controller {
		public void selectUser() {
			User user = lwUsers.getSelectionModel().getSelectedItem();
			
			if (user == null) {
				tfName.setText("");
				tfUsername.setText("");
				tfPassword.setText("");
				
				saveUser.setVisible(false);
				addUser.setVisible(true);
				deleteUser.setVisible(false);
			}
			else {
				tfName.setText(user.getName());
				tfUsername.setText(user.getUsername());
				tfPassword.setText("");
				
				saveUser.setVisible(true);
				addUser.setVisible(false);
				deleteUser.setVisible(true);
			}
		}
		
		public void saveUser() {
			User user = lwUsers.getSelectionModel().getSelectedItem();
			int selectedIndex = lwUsers.getSelectionModel().getSelectedIndex();
			
			String name = tfName.getText();
			String username = tfUsername.getText();
			String password = tfPassword.getText();
			
			if (name.isEmpty()) {
				lError.setText("du skal skrive et navn");
				return;
			}
			
			if (username.isEmpty()) {
				lError.setText("du skal skrive et brugernavn");
				return;
			}
			
			if (!password.isEmpty()) {
				if (password.length() < 8) {
					lError.setText("koden skal være mindst 8 karakterer");
					return;
				}
				
				service.setUserPassword(user, password);
			}
			
			service.setUserName(user, name);
			service.setUserUsername(user, username);
			
			lError.setText("");
			lwUsers.getItems().set(selectedIndex, user);
		}
		
		public void addUser() {
			String name = tfName.getText();
			String username = tfUsername.getText();
			String password = tfPassword.getText();
			
			if (name.isEmpty()) {
				lError.setText("du skal skrive et navn");
				return;
			}
			
			if (username.isEmpty()) {
				lError.setText("du skal skrive et brugernavn");
				return;
			}
			if (!service.usernameIsUnique(username)) {
				lError.setText("brugernavnet findes allerade");
				return;
			}
			
			if (password.isEmpty()) {
				lError.setText("du skal skrive en kode");
				return;
			}
			
			if (password.length() < 8) {
				lError.setText("koden skal være mindst 8 karakterer");
				return;
			}
			
			tfName.setText("");
			tfUsername.setText("");
			tfPassword.setText("");
			
			User u = service.createUser(name, username, password);
			
			lError.setText("");
			lwUsers.getItems().add(u);
		}
		
		public void deleteUser() {
			User user = lwUsers.getSelectionModel().getSelectedItem();
			User activeUser = service.getActiveUser();
			
			if (user.equals(activeUser)) {
				lError.setText("du kan ikke slette den nuværende aktive bruger");
				return;
			}
			
			service.deleteUser(user);
			
			lError.setText("");
			lwUsers.getItems().remove(user);
		}
	}
}
