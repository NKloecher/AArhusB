package gui;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Pricelist;
import model.Product;
import service.Service;
import storage.Storage;

public class ProductList extends BorderPane {
	private final Service service = Service.getInstance();
	private final Storage storage = Storage.getInstance();
	private final Controller controller = new Controller();
	private final Pricelist pricelist = service.getSelectedPricelist();
	private final TextField tfSearch = new TextField();
	private final ComboBox<String> cbCategories = new ComboBox<>();
	
	public ProductList() {
		HBox hbQuery = new HBox();
		
		tfSearch.setOnKeyTyped(e -> controller.findProduct());
		hbQuery.getChildren().add(tfSearch);
		
		List<String> categories = storage.getCategories();		
		categories.add(0, "All");
		cbCategories.getItems().setAll(categories);
		cbCategories.valueProperty().addListener(e -> controller.findProduct());
		cbCategories.getSelectionModel().select(0);
		hbQuery.getChildren().add(cbCategories);
		
		controller.showProducts(pricelist.getProducts());
		
		setTop(hbQuery);
	}
	
	class Controller {
		public void findProduct() {
			String selectedCategory = cbCategories.getSelectionModel().getSelectedItem();
			String query = tfSearch.getText();
			
			List<Product> matchingProducts = service.getMatchingProducts(query, selectedCategory, pricelist.getProducts());
			showProducts(matchingProducts);
		}
		
		public void showProducts(List<Product> products) {
			GridPane pane = new GridPane();
			
			pane.setHgap(10);
			pane.setVgap(10);
			pane.setAlignment(Pos.TOP_CENTER);
			
			for (int i = 0; i < products.size(); i++) {
				Product p = products.get(i);
				Label l = new Label(p.getName());
				
				pane.add(l, i%4, i/4);
			}
			
			setCenter(pane);
		}
	}
}
