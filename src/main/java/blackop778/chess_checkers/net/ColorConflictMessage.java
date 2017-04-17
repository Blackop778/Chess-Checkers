package blackop778.chess_checkers.net;

import blackop778.chess_checkers.graphics.ColorConflictCorrection;

public class ColorConflictMessage {
    public final ColorConflictCorrection ccc;

    public ColorConflictMessage(ColorConflictCorrection ccc) {
	this.ccc = ccc;
    }

    public static class ColorAgreementMessage {
	public final boolean senderBlack;

	public ColorAgreementMessage(boolean black) {
	    this.senderBlack = black;
	}
    }
}
