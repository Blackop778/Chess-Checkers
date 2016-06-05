package blackop778.chess_checkers.checkers;

import java.awt.Point;
import java.util.ArrayList;

public class JumpTree
{
	private Point endPoint;
	private ArrayList<Point> midPoints;

	public JumpTree()
	{
		endPoint = new Point(-1, -1);
	}

	public Point getEndPoint()
	{
		return endPoint;
	}

	public ArrayList<Point> getMidPoints()
	{
		return midPoints;
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
}
