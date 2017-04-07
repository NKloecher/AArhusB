package gui.table;

import javafx.scene.Node;

public interface RowData {
	Node getCell(String columnName);
}