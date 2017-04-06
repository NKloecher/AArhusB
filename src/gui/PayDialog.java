package gui;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
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
	private BorderPane cash;
	private BorderPane card;
	private BorderPane mobilePay;
	private BorderPane clipCard;
	
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
		int buttonSize = 150;
		
		pane.setPadding(new Insets(20));
		pane.setHgap(10);
		pane.setVgap(10);
		
		cash = new BorderPane(new ImageView(new Image(new File("images/kontant.jpg").toURI().toString(), buttonSize, buttonSize, true, true)));
		card = new BorderPane(new ImageView(new Image(new File("images/kredit-kort.jpg").toURI().toString(), buttonSize, buttonSize, true, true)));
		mobilePay = new BorderPane(new ImageView(new Image(new File("images/mobile-pay.png").toURI().toString(), buttonSize, buttonSize, true, true)));
		clipCard = new BorderPane(new ImageView(new Image(new File("images/klippekort.png").toURI().toString(), buttonSize, buttonSize, true, true)));
		
		cash.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
		cash.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), null, null)));
		cash.setOnMouseClicked(e -> controller.selectPaymentType(PaymentType.CASH, cash));
		
		card.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
		card.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), null, null)));
		card.setOnMouseClicked(e -> controller.selectPaymentType(PaymentType.CREDIT_CARD, card));
		
		mobilePay.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
		mobilePay.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), null, null)));
		mobilePay.setOnMouseClicked(e -> controller.selectPaymentType(PaymentType.MOBILE_PAY, mobilePay));
		
		clipCard.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
		clipCard.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), null, null)));
		clipCard.setOnMouseClicked(e -> controller.selectPaymentType(PaymentType.CLIP_CARD, clipCard));
		
		HBox hbButtons = new HBox();
		
		hbButtons.getChildren().addAll(cash, card, mobilePay, clipCard);
		hbButtons.setSpacing(10);
		
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
		tfAmount.setPromptText("Betalte mængde");
		
		HBox hb = new HBox();
		pane.add(hb, 0, 4);
		
		Button pay = new Button("Betal");
		pay.setDefaultButton(true);
		pay.setOnAction(e -> controller.pay());
		
		lError.setStyle("-fx-text-fill: red");
		hb.getChildren().addAll(pay, lError);
	}
	
	class Controller {
		private boolean endButtonIsAdded = false;
		
		public void selectPaymentType(PaymentType paymentType, BorderPane iw) {
			if (cash.equals(iw)) {
				cash.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 2px;");
			}
			else {
				cash.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
			}
			
			if (card.equals(iw)) {
				card.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 2px;");
			}
			else {
				card.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
			}
			
			if (mobilePay.equals(iw)) {
				mobilePay.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 2px;");
			}
			else {
				mobilePay.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
			}
			
			if (clipCard.equals(iw)) {
				clipCard.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 2px;");
			}
			else {
				clipCard.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px;");
			}
			
			PayDialog.this.paymentType = paymentType;
		}
		
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
			
			if (amount > total) {
				service.createPayment(payable, amount-(amount-total), paymentType);
			}
			else {
				service.createPayment(payable, amount, paymentType);
			}
			
			total -= amount;
			setTotal();
			
			lError.setText("");
			
			if (!endButtonIsAdded) {

				PaymentStatus status = payable.paymentStatus();

				if (status == PaymentStatus.ORDERPAID || status == PaymentStatus.DEPOSITPAID) {
					endButtonIsAdded = true;
					Button end = new Button("Ok");
					end.setOnAction(e -> PayDialog.this.close());
					hbTop.getChildren().add(end);
				}
			}
		}
	}
}
