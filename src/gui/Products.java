package gui;

import gui.table.ListColumn;
import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import model.Product;
import service.Service;
import storage.Storage;

public class Products extends GridPane {

    private final Storage storage = Storage.getInstance();
    private final Service service = Service.getInstance();
    private final Controller controller = new Controller();
    private final Table<Product> table = new Table<>();
    private final TextField txfCategory = new TextField();

    public Products() {

        ScrollPane sp = new ScrollPane();
        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setContent(table);

        table.addColumn(new PrimitiveColumn<Product>("Navn", x -> x.getName(),
            (x, y) -> controller.updateName(x, y)));
//        table.addColumn(
//            new PrimitiveColumn<Product>("Kategori", x -> x.getCategory(),
//                (x, y) -> controller.updateCategori(x, y)));
        table.addColumn(new ListColumn<Product, String>("Kategori", x -> x.getCategory(),
            (x, y) -> controller.updateCategory(x, y), storage.getCategories()));
        table.addColumn(new PrimitiveColumn<Product>("Klips", x -> x.getClips(),
            (x, y) -> controller.updateClips(x, y)));

        table.setItems(storage.getProduct());
        add(sp, 0, 0);

        add(txfCategory, 1, 1);

        Button btnCreateCategory = new Button("Lav ny Kategori");
        add(btnCreateCategory, 2, 1);
        btnCreateCategory.setOnAction(e -> controller.createCategoryAction());

    }

    private class Controller {

        public void updateName(Product product, String name) {
            service.updateProductName(product, name);
        }

        public void createCategoryAction() {
            service.addCategory(txfCategory.getText().trim());
        }

        public void updateClips(Product product, String clips) {
            if (clips.isEmpty()) {
                service.updateProductClips(product, null);
            }
            else {
                service.updateProductClips(product, Integer.parseInt(clips));
            }

        }

        public void updateCategory(Product x, String category) {
            service.updateProductCategory(x, category);
        }

    }

}
