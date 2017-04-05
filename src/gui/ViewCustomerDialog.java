package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;

public class ViewCustomerDialog extends Stage {
    private Customer c;

    public ViewCustomerDialog(Customer c) {
        this.c = c;
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);

        setTitle(c.getName());
        GridPane pane = new GridPane();
        initContent(pane);

        Scene scene = new Scene(pane);
        setScene(scene);

    }

    private final TextField txfName = new TextField();
    private final TextField txfPhone = new TextField();
    private final TextField txfEmail = new TextField();
    private final TextField txfAddress = new TextField();
    private final Button btnCancel = new Button("Afslut");
    private final Button btnOk = new Button("OK");
    private final Controller controller = new Controller();
    private final Label lblError = new Label();

    private void initContent(GridPane pane) {
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setAlignment(Pos.TOP_CENTER);

        txfName.setText(c.getName());
        txfPhone.setText(c.getPhone());
        txfEmail.setText(c.getEmail());
        txfAddress.setText(c.getAddress());
        HBox hbox = new HBox();
        hbox.getChildren().addAll(txfName, txfPhone, txfEmail, txfAddress);
        pane.add(hbox, 0, 0, 2, 1);

        HBox btnBox = new HBox();
        btnBox.getChildren().addAll(btnCancel, btnOk);
        btnBox.setAlignment(Pos.BASELINE_RIGHT);
        btnBox.setSpacing(15);
        pane.add(btnBox, 1, 1);

        btnCancel.setOnAction(e -> ViewCustomerDialog.this.close());
        btnOk.setOnAction(e -> controller.okAction());
        btnOk.setDefaultButton(true);

        pane.add(lblError, 0, 1);

    }

    private class Controller {
        CustomerEvaluator ce = new CustomerEvaluator();

        public void okAction() {
            String name = txfName.getText().trim();
            String phone = txfPhone.getText().trim();
            String email = txfEmail.getText().trim();
            String address = txfAddress.getText().trim();
            if (ce.isValid(name, phone, email) && (!phone.isEmpty() || !email.isEmpty())) {
                c.setName(name);
                c.setPhone(phone);
                c.setEmail(email);
                c.setAddress(address);
                close();
            }
            else if (!ce.nameIsValid(name)) {
                lblError.setText("Navn skal skrives");
            }
            else if (!ce.phoneIsValid(phone)) {
                lblError.setText("Ugyldigt telefonnummer");
            }
            else if (!ce.emailIsValid(email)) {
                lblError.setText("Email er ugyldig");
            }
            else if (phone.isEmpty() && email.isEmpty()) {
                lblError.setText("Skal have mindst én kontaktoplysning");
            }
        }

    }
}