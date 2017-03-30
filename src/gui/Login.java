package gui;

import javax.security.sasl.AuthenticationException;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Handler;
import service.Service;

public class Login extends GridPane {
	private final Service service = Service.getInstance();
	private final Controller controller = new Controller();
	private final TextField tfUsername = new TextField();
	private final TextField tfPassword = new TextField();
	private final Label lError = new Label();
	private Handler loginHandler;
	
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
		this.add(lError, 0, 2, 2, 1);
	}
	
	public void setOnLogin(Handler handler) {
		loginHandler = handler;
	}
	
	class Controller {
		public void login() {
			String username = tfUsername.getText().trim();
			String password = tfPassword.getText().trim();

			try {
				service.login(username, password);
				
				if (loginHandler != null) loginHandler.exec();
			} catch (AuthenticationException e) {
				lError.setText("Brugernavn eller kodeord er forkert");
			}
		}
	}
}
