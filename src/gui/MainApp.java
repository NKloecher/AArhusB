package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.DiscountParseException;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
        StackPane stackPane = new StackPane();

    	HBox hMenu = new HBox(10);
        Button home = new Button("Hjem");
        Label lUserName = new Label("Ikke logget ind");
        Label lUser = new Label("Bruger:");
        Label lPricelist = new Label("   Prislisten:");
        Button logout = new Button("Log ud");


        hMenu.setStyle("-fx-background-color: #135b1f; -fx-padding: 20px;");
        hMenu.setAlignment(Pos.BASELINE_LEFT);
        hMenu.setMaxHeight(50);

        lUserName.setStyle("-fx-text-fill: white; -fx-font-weight: bolder");
        lUser.setStyle("-fx-text-fill: darkgray;");
        lPricelist.setStyle("-fx-text-fill: darkgray;");

        List<String> pricelists = new ArrayList<>();
        for (Pricelist pl : service.getPricelists()) {
            pricelists.add(pl.getName());
        }
        cbPricelist.getItems().setAll(pricelists);
        cbPricelist.setOnAction(e -> controller.selectPricelist());
        cbPricelist.getSelectionModel().select(0);

        Login l = new Login(x -> {
            MainMenu m = new MainMenu(owner);
            home.setOnAction(e -> controller.setScreen(m));
            m.setOnSelect(controller::setScreen);
            controller.setScreen(m);

            lUserName.setText(service.getActiveUser().getUsername());
            this.pane.setTop(stackPane);
        });


        logout.setTranslateX(hMenu.getWidth());
        logout.setOnAction(e -> {
            service.logout();
            controller.setScreen(l);
            this.pane.getChildren().removeIf(n -> (n instanceof HBox));
        });

        hMenu.getChildren().add(home);
        hMenu.getChildren().add(lPricelist);
        hMenu.getChildren().add(cbPricelist);

        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        hMenu.getChildren().add(r);

        hMenu.getChildren().add(lUser);
        hMenu.getChildren().add(lUserName);
        hMenu.getChildren().add(logout);

        stackPane.getChildren().add(hMenu);

        VBox imgPane = new VBox();
        imgPane.setPickOnBounds(false);
        ImageView img = new ImageView(new Image(new File("images/rsz_logo.png").toURI().toString()));
        imgPane.setPadding(new Insets(5, 0, 5, 0));
        imgPane.getChildren().add(img);
        imgPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(imgPane);

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

            if (MainApp.this.pane.getCenter() instanceof Pricelists){
                setScreen(new Pricelists());
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

            if (pane instanceof MainMenu || pane instanceof Pricelists) {
                lockPricelist(false);
            }
            else {
                lockPricelist(true);
            }

            MainApp.this.pane.setCenter(pane);
        }
    }
}