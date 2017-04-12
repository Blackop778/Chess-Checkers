package blackop778.chess_checkers.pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;

public class Knight extends ChessPiece {

    public static BufferedImage blackImage;
    public static BufferedImage whiteImage;

    public Knight(boolean black) {
	this.black = black;
	this.selected = false;
	this.selector = null;
	this.possible = false;
    }

    @Override
    public Point[] getValidLocations(int x, int y) {
	Piece[][] board = Chess_Checkers.client.getBoard();
	ArrayList<Point> validLocations = new ArrayList<>();

	Point[] xYChanges = { new Point(-2, -1), new Point(-2, 1), new Point(-1, -2), new Point(-1, 2),
		new Point(1, -2), new Point(1, 2), new Point(2, -1), new Point(2, 1) };

	for (Point point : xYChanges) {
	    int xChange = point.x;
	    int yChange = point.y;
	    if (x + xChange > -1 && x + xChange < 8) {
		// Ensure the changes remain within the borders of the board
		if (y + yChange > -1 && y + yChange < 8) {
		    boolean check = false;
		    if (board[x + xChange][y + yChange] instanceof Empty) {
			check = true;
		    } else if (board[x + xChange][y + yChange].black != black) {
			check = true;
		    }
		    if (check) {
			Piece replacingPiece = board[x + xChange][y + yChange];
			board[x + xChange][y + yChange] = this;
			board[x][y] = new Empty();
			if (!isKingInCheck(black)) {
			    validLocations.add(new Point(x + xChange, y + yChange));
			}
			board[x + xChange][y + yChange] = replacingPiece;
			board[x][y] = this;
		    }
		}
	    }
	}

	Point[] array = new Point[0];
	validLocations.trimToSize();
	return validLocations.toArray(array);
    }

    @Override
    public BufferedImage getBufferedImage() {
	if (black)
	    return blackImage;
	else
	    return whiteImage;
    }
}
