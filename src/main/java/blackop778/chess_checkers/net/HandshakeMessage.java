package blackop778.chess_checkers.net;

import blackop778.chess_checkers.Chess_Checkers;

public class HandshakeMessage {
    public final boolean black;
    public final long seed;

    // More to be added
    public HandshakeMessage() {
	black = Chess_Checkers.client.getBlack();
	Chess_Checkers.ourSeed = System.currentTimeMillis();
	seed = Chess_Checkers.ourSeed;
    }
}
