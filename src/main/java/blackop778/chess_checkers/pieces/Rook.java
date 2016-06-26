package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import blackop778.chess_checkers.Chess_Checkers;

public class Rook extends ChessPiece
{
	private boolean moved;

	public Rook(boolean black)
	{
		this.black = black;
		this.selected = false;
		this.selector = null;
		this.possible = false;
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
		File image = new File("resources\\" + color + "Rook.png");
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
		super.move(x, y);
		moved = true;
	}

	@Override
	public Point[] getValidLocations(int x, int y)
	{
		ArrayList<Point> validLocations = new ArrayList<Point>();
		int currentX = x;
		int currentY = y;
		int changeX = 0;
		int changeY = -1;

		for(int i = 0; i < 4; i++)
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
						if(Chess_Checkers.board[currentX][currentY] instanceof Empty)
						{
							Piece replacingPiece = Chess_Checkers.board[currentX][currentY];
							Chess_Checkers.board[currentX][currentY] = this;
							Chess_Checkers.board[x][y] = new Empty();
							if(!isKingInCheck(black))
							{
								validLocations.add(new Point(currentX, currentY));
							}
							Chess_Checkers.board[currentX][currentY] = replacingPiece;
							Chess_Checkers.board[x][y] = this;
						}
						else
						{
							done = true;
							if(Chess_Checkers.board[currentX][currentY].black != black)
							{
								Piece replacingPiece = Chess_Checkers.board[currentX][currentY];
								Chess_Checkers.board[currentX][currentY] = this;
								Chess_Checkers.board[x][y] = new Empty();
								if(!isKingInCheck(black))
								{
									validLocations.add(new Point(currentX, currentY));
								}
								Chess_Checkers.board[currentX][currentY] = replacingPiece;
								Chess_Checkers.board[x][y] = this;
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
			}
		}

		Point[] array = new Point[0];
		validLocations.trimToSize();
		return validLocations.toArray(array);
	}

	public boolean getMoved()
	{
		return moved;
	}
}
