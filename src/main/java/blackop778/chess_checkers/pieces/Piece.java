package blackop778.chess_checkers.pieces;

import java.awt.Graphics;

public abstract class Piece
{
	public boolean black;

	public boolean selected;

	public Piece selector;

	public boolean possible;

	public abstract void drawSelf(Graphics g, int x, int y);

	public abstract void select(int x, int y);

	/**
	 * @param x The x coordinate to move to
	 * @param y The y coordinate to move to
	 */
	public abstract void move(int x, int y);
}
