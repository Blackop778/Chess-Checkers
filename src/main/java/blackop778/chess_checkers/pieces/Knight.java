package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import blackop778.chess_checkers.Chess_Checkers;

public class Knight extends ChessPiece {
    public Knight(boolean black) {
	this.black = black;
	this.selected = false;
	this.selector = null;
	this.possible = false;
    }

    @Override
    public void drawSelf(Graphics g, int x, int y) {
	if (possible) {
	    g.setColor(Color.YELLOW);
	    g.fillRect(x + 2, y + 2, 86, 86);
	}
	String color = black ? "Black" : "White";
	File image = new File("resources\\" + color + "Knight.png");
	try {
	    g.drawImage(ImageIO.read(image), x, y, null);
	} catch (IOException e) {
	    e.printStackTrace();
	}
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
}
