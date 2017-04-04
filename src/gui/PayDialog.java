package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Payable;
import model.PaymentStatus;
import model.PaymentType;
import service.Service;

public class PayDialog extends Stage {
	private final Service service = Service.getInstance();
	private final Controller controller = new Controller();
	private final TextField tfAmount = new TextField();
	private final Label lTotal = new Label();
	private final Label lError = new Label();
	private final GridPane pane = new GridPane();
	private final HBox hbTop = new HBox();
	private PaymentType paymentType = null;
	private double total;
	private Double deposit;
	private final Payable payable;
	
	public PayDialog(Stage owner, Payable payable, double total, Double deposit) {
		this.payable = payable;
		this.deposit = deposit;
		
		this.total = total - payable.totalPayment();
		
		if (deposit != null) {
			this.total += deposit;
		}
		
		initOwner(owner);
		initModality(Modality.APPLICATION_MODAL);
		setResizable(false);
		
		setTitle("Add Person");
		initContent(pane);
		
		Scene scene = new Scene(pane);
		
		setScene(scene);
	}

	private void initContent(GridPane pane) {
		Button cash = new Button("Kontant");
		Button card= new Button("Kredit kort");
		Button mobilePay = new Button("Mobile pay");
		Button clipCard = new Button("Klippe kort");
		
		cash.setOnAction(e -> paymentType = PaymentType.CASH);
		card.setOnAction(e -> paymentType = PaymentType.CREDIT_CARD);
		mobilePay.setOnAction(e -> paymentType = PaymentType.MOBILE_PAY);
		clipCard.setOnAction(e -> paymentType = PaymentType.CLIP_CARD);
		
		HBox hbButtons = new HBox();
		
		hbButtons.getChildren().addAll(cash, card, mobilePay, clipCard);
		
		HBox hbPrice = new HBox();
		hbPrice.getChildren().add(new Label("Total: " + String.format("%.2f kr.", total)));
		
		if (deposit != null) {
			hbPrice.getChildren().add(new Label("Pant: " + String.format("%.2f kr.", deposit)));
		}
		pane.add(hbPrice, 0, 0);
		
		
		controller.setTotal();
		hbTop.getChildren().add(lTotal);
		pane.add(hbTop, 0, 1);
		
		pane.add(hbButtons, 0, 2);
		pane.add(tfAmount, 0, 3);
		
		Button pay = new Button("Betal");
		pay.setOnAction(e -> controller.pay());
		pane.add(pay, 0, 4);
		pane.add(lError, 1, 4);
	}
	
	class Controller {
		private boolean endButtonIsAdded = false;
		
		public void setTotal() {
			lTotal.setText("Mangler at betale: " + String.format("%.2f kr.", total));
		}
		
		public void pay() {
			double amount = -1;
					
			try {
				amount = Double.parseDouble(tfAmount.getText());
			} catch (NumberFormatException e) {}
			if (amount < 0) {
				lError.setText("den betalte mængde skal være et posetivt tal");
				return;
			}
			
			if (paymentType == null) {
				lError.setText("du skal vælge en betalingstype");
				return;
			}
			
			service.createPayment(payable, amount, paymentType);
			
			total -= amount;
			setTotal();
			
			paymentType = null;
			lError.setText("");
			
			if (!endButtonIsAdded) {
				endButtonIsAdded = true;
				
				PaymentStatus status = payable.paymentStatus();
				
				if (status == PaymentStatus.ORDERPAID || status == PaymentStatus.DEPOSITPAID) {
					Button end = new Button("Ok");
					end.setOnAction(e -> PayDialog.this.close());
					hbTop.getChildren().add(end);
				}
			}
		}
	}
}
