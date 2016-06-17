package blackop778.chess_checkers.pieces;

import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.checkers.Jump;
import blackop778.chess_checkers.checkers.JumpTree;

public abstract class CheckersPiece extends Piece
{
	public boolean kinged;

	public JumpTree[] lastValidLocations;

	public abstract JumpTree[] getValidLocations(int x, int y);

	public abstract Jump[] getJumpablePlaces(int x, int y, int previousDirection);

	public abstract Jump[] getMoveablePlaces(int x, int y);

	public static CheckersPiece[] checkJumps(boolean black, boolean includeMoves)
	{
		ArrayList<CheckersPiece> jumpers = new ArrayList<CheckersPiece>();

		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				Piece piece = Chess_Checkers.board[x][y];
				if(piece instanceof CheckersPiece && piece.black == black)
				{
					CheckersPiece checker = (CheckersPiece) piece;
					if(!Utilities.isArrayEmpty(checker.getJumpablePlaces(x, y, JumpTree.NONE)))
					{
						jumpers.add(checker);
					}
					else if(includeMoves)
					{
						if(!Utilities.isArrayEmpty(checker.getMoveablePlaces(x, y)))
						{
							jumpers.add(checker);
						}
					}
				}
			}
		}

		CheckersPiece[] toReturn = new CheckersPiece[0];
		return jumpers.toArray(toReturn);
	}
}
