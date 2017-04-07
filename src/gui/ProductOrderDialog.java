package gui;

import java.util.Locale;

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

    private final Table<ProductOrder> table = new Table<>(null);

    private void initContent(GridPane pane) {
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setAlignment(Pos.TOP_CENTER);

        ScrollPane sp = new ScrollPane();
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setContent(table.getPane());

        table.addColumn(new LabelColumn<>("Produkt", x -> x.getProduct().toString()));
        table.addColumn(new LabelColumn<>("Antal", x -> "" + x.getAmount()));
        table.addColumn(new LabelColumn<>("Pris", x -> String.format(Locale.GERMAN, "%.2f", x.price())));
        table.addColumn(new LabelColumn<>("Rabat", x -> {
			try {
				return "" + (x.getAmount() * x.getOriginalPrice() - x.price());
			} catch (DiscountParseException e) {
				e.printStackTrace();
				return "";
			}
		}));
        table.setItems(order.getAllProducts());

        pane.add(table.getPane(), 0, 0);
    }

}
