package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Order;

public class AddCustomerDialog extends Stage {
    private Order order;

    public AddCustomerDialog(Order order) {
        this.order = order;
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);

        setTitle("Tilføj Kunde");
        GridPane pane = new GridPane();
        initContent(pane);

        Scene scene = new Scene(pane);
        setScene(scene);

    }

    private final Button btnNewCustomer = new Button("Ny Kunde");
    private final Button btnOldCustomer = new Button("Eksiterende Kunde");
    private final Button btnCancel = new Button("Fortryd");
    private final Controller controller = new Controller();

    private void initContent(GridPane pane) {
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setAlignment(Pos.TOP_CENTER);

        pane.add(btnCancel, 0, 0);
        pane.add(btnNewCustomer, 1, 0);
        pane.add(btnOldCustomer, 2, 0);

        btnCancel.setOnAction(e -> close());
        btnNewCustomer.setOnAction(e -> controller.newCustomer());
        btnOldCustomer.setOnAction(e -> controller.oldCustomer());
    }

    private class Controller {
        public void newCustomer() {
            try {
                CreateCustomerDialog cc = new CreateCustomerDialog();
                cc.showAndWait();
                if (cc.getNewCustomer() != null) {
                    order.setCustomer(cc.getNewCustomer());
                    close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void oldCustomer() {
            try {
                ExistingCustomers ec = new ExistingCustomers();
                ec.showAndWait();
                if (ec.getCustomer() != null) {
                    order.setCustomer(ec.getCustomer());
                    close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}