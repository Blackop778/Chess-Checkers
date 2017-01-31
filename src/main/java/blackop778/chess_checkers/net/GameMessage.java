package blackop778.chess_checkers.net;

import java.awt.Point;

import blackop778.chess_checkers.checkers.JumpTree;
import blackop778.chess_checkers.chess.PawnPromotion.Promotion;

public class GameMessage {
    /**
     * Assumes (0,0) is the top left square
     * 
     * @param x
     * @param y
     * @return
     */
    public static String pointToChessNotation(int x, int y) {
	String xL;
	switch (x) {
	case 0:
	    xL = "a";
	    break;
	case 1:
	    xL = "b";
	    break;
	case 2:
	    xL = "c";
	    break;
	case 3:
	    xL = "d";
	    break;
	case 4:
	    xL = "e";
	    break;
	case 5:
	    xL = "f";
	    break;
	case 6:
	    xL = "g";
	    break;
	case 7:
	    xL = "h";
	    break;
	default:
	    throw new InvalidCoordinateException();
	}

	return xL + String.valueOf(Math.abs(y - 8));
    }

    public static Point ChessNotationToPoint(String notation) {
	int x;
	switch (notation.charAt(0)) {
	case 'a':
	    x = 0;
	    break;
	case 'b':
	    x = 1;
	    break;
	case 'c':
	    x = 2;
	    break;
	case 'd':
	    x = 3;
	    break;
	case 'e':
	    x = 4;
	    break;
	case 'f':
	    x = 5;
	    break;
	case 'g':
	    x = 6;
	    break;
	case 'h':
	    x = 7;
	    break;
	default:
	    throw new InvalidCoordinateException();
	}

	return new Point(x, Math.abs(Integer.valueOf(notation.substring(1)) - 8));
    }

    public static class ChessMessage extends GameMessage {
	public final String coordinate1;
	public final String coordinate2;
	public final boolean offerSurrender;

	/**
	 * 
	 * @param coordinate1
	 * @param coordinate2
	 * @return Will be null if invalid input
	 */
	public static ChessMessage instantiate(String coordinate1, String coordinate2) {
	    return instantiate(coordinate1, coordinate2, false);
	}

	public static ChessMessage instantiate(String coordinate1, String coordinate2, boolean offerSurrender) {
	    if (coordinate1.matches("[a-h][1-8]") && coordinate2.matches("[a-h][1-8]"))
		return new ChessMessage(coordinate1, coordinate2, offerSurrender);
	    return null;
	}

	private ChessMessage(String coordinate1, String coordinate2, boolean offerSurrender) {
	    this.coordinate1 = coordinate1;
	    this.coordinate2 = coordinate2;
	    this.offerSurrender = offerSurrender;
	}
    }

    public static class PawnPromotionMessage extends ChessMessage {
	public final Promotion promo;

	private PawnPromotionMessage(String coordinate1, String coordinate2, boolean offerSurrender, Promotion promo) {
	    super(coordinate1, coordinate2, offerSurrender);
	    this.promo = promo;
	}

	public static PawnPromotionMessage instantiate(String coordinate1, String coordinate2, boolean offerSurrender,
		Promotion promo) {
	    if (coordinate1.matches("[a-h][1-8]") && coordinate2.matches("[a-h][1-8]"))
		return new PawnPromotionMessage(coordinate1, coordinate2, offerSurrender, promo);
	    return null;
	}
    }

    public static class CheckersMessage extends GameMessage {
	public final String coordinate1;
	public final JumpTree tree;
	public final boolean offerSurrender;

	public static CheckersMessage instantiate(String coordinate1, JumpTree tree) {
	    return instantiate(coordinate1, tree, false);
	}

	public static CheckersMessage instantiate(String coordinate1, JumpTree tree, boolean offerSurrender) {
	    if (coordinate1.matches("[a-h][1-8]"))
		return new CheckersMessage(coordinate1, tree, offerSurrender);
	    return null;
	}

	private CheckersMessage(String coordinate1, JumpTree tree, boolean offerSurrender) {
	    this.coordinate1 = coordinate1;
	    this.tree = tree;
	    this.offerSurrender = offerSurrender;
	}
    }

    public static class InvalidCoordinateException extends RuntimeException {
	private static final long serialVersionUID = -8498929249310011469L;

    }
}
