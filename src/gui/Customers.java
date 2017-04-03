package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Customer;
import model.Order;
import model.ProductOrder;
import model.RentalProductOrder;
import service.Service;
import storage.Storage;

public class Customers extends GridPane {

    private final Controller controller = new Controller();
    private final Storage storage = Storage.getInstance();
    private final Service service = Service.getInstance();
    private final ListView<Customer> lvCustomers = new ListView<>();
    private final ListView<Order> lvOrders = new ListView<>();
    private final Button btnAddCustomer = new Button("Tilføj Kunde");
    private final Button btnViewOrder = new Button("Vis Ordre");

    public Customers() {
        setVgap(10);
        setHgap(10);
        setAlignment(Pos.TOP_CENTER);

        ScrollPane sp = new ScrollPane();
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setContent(lvCustomers);

        lvCustomers.setPrefSize(200, 300);
        lvCustomers.getItems().addAll(storage.getCustomers());
        ChangeListener<Customer> listener =
            (ov, oldString, newString) -> controller.loadOrdersAction();
        lvCustomers.getSelectionModel().selectedItemProperty().addListener(listener);

        add(lvCustomers, 0, 0);

        lvOrders.setPrefSize(200, 200);

        add(lvOrders, 1, 0);

        add(btnViewOrder, 1, 1);
        btnViewOrder.setOnAction(e -> controller.loadProductOrdersAction());

    }

    private class Controller {
        private ProductOrderDialog createProductOrderDialog;

        public void loadOrdersAction() {
            List<Order> orders = new ArrayList<>();
            for (Order o : storage.getOrders()) {
                if (o.getCustomer().equals(lvCustomers.getSelectionModel().getSelectedItem())) {
                    orders.add(o);
                }
            }
            lvOrders.getItems().addAll(orders);
        }

        public void loadProductOrdersAction() {
            if (lvOrders.getSelectionModel().getSelectedItem() != null) {
                Order order = lvOrders.getSelectionModel().getSelectedItem();
                if (createProductOrderDialog == null) {
                    createProductOrderDialog = new ProductOrderDialog(order);
                    Stage stage = (Stage) lvOrders.getScene().getWindow();
                    createProductOrderDialog.initOwner(stage);
                }
                createProductOrderDialog.showAndWait();
            }

            //MÅSKE TIL SENERE
//            List<ProductOrder> orders = new ArrayList<>();
//            for (ProductOrder p : lvOrders.getSelectionModel().getSelectedItem().getProducts()) {
//                orders.add(p);
//            }
//            for (RentalProductOrder r : lvOrders.getSelectionModel().getSelectedItem()
//                .getProductsRental()) {
//                orders.add(r);
//            }

        }
    }

}
