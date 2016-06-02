package blackop778.chess_checkers.pieces;

import java.awt.Graphics;
import java.awt.Point;

public class Checker extends CheckersPiece
{
	public Checker(boolean white, boolean kinged)
	{
		this.white = white;
		this.kinged = kinged;
	}

	@Override
	public void drawSelf(Graphics g, int x, int y)
	{

	}

	@Override
	public Point[] getValidLocations(int x, int y)
	{

		return null;
	}

	@Override
	public void selected(int x, int y)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void move(int x, int y)
	{
		// TODO Auto-generated method stub

	}

}
