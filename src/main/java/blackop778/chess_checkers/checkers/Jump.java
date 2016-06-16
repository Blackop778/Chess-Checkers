package blackop778.chess_checkers.checkers;

import java.awt.Point;

public class Jump
{

	private Point midPoint;
	private Point endPoint;

	public Jump(Point endPoint)
	{
		this(null, endPoint);
	}

	public Jump(Point midPoint, Point endPoint)
	{
		this.midPoint = midPoint;
		this.endPoint = endPoint;
	}

	public Point getMidPoint()
	{
		return new Point(midPoint);
	}

	public Point getEndPoint()
	{
		return new Point(endPoint);
	}
}
