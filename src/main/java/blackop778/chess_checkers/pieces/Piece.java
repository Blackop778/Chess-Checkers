package blackop778.chess_checkers.pieces;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Piece
{
	public boolean white;

	public abstract void drawSelf(Graphics g, int x, int y);

	public abstract Point[] getValidLocations(int x, int y);

	public abstract void selected(int x, int y);

	public abstract void move(int x, int y);
}
