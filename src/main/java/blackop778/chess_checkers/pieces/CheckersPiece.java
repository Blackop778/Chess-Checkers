package blackop778.chess_checkers.pieces;

public abstract class CheckersPiece extends Piece
{
	public boolean kinged;

	public abstract boolean canJumpPiece(int x, int y);
}
