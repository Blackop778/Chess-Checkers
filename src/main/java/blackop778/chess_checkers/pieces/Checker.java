package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.checkers.Jump;
import blackop778.chess_checkers.checkers.JumpTree;

public class Checker extends CheckersPiece {
    public Checker(boolean black, Integer UID) {
	this.black = black;
	this.kinged = false;
	this.selected = false;
	this.possible = false;
	this.UID = UID;
    }

    @Override
    public void drawSelf(Graphics g, int x, int y) {
	if (possible) {
	    g.setColor(Color.YELLOW);
	    g.fillRect(x + 2, y + 2, 86, 86);
	}
	if (black) {
	    g.setColor(Color.BLACK);
	} else {
	    g.setColor(Color.RED);
	}
	g.fillOval(x, y, 90, 90);
	if (kinged) {
	    g.setColor(Color.WHITE);
	    g.fillRect(x + 15, y + 50, 60, 15);
	    for (int i = 0; i < 3; i++) {
		g.fillPolygon(new int[] { x + 15 + (i * 20), x + 25 + (i * 20), x + 35 + (i * 20) },
			new int[] { y + 50, y + 20, y + 50 }, 3);
		g.fillOval(x + 20 + (i * 20), y + 20, 10, 10);
	    }
	}
    }

    @Override
    public JumpTree[] getValidLocations(int x, int y) {
	JumpTree[] treeArray = JumpTree.makeTree(this, x, y);
	return treeArray;
    }

    @Override
    public PossibleMove[] select(int x, int y) {
	if ((black == Chess_Checkers.client.getBlack()) && Chess_Checkers.client.getTurn()) {
	    Chess_Checkers.client.unselectAll();
	    JumpTree[] jumps = getValidLocations(x, y);
	    PossibleMove[] pLocations = new PossibleMove[jumps.length];
	    lastValidLocations = jumps;
	    for (int i = 0; i < jumps.length; i++) {
		if (jumps[i] != null) {
		    selected = true;
		    Chess_Checkers.client.select(jumps[i].getEndJump().getEndPoint(), this);
		}
	    }

	    return pLocations;
	}

	return null;
    }

    @Override
    public void move(int x, int y) {
	Chess_Checkers.client.moveChecker(x, y, this);
    }

    @Override
    public Jump[] getJumpablePlaces(int x, int y, ArrayList<Integer> previousUIDs) {
	ArrayList<Jump> places = new ArrayList<>();
	Piece[][] board = Chess_Checkers.client.getBoard();

	if ((black || kinged) && y < 6) {
	    if (x > 1) {
		if (board[x - 1][y + 1].black != black && !(board[x - 1][y + 1] instanceof Empty)) {
		    if (board[x - 2][y + 2] instanceof Empty) {
			if (previousUIDs == null) {
			    places.add(new Jump(new Point(x - 1, y + 1), new Point(x - 2, y + 2)));
			} else {
			    Checker piece = (Checker) board[x - 1][y + 1];
			    if (!previousUIDs.contains(piece.UID)) {
				places.add(new Jump(new Point(x - 1, y + 1), new Point(x - 2, y + 2)));
			    }
			}
		    }
		}
	    }
	    if (x < 6) {
		if (board[x + 1][y + 1].black != black && !(board[x + 1][y + 1] instanceof Empty)) {
		    if (board[x + 2][y + 2] instanceof Empty) {
			if (previousUIDs == null) {
			    places.add(new Jump(new Point(x + 1, y + 1), new Point(x + 2, y + 2)));
			} else {
			    Checker piece = (Checker) board[x + 1][y + 1];
			    if (!previousUIDs.contains(piece.UID)) {
				places.add(new Jump(new Point(x + 1, y + 1), new Point(x + 2, y + 2)));
			    }
			}
		    }
		}
	    }
	}
	if ((!black || kinged) && y > 1) {
	    if (x > 1) {
		if (board[x - 1][y - 1].black != black && !(board[x - 1][y - 1] instanceof Empty)) {
		    if (board[x - 2][y - 2] instanceof Empty) {
			if (previousUIDs == null) {
			    places.add(new Jump(new Point(x - 1, y - 1), new Point(x - 2, y - 2)));
			} else {
			    Checker piece = (Checker) board[x - 1][y - 1];
			    if (!previousUIDs.contains(piece.UID)) {
				places.add(new Jump(new Point(x - 1, y - 1), new Point(x - 2, y - 2)));
			    }
			}
		    }
		}
	    }
	    if (x < 6) {
		if (board[x + 1][y - 1].black != black && !(board[x + 1][y - 1] instanceof Empty)) {
		    if (board[x + 2][y - 2] instanceof Empty) {
			if (previousUIDs == null) {
			    places.add(new Jump(new Point(x + 1, y - 1), new Point(x + 2, y - 2)));
			} else {
			    Checker piece = (Checker) board[x + 1][y - 1];
			    if (!previousUIDs.contains(piece.UID)) {
				places.add(new Jump(new Point(x + 1, y - 1), new Point(x + 2, y - 2)));
			    }
			}
		    }
		}
	    }
	}

	Jump[] toReturn = new Jump[0];
	places.trimToSize();
	return places.toArray(toReturn);
    }

    @Override
    public Jump[] getMoveablePlaces(int x, int y) {
	ArrayList<Jump> places = new ArrayList<>();
	Piece[][] board = Chess_Checkers.client.getBoard();
	if (Utilities.isArrayEmpty(CheckersPiece.checkJumps(black, false))) {
	    if ((black || kinged) && y != 7) {
		if (x > 0) {
		    if (board[x - 1][y + 1] instanceof Empty) {
			places.add(new Jump(new Point(x - 1, y + 1)));
		    }
		}
		if (x < 7) {
		    if (board[x + 1][y + 1] instanceof Empty) {
			places.add(new Jump(new Point(x + 1, y + 1)));
		    }
		}
	    }
	    if ((!black || kinged) && y != 0) {
		if (x > 0) {
		    if (board[x - 1][y - 1] instanceof Empty) {
			places.add(new Jump(new Point(x - 1, y - 1)));
		    }
		}
		if (x < 7) {
		    if (board[x + 1][y - 1] instanceof Empty) {
			places.add(new Jump(new Point(x + 1, y - 1)));
		    }
		}
	    }
	}

	Jump[] toReturn = new Jump[0];
	places.trimToSize();
	return places.toArray(toReturn);
    }
}
