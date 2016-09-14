public enum GridValue {
	EMPTY, // player hasn't fired at this space yet
	SHIP, // unhit ship in this space
	HIT, // player hit a ship in this space
	MISS; // player fired at this space but there was no ship
}
