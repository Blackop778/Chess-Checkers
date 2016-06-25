package blackop778.chess_checkers.pieces;

import java.awt.Point;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;

public abstract class ChessPiece extends Piece
{
	public abstract Point[] getValidLocations(int x, int y);

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
	public void move(int x, int y)
	{
		Chess_Checkers.unselectAll();
		Chess_Checkers.blackTurn = Utilities.opposite(Chess_Checkers.blackTurn);
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
		Chess_Checkers.board[x][y] = this;
		ChessPiece.whiteKingInCheck = ChessPiece.isKingInCheck(false);
		ChessPiece.blackKingInCheck = ChessPiece.isKingInCheck(true);
	}

	public static boolean blackKingInCheck;

	public static boolean whiteKingInCheck;

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
				}
			}
		}
		return false;
	}
}
