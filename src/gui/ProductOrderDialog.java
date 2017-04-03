package gui;

import exceptions.DiscountParseException;
import gui.table.LabelColumn;
import gui.table.Table;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Order;
import model.ProductOrder;

public class ProductOrderDialog extends Stage {
    private final Order order;

    public ProductOrderDialog(Order order) {
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);

        setTitle(order.toString());
        GridPane pane = new GridPane();
        this.order = order;
        initContent(pane);

        Scene scene = new Scene(pane);
        setScene(scene);

    }

    private final Table<ProductOrder> table = new Table<>();

    private void initContent(GridPane pane) {
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setAlignment(Pos.TOP_CENTER);

        ScrollPane sp = new ScrollPane();
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setContent(table);

        table.addColumn(new LabelColumn<ProductOrder>("Produkt", x -> x.getProduct().toString()));
        table.addColumn(new LabelColumn<ProductOrder>("Antal", x -> "" + x.getAmount()));
        table.addColumn(new LabelColumn<ProductOrder>("Pris", x -> {
            try {
                return "" + x.price();
            }
            catch (DiscountParseException e) {
                e.printStackTrace();
                //Dette burde aldrig være et problem
                return "";
            }
        }));
        table.addColumn(new LabelColumn<ProductOrder>("Rabat Pris", x -> {
            try {
                return "" + (x.getAmount() * x.getOriginalPrice() - x.price());
            }
            catch (DiscountParseException e) {
                e.printStackTrace();
                return "";
            }
        }));
        table.setItems(order.getAllProducts());
//        table.setPrefSize(300, 500);
        table.setHgap(10);

        pane.add(table, 0, 0);
    }

}
