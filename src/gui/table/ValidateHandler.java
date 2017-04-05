package gui.table;

public interface ValidateHandler<A> {
	public boolean validate(RowData cell, A rowData);	
}