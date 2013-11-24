package david.restaurant.gui;

import david.restaurant.CustomerAgent;

public class Table {
	CustomerAgent occupiedBy;
	public int tableNumber;
	public int xPos;
	public int yPos;

	public Table(int tableNumber, int x, int y) {
		this.tableNumber = tableNumber;
		xPos = x;
		yPos = y;
	}

	public void setOccupant(CustomerAgent cust) {
		occupiedBy = cust;
	}

	public void setUnoccupied() {
		occupiedBy = null;
	}

	public CustomerAgent getOccupant() {
		return occupiedBy;
	}

	public boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
}