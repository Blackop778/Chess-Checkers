package blackop778.chess_checkers.checkers;

import java.awt.Point;
import java.util.ArrayList;

import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.pieces.Checker;

public class JumpTree
{
	private Point endPoint;
	private ArrayList<Point> midPoints;
	private boolean jumpMode;
	private int direction;

	// The values for direction
	static final int NONE = 0;
	static final int NE = 2;
	static final int SE = 4;
	static final int SW = 6;
	static final int NW = 8;

	public JumpTree()
	{
		endPoint = new Point(-1, -1);
	}

	public Point getEndPoint()
	{
		return new Point(endPoint);
	}

	public ArrayList<Point> getMidPoints()
	{
		midPoints.trimToSize();
		return new ArrayList<Point>(midPoints);
	}

	public void addMidPoint(Point point)
	{
		midPoints.add(point);
	}

	public void setEndPoint(Point point)
	{
		if(endPoint.equals(new Point(-1, -1)))
		{
			endPoint = point;
		}
	}

	public void jumpMode()
	{
		if(!jumpMode)
			jumpMode = true;
	}

	public void setDirection(int direction)
	{
		if(direction == NE || direction == SE || direction == SW || direction == NW)
			this.direction = direction;
		else
			this.direction = NONE;
	}

	public int getDirection()
	{
		return new Integer(direction);
	}

	static JumpTree[] makeTree(Checker checker, int x, int y)
	{
		JumpTree[] trees = new JumpTree[0];

		if(!Utilities.isArrayEmpty(checker.getJumpablePlaces(x, y)))
		{

		}
		else if(!Utilities.isArrayEmpty(checker.getMoveablePlaces(x, y)))
		{
			Jump[] places = checker.getMoveablePlaces(x, y);
			trees = extendArray(trees, places.length);
			for(int i = 0; i < places.length; i++)
			{
				trees[i] = new JumpTree();
				trees[i].setEndPoint(places[i].getEndPoint());
			}
		}

		return trees;
	}

	private static JumpTree[] continueTree(JumpTree[] tree)
	{

	}

	static JumpTree[] extendArray(JumpTree[] source, int timesToExtend)
	{
		JumpTree[] toReturn = new JumpTree[source.length + timesToExtend];
		for(int i = 0; i < toReturn.length; i++)
		{
			if(i < source.length)
				toReturn[i] = source[i];
			else
				toReturn[i] = null;
		}

		return toReturn;
	}
}
