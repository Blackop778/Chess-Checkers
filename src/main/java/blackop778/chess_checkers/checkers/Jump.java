package blackop778.chess_checkers.checkers;

import java.awt.Point;

public class Jump
{

	private Point midPoint;
	private Point endPoint;
	private int direction;

	public Jump(Point endPoint)
	{
		this(null, endPoint);
	}

	public Jump(Point midPoint, Point endPoint)
	{
		this.midPoint = midPoint;
		this.endPoint = endPoint;
	}

	public Jump(Jump jump)
	{
		this.midPoint = jump.getMidPoint();
		this.endPoint = jump.getEndPoint();
		this.direction = jump.getDirection();
	}

	public Point getMidPoint()
	{
		if(midPoint != null)
			return new Point(midPoint);
		else
			return null;
	}

	public Point getEndPoint()
	{
		if(endPoint != null)
			return new Point(endPoint);
		else
			return null;
	}

	public int getDirection()
	{
		return direction;
	}
}
