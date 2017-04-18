package gui;

import java.util.Locale;

import exceptions.DiscountParseException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    private void initContent(GridPane pane) {
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10));
        pane.setAlignment(Pos.TOP_CENTER);

        pane.add(new Label("Produkt"), 0, 0);
        pane.add(new Label("Antal"), 1, 0);
        pane.add(new Label("Pris"), 2, 0);
        pane.add(new Label("Rabat"), 3, 0);
        
        for (int i = 0; i < order.getAllProducts().size(); i++) {
        	ProductOrder po = order.getAllProducts().get(i);
        	
        	pane.add(new Label(po.getProduct().toString()), 0, i+1);
        	pane.add(new Label(""+po.getAmount()), 1, i+1);
        	pane.add(new Label(String.format(Locale.GERMAN, "%.2f", po.price())), 2, i+1);
        	String rabat = "";
        	
        	try {
        		rabat = "" + (po.getAmount() * po.getOriginalPrice() - po.price());
			} catch (DiscountParseException e) {}
        	
        	pane.add(new Label(rabat), 3, i+1);
        }
        
        pane.add(new Label("Payments"), 0, order.getAllProducts().size() + 1);
        
        for (int i = 0; i < order.getPayments().size(); i++) {
        	pane.add(new Label(order.getPayments().get(i).toString()), 0, i + order.getAllProducts().size() + 2);
        }
    }

}
