package model.bdd;

public class Projection extends Table {
	
	private Table table;
	private String projection;

	public Projection(Table table, String projection) {
		super(null);
		this.table = table;
		this.projection = projection;
	}
	
	@Override
	public String toString() {
		return String.format("select %s from %s", projection, table.toString());
	}

}
