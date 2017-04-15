package blackop778.chess_checkers.net;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.checkers.JumpTree;
import blackop778.chess_checkers.chess.PawnPromotion.Promotion;

public class GameMessage {

    public static final Pattern chessMessagePattern = Pattern.compile("[a-h][1-8][-x][a-h][1-8]");

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
	    Matcher m = chessMessagePattern.matcher(notation);
	    if (m.find() || notation.equals("0-0") || notation.equals("0-0-0") || notation.equals("(=)")
		    || notation.equals("(=+)") || notation.equals(".5-.5") || notation.equals("0-1")
		    || notation.equals("1-0"))
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
	public final boolean offerDraw;
	public final boolean blackWin;
	public final boolean whiteWin;
	public final boolean draw;
	public final Promotion pawnPromotion;

	public EvaluatedChessMessage(ChessMessage cm) throws InvalidMessageException {
	    final String notation = cm.notation;
	    Matcher m = chessMessagePattern.matcher(notation);
	    // Normal move
	    if (m.find()) {
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
		blackWin = checkmate && Chess_Checkers.client.black;
		whiteWin = checkmate && !Chess_Checkers.client.black;
		castleDirection = Direction.NONE;
		offerSurrender = false;
		offerDraw = false;
		draw = false;
		String right = notation.substring(m.end(), notation.length());
		if (right.startsWith("Q"))
		    pawnPromotion = Promotion.QUEEN;
		else if (right.startsWith("N"))
		    pawnPromotion = Promotion.KNIGHT;
		else if (right.startsWith("R"))
		    pawnPromotion = Promotion.ROOK;
		else if (right.startsWith("B"))
		    pawnPromotion = Promotion.BISHOP;
		else
		    pawnPromotion = Promotion.NONE;
		// Kingside castle
	    } else {
		capture = false;
		check = false;
		checkmate = false;
		pawnPromotion = Promotion.NONE;
		if (notation.equals("0-0")) {
		    fromX = 4;
		    fromY = -1;
		    toX = 6;
		    toY = -1;
		    castleDirection = Direction.RIGHT;
		    offerSurrender = false;
		    offerDraw = false;
		    whiteWin = false;
		    blackWin = false;
		    draw = false;
		    // Queenside castle
		} else if (notation.equals("0-0-0")) {
		    fromX = 4;
		    fromY = -1;
		    toX = 2;
		    toY = -1;
		    castleDirection = Direction.LEFT;
		    offerSurrender = false;
		    offerDraw = false;
		    whiteWin = false;
		    blackWin = false;
		    draw = false;
		    // Offer surrender
		} else {
		    fromX = -1;
		    fromY = -1;
		    toX = -1;
		    toY = -1;
		    castleDirection = Direction.NONE;
		    if (notation.equals(".5-.5")) {
			whiteWin = false;
			blackWin = false;
			draw = true;
			offerDraw = false;
			offerSurrender = false;
		    } else if (notation.equals("1-0")) {
			whiteWin = true;
			blackWin = false;
			draw = false;
			offerDraw = false;
			offerSurrender = false;
		    } else if (notation.equals("0-1")) {
			whiteWin = false;
			blackWin = true;
			draw = false;
			offerDraw = false;
			offerSurrender = false;
		    } else {
			whiteWin = false;
			blackWin = false;
			draw = false;
			if (notation.equals("(=)")) {
			    offerDraw = true;
			    offerSurrender = false;
			} else if (notation.equals("(=+)")) {
			    offerSurrender = true;
			    offerDraw = false;
			} else {
			    throw new InvalidMessageException(
				    "Error: ChessMessage could not be matched to a valid pattern. Closing connection. Your opponent may have attempted to cheat.");
			}
		    }
		}
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
