package model.bdd;

public class Restriction extends Table {
	
	private Table table;
	private String restriction;

	public Restriction(Table table, String restriction) {
		super(null);
		this.table = table;
		this.restriction = restriction;
	}
	
	@Override
	public String toString() {
		return String.format("%s where %s", table.toString(), restriction);
	}
}
