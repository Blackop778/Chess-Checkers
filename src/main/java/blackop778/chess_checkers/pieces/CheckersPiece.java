package blackop778.chess_checkers.pieces;

import blackop778.chess_checkers.checkers.Jump;
import blackop778.chess_checkers.checkers.JumpTree;

public abstract class CheckersPiece extends Piece
{
	public boolean kinged;

	public abstract JumpTree[] getValidLocations(int x, int y);

	public abstract Jump[] getJumpablePlaces(int x, int y);

	public abstract Jump[] getMoveablePlaces(int x, int y);
}
