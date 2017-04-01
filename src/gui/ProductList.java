package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
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
	private final List<Product> selectedProducts = new ArrayList<>();
	private final Handler<Product> selectHandler;
	private final Handler<Product> deselectHandler;

	public ProductList(Handler<Product> selectHandler, Handler<Product> deselectHandler) {
		this.selectHandler = selectHandler;
		this.deselectHandler = deselectHandler;

		setMaxWidth(460);

		HBox hbQuery = new HBox();
		hbQuery.setStyle("-fx-padding: 0 0 16px 0;");

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

			List<Product> matchingProducts = service.getMatchingProducts(query, selectedCategory,
					pricelist.getProducts());
			showProducts(matchingProducts);
		}

		public void showProducts(List<Product> products) {
			GridPane pane = new GridPane();

			pane.setHgap(10);
			pane.setVgap(10);
			pane.setAlignment(Pos.TOP_CENTER);

			for (int i = 0; i < products.size(); i++) {
				Product p = products.get(i);
				Node node;

				if (p.getImage() != null) {
					ImageView image = new ImageView(
							new Image("file:\\\\..\\product_images\\" + p.getImage(), 80, 80, true, true));
					BorderPane bp = new BorderPane(image);

					bp.setPadding(new Insets(10));

					node = bp;
				} else {
					Label label = new Label(p.getName());

					label.setMinWidth(100);
					label.setMaxWidth(100);
					label.setMinHeight(100);
					label.setMaxHeight(100);
					label.setWrapText(true);
					label.setTextAlignment(TextAlignment.CENTER);
					
					node = label;
				}

				node.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
				GridPane.setHalignment(node, HPos.CENTER);

				node.setOnMouseClicked(e -> {
					if (selectedProducts.contains(p)) {
						selectedProducts.remove(p);

						deselectHandler.exec(p);

						node.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
					} else {
						selectedProducts.add(p);

						selectHandler.exec(p);

						node.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 2px;");
					}

				});

				pane.add(node, i % 4, i / 4);
			}

			setCenter(pane);
		}
	}
}
