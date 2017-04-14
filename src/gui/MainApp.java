package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.DiscountParseException;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Pricelist;
import service.Service;

public class MainApp extends Application {
    private final Service service = Service.getInstance();
    private final Controller controller = new Controller();
    private final BorderPane pane = new BorderPane();
    private final ComboBox<String> cbPricelist = new ComboBox<>();
    private Stage owner;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws DiscountParseException {
        this.owner = stage;
        Service.getInstance().initStorage();

        Scene scene = new Scene(pane);

        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();

        stage.setTitle("Aarhus Bryghus");

        initContent();
    }

    private void initContent() {
        MainMenu m = new MainMenu(owner);

        HBox hbMenu = new HBox();
        hbMenu.setStyle("-fx-background-color: #666; -fx-padding: 20px;");

        hbMenu.setAlignment(Pos.BASELINE_LEFT);

        Button home = new Button("Hjem");
        home.setOnAction(e -> controller.setScreen(m));
        hbMenu.getChildren().add(home);

        Label lUser = new Label();
        lUser.setStyle("-fx-text-fill: white;");
        hbMenu.getChildren().add(lUser);

        List<String> pricelists = new ArrayList<>();

        for (Pricelist pl : service.getPricelists()) {
            pricelists.add(pl.getName());
        }

        cbPricelist.getItems().setAll(pricelists);
        cbPricelist.setOnAction(e -> controller.selectPricelist());
        cbPricelist.getSelectionModel().select(0);

        hbMenu.getChildren().add(cbPricelist);

        Login l = new Login(x -> {
            controller.setScreen(m);

            lUser.setText(service.getActiveUser().getUsername());
            this.pane.setTop(hbMenu);
        });

        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        hbMenu.getChildren().add(r);

        Button logout = new Button("Log ud");
        logout.setTranslateX(hbMenu.getWidth());
        logout.setOnAction(e -> {
            service.logout();
            controller.setScreen(l);
            this.pane.getChildren().removeIf(n -> (n instanceof HBox));
        });
        hbMenu.getChildren().add(logout);

        m.setOnSelect(controller::setScreen);
        controller.setScreen(l);
    }

    @Override
    public void stop() {
        try {
            service.saveStorage();
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error saving data");
            alert.setContentText("Could not save the data: " + e);
            alert.showAndWait();
        }
    }

    private class Controller {
        public void lockPricelist(boolean state) {
            cbPricelist.setDisable(state);
        }

        public void selectPricelist() {
            String pricelistName = cbPricelist.getSelectionModel().getSelectedItem();

            for (Pricelist pl : service.getPricelists()) {
                if (pl.getName().equals(pricelistName)) {
                    service.setSelectedPricelist(pl);
                }
            }
        }

        public void setScreen(Pane pane) {
            List<String> pricelists = new ArrayList<>();

            for (Pricelist pl : service.getPricelists()) {
                pricelists.add(pl.getName());
            }

            cbPricelist.getItems().setAll(pricelists);

            pane.setPadding(new Insets(20));

            ObservableList<Node> children = MainApp.this.pane.getChildren();

            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) instanceof GridPane) {
                    children.remove(i);
                }
            }

            if (pane instanceof MainMenu) {
                lockPricelist(false);
            }
            else {
                lockPricelist(true);
            }

            MainApp.this.pane.setCenter(pane);
        }
    }
}