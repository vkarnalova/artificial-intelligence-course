package bg.sofia.uni.fmi.ai.sliding.puzzle.tile;

public enum Movement {
	LEFT("left"), RIGHT("right"), UP("up"), DOWN("down");

	public final String value;

	private Movement(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
