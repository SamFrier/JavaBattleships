/***/
public abstract class Player {

	// attributes

	protected int gridLength;
	protected int gridWidth;

	// constructor
	public Player(int gridLength, int gridWidth) {
		this.gridLength = gridLength;
		this.gridWidth = gridWidth;
	}

	// methods

	public abstract int chooseRow();

	public abstract int chooseColumn();

	public abstract String chooseOrientation();
	
	public abstract boolean isHuman();

	public abstract void resetResources();

	public abstract void closeResources();
}
