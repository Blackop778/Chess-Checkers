package blackop778.chess_checkers.net;

import blackop778.chess_checkers.Chess_Checkers;

public class HandshakeMessage {
    public final boolean black;

    // More to be added
    public HandshakeMessage() {
	black = Chess_Checkers.client.getBlack();
    }
}
