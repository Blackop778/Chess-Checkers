package blackop778.chess_checkers.pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;

public class Pawn extends ChessPiece {

    public static BufferedImage blackImage;
    public static BufferedImage whiteImage;

    public Pawn(boolean black) {
	this.black = black;
	this.selected = false;
	this.selector = null;
	this.possible = false;
    }

    @Override
    public Point[] getValidLocations(int x, int y) {
	ArrayList<Point> validLocations = new ArrayList<>();
	Piece[][] board = Chess_Checkers.client.getBoard();

	int yOffset = black ? 1 : -1;

	if (board[x][y + yOffset] instanceof Empty) {
	    Piece replacingPiece = board[x][y + yOffset];
	    board[x][y + yOffset] = this;
	    board[x][y] = new Empty();
	    if (!isKingInCheck(black)) {
		validLocations.add(new Point(x, y + yOffset));
	    }
	    board[x][y + yOffset] = replacingPiece;
	    board[x][y] = this;
	}
	if (x - 1 > -1) {
	    if (board[x - 1][y + yOffset] instanceof ChessPiece && board[x - 1][y + yOffset].black != black) {
		Piece replacingPiece = board[x - 1][y + yOffset];
		board[x - 1][y + yOffset] = this;
		board[x][y] = new Empty();
		if (!isKingInCheck(black)) {
		    validLocations.add(new Point(x - 1, y + yOffset));
		}
		board[x - 1][y + yOffset] = replacingPiece;
		board[x][y] = this;
	    }
	}
	if (x + 1 < 8) {
	    if (board[x + 1][y + yOffset] instanceof ChessPiece && board[x + 1][y + yOffset].black != black) {
		Piece replacingPiece = board[x + 1][y + yOffset];
		board[x + 1][y + yOffset] = this;
		board[x][y] = new Empty();
		if (!isKingInCheck(black)) {
		    validLocations.add(new Point(x + 1, y + yOffset));
		}
		board[x + 1][y + yOffset] = replacingPiece;
		board[x][y] = this;
	    }

	}
	if ((y == 1 && black) || (y == 6 && !black)) {
	    if (board[x][y + yOffset] instanceof Empty && board[x][y + (yOffset * 2)] instanceof Empty) {
		Piece replacingPiece = board[x][y + (yOffset * 2)];
		board[x][y + (yOffset * 2)] = this;
		board[x][y] = new Empty();
		if (!isKingInCheck(black)) {
		    validLocations.add(new Point(x, y + (yOffset * 2)));
		}
		board[x][y + (yOffset * 2)] = replacingPiece;
		board[x][y] = this;
	    }
	}
	Integer sides = canEnPassantCapture(x, y);
	if (sides != null) {
	    validLocations.add(new Point(x + sides, y + yOffset));
	}

	Point[] array = new Point[0];
	validLocations.trimToSize();
	return validLocations.toArray(array);
    }

    public Integer canEnPassantCapture(int x, int y) {
	// null means no captures possible, negative means left side, positive
	// means right side
	Integer sides = null;
	int yOffset = black ? 1 : -1;
	Piece[][] board = Chess_Checkers.client.getBoard();

	if (x + 1 < 8) {
	    if (board[x + 1][y] instanceof Pawn && board[x + 1][y].black != black) {
		if (board[x + 1][y + yOffset] instanceof Empty) {
		    Pawn pawn = (Pawn) board[x + 1][y];
		    if (pawn.equals(doubleMovePawn)) {
			sides = new Integer(1);
		    }
		}
	    }
	}
	if (x - 1 > -1) {
	    if (board[x - 1][y] instanceof Pawn && board[x - 1][y].black != black) {
		if (board[x - 1][y + yOffset] instanceof Empty) {
		    Pawn pawn = (Pawn) board[x - 1][y];
		    if (pawn.equals(doubleMovePawn)) {
			sides = new Integer(-1);
		    }
		}
	    }
	}

	return sides;
    }

    @Override
    public BufferedImage getBufferedImage() {
	if (black)
	    return blackImage;
	else
	    return whiteImage;
    }
}
