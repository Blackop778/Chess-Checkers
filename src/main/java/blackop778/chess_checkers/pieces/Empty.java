package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Empty extends Piece
{

	/**
	 * Represents an empty square on the board.
	 */
	public Empty()
	{
		this.possible = true;
	}

	@Override
	public void drawSelf(Graphics g, int x, int y)
	{
		if(possible)
		{
			g.setColor(Color.YELLOW);
			g.fillRect(x + 2, y + 2, 86, 86);
		}
	}

	@Override
	public Point[] getValidLocations(int x, int y)
	{
		return null;
	}

	@Override
	public void select(int x, int y)
	{

	}

	@Override
	public void move(int x, int y)
	{

	}

}
