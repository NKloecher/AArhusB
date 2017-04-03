package gui;

import java.util.Locale;

import exceptions.DiscountParseException;
import gui.table.LabelColumn;
import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.DepositProduct;
import model.Order;
import model.ProductOrder;
import service.Service;



public class Sale extends GridPane {
	private final Service service = Service.getInstance();
	private final Controller controller = new Controller();
	private final Order order = service.createOrder();
	private final Label lError = new Label(); 
	private final LabelColumn<ProductOrder> priceColumn = new LabelColumn<>("Pris", po -> {
		try {
			return String.format(Locale.GERMAN, "%.2f kr.", po.price());			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	});
	private final Label lTotal = new Label();
	private final PrimitiveColumn<ProductOrder> amountColumn = new PrimitiveColumn<ProductOrder>("Antal", po -> po.getAmount(), controller::updateAmount);
	private final PrimitiveColumn<ProductOrder> discountColumn = new PrimitiveColumn<ProductOrder>("Rabat", po -> po.getDiscount(), controller::updateDiscount);
	
	public Sale() {
		Table<ProductOrder> productTable = new Table<>();
		productTable.addColumn(new LabelColumn<ProductOrder>("Navn", po -> po.getProduct().getName()));
		productTable.addColumn(amountColumn);
		productTable.addColumn(discountColumn);
		productTable.addColumn(new LabelColumn<ProductOrder>("Pant", po -> {
			if (po.getProduct() instanceof DepositProduct) {
				return String.format(Locale.GERMAN, "%.2f kr.", ((DepositProduct)po.getProduct()).getDeposit());
			}
			else {
				return "";
			}
		}));
		productTable.addColumn(priceColumn);
		productTable.setItems(order.getAllProducts());
		
		ProductList pl = new ProductList(service.getSelectedPricelist().getProducts());
		pl.setSelectHandler(p -> {
			ProductOrder po = order.addProduct(p);
			productTable.addItem(po);
			
			controller.updateRow();
		});
		
		pl.setDeselectHandler(p -> {
			ProductOrder po = order.removeProduct(p);
			
			productTable.removeItem(po);
			
			controller.updateRow();
		});
		
		lError.setStyle("-fx-text-fill: red");
		add(lError, 0, 1);
		
		add(pl, 0, 0);
		add(productTable, 1, 0);
		add(lTotal, 1, 1);
		controller.updateTotal();
		
		Button pay = new Button("Betal");
		add(pay, 3, 1);
	}
	
	class Controller {
		public void updateRow() {
			updateTotal();
			
			amountColumn.updateNodes(n -> ((TextField)n).setMaxWidth(50));
			discountColumn.updateNodes(n -> ((TextField)n).setMaxWidth(50));
		}
		
		public void updateTotal() {
			try {
				lTotal.setText("Total " + String.format(Locale.GERMAN, "%.2f kr.", order.totalPrice()));
			} catch (DiscountParseException e) {
				e.printStackTrace();
			}
		}
		
		public void updateDiscount(ProductOrder po, String value) {
			try {
				po.setDiscount(value);
				
				lError.setText("");
				
				priceColumn.updateChild(po);
				controller.updateTotal();
			} catch (Exception e) {
				lError.setText("ugyldig rabat på \"" + po.getProduct().getName() + "\"");
			}
		}
		
		public void updateAmount(ProductOrder po, String value) {
			int amount = -1;
			
			try {
				amount = Integer.parseInt(value);
			} catch (Exception e) {}
			if (amount < 1) {
				lError.setText("antal skal være et tal og mere over 0");
				return;
			}
			
			lError.setText("");
			
			service.updateProductOrderAmount(po, amount);
			
			priceColumn.updateChild(po);
			controller.updateTotal();
		}
	}
}
