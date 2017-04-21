package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import storage.Storage;

public class SelectCustomerDialog extends Stage {
	private final Storage storage = Storage.getInstance();
	private final Controller controller = new Controller();
	private final ListView<Customer> lwCustomers = new ListView<>();
	private Customer selectedCustomer;
	
    public SelectCustomerDialog() {
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);

        setTitle("Tilføj Kunde");
        GridPane pane = new GridPane();
        initContent(pane);

        Scene scene = new Scene(pane);
        setScene(scene);
    }
    
    /**
     * returns the selected customer or null if none is selected
     */
    public Customer getSelectedCustomer() {
    	return selectedCustomer;
    }
    
    private void initContent(GridPane pane) {
    	lwCustomers.getItems().setAll(storage.getCustomers());
    	lwCustomers.getSelectionModel().selectedItemProperty()
    		.addListener(e -> controller.selectCustomer());
    	pane.add(lwCustomers, 0, 0);
    	
    	Button createCustomer = new Button("Opret ny kunde");
    	createCustomer.setOnAction(e -> controller.createCustomer());
    	pane.add(createCustomer, 0, 0);
    }

    private class Controller {
    	public void selectCustomer() {
    		selectedCustomer = lwCustomers.getSelectionModel().getSelectedItem();
    		
    		SelectCustomerDialog.this.close();
    	}
    	
    	public void createCustomer() {
    		CreateCustomerDialog ccd = new CreateCustomerDialog(getOwner());
    		
    		ccd.showAndWait();
    		
    		lwCustomers.getItems().setAll(storage.getCustomers());
    	}
    }
}
