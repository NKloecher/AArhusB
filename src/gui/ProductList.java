package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
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
import model.Product;
import service.Service;
import storage.Storage;

public class ProductList extends BorderPane {
	private final Service service = Service.getInstance();
	private final Storage storage = Storage.getInstance();
	private final Controller controller = new Controller();
	private final TextField tfSearch = new TextField();
	private final ComboBox<String> cbCategories = new ComboBox<>();
	private final List<Product> selectedProducts = new ArrayList<>();
	private final List<Product> allProducts;
	private Handler<Product> selectHandler;
	private Handler<Product> deselectHandler;

	public ProductList(List<Product> products) {
		allProducts = products;

		setMinWidth(630);
		setMaxWidth(630);
		
		final HBox hbQuery = new HBox();
		hbQuery.setStyle("-fx-padding: 0 0 16px 0;");

		tfSearch.setOnKeyTyped(e -> controller.findProduct());
		tfSearch.setPrefWidth(540);
		hbQuery.getChildren().add(tfSearch);

		final List<String> categories = storage.getCategories();
		categories.add(0, "All");
		cbCategories.getItems().setAll(categories);
		cbCategories.valueProperty().addListener(e -> controller.findProduct());
		cbCategories.getSelectionModel().select(0);
		cbCategories.setPrefWidth(100);
		hbQuery.getChildren().add(cbCategories);

		controller.showProducts(products);

		setTop(hbQuery);
	}

	public void setSelectHandler(Handler<Product> handler) {
		selectHandler = handler;
	}

	public void setDeselectHandler(Handler<Product> handler) {
		deselectHandler = handler;
	}

	public List<Product> getSelectedProducts() {
		return new ArrayList<>(selectedProducts);
	}

	class Controller {
		public void findProduct() {
			final String selectedCategory = cbCategories.getSelectionModel().getSelectedItem();
			final String query = tfSearch.getText();

			final List<Product> matchingProducts = service.getMatchingProducts(query, selectedCategory, allProducts);
			showProducts(matchingProducts);
		}

		public void showProducts(List<Product> products) {
			final GridPane pane = new GridPane();
			final int productSize = 150;

			pane.setHgap(10);
			pane.setVgap(10);
			pane.setAlignment(Pos.TOP_CENTER);

			products.sort(Comparator.comparing(Product::getName));
			
			for (int i = 0; i < products.size(); i++) {
				final Product p = products.get(i);
				Node node;

				if (p.getImage() != null) {
					File file = new File("product_images/" + p.getImage());
					Image image = new Image(file.toURI().toString(), productSize - 20, productSize - 20, true, true);
					ImageView imageView = new ImageView(image);
					BorderPane bp = new BorderPane(imageView);

					bp.setPadding(new Insets(10));

					node = bp;
				} else {
					Label label = new Label(p.getName());

					label.setMinWidth(productSize);
					label.setMaxWidth(productSize);
					label.setMinHeight(productSize);
					label.setMaxHeight(productSize);
					label.setWrapText(true);
					label.setTextAlignment(TextAlignment.CENTER);

					node = label;
				}

				node.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
				GridPane.setHalignment(node, HPos.CENTER);

				node.setOnMouseClicked(e -> {
					if (selectedProducts.contains(p)) {
						selectedProducts.remove(p);

						if (deselectHandler != null)
							deselectHandler.exec(p);

						node.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
					} else {
						selectedProducts.add(p);

						if (selectHandler != null)
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