package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
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
		MainMenu m = new MainMenu();
    	
		m.setOnSelect(controller::setScreen);
    	l.setOnLogin(() -> controller.setScreen(m));
    	
		controller.setScreen((GridPane)l);
    }
    
    private class Controller {
    	public void setScreen(GridPane pane) {
    		pane.setPadding(new Insets(20));
    		pane.setHgap(10);
    		pane.setVgap(10);
    		
    		Scene scene = new Scene(pane);
            
            stage.setScene(scene);
            stage.show();
    	}
    }

}