package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import blackop778.chess_checkers.Chess_Checkers;

public class King extends ChessPiece
{
	private boolean moved;

	public King(boolean black)
	{
		this.black = black;
		this.selected = false;
		this.selector = null;
		this.possible = false;
		this.moved = false;
	}

	@Override
	public void drawSelf(Graphics g, int x, int y)
	{
		if(possible)
		{
			g.setColor(Color.YELLOW);
			g.fillRect(x + 2, y + 2, 86, 86);
		}
		String color = black ? "Black" : "White";
		File image = new File("resources\\" + color + "King.png");
		try
		{
			g.drawImage(ImageIO.read(image), x, y, null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void move(int x, int y)
	{
		Chess_Checkers.unselectAll();
		Chess_Checkers.blackTurn = !Chess_Checkers.blackTurn;
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
		if(!moved)
		{
			if(x == 2)
			{
				Chess_Checkers.board[3][y] = Chess_Checkers.board[0][y];
				Chess_Checkers.board[0][y] = new Empty();
			}
			else if(x == 6)
			{
				Chess_Checkers.board[5][y] = Chess_Checkers.board[7][y];
				Chess_Checkers.board[7][y] = new Empty();
			}
		}
		if(Chess_Checkers.board[x][y] instanceof Empty)
			pawnCaptureCount++;
		else
			pawnCaptureCount = 0;
		Chess_Checkers.board[x][y] = this;
		moved = true;
		endGameCheck();
	}

	@Override
	public Point[] getValidLocations(int x, int y)
	{
		ArrayList<Point> validLocations = new ArrayList<Point>();

		for(int xChange = -1; xChange < 2; xChange++)
		{
			for(int yChange = -1; yChange < 2; yChange++)
			{
				if(xChange != 0 || yChange != 0)
				{
					if(x + xChange > -1 && x + xChange < 8)
					{
						if(y + yChange > -1 && y + yChange < 8)
						{
							boolean check = false;
							if(Chess_Checkers.board[x + xChange][y + yChange] instanceof Empty)
							{
								check = true;
							}
							else if(Chess_Checkers.board[x + xChange][y + yChange].black != black)
							{
								check = true;
							}
							if(check)
							{
								Piece replacingPiece = Chess_Checkers.board[x + xChange][y + yChange];
								Chess_Checkers.board[x + xChange][y + yChange] = this;
								Chess_Checkers.board[x][y] = new Empty();
								if(!isKingInCheck(black))
								{
									validLocations.add(new Point(x + xChange, y + yChange));
								}
								Chess_Checkers.board[x + xChange][y + yChange] = replacingPiece;
								Chess_Checkers.board[x][y] = this;
							}
						}
					}
				}
			}
		}

		Integer sides = canCastle(x, y);
		if(sides != null)
		{
			if(sides < 1)
			{
				validLocations.add(new Point(2, y));
			}
			if(sides > -1)
			{
				validLocations.add(new Point(6, y));
			}
		}

		Point[] array = new Point[0];
		validLocations.trimToSize();
		return validLocations.toArray(array);
	}

	public Integer canCastle(int x, int y)
	{
		// Queen side is negative, King side is positive, both is 0, neither
		// is null
		Integer sides = null;
		if(!moved)
		{
			// Checks if the rooks have moved
			if(Chess_Checkers.board[0][y] instanceof Rook)
			{
				Rook rook = (Rook) Chess_Checkers.board[0][y];
				if(!rook.getMoved())
				{
					sides = -1;
				}
			}
			if(Chess_Checkers.board[7][y] instanceof Rook)
			{
				Rook rook = (Rook) Chess_Checkers.board[7][y];
				if(!rook.getMoved())
				{
					if(sides != null)
						sides = 0;
					else
						sides = 1;
				}
			}
			if(sides != null)
			{
				// Checks if the space between the king and rook(s) is empty
				if(sides < 1)
				{
					if(Chess_Checkers.board[3][y] instanceof Empty)
					{
						if(Chess_Checkers.board[2][y] instanceof Empty)
						{
							if(Chess_Checkers.board[1][y] instanceof ChessPiece)
							{
								if(sides != 0)
									sides = null;
								else
									sides = 1;
							}
						}
						else
						{
							if(sides != 0)
								sides = null;
							else
								sides = 1;
						}
					}
					else
					{
						if(sides != 0)
							sides = null;
						else
							sides = 1;
					}
				}
				if(sides > -1)
				{
					if(Chess_Checkers.board[5][y] instanceof Empty)
					{
						if(Chess_Checkers.board[6][y] instanceof ChessPiece)
						{
							if(sides != 0)
								sides = null;
							else
								sides = -1;
						}
					}
					else
					{
						if(sides != 0)
							sides = null;
						else
							sides = -1;
					}
				}
				if(sides != null)
				{
					// King cannot move out of check
					if(!ChessPiece.isKingInCheck(black))
					{
						// King cannot pass through or move into check
						if(sides < 1)
						{
							Piece piece = Chess_Checkers.board[3][y];
							Chess_Checkers.board[3][y] = this;
							Chess_Checkers.board[4][y] = new Empty();
							if(!ChessPiece.isKingInCheck(black))
							{
								Chess_Checkers.board[3][y] = piece;
								piece = Chess_Checkers.board[2][y];
								Chess_Checkers.board[2][y] = this;
								if(ChessPiece.isKingInCheck(black))
								{
									if(sides != 0)
										sides = null;
									else
										sides = 1;
								}
								Chess_Checkers.board[2][y] = piece;
								Chess_Checkers.board[4][y] = this;
							}
							else
							{
								Chess_Checkers.board[3][y] = piece;
								Chess_Checkers.board[4][y] = this;
								if(sides != 0)
									sides = null;
								else
									sides = 1;
							}
						}
						if(sides > -1)
						{
							Piece piece = Chess_Checkers.board[5][y];
							Chess_Checkers.board[5][y] = this;
							Chess_Checkers.board[4][y] = new Empty();
							if(!ChessPiece.isKingInCheck(black))
							{
								Chess_Checkers.board[5][y] = piece;
								piece = Chess_Checkers.board[6][y];
								Chess_Checkers.board[6][y] = this;
								if(ChessPiece.isKingInCheck(black))
								{
									if(sides != 0)
										sides = null;
									else
										sides = -1;
								}
								Chess_Checkers.board[6][y] = piece;
								Chess_Checkers.board[4][y] = this;
							}
							else
							{
								Chess_Checkers.board[5][y] = piece;
								Chess_Checkers.board[4][y] = this;
								if(sides != 0)
									sides = null;
								else
									sides = -1;
							}
						}
					}
					else
					{
						sides = null;
					}

				}
			}
		}
		return sides;
	}
}
