package gui;

import exceptions.DiscountParseException;
import gui.table.Column;
import gui.table.LabelColumn;
import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Order;
import model.PaymentStatus;
import model.ProductOrder;
import model.RentalProductOrder;
import service.Service;
import storage.Storage;

public class Rentals extends GridPane {
	private final Service service = Service.getInstance();
	private final Storage storage = Storage.getInstance();
	private final Controller controller = new Controller();
	private final ListView<Order> lwRentals = new ListView<>();
	private final Table<ProductOrder> table = new Table<>();
	private final Label lTotal = new Label();
	private final Label lError = new Label();
	private final Handler<?> rentalPaidHandler;
	private final Stage owner;
	private Order selectedRental;
	private double total;
	
	public Rentals(Stage owner, Handler<?> rentalPaidHandler) {
		this.owner = owner;
		this.rentalPaidHandler = rentalPaidHandler;
		
		lwRentals.getItems().addAll(service.getRentals());
		lwRentals.getSelectionModel().selectedItemProperty().addListener(e -> controller.selectRental());
		add(lwRentals, 0, 0);

		table.addColumn(new LabelColumn<ProductOrder>("Navn", po -> po.getProduct().getName()));
		table.addColumn(new LabelColumn<ProductOrder>("Antal", po -> Integer.toString(po.getAmount())));
		table.addColumn(new PrimitiveColumn<ProductOrder>("Ubrugte", po -> {
			if (po instanceof RentalProductOrder) {
				return ((RentalProductOrder) po).getUnused();
			} else {
				return null;
			}
		}, controller::updateUnused));
		table.addColumn(new PrimitiveColumn<>("Returneret", po -> {
			if (po instanceof RentalProductOrder) {
				return ((RentalProductOrder) po).getReturned();
			} else {
				return null;
			}
		}, controller::updateReturned));
		table.setPadding(new Insets(10));
		
		add(table, 1, 0);
		add(lError, 0, 1);
		add(lTotal, 1, 1);
		
		Button pay = new Button("Betal");
		pay.setOnAction(e -> controller.pay());
		add(pay, 2, 1);
	}

	class Controller {
		public void pay() {
			PayDialog pd = new PayDialog(owner, selectedRental, total + selectedRental.totalPayment(), null);
			
			pd.showAndWait();
			
			if (selectedRental.paymentStatus() == PaymentStatus.ORDERPAID) {
				rentalPaidHandler.exec(null);
			}
		}
		
		public void setTotal() {
			total = selectedRental.totalPrice() + selectedRental.totalDepositAfterReturn() - selectedRental.totalPayment();
			
			lTotal.setText(String.format("At betale: %.2f kr.", total));
		}
		
		public void selectRental() {
			Order o = lwRentals.getSelectionModel().getSelectedItem();

			for (RentalProductOrder po : o.getRentalProductOrders()) {
				po.setNotReturned(po.getAmount());
			}
			
			selectedRental = o;
			
			table.setItems(o.getAllProducts());
			
			setTotal();
		}

		public void updateUnused(ProductOrder po, String value) {
			int unused = -1;

			try {
				unused = Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
			if (unused < 0) {
				lError.setText("Ubrugte skal være et posetivt tal");
				return;
			}

			lError.setText("");
			((RentalProductOrder) po).setUnused(unused);
			
			setTotal();
		}

		public void updateReturned(ProductOrder po, String value) {
			int returned = -1;
			
			try {
				returned = Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
			if (returned < 0) {
				lError.setText("Returneret skal være et posetivt tal");
				return;
			}

			int notReturned = po.getAmount() - returned;
			
			lError.setText("");
			((RentalProductOrder) po).setReturned(returned);
			((RentalProductOrder) po).setNotReturned(notReturned);
			setTotal();
		}
	}
}
