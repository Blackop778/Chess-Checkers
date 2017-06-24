package blackop778.chess_checkers.pieces;

import java.awt.Graphics;
import java.awt.Point;

import blackop778.chess_checkers.checkers.JumpTree;

public abstract class Piece {
    public boolean black;

    public boolean selected;

    public Piece selector;

    public boolean possible;

    public abstract void drawSelf(Graphics g, int x, int y);

    public abstract PossibleMove[] select(int x, int y);

    /**
     * @param x
     *            The x coordinate to move to
     * @param y
     *            The y coordinate to move to
     */
    public abstract void move(int x, int y);

    public static class PossibleMove {
	public final Point point;
	public final JumpTree tree;

	public PossibleMove(Point point) {
	    this.point = point;
	    tree = null;
	}

	public PossibleMove(JumpTree tree) {
	    this.tree = tree;
	    point = null;
	}
    }
}
