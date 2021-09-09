package model.bdd;

public class Main {

	public static void main(String[] args) {
		
		Table table = new Table("Client");
		Restriction restriction = new Restriction(table, "nom = 'Dupont'");
		Projection projection = new Projection(restriction, "nom");
		
		System.out.println(projection.toString());
		
	}
}
