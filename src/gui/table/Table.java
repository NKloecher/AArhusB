package gui.table;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Table<A> {
    private BorderPane pane = new BorderPane(new VBox(), new HBox(), null, null, null);
    private List<A> items;
    private final List<Column<A>> columns = new ArrayList<>();
    private final ValidationHandler validationHanlder;

    public Table(ValidationHandler validationHanlder) {
        this.validationHanlder = validationHanlder;

        ((HBox) pane.getTop()).setSpacing(10);
        ((VBox) pane.getCenter()).setSpacing(10);
    }

    /**
     * removes and sets all items in the table
     */
    public void setItems(List<A> items) {
        assert items != null;
        this.items = items;

        ((VBox) pane.getCenter()).getChildren().clear();

        for (A item : items) {
            addRow(item);
        }
    }

    /**
     * adds an item to the table
     * @param item cannot already be in the table
     */
    public void addItem(A item) {
        assert item != null;
        assert !items.contains(item);

        this.items.add(item);

        addRow(item);
    }

    /**
     * removes an item from the table
     * @param item must be it the table
     */
    public void removeItem(A item) {
        assert item != null;
        assert items.contains(item);

        int i = items.indexOf(item);

        this.items.remove(item);

        ((VBox) pane.getCenter()).getChildren().remove(i);
    }

    private void addRow(A item) {
        VBox rows = (VBox) pane.getCenter();
        HBox row = new HBox();

        row.setSpacing(10);

        for (Column<A> column : columns) {
            Node node = column.getNode(item);
            String style = "";

            if (column.getMinWidth() != null) {
                style += "-fx-min-width: " + column.getMinWidth() + "px; ";
            }

            if (column.getPrefWidth() != null) {
                style += "-fx-pref-width: " + column.getPrefWidth() + "px; ";
            }

            if (column.getMaxWidth() != null) {
                style += "-fx-max-width: " + column.getMaxWidth() + "px; ";
            }

            node.setStyle(style);

//			System.out.println(node.getStyle());

            row.getChildren().add(node);
        }

        rows.getChildren().add(row);
    }

    /**
     * returns the pain containing the table contents
     */
    public BorderPane getPane() {
        return pane;
    }

    /**
     * adds a column to the table
     * @param column cannot be null
     */
    public void addColumn(Column<A> column) {
        assert column != null;

        if (validationHanlder != null) {
            column.validationHandler =
                (error, isValid) -> validationHanlder.onValidate(error, isValid());
        }

        Label columnName = new Label(column.getName());

        if (column.getMinWidth() != null) {
            columnName.setMinWidth(column.getMinWidth());
        }

        if (column.getPrefWidth() != null) {
            columnName.setPrefWidth(column.getPrefWidth());
        }

        if (column.getMaxWidth() != null) {
            columnName.setMaxWidth(column.getMaxWidth());
        }

        ((HBox) pane.getTop()).getChildren().add(columnName);

        columns.add(column);
    }

    public boolean isValid() {
        for (Column<A> c : columns) {
            if (c.isValid() == false) {
                return false;
            }
        }

        return true;
    }
}
