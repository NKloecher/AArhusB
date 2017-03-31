package gui;

import java.util.ArrayList;
import java.util.List;

import gui.table.ButtonColumn;
import gui.table.LabelColumn;
import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import model.Pricelist;
import model.Product;
import service.Service;

class Test {
	public Product product;
	public double price;
	
	public Test(Product product, double price) {
		this.product = product;
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}
	
	public double getPrice() {
		return price;
	}
}

public class Pricelists extends GridPane {
	private final Controller controller = new Controller();
	private final Service service = Service.getInstance();
	private final Pricelist selectedPricelist = service.getSelectedPricelist();
	
	public Pricelists() {
		setHgap(10);
		setVgap(10);
		setAlignment(Pos.TOP_CENTER);
		
		Table<Test> table = new Table<>();
		
		table.addColumn(new LabelColumn<Test>("Produkt", t -> t.getProduct().getName()));
		table.addColumn(new PrimitiveColumn<Test>("Pris", t -> t.getPrice(), controller::updatePrice));
		table.addColumn(new ButtonColumn<>("Delete", controller::deleteProduct));
		
		List<Test> tests = new ArrayList<Test>();
		
		for (Product p : selectedPricelist.getProducts()) {
			tests.add(new Test(p, selectedPricelist.getPrice(p)));
		}
		
		table.setItems(tests);
	
		ScrollPane sp = new ScrollPane();
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setStyle("-fx-background-color:transparent;");
		sp.setContent(table);
		add(sp, 0, 0);
		
		Button addProduct = new Button("Tilføj produkt");
		
		add(addProduct, 0, 1);
	}
	
	class Controller {
		public void deleteProduct(Test t) {
			
		}
		
		public void updatePrice(Test t, String price) {
			double p;
			
			try {
				p = Double.parseDouble(price);
			} catch (NumberFormatException e) {
				// TODO: handle exception
				return;
			}
			
			service.setPricelistPrice(service.getSelectedPricelist(), t.getProduct(), p);
		}
	}
}
