package bg.sofia.uni.fmi.ai.traveling.salesman.path;

public class City {
	private int xCoordinate;
	private int yCoordinate;

	public City(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public double calculateDistance(City otherCity) {
		return Math.sqrt(Math.pow(otherCity.getxCoordinate() - xCoordinate, 2)
				+ Math.pow(otherCity.getyCoordinate() - yCoordinate, 2));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + xCoordinate;
		result = prime * result + yCoordinate;
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
		City other = (City) obj;
		if (xCoordinate != other.xCoordinate) {
			return false;
		}
		if (yCoordinate != other.yCoordinate) {
			return false;
		}
		return true;
	}
}
