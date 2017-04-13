package blackop778.chess_checkers.net;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import blackop778.chess_checkers.checkers.JumpTree;

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

    public static Point chessNotationToPoint(String notation) {
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
	public final String notation;

	public static ChessMessage instantiate(String notation) {
	    if (notation.contains("[a-h][1-8][-x][a-h][1-8]") || notation.equals("0-0") || notation.equals("0-0-0")
		    || notation.equals("="))
		return new ChessMessage(notation);
	    return null;
	}

	private ChessMessage(String notation) {
	    this.notation = notation;
	}
    }

    public static enum Direction {
	LEFT, RIGHT, NONE;
    }

    public static class EvaluatedChessMessage {
	public final byte fromX;
	public final byte fromY;
	public final boolean capture;
	public final byte toX;
	public final byte toY;
	public final boolean check;
	public final boolean checkmate;
	public final Direction castleDirection;
	public final boolean offerSurrender;

	public EvaluatedChessMessage(ChessMessage cm) throws InvalidMessageException {
	    final String notation = cm.notation;
	    // Normal move
	    if (notation.contains("[a-h][1-8][-x][a-h][1-8]")) {
		Pattern p = Pattern.compile("[a-h][1-8][-x][a-h][1-8]");
		Matcher m = p.matcher(notation);
		String coords = notation.substring(m.start(), m.start() + 5);
		Point from = chessNotationToPoint(coords.substring(0, 2));
		fromX = (byte) from.x;
		fromY = (byte) from.y;
		Point to = chessNotationToPoint(coords.substring(3, 5));
		toX = (byte) to.x;
		toY = (byte) to.y;
		capture = coords.substring(2, 3).equals("x");
		check = notation.endsWith("+");
		checkmate = notation.endsWith("#");
		castleDirection = Direction.NONE;
		offerSurrender = false;
		// Kingside castle
	    } else if (notation.equals("0-0")) {
		capture = false;
		fromX = 4;
		fromY = -1;
		toX = 6;
		toY = -1;
		check = false;
		checkmate = false;
		castleDirection = Direction.RIGHT;
		offerSurrender = false;
		// Queenside castle
	    } else if (notation.equals("0-0-0")) {
		capture = false;
		fromX = 4;
		fromY = -1;
		toX = 2;
		toY = -1;
		check = false;
		checkmate = false;
		castleDirection = Direction.LEFT;
		offerSurrender = false;
		// Offer surrender
	    } else if (notation.equals("=")) {
		offerSurrender = true;
		capture = false;
		fromX = -1;
		fromY = -1;
		toX = -1;
		toY = -1;
		check = false;
		checkmate = false;
		castleDirection = Direction.NONE;
	    } else {
		throw new InvalidMessageException("Error: ChessMessage matched no patterns");
	    }
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
