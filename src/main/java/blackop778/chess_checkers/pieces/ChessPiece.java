package blackop778.chess_checkers.pieces;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.chess.Snapshot;

public abstract class ChessPiece extends Piece
{
	public abstract Point[] getValidLocations(int x, int y);

	public static Point doubleMovePawn;

	public static int pawnCaptureCount;

	public static ArrayList<Snapshot> snapshots;

	@Override
	public void select(int x, int y)
	{
		if(black == Chess_Checkers.blackTurn)
		{
			Chess_Checkers.unselectAll();
			Point[] locations = getValidLocations(x, y);
			for(Point point : locations)
			{
				if(point != null)
				{
					selected = true;
					Chess_Checkers.board[point.x][point.y].possible = true;
					Chess_Checkers.board[point.x][point.y].selector = this;
				}
			}
		}
	}

	@Override
	// Remember King and Pawn have their own implementations so edit them too
	public void move(int x, int y)
	{
		Chess_Checkers.unselectAll();
		Chess_Checkers.blackTurn = Utilities.opposite(Chess_Checkers.blackTurn);
		if(doubleMovePawn != null)
		{
			Pawn pawn = (Pawn) Chess_Checkers.board[doubleMovePawn.x][doubleMovePawn.y];
			pawn.lastMoveDouble = false;
			doubleMovePawn = null;
		}
		findSelfLoop: for(int i = 0; i < 8; i++)
		{
			for(int n = 0; n < 8; n++)
			{
				if(Chess_Checkers.board[i][n].equals(this))
				{
					Chess_Checkers.board[i][n] = new Empty();
					break findSelfLoop;
				}
			}
		}
		if(Chess_Checkers.board[x][y] instanceof Empty)
			pawnCaptureCount++;
		else
			pawnCaptureCount = 0;
		Chess_Checkers.board[x][y] = this;
		endGameCheck();
	}

	public static boolean canMove(boolean black)
	{
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				if(Chess_Checkers.board[x][y].black == black && Chess_Checkers.board[x][y] instanceof ChessPiece)
				{
					ChessPiece piece = (ChessPiece) Chess_Checkers.board[x][y];
					if(!Utilities.isArrayEmpty(piece.getValidLocations(x, y)))
						return true;
				}
			}
		}

		return false;
	}

	public static final boolean isKingInCheck(boolean black)
	{
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				if(Chess_Checkers.board[x][y] instanceof King && Chess_Checkers.board[x][y].black == black)
				{
					int currentX = x;
					int currentY = y;
					int changeX = 0;
					int changeY = -1;

					for(int i = 0; i < 8; i++)
					{
						boolean done = false;
						while(!done)
						{
							currentX += changeX;
							currentY += changeY;
							if(currentX > -1 && currentX < 8)
							{
								if(currentY > -1 && currentY < 8)
								{
									if(Chess_Checkers.board[currentX][currentY] instanceof ChessPiece)
									{
										done = true;
										if(Chess_Checkers.board[currentX][currentY].black != black)
										{
											if(i < 4)
											{
												if(Chess_Checkers.board[currentX][currentY] instanceof Rook
														|| Chess_Checkers.board[currentX][currentY] instanceof Queen)
												{
													return true;
												}
											}
											else
											{
												if(Chess_Checkers.board[currentX][currentY] instanceof Bishop
														|| Chess_Checkers.board[currentX][currentY] instanceof Queen)
												{
													return true;
												}
											}
										}
									}
								}
								else
								{
									done = true;
								}
							}
							else
							{
								done = true;
							}
						}
						currentX = x;
						currentY = y;
						switch(i)
						{
							case 0:
								changeY = 0;
								changeX = 1;
								break;
							case 1:
								changeY = 1;
								changeX = 0;
								break;
							case 2:
								changeY = 0;
								changeX = -1;
								break;
							case 3:
								changeX = 1;
								changeY = -1;
								break;
							case 4:
								changeY = 1;
								changeX = 1;
								break;
							case 5:
								changeY = 1;
								changeX = -1;
								break;
							case 6:
								changeY = -1;
								changeX = -1;
								break;
						}
					}
					if(black)
					{
						if(y + 1 < 8)
						{
							if(x - 1 > -1)
							{
								if(Chess_Checkers.board[x - 1][y + 1].black != black
										&& Chess_Checkers.board[x - 1][y + 1] instanceof Pawn)
									return true;
							}
							if(x + 1 < 8)
							{
								if(Chess_Checkers.board[x + 1][y + 1].black != black
										&& Chess_Checkers.board[x + 1][y + 1] instanceof Pawn)
									return true;
							}
						}
					}
					else
					{
						if(y - 1 > -1)
						{
							if(x - 1 > -1)
							{
								if(Chess_Checkers.board[x - 1][y - 1].black != black
										&& Chess_Checkers.board[x - 1][y - 1] instanceof Pawn)
									return true;
							}
							if(x + 1 < 8)
							{
								if(Chess_Checkers.board[x + 1][y - 1].black != black
										&& Chess_Checkers.board[x + 1][y - 1] instanceof Pawn)
									return true;
							}
						}
					}
					Point[] xYChanges = {new Point(-2, -1), new Point(-2, 1), new Point(-1, -2), new Point(-1, 2),
							new Point(1, -2), new Point(1, 2), new Point(2, -1), new Point(2, 1)};

					for(Point point : xYChanges)
					{
						int xChange = point.x;
						int yChange = point.y;
						if(x + xChange > -1 && x + xChange < 8)
						{
							// Ensure the changes remain within the borders of
							// the board
							if(y + yChange > -1 && y + yChange < 8)
							{
								if(Chess_Checkers.board[x + xChange][y + yChange].black != black
										&& Chess_Checkers.board[x + xChange][y + yChange] instanceof Knight)
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public void endGameCheck()
	{
		if(pawnCaptureCount == 50)
		{
			Chess_Checkers.gameOver = true;
			JOptionPane.showMessageDialog(null,
					"50 turns have passed since a piece has been taken or a pawn has moved. The game is a draw.",
					"Deadlock has been reached", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(!canMove(!black))
		{
			Chess_Checkers.gameOver = true;
			if(isKingInCheck(!black))
			{
				String winner = black ? "black" : "white";
				JOptionPane.showMessageDialog(null,
						"Congratulations, " + winner + " wins. Exit this message and click on the board to restart.",
						"A Champion has been decided", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				String cause = !black ? "black" : "white";
				JOptionPane.showMessageDialog(null,
						"The game is a draw because " + cause + " cannot move but isn't in check.",
						"Deadlock has been reached", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
