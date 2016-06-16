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
		this(midPoint, endPoint, JumpTree.NONE);
	}

	public Jump(Point endPoint, int direction)
	{
		this(null, endPoint, direction);
	}

	public Jump(Point midPoint, Point endPoint, int direction)
	{
		this.midPoint = midPoint;
		this.endPoint = endPoint;
		if(direction == JumpTree.NE || direction == JumpTree.SE || direction == JumpTree.SW || direction == JumpTree.NW)
		{
			this.direction = direction;
		}
		else
		{
			this.direction = JumpTree.NONE;
		}
	}

	public Jump(Jump jump)
	{
		this.midPoint = jump.getMidPoint();
		this.endPoint = jump.getEndPoint();
		this.direction = jump.getDirection();
	}

	public Point getMidPoint()
	{
		return new Point(midPoint);
	}

	public Point getEndPoint()
	{
		return new Point(endPoint);
	}

	public int getDirection()
	{
		return direction;
	}
}
