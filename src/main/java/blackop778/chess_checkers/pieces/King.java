package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import blackop778.chess_checkers.Chess_Checkers;

public class King extends ChessPiece {
    public boolean moved;

    public King(boolean black) {
	this.black = black;
	this.selected = false;
	this.selector = null;
	this.possible = false;
	this.moved = false;
    }

    @Override
    public void drawSelf(Graphics g, int x, int y) {
	if (possible) {
	    g.setColor(Color.YELLOW);
	    g.fillRect(x + 2, y + 2, 86, 86);
	}
	String color = black ? "Black" : "White";
	File image = new File("resources\\" + color + "King.png");
	try {
	    g.drawImage(ImageIO.read(image), x, y, null);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void move(int x, int y) {
	Chess_Checkers.client.moveChess(x, y, this);
    }

    @Override
    public Point[] getValidLocations(int x, int y) {
	ArrayList<Point> validLocations = new ArrayList<>();
	Piece[][] board = Chess_Checkers.client.getBoard();

	for (int xChange = -1; xChange < 2; xChange++) {
	    for (int yChange = -1; yChange < 2; yChange++) {
		if (xChange != 0 || yChange != 0) {
		    if (x + xChange > -1 && x + xChange < 8) {
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
	    }
	}

	Integer sides = canCastle(x, y);
	if (sides != null) {
	    if (sides < 1) {
		validLocations.add(new Point(2, y));
	    }
	    if (sides > -1) {
		validLocations.add(new Point(6, y));
	    }
	}

	Point[] array = new Point[0];
	validLocations.trimToSize();
	return validLocations.toArray(array);
    }

    /**
     * 
     * @param x
     *            X of the king
     * @param y
     *            Y of the king
     * @return Valid sides: Queen side is negative, King side is positive, both
     *         is 0, neither is null
     */
    public Integer canCastle(int x, int y) {
	// Queen side is negative, King side is positive, both is 0, neither is
	// null
	Integer sides = null;
	Piece[][] board = Chess_Checkers.client.getBoard();
	if (!moved) {
	    // Checks if the rooks have moved
	    if (board[0][y] instanceof Rook) {
		Rook rook = (Rook) board[0][y];
		if (!rook.getMoved()) {
		    sides = -1;
		}
	    }
	    if (board[7][y] instanceof Rook) {
		Rook rook = (Rook) board[7][y];
		if (!rook.getMoved()) {
		    if (sides != null) {
			sides = 0;
		    } else {
			sides = 1;
		    }
		}
	    }
	    if (sides != null) {
		// Checks if the space between the king and rook(s) is empty
		if (sides < 1) {
		    if (board[3][y] instanceof Empty) {
			if (board[2][y] instanceof Empty) {
			    if (board[1][y] instanceof ChessPiece) {
				if (sides != 0) {
				    sides = null;
				} else {
				    sides = 1;
				}
			    }
			} else {
			    if (sides != 0) {
				sides = null;
			    } else {
				sides = 1;
			    }
			}
		    } else {
			if (sides != 0) {
			    sides = null;
			} else {
			    sides = 1;
			}
		    }
		}
		if (sides > -1) {
		    if (board[5][y] instanceof Empty) {
			if (board[6][y] instanceof ChessPiece) {
			    if (sides != 0) {
				sides = null;
			    } else {
				sides = -1;
			    }
			}
		    } else {
			if (sides != 0) {
			    sides = null;
			} else {
			    sides = -1;
			}
		    }
		}
		if (sides != null) {
		    // King cannot move out of check
		    if (!ChessPiece.isKingInCheck(black)) {
			// King cannot pass through or move into check
			if (sides < 1) {
			    Piece piece = board[3][y];
			    board[3][y] = this;
			    board[4][y] = new Empty();
			    if (!ChessPiece.isKingInCheck(black)) {
				board[3][y] = piece;
				piece = board[2][y];
				board[2][y] = this;
				if (ChessPiece.isKingInCheck(black)) {
				    if (sides != 0) {
					sides = null;
				    } else {
					sides = 1;
				    }
				}
				board[2][y] = piece;
				board[4][y] = this;
			    } else {
				board[3][y] = piece;
				board[4][y] = this;
				if (sides != 0) {
				    sides = null;
				} else {
				    sides = 1;
				}
			    }
			}
			if (sides > -1) {
			    Piece piece = board[5][y];
			    board[5][y] = this;
			    board[4][y] = new Empty();
			    if (!ChessPiece.isKingInCheck(black)) {
				board[5][y] = piece;
				piece = board[6][y];
				board[6][y] = this;
				if (ChessPiece.isKingInCheck(black)) {
				    if (sides != 0) {
					sides = null;
				    } else {
					sides = -1;
				    }
				}
				board[6][y] = piece;
				board[4][y] = this;
			    } else {
				board[5][y] = piece;
				board[4][y] = this;
				if (sides != 0) {
				    sides = null;
				} else {
				    sides = -1;
				}
			    }
			}
		    } else {
			sides = null;
		    }

		}
	    }
	}
	return sides;
    }
}
