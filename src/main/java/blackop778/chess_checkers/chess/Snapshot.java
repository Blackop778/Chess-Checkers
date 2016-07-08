package blackop778.chess_checkers.chess;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.pieces.Bishop;
import blackop778.chess_checkers.pieces.Empty;
import blackop778.chess_checkers.pieces.King;
import blackop778.chess_checkers.pieces.Knight;
import blackop778.chess_checkers.pieces.Pawn;
import blackop778.chess_checkers.pieces.Piece;
import blackop778.chess_checkers.pieces.Queen;
import blackop778.chess_checkers.pieces.Rook;

public class Snapshot
{
	public short[][] board;

	public Snapshot()
	{
		board = new short[8][8];
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				Piece piece = Chess_Checkers.board[x][y];
				if(piece instanceof Empty)
				{
					board[x][y] = 0;
				}
				else
				{
					int colorDifferentiationMultiplier = piece.black ? 1 : -1;
					if(piece instanceof Pawn)
					{
						Pawn pawn = (Pawn) piece;
						Integer sides = pawn.canEnPassantCapture(x, y);
						if(sides == null)
						{
							board[x][y] = (short) (1 * colorDifferentiationMultiplier);
						}
						else
						{
							board[x][y] = (short) (2 * colorDifferentiationMultiplier);
						}
					}
					else if(piece instanceof Rook)
					{
						board[x][y] = (short) (3 * colorDifferentiationMultiplier);
					}
					else if(piece instanceof Knight)
					{
						board[x][y] = (short) (4 * colorDifferentiationMultiplier);
					}
					else if(piece instanceof Bishop)
					{
						board[x][y] = (short) (5 * colorDifferentiationMultiplier);
					}
					else if(piece instanceof Queen)
					{
						board[x][y] = (short) (6 * colorDifferentiationMultiplier);
					}
					else if(piece instanceof King)
					{
						King king = (King) piece;
						Integer sides = king.canCastle(x, y);
						if(sides == null)
						{
							board[x][y] = (short) (7 * colorDifferentiationMultiplier);
						}
						else
						{
							if(sides == -1)
							{
								board[x][y] = (short) (8 * colorDifferentiationMultiplier);
							}
							else if(sides == 0)
							{
								board[x][y] = (short) (9 * colorDifferentiationMultiplier);
							}
							else if(sides == 1)
							{
								board[x][y] = (short) (10 * colorDifferentiationMultiplier);
							}
						}
					}
					else
					{
						board[x][y] = (short) (15 * colorDifferentiationMultiplier);
					}
				}
			}
		}
	}
}
