package bg.sofia.uni.fmi.ai.tictactoe.board;

public enum State {
	CIRCLE("O"), CROSS("X"), BLANK("_");

	public final String value;

	private State(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
