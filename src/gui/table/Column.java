package gui.table;

import java.util.ArrayList;
import java.util.List;

import gui.Handler;
import javafx.scene.Node;

public abstract class Column<A> {
	private final String name;
	private Double minWidth, prefWidth, maxWidth;
	protected List<Node> nodes = new ArrayList<>();
	
	public Column(String name) {
		super();
		this.name = name;
	}
	
	public abstract Node getNode(A owner);

	public String getName() {
		return name;
	}

	public Double getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(Double minWidth) {
		this.minWidth = minWidth;
	}

	public Double getPrefWidth() {
		return prefWidth;
	}

	public void setPrefWidth(Double prefWidth) {
		this.prefWidth = prefWidth;
	}

	public Double getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Double maxWidth) {
		this.maxWidth = maxWidth;
	}
}
