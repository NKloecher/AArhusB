package gui;

import java.util.ArrayList;
import java.util.List;

import gui.table.ListColumn;
import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Product;
import service.Service;
import storage.Storage;

public class Products extends GridPane {

    private final Storage storage = Storage.getInstance();
    private final Service service = Service.getInstance();
    private final Controller controller = new Controller();
    private final Table<Product> table = new Table<>(null);
    private final TextField txfCategory = new TextField();

    public Products() {
        setHgap(10);
        setVgap(10);
        setAlignment(Pos.TOP_CENTER);

        ScrollPane sp = new ScrollPane();
        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setContent(table.getPane());

        List<String> categories = new ArrayList<>();
        categories.addAll(storage.getCategories());

        table.addColumn(new PrimitiveColumn<>("Navn", String.class, Product::getName,
        		service::updateProductName));
        table.addColumn(new ListColumn<Product, String>("Kategori", Product::getCategory,
            service::updateProductCategory,
            categories.toArray(new String[categories.size()])));
        table.addColumn(new PrimitiveColumn<>("Klips", Integer.class, x -> x.getClips(),
        	service::updateProductClips));

        table.setItems(storage.getProducts());
        add(sp, 0, 0);

        add(txfCategory, 1, 1);

        Button btnCreateCategory = new Button("Lav ny Kategori");
        add(btnCreateCategory, 2, 1);
        btnCreateCategory.setOnAction(e -> controller.createCategoryAction());

    }

    private class Controller {
        public void createCategoryAction() {
            service.addCategory(txfCategory.getText().trim());
        }
    }

}
