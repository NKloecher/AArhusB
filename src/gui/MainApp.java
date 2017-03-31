package gui;

import javax.management.relation.RelationServiceNotRegisteredException;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import service.Service;

public class MainApp extends Application {
	private final Service service = Service.getInstance();
    private final Controller controller = new Controller();
    private final BorderPane pane = new BorderPane();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
    	Service.getInstance().initStorage();
    	
    	Scene scene = new Scene(pane);
        
    	stage.setFullScreen(true);
    	
        stage.setScene(scene);
        stage.show();
    	
        stage.setTitle("Aarhus Bryghus");
        
        initContent();
    }

    private void initContent() {
    	Login l = new Login();
		MainMenu m = new MainMenu();
		
    	HBox hbMenu = new HBox();
    	hbMenu.setStyle("-fx-background-color: #666; -fx-padding: 20px;");
    	
    	hbMenu.setAlignment(Pos.BASELINE_LEFT);
    	
    	Button home = new Button("Hjem");
    	home.setOnAction(e -> controller.setScreen(m));
    	hbMenu.getChildren().add(home);
    	
    	Label lUser = new Label();
    	lUser.setStyle("-fx-text-fill: white;");
    	hbMenu.getChildren().add(lUser);
    	
    	Region r = new Region();
    	HBox.setHgrow(r, Priority.ALWAYS);
    	hbMenu.getChildren().add(r);
    			
    	
    	Button logout = new Button("Log ud");
    	logout.setTranslateX(hbMenu.getWidth());
    	logout.setOnAction(e -> {
    		service.logout();
    		controller.setScreen(l);
    		this.pane.getChildren().removeIf(n -> (n instanceof HBox));
    	});
    	hbMenu.getChildren().add(logout);
    	
		m.setOnSelect(controller::setScreen);
    	l.setOnLogin(() -> {
    		controller.setScreen(m);
    		
    		lUser.setText(service.getActiveUser().getUsername());
    		this.pane.setTop(hbMenu);
    	});
    	
		controller.setScreen((GridPane)l);
    }
    
    private class Controller {
    	public void setScreen(GridPane pane) {
    		pane.setPadding(new Insets(20));
    		pane.setHgap(10);
    		pane.setVgap(10);
    		pane.setAlignment(Pos.TOP_CENTER);
    		
    		ObservableList<Node> children = MainApp.this.pane.getChildren();
    		
    		for (int i = 0; i < children.size(); i++) {
    			if (children.get(i) instanceof GridPane) {
    				children.remove(i);
    			}
    		}
    		
    		MainApp.this.pane.setCenter(pane);
    	}
    }

}