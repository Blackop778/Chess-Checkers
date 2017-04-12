package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import blackop778.chess_checkers.Chess_Checkers;

public class Bishop extends ChessPiece {
    public Bishop(boolean black) {
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
	URL image = ClassLoader.getSystemClassLoader()
		.getResource("assets" + File.separator + "images" + File.separator + color + "Bishop.png");
	try {
	    g.drawImage(ImageIO.read(image), x, y, null);
	} catch (IOException e) {
	    // e.printStackTrace();
	}
    }

    @Override
    public Point[] getValidLocations(int x, int y) {
	ArrayList<Point> validLocations = new ArrayList<Point>();
	int currentX = x;
	int currentY = y;
	int changeX = 1;
	int changeY = -1;

	for (int i = 0; i < 4; i++) {
	    boolean done = false;
	    while (!done) {
		currentX += changeX;
		currentY += changeY;
		if (currentX > -1 && currentX < 8) {
		    if (currentY > -1 && currentY < 8) {
			Piece[][] board = Chess_Checkers.client.getBoard();
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
		changeY = 1;
		changeX = 1;
		break;
	    case 1:
		changeY = 1;
		changeX = -1;
		break;
	    case 2:
		changeY = -1;
		changeX = -1;
		break;
	    }
	}

	Point[] array = new Point[0];
	validLocations.trimToSize();
	return validLocations.toArray(array);
    }
}
