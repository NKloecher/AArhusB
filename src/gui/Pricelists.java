package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import gui.table.ButtonColumn;
import gui.table.Column;
import gui.table.LabelColumn;
import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Permission;
import model.Pricelist;
import model.Product;
import service.Service;

public class Pricelists extends GridPane {

    private final Controller controller = new Controller();
    private final Service service = Service.getInstance();
    private final Pricelist selectedPricelist = service.getSelectedPricelist();
    private final TextField tfNewPrice = new TextField();
    private final ProductList productList;
    private final Label lError = new Label();
    private final Table<PricelistElement> table =
        new Table<>((error, isValid) -> lError.setText(error));

    public Pricelists() {
        setHgap(10);
        setVgap(10);
        setAlignment(Pos.TOP_CENTER);

        LabelColumn<PricelistElement> lblC =
            new LabelColumn<>("Produkt", t -> t.getProduct().getName());
        lblC.setPrefWidth(99999.0);

        Column<PricelistElement> pleC = new PrimitiveColumn<>("Pris", PrimitiveColumn.Type.Double,
            PricelistElement::getPrice, controller::updatePrice, (pe, v) -> {
                if (Pattern.matches("^\\d+(\\.\\d+)?", v)) {
                    return null;
                }
                return "Pris skal være et positivt tal";
            });
        pleC.setMinWidth(60.0);

        ButtonColumn<PricelistElement> btnC =
            new ButtonColumn<>("Delete", controller::deleteProduct);
        btnC.setMinWidth(100.0);

        table.addColumn(lblC);
        table.addColumn(pleC);
        table.addColumn(btnC);
        table.getPane().setMaxWidth(700);

        List<PricelistElement> tests = new ArrayList<>();

        for (Product p : selectedPricelist.getProducts()) {
            tests.add(new PricelistElement(p, selectedPricelist.getPrice(p)));
        }

        table.setItems(tests);

        GridPane gp = new GridPane();
        ScrollPane sp = new ScrollPane();
        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setStyle("-fx-background-color:transparent;");
        sp.setContent(gp);
        add(sp, 0, 0);

        table.getPane().maxWidth(300);
        gp.add(table.getPane(), 0, 0, 2, 1);

        Label newProductLabel = new Label("Tilføj nyt produkt:");
        GridPane.setMargin(newProductLabel, new Insets(10, 0, 10, 0));
        newProductLabel.setStyle("-fx-font-size: 16px");
        gp.add(newProductLabel, 0, 1, 2, 1);

        productList = new ProductList(service.getProducts());
        productList.maxWidth(300);
        gp.add(productList, 1, 2, 2, 1);

        tfNewPrice.setPromptText("Pris");
        gp.add(tfNewPrice, 1, 3);

        Button addProduct = new Button("Tilføj produkt");
        addProduct.setOnAction(e -> controller.addProducts());
        gp.add(addProduct, 2, 3);

        lError.setStyle("-fx-text-fill: red");
        gp.add(lError, 0, 4, 2, 1);

        Button btnCreatePriceList = new Button("Lav ny prisliste");
        btnCreatePriceList.setOnAction(e -> controller.createPriceList());
        gp.add(btnCreatePriceList, 0, 5, 2, 1);

        Button btnRemovePriceList = new Button("Fjern denne prisliste");
        btnRemovePriceList.setOnAction(e -> controller.removePriceList());
        gp.add(btnRemovePriceList, 2, 5);
        if (service.getActiveUser().getPermission() != Permission.ADMIN) {
            btnRemovePriceList.setDisable(true);
        }

    }

    class Controller {

        public void removePriceList() {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Fjernelse af prisliste");
            alert.setHeaderText("Du er i gang med at fjerne " + service.getSelectedPricelist());
            alert.setContentText(
                "Tryk OK for at fjerne denne liste, bemærk at den ikke kan genoprettes");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                service.removePricelist(service.getSelectedPricelist());
            }
            else {
                alert.close();
            }
        }

        public void createPriceList() {
            try {
                TextInputDialog id = new TextInputDialog();
                id.setTitle("Ny Prisliste");
                id.setHeaderText("Skriv navnet på den nye prisliste\n"
                    + "For at tilføje produkter vælg da prislisten i den øverste menu, og tilføj produkter");

                Optional<String> result = id.showAndWait();
                if (result.isPresent()) {
                    String name = result.get();
                    service.createPricelist(name);
                }
//                result.ifPresent(name -> System.out.println(name + " " + service.getPricelists()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void addProducts() {
            double price;

            try {
                price = Double.parseDouble(tfNewPrice.getText());
            }
            catch (NumberFormatException ex) {
                lError.setText("Prisen skal være et tal");
                return;
            }

            for (Product product : productList.getSelectedProducts()) {
                if (selectedPricelist.getProducts().contains(product)) {
                    lError.setText("Et af produkterne findes allerede i prislisten");
                    return;
                }
                else {
                    service.addProductToPricelist(product, selectedPricelist, price);
                    table.addItem(new PricelistElement(product, price));
                }
            }

            lError.setText("");
        }

        public void deleteProduct(PricelistElement t) {
            table.removeItem(t);
            service.removeProductFromPricelist(t.product, selectedPricelist);
        }

        public void updatePrice(PricelistElement t, Double price) {
            service.setPricelistPrice(service.getSelectedPricelist(), t.getProduct(), price);
        }

    }

    class PricelistElement {
        public Product product;
        public double price;

        public PricelistElement(Product product, double price) {
            this.product = product;
            this.price = price;
        }

        public Product getProduct() {
            return product;
        }

        public double getPrice() {
            return price;
        }
    }
}
