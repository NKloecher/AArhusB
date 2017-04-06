package gui;

import java.util.Locale;
import java.util.regex.Pattern;

import exceptions.DiscountParseException;
import gui.table.Column;
import gui.table.LabelColumn;
import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.DepositProduct;
import model.Order;
import model.PaymentStatus;
import model.ProductOrder;
import service.Service;

public class Sale extends GridPane {
	private final Stage owner;
	private final Service service = Service.getInstance();
	private final Controller controller = new Controller();
	private final Order order = service.createOrder();
	private final Label lError = new Label(); 
	private final Button pay = new Button("Betal");
	private final LabelColumn<ProductOrder> priceColumn = new LabelColumn<>("Pris", po -> {
		try {
			return String.format(Locale.GERMAN, "%.2f kr.", po.price());			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	});
	private final Label lTotal = new Label();
	private final Handler<?> orderPaidHanlder;
	
	public Sale(Stage owner, Handler<?> orderPaidHanlder) {
		setPadding(new Insets(20));
		setHgap(10);
		setVgap(10);
		
		
		
		this.owner = owner;
		this.orderPaidHanlder = orderPaidHanlder;

		Table<ProductOrder> productTable = new Table<>((error, isValid)-> {
			pay.setDisable(!isValid);
			
			lError.setText(error);
		});
		
		LabelColumn<ProductOrder> nameColumn = new LabelColumn<>("Navn", po -> po.getProduct().getName());
		nameColumn.setPrefWidth(99999.0);
		
		Column<ProductOrder> amountColumn = new PrimitiveColumn<>("Antal", Integer.class, ProductOrder::getAmount, controller::updateAmount, (po, v) -> {
			if (Pattern.matches("^\\d+$", v)) return null;
			return "Antal skal være et posetivt tal";
		});
		amountColumn.setMinWidth(50.0);
		
		Column<ProductOrder> discountColumn = new PrimitiveColumn<>("Rabat", String.class, ProductOrder::getDiscount, controller::updateDiscount);
		discountColumn.setMinWidth(50.0);
		
		LabelColumn<ProductOrder> depositColumn = new LabelColumn<>("Pant", po -> {
			if (po.getProduct() instanceof DepositProduct) {
				return String.format(Locale.GERMAN, "%.2f kr.", ((DepositProduct)po.getProduct()).getDeposit());
			}
			else {
				return "";
			}
		});
		depositColumn.setMinWidth(80.0);

		priceColumn.setMinWidth(80.0);
		
		productTable.addColumn(nameColumn);
		productTable.addColumn(amountColumn);
		productTable.addColumn(discountColumn);
		productTable.addColumn(depositColumn);
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
		add(productTable.getPane(), 1, 0);
		add(lTotal, 1, 1);
		controller.updateTotal();
		
		pay.setDefaultButton(true);
		pay.setMinWidth(80);
		pay.setOnAction(e -> controller.showPayDialog());
		add(pay, 3, 1);
	}
	
	class Controller {
		public void showPayDialog() {
			try {
				PayDialog pd = new PayDialog(owner, order, order.totalPrice(), order.totalDeposit());
				
				pd.showAndWait();
				
				PaymentStatus status = order.paymentStatus();
				
				boolean depositOrPriceIsPaid = status == PaymentStatus.ORDERPAID || status == PaymentStatus.DEPOSITPAID;
				if (depositOrPriceIsPaid) {
					service.updateOrder(order);
					
					orderPaidHanlder.exec(null);
				}
			} catch (DiscountParseException e) {
				e.printStackTrace();
			}
		}
		
		public void updateRow() {
			updateTotal();
		}
		
		public void updateTotal() {
			try {
				// TODO manger at betalte			
				lTotal.setText("Mangler at betale: " + String.format("%.2f kr.", 0.0));
				lTotal.setText("Total " + String.format(Locale.GERMAN, "%.2f kr.", order.totalPrice()));
			} catch (DiscountParseException e) {
				e.printStackTrace();
			}
		}
		
		public void updateDiscount(ProductOrder po, String value) {
			try {
				po.setDiscount(value);
				
				lError.setText("");
				
				priceColumn.updateCell(po);
				controller.updateTotal();
			} catch (Exception e) {
				lError.setText("ugyldig rabat på \"" + po.getProduct().getName() + "\"");
			}
		}
		
		public void updateAmount(ProductOrder po, int amount) {
			service.updateProductOrderAmount(po, amount);
			priceColumn.updateCell(po);
			controller.updateTotal();
		}
	}
}
