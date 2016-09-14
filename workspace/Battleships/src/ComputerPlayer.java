import java.util.Random;

/** Class for an AI player. */
public class ComputerPlayer extends Player {

	// attributes
	Random random;

	// constructor
	public ComputerPlayer(int gridLength, int gridWidth) {
		super(gridLength, gridWidth);
		random = new Random();
	}

	// methods

	// return a random row number for own grid or opponent's grid
	public int chooseRow() {
		return random.nextInt(gridLength - 1) + 1;
	}

	// return a random column number for own grid or opponent's grid
	public int chooseColumn() {
		return random.nextInt(gridWidth - 1) + 1;
	}

	// randomly choose horizontal or vertical orientation
	public String chooseOrientation() {
		return (random.nextBoolean() ? "h" : "v");
	}
	
	public boolean isHuman() {
		return false;
	}
	
	public void resetResources() {
		// nothing needs resetting
	}

	public void closeResources() {
		// nothing needs closing
	}
}
