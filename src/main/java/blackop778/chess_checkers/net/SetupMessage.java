package blackop778.chess_checkers.net;

public class SetupMessage {
    public static class ColorChoice {
	public static enum ColorC {
	    BLACK, WHITE
	};

	public final ColorC choice;

	public ColorChoice(ColorC c) {
	    this.choice = c;
	}
    }

    public static class RPSChoice {
	public enum RPS {
	    ROCK, PAPER, SCISSCORS
	};

	public final RPS choice;

	public RPSChoice(RPS rps) {
	    this.choice = rps;
	}
    }
}
