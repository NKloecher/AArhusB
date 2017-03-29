package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import service.Service;

public class MainApp extends Application {
    private final Controller controller = new Controller();
    private Stage stage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
    	Service.getInstance().initStorage();
    	
    	this.stage = stage;
    	
        stage.setTitle("Aarhus Bryghus");
        
        initContent();
    }

    private void initContent() {
    	Login l = new Login();
    	
    	l.setOnLogin(() -> System.out.println("User logged in"));
    	
		controller.setScreen((GridPane)l);
    }
    
    private class Controller {
    	public void setScreen(GridPane pane) {
    		Scene scene = new Scene(pane);
            
            stage.setScene(scene);
            stage.show();
    	}
    }

}
