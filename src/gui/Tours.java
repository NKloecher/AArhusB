package gui;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import gui.table.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Tour;
import service.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

public class Tours extends GridPane {
	private final Service service = Service.getInstance();
	private final Controller controller = new Controller();
	private final DatePicker dp = new DatePicker(LocalDate.now());
	private final DatePickerSkin datePickerSkin = new DatePickerSkin(dp);
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	private final Label lError = new Label();
	private final Table<Tour> table = new Table<>();

	private final TextField tfNewPersons = new TextField();
	private final DatePicker dpNewDate = new DatePicker();
	private final TextField tfNewStart = new TextField();
	private final TextField tfNewEnd = new TextField();
	private final TextField tfNewPrice = new TextField();
	private final Button btnNew = new Button("Opret");
	private Set<LocalDate> tourDates;

	public Tours() {
		setHgap(10);
		setVgap(10);
		setAlignment(Pos.TOP_CENTER);
		table.setMinWidth(800);

		table.addColumn(new PrimitiveColumn<>("Antal Personer", Tour::getPersons, controller::updatePersons));
		table.addColumn(new DateColumn<>("Dato", controller::getDate, controller::updateDate));
		table.addColumn(new PrimitiveColumn<>("Start", controller::getTimeStart, controller::updateStartTime));
		table.addColumn(new PrimitiveColumn<>("Slut", controller::getTimeEnd, controller::updateEndTime));
		table.addColumn(new PrimitiveColumn<>("Pris", Tour::getPrice, controller::updatePrice));


		tourDates = service.getTourDates();

		// Styling for the datepicker widget so that days with active events are bold and underlined
		Callback<DatePicker, DateCell> dayCellFactory = (DatePicker datePicker) -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				if (tourDates.contains(item)){
					setStyle("-fx-underline: true; -fx-font-weight: bolder");
				}
			}
		};

		dp.setDayCellFactory(dayCellFactory);
		add(datePickerSkin.getPopupContent(), 0, 0);


		controller.openDate(LocalDate.now());

		add(table, 0,1);

		dp.valueProperty().addListener((x, y, value) -> controller.openDate(value));

		add(new Label("Opret ny:"), 0, 2);

		HBox hbAdd = new HBox();
		tfNewPersons.setPromptText("Antal Personer");
		hbAdd.getChildren().add(tfNewPersons);
		dpNewDate.setPromptText("Dato");
		hbAdd.getChildren().add(dpNewDate);
		tfNewStart.setPromptText("Startstidspunkt");
		hbAdd.getChildren().add(tfNewStart);
		tfNewEnd.setPromptText("Startstidspunkt");
		hbAdd.getChildren().add(tfNewEnd);
		tfNewPrice.setPromptText("Pris");
		hbAdd.getChildren().add(tfNewPrice);
		hbAdd.getChildren().add(btnNew);
		btnNew.setOnAction(x -> controller.createTour());
		add(hbAdd, 0, 3);

		lError.setStyle("-fx-text-fill: red");
		add(lError, 0, 4);
	}

	private class Controller {
		public void openDate(LocalDate date){
			List<Tour> tours = service.getTours(date);
			table.setVisible(tours.size() != 0);
			table.setItems(tours);
			tourDates = service.getTourDates(); // Reload the calendar widget style
			dpNewDate.setValue(dp.getValue());
		}

		public LocalDate getDate(Tour tour){
			return tour.getDate().toLocalDate();
		}

		public String getTimeStart(Tour tour){
			return tour.getDate().toLocalTime().format(timeFormatter);
		}

		public String getTimeEnd(Tour tour){
			return tour.getDate().toLocalTime().plus(tour.getDuration()).format(timeFormatter);
		}

		public void createTour(){
			Integer persons;
			try {
				int amount = Integer.parseInt(tfNewPersons.getText());
				if (amount > 0){
					persons = amount;
				} else {
					lError.setText("Antallet af personer skal være størrer en 0");
					return;
				}
			} catch (NumberFormatException ex){
				lError.setText("Antal personer er formateret forkert");
				return;
			}

			LocalDateTime startDateTime;
			Duration duration;
			try {
				LocalTime newStartTime = LocalTime.parse(tfNewStart.getText(), timeFormatter);
				LocalTime newEndTime = LocalTime.parse(tfNewEnd.getText(), timeFormatter);
				startDateTime = LocalDateTime.of(dpNewDate.getValue(), newStartTime);
				duration = Duration.between(newStartTime, newEndTime);
				if (duration.isNegative()){
					lError.setText("Starttidspunktet skal være før sluttidspunktet");
					return;
				}
			} catch (DateTimeParseException e){
				lError.setText("Starttiden eller Sluttiden er formateret forkert (HH:MM)");
				return;
			}

			Double price;
			try {
				double amount = Double.parseDouble(tfNewPrice.getText());
				if (amount >= 0){
					price = amount;
				} else {
					lError.setText("Prisen skal mindst værre 0");
					return;
				}
			} catch (NumberFormatException ex){
				lError.setText("Prisen er formateret forkert");
				return;
			}


			service.createTour(persons, startDateTime, price, duration);
			//update ui
			openDate(dp.getValue());
			tfNewEnd.clear();
			tfNewPersons.clear();
			tfNewPrice.clear();
			tfNewStart.clear();
			dpNewDate.getEditor().clear();
			lError.setText("");
		}

		public void updatePersons(Tour tour, String value){
			try {
				int amount = Integer.parseInt(value);
				if (amount > 0){
					service.updateTourPersons(tour, amount);
					lError.setText("");
				} else {
					lError.setText("Antallet af personer skal være størrer en 0");
				}
			} catch (NumberFormatException ex){
				lError.setText("Antal personer er formateret forkert");
			}
		}

		public void updateDate(Tour tour, LocalDate date){
			service.updateTourDate(tour, LocalDateTime.of(date, tour.getDate().toLocalTime()));
		}

		public void updateStartTime(Tour tour, String time){
			try {
				LocalTime newStartTime = LocalTime.parse(time, timeFormatter);
				LocalTime endTime = tour.getDate().toLocalTime().plus(tour.getDuration());

				if (newStartTime.isBefore(endTime)) {
					service.updateTourDate(tour, LocalDateTime.of(tour.getDate().toLocalDate(), newStartTime));
					lError.setText("");
				} else {
					lError.setText("Starttidspunktet skal være før sluttidspunktet");
				}
			} catch (DateTimeParseException e){
				lError.setText("Starttiden er formateret forkert (HH:MM)");
			}
		}

		public void updateEndTime(Tour tour, String time){
			try {
				LocalTime startTime = tour.getDate().toLocalTime();
				LocalTime newEndTime = LocalTime.parse(time, timeFormatter);

				if (newEndTime.isAfter(startTime)){
					LocalDateTime newEndDateTime = LocalDateTime.of(tour.getDate().toLocalDate(), newEndTime);
					service.updateTourDuration(tour, Duration.between(tour.getDate(), newEndDateTime));
					lError.setText("");
				} else {
					lError.setText("Sluttidspunktet skal være efter starttidspunktet");
				}
			} catch (DateTimeParseException e){
				lError.setText("Sluttiden er formateret forkert (HH:MM)");
			}
		}

		public void updatePrice(Tour tour, String value){
			try {
				Double price = Double.parseDouble(value);
				if (price >= 0){
					service.updateTourPrice(tour, price);
					lError.setText("");
				} else {
					lError.setText("Prisen skal mindst værre 0");
				}
			} catch (NumberFormatException ex){
				lError.setText("Prisen er formateret forkert");
			}

		}
	}
}