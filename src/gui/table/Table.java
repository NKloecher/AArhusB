package gui.table;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class Table<A> extends GridPane {
	private final List<Column<A>> columns = new ArrayList<>();
	private List<A> items = new ArrayList<>();
	
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
		
		if (children.size() != 0) {
			for (int i = columns.size() - 2; i >= 0; i--) {
				children.remove(i);
			}
		}
		
		for (int i = 0; i < columns.size(); i++) {
			Column<A> column = columns.get(i);
			Label l = new Label();				
			
			if (!(column instanceof ButtonColumn<?>)) {
				l.setText(column.getName());
			}
			
			add(l, i, 0);
		}
	}
	
	private void showItems() {
		ObservableList<Node> children = getChildren();
		
		for (int i = children.size() - 1; i > columns.size(); i--) {
			children.remove(i);
		}
		
		for (int i = 0; i < columns.size(); i++) {
			Column<A> column = columns.get(i);
			
			for (int j = 0; j < items.size(); j++) {
				A item = items.get(j);
				add(column.getNode(item), i, j+1);
			}
		}
	}
}
