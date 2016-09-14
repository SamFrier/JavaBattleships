import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.ListIterator;

/** Main game functionality class. */
public class Game {

	// attributes
	private static final int GRID_LENGTH = 12;
	private static final int GRID_WIDTH = 12;

	private Grid grid1; // player 1's grid
	private Grid grid2; // player 2's grid
	private LinkedList<Ship> ships1; // player 1's ships
	private LinkedList<Ship> ships2; // player 2's ships
	private int shipCount1; // how many ships player 1 has left
	private int shipCount2; // how many ships player 2 has left

	private Player p1; // the human player
	private Player p2; // the AI player

	// constructor
	public Game() {
		grid1 = new Grid(GRID_LENGTH, GRID_WIDTH);
		grid2 = new Grid(GRID_LENGTH, GRID_WIDTH);
		ships1 = new LinkedList<Ship>();
		ships2 = new LinkedList<Ship>();

		p1 = new HumanPlayer(GRID_LENGTH, GRID_WIDTH);
		p2 = new ComputerPlayer(GRID_LENGTH, GRID_WIDTH);

		// populate list of ships
		listShips(ships1);
		listShips(ships2);

		shipCount1 = ships1.size();
		shipCount2 = ships2.size();
	}

	// methods
	public void start() {
		System.out.println("Welcome to Battleships!");
		printLine();
		phaseOne();
		phaseTwo();

		p1.closeResources();
		p2.closeResources();
	}

	// show a horizontal line
	private void printLine() {
		System.out.println("----------------------------");
	}

	// populate list of ships
	private void listShips(LinkedList<Ship> ships) {
		ships.add(new Carrier());
		ships.add(new Destroyer());
		ships.add(new Submarine());
		ships.add(new Battleship());
		ships.add(new Battleship());
		ships.add(new PatrolBoat());
		ships.add(new PatrolBoat());
	}

	// display the grids
	private void displayGrids(boolean forPlayer1) {
		System.out.println("     Player 1 | Player 2");
		System.out.println("--------------+-------------");

		String row = " ";
		for (int j = 0; j < GRID_WIDTH; j++) {
			row += Integer.toHexString(j + 1);
		}
		row += " | ";
		for (int j = 0; j < GRID_WIDTH; j++) {
			row += Integer.toHexString(j + 1);
		}
		System.out.println(row);

		for (int i = 0; i < GRID_LENGTH; i++) {
			row = "" + Integer.toHexString(i + 1);

			/* Player 1's grid */
			for (int j = 0; j < GRID_WIDTH; j++) {
				switch (grid1.getSpace(i + 1, j + 1)) {
				case EMPTY:
					row += '.';
					break;
				case SHIP:
					// opponent's ships are invisible
					if (forPlayer1) {
						row += '#';
					} else {
						row += '.';
					}
					break;
				case HIT:
					row += '@';
					break;
				case MISS:
					row += 'X';
					break;
				default: // shouldn't get here
					row += '?';
				}
			}

			row += " | ";

			/* Player 2's grid */
			for (int j = 0; j < GRID_WIDTH; j++) {
				switch (grid2.getSpace(i + 1, j + 1)) {
				case EMPTY:
					row += '.';
					break;
				case SHIP:
					// opponent's ships are invisible
					if (!forPlayer1) {
						row += '#';
					} else {
						row += '.';
					}
					break;
				case HIT:
					row += '@';
					break;
				case MISS:
					row += 'X';
					break;
				default: // shouldn't get here
					row += '?';
				}
			}
			row += Integer.toHexString(i+1);
			System.out.println(row);
		}
		
		row = " ";
		for (int j = 0; j < GRID_WIDTH; j++) {
			row += Integer.toHexString(j + 1);
		}
		row += " | ";
		for (int j = 0; j < GRID_WIDTH; j++) {
			row += Integer.toHexString(j + 1);
		}
		System.out.println(row);
	}

	// place ships on the grid
	private void phaseOne() {
		System.out.println("PLACEMENT PHASE");

		/* Player 1 */
		printLine();
		System.out.println("Player 1");
		if (p1.isHuman()) {
			phaseOneHuman(p1, true, grid1, ships2);
		} else {
			phaseOneAI(p1, grid1, ships2);
		}

		/* Player 2 */
		printLine();
		System.out.println("Player 2");

		if (p2.isHuman()) {
			phaseOneHuman(p2, false, grid2, ships1);
		} else {
			phaseOneAI(p2, grid2, ships1);
		}
	}

	public void phaseOneHuman(Player p, boolean player1, Grid grid,
			LinkedList<Ship> ships) {

		ListIterator<Ship> iterator = ships.listIterator();
		while (iterator.hasNext()) {
			printLine();
			displayGrids(player1);
			printLine();

			Ship s = iterator.next();

			// read in ship position
			System.out.println("Now placing: " + s.getDescription()
					+ " (Length " + s.getLength() + ")");
			int startX, startY;
			boolean horizontal;
			do {
				try {
					System.out.print("Select row: ");
					startX = p.chooseRow();
					if (startX <= 0 || startX > grid1.getLength()) {
						System.out.println("Invalid row number!");
						continue;
					}

					System.out.print("Select column: ");
					startY = p.chooseColumn();
					if (startY <= 0 || startY > grid1.getWidth()) {
						System.out.println("Invalid column number!");
						continue;
					}

					System.out.print("Horizontal (H) or vertical (V): ");
					String orientation = p.chooseOrientation();
					if (orientation.equalsIgnoreCase("h")) {
						horizontal = true;
						// break;
						// }
					} else if (orientation.equalsIgnoreCase("v")) {
						horizontal = false; // break;
					} else {
						System.out.println("Invalid orientation!");
						continue;
					}

					// place ship
					boolean shipPlacement = grid.placeShip(s, startX, startY,
							horizontal);
					if (!shipPlacement) { // error code
						System.out.println("Invalid ship placement!");
						// iterator.previous();
						continue;
					}
					break;

				} catch (InputMismatchException e) {
					System.out.println("Please enter an integer!");
					p.resetResources();
				}
			} while (true);
		}
	}

	public void phaseOneAI(Player p, Grid grid, LinkedList<Ship> ships) {
		printLine();
		ListIterator<Ship> iterator = ships.listIterator();
		while (iterator.hasNext()) {
			Ship s = iterator.next();
			int startX, startY;
			boolean horizontal;
			System.out.println("Placing " + s.getDescription() + " (Length "
					+ s.getLength() + ")");

			do {

				// choose position
				startX = p.chooseRow(); // row on own grid
				startY = p.chooseColumn(); // column on own grid

				String orientation = p.chooseOrientation();
				if (orientation == "h") {
					horizontal = true;
				} else if (orientation == "v") {
					horizontal = false;
				} else {
					System.err
							.println("Something has gone wrong in ComputerPlayer.chooseOrientation()");
					return;
				}

				// place ship
				boolean placedShip = grid.placeShip(s, startX, startY,
						horizontal);
				if (placedShip) { // try again if invalid placement
					break;
				}

			} while (true);
		}
	}

	// take turns firing at each other's grid

	private void phaseTwo() {
		printLine();
		System.out.println("FIRING PHASE");
		boolean gameOver = false;
		do {
			if (p1.isHuman()) {
				gameOver = phaseTwoHuman(p1, true, grid2, shipCount2);
			} else {
				gameOver = phaseTwoAI(p1, true, grid2, shipCount2);
			}

			if (gameOver) {
				return;
			}

			if (p2.isHuman()) {
				gameOver = phaseTwoHuman(p2, false, grid1, shipCount1);
			} else {
				gameOver = phaseTwoAI(p2, false, grid1, shipCount1);
			}

			if (gameOver) {
				return;
			}
		} while (true);

	}

	public boolean phaseTwoHuman(Player p, boolean player1, Grid grid,
			int shipCount) {
		printLine();
		System.out.println("Player " + (player1 ? 1 : 2) + "'s turn");
		printLine();
		displayGrids(player1);
		printLine();

		// read in shot position
		int shotX, shotY;
		do {
			try {

				System.out.print("Choose a row: ");
				shotX = p.chooseRow();
				if (shotX <= 0) {
					System.out.println("Invalid row number!");
					continue;
				}

				System.out.print("Choose a column: ");
				shotY = p.chooseColumn();
				if (shotY <= 0) {
					System.out.println("Invalid column number!");
					continue;
				}

				// take the shot
				if (grid.alreadyFiredAt(shotX, shotY)) {
					System.out.println("Already selected that space!");
					continue;
				} else {
					boolean hit = grid.fireAt(shotX, shotY);
					if (hit) { // hit a ship
						System.out.println("Hit!");
						Ship hitShip = grid.getShip(shotX, shotY);
						hitShip.hit();
						if (hitShip.isSunk()) { // if ship sinks
							shipCount--;
							System.out.println("Sank Player "
									+ (player1 ? 2 : 1) + "'s "
									+ hitShip.getDescription() + "!");
							if (shipCount <= 0) { // if opponent runs out of
													// ships
								printLine();
								System.out.println("Player "
										+ (player1 ? 1 : 2) + " wins!!");
								return true; // game ends
							}
						}
						// if game not over, player has another turn
						printLine();
						System.out.println("Player " + (player1 ? 1 : 2)
								+ " has another turn!");
						printLine();
						displayGrids(player1);
						printLine();
					} else { // missed
						System.out.println("Miss!");
						return false;
					}
				}
			} catch (InputMismatchException e) {
				System.out.println("Please enter an integer!");
				p.resetResources();
			}
		} while (true);
	}

	public boolean phaseTwoAI(Player p, boolean player1, Grid grid,
			int shipCount) {
		printLine();
		System.out.println("Player " + (player1 ? 1 : 2) + "'s turn");
		printLine();

		// read in shot position
		int shotX, shotY;

		do {
			// choose square
			shotX = p.chooseRow();
			shotY = p.chooseColumn();

			// take the shot
			if (grid.alreadyFiredAt(shotX, shotY)) {
				continue;
			} else {
				System.out
						.println("Fired at row " + shotX + " column " + shotY);
				boolean hit = grid.fireAt(shotX, shotY);
				if (hit) { // hit a ship
					System.out.println("Hit!");
					Ship hitShip = grid.getShip(shotX, shotY);
					hitShip.hit();
					if (hitShip.isSunk()) { // if ship sinks
						shipCount--;
						System.out.println("Sank Player " + (player1 ? 2 : 1)
								+ "'s " + hitShip.getDescription() + "!");
						if (shipCount <= 0) { // if opponent runs out of ships
							printLine();
							System.out.println("Player " + (player1 ? 1 : 2)
									+ " wins!!");
							return true; // game ends
						}
						// if game not over, player has another turn
					}
				} else { // missed
					System.out.println("Miss!");
					return false;
				}
			}
		} while (true);
	}
}
