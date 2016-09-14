import java.util.InputMismatchException;
import java.util.Scanner;

/** Class for a human player. */
public class HumanPlayer extends Player {
	
	// attributes
	Scanner intScanner; // for ints
	Scanner stringScanner; // for strings

	// constructor
	public HumanPlayer(int gridLength, int gridWidth) {
		super(gridLength, gridWidth);
		intScanner = new Scanner(System.in);
		stringScanner = new Scanner(System.in);
	}

	// methods
	
	public int chooseRow() throws InputMismatchException {
		return intScanner.nextInt();
	}

	public int chooseColumn() throws InputMismatchException {
		return intScanner.nextInt();
	}

	public String chooseOrientation() {
		return stringScanner.nextLine();
	}
	
	public boolean isHuman() {
		return true;
	}
	
	public void resetResources() {
		intScanner = new Scanner(System.in);
		stringScanner = new Scanner(System.in);
	}

	public void closeResources() {
		intScanner.close();
		stringScanner.close();
	}
	
}
