package gui;

import gui.table.ButtonColumn;
import gui.table.Column;
import gui.table.ListColumn;
import gui.table.Table;
import gui.table.PrimitiveColumn;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import storage.Storage;
import service.Service;

import model.User;
import model.Permission;

public class Users extends GridPane {
    private final Storage storage = Storage.getInstance();
    private final Service service = Service.getInstance();
    private final Controller controller = new Controller();
    private final Label lError = new Label();
    private final Table<User> table = new Table<>();
    private final TextField tfName = new TextField();
    private final TextField tfUsername = new TextField();
    private final TextField tfPassword = new TextField();
    private final ComboBox<Permission> cbPermission = new ComboBox<>();

    public Users() {
        table.addColumn(
            new PrimitiveColumn<User, String>("Navn", u -> u.getName(), controller::updateName));
        table.addColumn(new PrimitiveColumn<User, String>("Brugernavn", u -> u.getUsername(),
            controller::updateUsername));
        table.addColumn(new ListColumn<User, Permission>("Permission", u -> u.getPermission(),
            (u, v) -> controller.updatePermission(u, v), Permission.values()));
//		table.addColumn(new ButtonColumn<User>("Sæt kode", controller::setPassword));
        table.addColumn(new ButtonColumn<User>("Delete", controller::deleteUser));
        table.setItems(storage.getUsers());
        add(table, 0, 0);

        HBox hbAdd = new HBox();

        tfName.setPromptText("Navn");
        hbAdd.getChildren().add(tfName);

        tfUsername.setPromptText("Brugernavn");
        hbAdd.getChildren().add(tfUsername);

        tfPassword.setPromptText("Kodeord");
        hbAdd.getChildren().add(tfPassword);

        cbPermission.getItems().setAll(Permission.values());
        hbAdd.getChildren().add(cbPermission);

        Button bAdd = new Button("Tilføj");
        bAdd.setOnAction(e -> controller.addUser());
        hbAdd.getChildren().add(bAdd);

        add(hbAdd, 0, 1);

        lError.setStyle("-fx-text-fill: red");
        add(lError, 0, 2);
    }

    private class Controller {
        public boolean validateName(String name) {
            if (name.isEmpty()) {
                lError.setText("Navn er påkrævet");
                return false;
            }

            return true;
        }

        public boolean validateUsername(String username) {
            if (username.isEmpty()) {
                lError.setText("Brugernavn er påkrævet");
                return false;
            }
            if (!service.usernameIsUnique(username)) {
                lError.setText("Brugernavnet er allerede brugt");
                return false;
            }

            return true;
        }

        public void updatePermission(User user, Permission permission) {
            // TODO hvis du er brugeren kan du ikke fjerne admin

            service.updateUserPermission(user, permission);
        }

        public void updateName(User user, String name) {
            if (!validateName(name)) {
                return;
            }

            service.updateUserName(user, name);
        }

        public void updateUsername(User user, String username) {
            if (!validateUsername(username)) {
                return;
            }

            service.updateUserUsername(user, username);
        }

        public void deleteUser(User user) {
            if (service.getActiveUser().equals(user)) {
                lError.setText("Du kan ikke slette den bruger du er logget ind som");
                return;
            }

            table.removeItem(user);
            service.deleteUser(user);
        }

//		public void setPassword(User user) {
//
//		}

        public void addUser() {
            String name = tfName.getText();
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            Permission permission = cbPermission.getSelectionModel().getSelectedItem();

            if (permission == null) {
                lError.setText("Du skal vælge en permission");
                return;
            }

            if (!validateName(name) || !validateUsername(username)) {
                return;
            }

            if (password.isEmpty()) {
                lError.setText("Kodeord er påkrævet");
                return;
            }

            if (password.length() < 8) {
                lError.setText("Koden skal være mindst 8 karakterer langt");
                return;
            }

            User user = service.createUser(name, username, password, permission);
            table.addItem(user);
        }
    }
}
