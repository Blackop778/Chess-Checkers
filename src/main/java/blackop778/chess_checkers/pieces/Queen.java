package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Queen extends ChessPiece
{
	public Queen(boolean black)
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
		File image = new File("resources\\" + color + "Queen.png");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void select(int x, int y)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void move(int x, int y)
	{
		// TODO Auto-generated method stub

	}

}
