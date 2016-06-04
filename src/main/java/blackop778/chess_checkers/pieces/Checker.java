package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;

public class Checker extends CheckersPiece
{
	public Checker(boolean black, boolean kinged)
	{
		this.black = black;
		this.kinged = kinged;
		this.selected = true;
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
		if(black)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.RED);
		g.fillOval(x, y, 90, 90);
	}

	@Override
	public Point[] getValidLocations(int x, int y)
	{
		ArrayList<Point> points = new ArrayList<Point>();
		Point[] pointArray = {new Point(0, 0)};

		return points.toArray(pointArray);
	}

	@Override
	public void select(int x, int y)
	{
		selected = true;
		Point[] points = getValidLocations(x, y);
		for(Point point : points)
		{

			Chess_Checkers.board[point.x][point.y].possible = true;
		}
	}

	@Override
	public void move(int x, int y)
	{
		Chess_Checkers.unselectAll();
	}

	@Override
	public boolean canJumpPiece(int x, int y)
	{
		if(black || kinged)
		{
			if(Chess_Checkers.board[x - 1][y + 1].black != black)
			{
				if(Chess_Checkers.board[x - 2][y + 2] instanceof Empty)
					return true;
			}
			if(Chess_Checkers.board[x + 1][y + 1].black != black)
			{
				if(Chess_Checkers.board[x + 2][y + 2] instanceof Empty)
					return true;
			}
		}
		if(!black || kinged)
		{

		}

		return false;
	}
}
