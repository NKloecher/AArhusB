package gui.table;

import javafx.scene.Node;

public interface RowData {
	public Node getCell(String columnName);
}