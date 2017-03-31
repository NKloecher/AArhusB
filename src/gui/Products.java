package gui;

import gui.table.PrimitiveColumn;
import gui.table.Table;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import model.Product;
import service.Service;
import storage.Storage;

public class Products extends GridPane {

    private final Storage storage = Storage.getInstance();
    private final Service service = Service.getInstance();
    private final Controller controller = new Controller();
    private final Table<Product> table = new Table<>();

    public Products() {
    	setHgap(10);
		setVgap(10);
		setAlignment(Pos.TOP_CENTER);

        ScrollPane sp = new ScrollPane();
        sp.setContent(table);

        table.addColumn(new PrimitiveColumn<Product, String>("Navn", x -> x.getName(),
            (x, y) -> controller.updateName(x, y)));
        table.addColumn(
            new PrimitiveColumn<Product, String>("Kategori", x -> x.getCategory(),
                (x, y) -> controller.updateCategori(x, y)));
        table.setItems(storage.getProduct());
        table.addColumn(new PrimitiveColumn<Product, Integer>("Klips", x -> x.getClips(),
            (x, y) -> controller.updateClips(x, y)));

        add(sp, 0, 0);

    }

    private class Controller {
        public void updateName(Product product, String name) {
            service.updateProductName(product, name);
        }

        public Object updateClips(Product x, String y) {
            // TODO Auto-generated method stub
            return null;
        }

        public Object updateCategori(Product x, String y) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
