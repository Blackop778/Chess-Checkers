package blackop778.chess_checkers.pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;

public class Rook extends ChessPiece {
    public boolean moved;

    public static BufferedImage blackImage;
    public static BufferedImage whiteImage;

    public Rook(boolean black) {
	this.black = black;
	this.selected = false;
	this.selector = null;
	this.possible = false;
    }

    @Override
    public Point[] getValidLocations(int x, int y) {
	Piece[][] board = Chess_Checkers.client.getBoard();
	ArrayList<Point> validLocations = new ArrayList<Point>();
	int currentX = x;
	int currentY = y;
	int changeX = 0;
	int changeY = -1;

	for (int i = 0; i < 4; i++) {
	    boolean done = false;
	    while (!done) {
		currentX += changeX;
		currentY += changeY;
		if (currentX > -1 && currentX < 8) {
		    if (currentY > -1 && currentY < 8) {
			if (board[currentX][currentY] instanceof Empty) {
			    Piece replacingPiece = board[currentX][currentY];
			    board[currentX][currentY] = this;
			    board[x][y] = new Empty();
			    if (!isKingInCheck(black)) {
				validLocations.add(new Point(currentX, currentY));
			    }
			    board[currentX][currentY] = replacingPiece;
			    board[x][y] = this;
			} else {
			    done = true;
			    if (board[currentX][currentY].black != black) {
				Piece replacingPiece = board[currentX][currentY];
				board[currentX][currentY] = this;
				board[x][y] = new Empty();
				if (!isKingInCheck(black)) {
				    validLocations.add(new Point(currentX, currentY));
				}
				board[currentX][currentY] = replacingPiece;
				board[x][y] = this;
			    }
			}
		    } else {
			done = true;
		    }
		} else {
		    done = true;
		}
	    }
	    currentX = x;
	    currentY = y;
	    switch (i) {
	    case 0:
		changeY = 0;
		changeX = 1;
		break;
	    case 1:
		changeY = 1;
		changeX = 0;
		break;
	    case 2:
		changeY = 0;
		changeX = -1;
		break;
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
