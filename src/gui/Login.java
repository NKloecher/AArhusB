package gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Listener;
import service.Service;

public class Login extends GridPane {
	private final Service service = Service.getInstance();
	private final Controller controller = new Controller();
	private final TextField tfUsername = new TextField();
	private final TextField tfPassword = new TextField();
	private final Label lError = new Label();
	private Listener loginListener;
	
	public Login() {
		this.setPadding(new Insets(20));
		this.setHgap(10);
		this.setVgap(10);
		
		this.add(new Label("Brugernavn"), 0, 0);
		this.add(tfUsername, 1, 0);
		
		this.add(new Label("Kodeord"), 0, 1);
		this.add(tfPassword, 1, 1);
		
		Button bLogin = new Button("Login");
		bLogin.setOnAction(e -> controller.login());
		GridPane.setHalignment(bLogin, HPos.RIGHT);
		this.add(bLogin, 1, 2);
		
		lError.setStyle("-fx-text-fill: red");
		
	}
	
	public void setOnLogin(Listener listener) {
		loginListener = listener;
	}
	
	class Controller {
		public void login() {
			String username = tfUsername.getText();
			String password = tfPassword.getText();
			
			try {
				service.login(username, password);
				
				if (loginListener != null) loginListener.exec();
			} catch (Exception e) {
				lError.setText("Brugernavn eller kodeord er forkert");
			}
		}
	}
}
