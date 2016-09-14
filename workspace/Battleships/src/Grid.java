import java.util.Arrays;

/** Class representing how ships are placed on the grid. */
public class Grid {

	// attributes
	private final int length;
	private final int width;

	private GridValue[][] spaces; // whether spaces are occupied/hit
	private Ship[][] ships; // which ship occupies which space

	// constructor
	public Grid(int length, int width) {
		this.length = length;
		this.width = width;
		spaces = new GridValue[length][width];
		for (GridValue[] row : spaces) {
			Arrays.fill(row, GridValue.EMPTY);
		}
		ships = new Ship[length][width];
		for (Ship[] row : ships) {
			Arrays.fill(row, null);
		}
	}

	// methods
	public int getLength() {
		return length;
	}

	public int getWidth() {
		return width;
	}

	public GridValue getSpace(int row, int column) {
		return spaces[row - 1][column - 1];
	}

	public Ship getShip(int posX, int posY) {
		return ships[posX - 1][posY - 1];
	}

	// place a ship on the grid, return true if successful
	public boolean placeShip(Ship ship, int startX, int startY,
			boolean horizontal) {
		int shipLength = ship.getLength();
		if (horizontal) { // place horizontally
			// check if ship goes off edge
			if (startY + shipLength - 1 > width) {
				return false;
			}
			// check for no overlap with other ships
			for (int j = startY; j < startY + shipLength; j++) {
				if (spaces[startX - 1][j - 1] != GridValue.EMPTY) {
					return false;
				}
			}
			// if everything is ok, place ship
			for (int j = startY; j < startY + shipLength; j++) {
				spaces[startX - 1][j - 1] = GridValue.SHIP;
				ships[startX - 1][j - 1] = ship;
			}
		} else { // place vertically
			// check if ship goes off edge
			if (startX + shipLength - 1 > length) {
				return false;
			}
			// check for no overlap with other ships
			for (int i = startX; i < startX + shipLength; i++) {
				if (spaces[i - 1][startY - 1] != GridValue.EMPTY) {
					return false;
				}
			}
			// if everything is ok, place ship
			for (int i = startX; i < startX + shipLength; i++) {
				spaces[i - 1][startY - 1] = GridValue.SHIP;
				ships[i - 1][startY - 1] = ship;
			}
		}
		return true;
	}

	// determine if we have fired at a given space
	public boolean alreadyFiredAt(int shotX, int shotY) {
		return (spaces[shotX - 1][shotY - 1] == GridValue.HIT || spaces[shotX - 1][shotY - 1] == GridValue.MISS);
	}

	// fire at a square and return true if hit a ship
	public boolean fireAt(int shotX, int shotY) {
		switch (spaces[shotX - 1][shotY - 1]) {
		case EMPTY: // if no ship, shot missed
			spaces[shotX - 1][shotY - 1] = GridValue.MISS;
			return false;
		case SHIP: // if ship, shot hit
			spaces[shotX - 1][shotY - 1] = GridValue.HIT;
			return true;
		default: // hopefully will never get here
			return false;
		}
	}
}
