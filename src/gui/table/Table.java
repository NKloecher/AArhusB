package gui.table;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.ColumnConstraintsBuilder;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

import gui.Handler;

public class Table<A> extends GridPane {
	private final List<Column<A>> columns = new ArrayList<>();
	private List<A> items = new ArrayList<>();
	private ValidateHandler<A> validateHandler;
	private List<Boolean> validity = new ArrayList<>();

	public boolean isValid() {
		for (boolean b : validity) {
			if (b == false) return false; 
		}
		
		return true;
	}
	
	public void setItems(List<A> items) {
		this.items = items;
		showItems();
	}

	public void addItem(A item) {
		items.add(item);
		showItems();
	}

	public void removeItem(A item) {
		items.remove(item);
		showItems();
	}

	public void addColumn(Column<A> column) {
		columns.add(column);
		showHeader();
	}

	private void showHeader() {
		ObservableList<Node> children = getChildren();

		List<ColumnConstraints> constraints = new ArrayList<>();
		for (Column<A> column : columns) {
			ColumnConstraints constraint = new ColumnConstraints();
			if (column.getMinWidth() != null) {
				constraint.setMinWidth(column.getMinWidth());
			}
			if (column.getPrefWidth() != null) {
				constraint.setPrefWidth(column.getPrefWidth());
			}
			if (column.getMaxWidth() != null) {
				constraint.setMaxWidth(column.getMaxWidth());
			}
			constraints.add(constraint);
		}
		getColumnConstraints().setAll(constraints);

		setHgap(10);
		setVgap(10);

		if (children.size() != 0) {
			for (int i = columns.size() - 2; i >= 0; i--) {
				children.remove(i);
			}
		}

		for (int i = 0; i < columns.size(); i++) {
			Column<A> column = columns.get(i);
			Label l = new Label();
			l.setStyle("-fx-font-weight: bolder");

			if (!(column instanceof ButtonColumn<?>)) {
				l.setText(column.getName());
			}

			add(l, i, 0);
		}
	}

	public Node getCell(String columnName, int row) {
		int column = -1;

		for (Column<?> c : columns) {
			column += 1;

			if (c.getName().equals(columnName)) {
				break;
			}
		}

		return getChildren().get(columns.size() * row + column);
	}

	private void showItems() {
		ObservableList<Node> children = getChildren();

		for (int i = children.size() - 1; i > columns.size() - 1; i--) {
			children.remove(i);
		}

		for (int j = 0; j < items.size(); j++) {
			A item = items.get(j);
			for (int i = 0; i < columns.size(); i++) {
				final int row = i;
				validity.add(true);
				Column<A> column = columns.get(i);
	
				column.validateHandler = () -> {
					if (validateHandler == null)
						return false;
					
					RowData r = columnName -> getCell(columnName, row);
					boolean result = validateHandler.validate(r, item);
					validity.set(row, result);
					
					return result;
				};
				Node n = column.getNode(item);

				n.setUserData(item);

				add(n, i, j + 1);
			}
		}
	}

	public void setValidateHandler(ValidateHandler<A> handler) {
		validateHandler = handler;
	}
}
