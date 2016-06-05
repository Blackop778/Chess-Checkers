package blackop778.chess_checkers.pieces;

import blackop778.chess_checkers.checkers.JumpTree;

public abstract class CheckersPiece extends Piece
{
	public boolean kinged;

	public abstract boolean canJumpPiece(int x, int y);

	public abstract boolean canMovePiece(int x, int y);

	public abstract JumpTree[] getValidLocations(int x, int y);

	public abstract JumpTree extendJumpTree(JumpTree tree, int x, int y);
}
