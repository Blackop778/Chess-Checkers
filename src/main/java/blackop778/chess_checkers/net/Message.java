package blackop778.chess_checkers.net;

import java.awt.Point;

import blackop778.chess_checkers.checkers.JumpTree;
import blackop778.chess_checkers.chess.PawnPromotion.Promotion;

public class Message {

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

    public static char numberToLetter(int number) {
	switch (number) {
	case 0:
	    return 'A';
	case 1:
	    return 'B';
	case 2:
	    return 'C';
	case 3:
	    return 'D';
	case 4:
	    return 'E';
	case 5:
	    return 'F';
	case 6:
	    return 'G';
	case 7:
	    return 'H';
	}
	throw new InvalidCoordinateException();
    }

    public static int letterToNumber(char letter) {
	switch (letter) {
	case 'A':
	    return 0;
	case 'B':
	    return 1;
	case 'C':
	    return 2;
	case 'D':
	    return 3;
	case 'E':
	    return 4;
	case 'F':
	    return 5;
	case 'G':
	    return 6;
	case 'H':
	    return 7;
	}
	throw new InvalidCoordinateException();
    }

    public static class ChessMessage extends Message {
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
	    if (coordinate1.matches("[A-H][0-7]") && coordinate2.matches("[A-H][0-7]"))
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
	    if (coordinate1.matches("[A-H][0-7]") && coordinate2.matches("[A-H][0-7]"))
		return new PawnPromotionMessage(coordinate1, coordinate2, offerSurrender, promo);
	    return null;
	}
    }

    public static class CheckersMessage extends Message {
	public final String coordinate1;
	public final JumpTree tree;
	public final boolean offerSurrender;

	public static CheckersMessage instantiate(String coordinate1, JumpTree tree) {
	    return instantiate(coordinate1, tree, false);
	}

	public static CheckersMessage instantiate(String coordinate1, JumpTree tree, boolean offerSurrender) {
	    if (coordinate1.matches("[A-H][0-7]"))
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
