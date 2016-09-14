/** Generic ship class. */
public abstract class Ship {

	// attributes
	private int length;
	private String description;
	private int sectionsHit;
	private boolean hasSunk;

	// constructor
	public Ship(int length, String description) {
		this.length = length;
		this.description = description;
		sectionsHit = 0;
		hasSunk = false;
	}

	// methods
	public int getLength() {
		return length;
	}

	public String getDescription() {
		return description;
	}

	public int getSectionsHit() {
		return sectionsHit;
	}

	// one more section of the ship is hit
	public void hit() {
		sectionsHit++;
		if (sectionsHit >= length) {
			hasSunk = true;
		}
	}

	public boolean isSunk() {
		return hasSunk;
	}

}
