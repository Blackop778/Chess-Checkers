package blackop778.chess_checkers.ai;

import blackop778.chess_checkers.net.Client;
import blackop778.chess_checkers.pieces.Piece.PossibleMove;

public abstract class AI {
    public abstract PossibleMove decideMove(Client game);
}
