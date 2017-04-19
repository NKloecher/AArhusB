package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gui.table.ButtonColumn;
import gui.table.Column;
import gui.table.ListColumn;
import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Order;
import model.Permission;
import model.Pricelist;
import model.Product;
import model.ProductOrder;
import service.Service;
import storage.Storage;

public class Products extends GridPane {

    private final Storage storage = Storage.getInstance();
    private final Service service = Service.getInstance();
    private final Controller controller = new Controller();
    private final Table<Product> table = new Table<>(null);
    private final TextField txfCategory = new TextField();
    private final TextField txfProduct = new TextField();

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

        table.addColumn(new PrimitiveColumn<>("Navn", PrimitiveColumn.Type.String, Product::getName,
            service::updateProductName));
        table.addColumn(new ListColumn<>("Kategori", Product::getCategory,
            service::updateProductCategory,
            categories.toArray(new String[categories.size()])));
        table.addColumn(
            new PrimitiveColumn<>("Klip", PrimitiveColumn.Type.Integer, Product::getClips,
                service::updateProductClips));
        if (service.getActiveUser().getPermission() == Permission.ADMIN) {
            Column<Product> delete =
                new ButtonColumn<>("Delete", x -> controller.deleteProduct(x));
            table.addColumn(delete);
        }

        table.setItems(storage.getProducts());
        add(sp, 0, 0, 4, 1);

        add(txfCategory, 0, 1);

        Button btnCreateCategory = new Button("Lav ny Kategori");
        add(btnCreateCategory, 1, 1);
        btnCreateCategory.setOnAction(e -> controller.createCategoryAction());

        add(txfProduct, 0, 2);

        Button btnCreateProduct = new Button("Lav nyt Produkt");
        add(btnCreateProduct, 1, 2);
        btnCreateProduct.setOnAction(e -> controller.createProduct());

    }

    private class Controller {

//        public Button adminPrivileges() {
//            Button b = new Button("Delete");
//            if (service.getActiveUser().getPermission() != Permission.ADMIN) {
//                b.setDisable(true);
//            }
//            return b;
//        }

        public void deleteProduct(Product product) {
            try {
                boolean valid = true;
                for (Order o : storage.getOrders()) {
                    for (ProductOrder po : o.getAllProducts()) {
                        if (po.getProduct().equals(product)) {
                            valid = false;
                        }
                    }

                }
                if (valid) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Sletning af produkt");
                    alert.setHeaderText("Du er i gang med at slette " + product);
                    alert.setContentText(
                        "Tryk OK for at slette, vær opmærksom på at slettede produkter ikke kan genoprettes");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        table.removeItem(product);
                        service.removeProduct(product);
                        for (Pricelist p : storage.getPricelists()) {
                            if (p.getProducts().contains(product)) {
                                service.removeProductFromPricelist(product, p);
                            }
                        }
                    }
                }
                else {
                    Alert newalert = new Alert(AlertType.ERROR);
                    newalert.setTitle("Ugyldig handling");
                    newalert.setContentText("Produkt er brugt på ordrer og kan ikke slettes");
                    newalert.showAndWait();
                }

            }
            catch (

            Exception e) {
                e.printStackTrace();
            }
        }

        public void createProduct() {
            try {
                String name = txfProduct.getText().trim();
                if (!name.isEmpty()) {
                    service.createProduct(name, null, null, null);
                    table.setItems(storage.getProducts());
                    txfProduct.clear();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void createCategoryAction() {
            String category = txfCategory.getText().trim();
            if (!category.isEmpty()) {
                service.addCategory(category);
            }
        }
    }

}
