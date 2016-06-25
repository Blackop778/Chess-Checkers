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
	public King(boolean black)
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

		Point[] array = new Point[0];
		validLocations.trimToSize();
		return validLocations.toArray(array);
	}
}
