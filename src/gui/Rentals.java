package gui;

import java.util.Locale;
import java.util.regex.Pattern;

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

public class Rentals extends GridPane {
	private final Service service = Service.getInstance();
	private final Controller controller = new Controller();
	private final ListView<Order> lwRentals = new ListView<>();
	private final Label lTotal = new Label();
	private final Label lError = new Label();
	private final Table<ProductOrder> table = new Table<>((error, isValid) -> {
		controller.setTotal();
		
		lError.setText(error);
	});
	private final Handler<?> rentalPaidHandler;
	private final Stage owner;
	private Order selectedRental;
	private double total;

	public Rentals(Stage owner, Handler<?> rentalPaidHandler) {
		this.owner = owner;
		this.rentalPaidHandler = rentalPaidHandler;

		lwRentals.getItems().addAll(service.getRentals());
		System.out.println(service.getRentals());
		lwRentals.getSelectionModel().selectedItemProperty().addListener(e -> controller.selectRental());
		add(lwRentals, 0, 0);

		table.addColumn(new LabelColumn<>("Navn", po -> po.getProduct().getName()));
		table.addColumn(new LabelColumn<>("Antal", po -> Integer.toString(po.getAmount())));
		table.addColumn(new PrimitiveColumn<>("Ubrugte", PrimitiveColumn.Type.Integer, controller::getUnused, controller::updateUnused, controller::validateUnused));
		table.addColumn(new PrimitiveColumn<>("Returneret", PrimitiveColumn.Type.Integer, controller::getReturned, controller::updateReturned, controller::validateReturned));
		table.getPane().setPadding(new Insets(10));

		add(table.getPane(), 1, 0);
		add(lError, 0, 1);
		add(lTotal, 1, 1);

		Button pay = new Button("Betal");
		pay.setDefaultButton(true);
		pay.setOnAction(e -> controller.pay());
		add(pay, 2, 1);
	}

	class Controller {
		public void pay() {
			Order o = lwRentals.getSelectionModel().getSelectedItem();

			for (RentalProductOrder po : o.getRentalProductOrders()) {
				po.setNotReturned(po.getAmount() - po.getUnused() - po.getReturned());
			}

			PayDialog pd = new PayDialog(owner, selectedRental, total + selectedRental.totalPayment(), null);

			pd.showAndWait();

			if (selectedRental.paymentStatus() == PaymentStatus.ORDERPAID) {
				rentalPaidHandler.exec(null);
			}
		}

		public void setTotal() {
			total = selectedRental.totalPrice() + selectedRental.totalDepositAfterReturn()
					- selectedRental.totalPayment();

			lTotal.setText(String.format(Locale.GERMAN, "At betale: %.2f kr.", total));
		}

		public void selectRental() {
			Order o = lwRentals.getSelectionModel().getSelectedItem();

			selectedRental = o;

			table.setItems(o.getAllProducts());

			setTotal();
		}
		
		public Integer getUnused(ProductOrder po) {
			if (po instanceof RentalProductOrder) {
				return ((RentalProductOrder) po).getUnused();
			} else {
				return null;
			}
		}
		public void updateUnused(ProductOrder po, int value) {
			if (po instanceof RentalProductOrder) {
				// TODO service
				((RentalProductOrder) po).setUnused(value);
			}
		}
		public String validateUnused(ProductOrder po, String value) {
			if (!(po instanceof RentalProductOrder)) return null;
			if (Pattern.matches("^\\d+$", value)) {
				RentalProductOrder rpo = (RentalProductOrder)po;
				int sum = Integer.parseInt(value) + rpo.getReturned();
				
				if (sum > rpo.getAmount()) {
					return "Det kan ikke være flere ubrugte og returnerede end der er udlejet";
				}
				if (sum < rpo.getAmount()) {
					return "Der ikke nok ubrugte eller returnerede";
				}
				else {
					return null;					
				}
			}
			else {
				return "Ubrugte skal være et posetivt tal";
			}
		}
		
		public Integer getReturned(ProductOrder po) {
			if (po instanceof RentalProductOrder) {
				return ((RentalProductOrder) po).getReturned();
			} else {
				return null;
			}
		}
		public void updateReturned(ProductOrder po, int value) {
			if (po instanceof RentalProductOrder) {
				RentalProductOrder rpo = (RentalProductOrder) po;
				// TODO service
				rpo.setReturned(value);
				rpo.setNotReturned(po.getAmount() - value);
			}
		}
		public String validateReturned(ProductOrder po, String value) {
			if (!(po instanceof RentalProductOrder)) return null;
			if (Pattern.matches("^\\d+$", value)) {
				RentalProductOrder rpo = (RentalProductOrder)po;
				int sum = Integer.parseInt(value) + rpo.getUnused();
				
				if (sum > rpo.getAmount()) {
					return "Det kan ikke være flere ubrugte og retunerede end der er udlejet";
				}
				else if (sum < rpo.getAmount()) {
					return "Det er ikke nok afleverede eller returnerede";
				}
				else {
					return null;					
				}
			}
			else {
				return "Ubrugte skal være et posetivt tal";
			}
		}
	}
}
