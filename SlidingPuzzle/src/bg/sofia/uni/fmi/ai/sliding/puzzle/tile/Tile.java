package bg.sofia.uni.fmi.ai.sliding.puzzle.tile;

public class Tile {
	private Position position;
	private int currentValue;
	private int expextedValue;

	public Tile(Position position, int currentValue, int expextedValue) {
		this.position = position;
		this.currentValue = currentValue;
		this.expextedValue = expextedValue;
	}

	public int calculateManhattanDistance(Tile otherTile) {
		int xCoordinate = Math.abs(this.position.getxCoordinate() - otherTile.getPosition().getxCoordinate());
		int yCoordinate = Math.abs(this.position.getyCoordinate() - otherTile.getPosition().getyCoordinate());
		return xCoordinate + yCoordinate;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public int getExpextedValue() {
		return expextedValue;
	}

	public void setExpextedValue(int expextedValue) {
		this.expextedValue = expextedValue;
	}

	public boolean isEmpty() {
		return currentValue == 0;
	}

	public Tile copyTile() {
		Position copiedPosition = new Position(position.getxCoordinate(), position.getyCoordinate());
		return new Tile(copiedPosition, currentValue, expextedValue);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentValue;
		result = prime * result + expextedValue;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Tile other = (Tile) obj;
		if (currentValue != other.currentValue) {
			return false;
		}
		if (expextedValue != other.expextedValue) {
			return false;
		}
		if (position == null) {
			if (other.position != null) {
				return false;
			}
		} else if (!position.equals(other.position)) {
			return false;
		}
		return true;
	}
}
