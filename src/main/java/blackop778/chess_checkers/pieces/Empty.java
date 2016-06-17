package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;

import blackop778.chess_checkers.Chess_Checkers;

public class Empty extends Piece
{

	/**
	 * Represents an empty square on the board.
	 */
	public Empty()
	{
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
	}

	@Override
	public void select(int x, int y)
	{
		if(possible)
		{
			selector.move(x, y);
		}
		else
		{
			Chess_Checkers.unselectAll();
		}
	}

	@Override
	public void move(int x, int y)
	{

	}

}
