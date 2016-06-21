package blackop778.chess_checkers.pieces;

import java.awt.Point;

public abstract class ChessPiece extends Piece
{
	public abstract Point[] getValidLocations(int x, int y);

	public static boolean blackKingInCheck;

	public static boolean whiteKingInCheck;

	public static final boolean isKingInCheck(boolean black)
	{
		return false;
	}
}
