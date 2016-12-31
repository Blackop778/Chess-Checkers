package blackop778.chess_checkers.net;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.checkers.Jump;
import blackop778.chess_checkers.checkers.JumpTree;
import blackop778.chess_checkers.chess.PawnPromotion;
import blackop778.chess_checkers.chess.SnapshotStorage;
import blackop778.chess_checkers.pieces.Bishop;
import blackop778.chess_checkers.pieces.Checker;
import blackop778.chess_checkers.pieces.CheckersPiece;
import blackop778.chess_checkers.pieces.ChessPiece;
import blackop778.chess_checkers.pieces.Empty;
import blackop778.chess_checkers.pieces.King;
import blackop778.chess_checkers.pieces.Knight;
import blackop778.chess_checkers.pieces.Pawn;
import blackop778.chess_checkers.pieces.Piece;
import blackop778.chess_checkers.pieces.Queen;
import blackop778.chess_checkers.pieces.Rook;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Client extends ChannelInboundHandlerAdapter {

    private Piece[][] board;
    public final boolean black;
    public final boolean gameIsCheckers;
    private boolean turn;

    public Client(boolean black, boolean gameIsCheckers) {
	this.black = black;
	this.gameIsCheckers = gameIsCheckers;

	board = new Piece[8][8];
	for (int i = 0; i < board.length; i++) {
	    for (int n = 0; n < board[0].length; n++) {
		board[i][n] = new Empty();
	    }
	}

	if (gameIsCheckers) {
	    ChessPiece.threefoldRepetition = false;
	    SnapshotStorage.initialize(false);
	    gameIsCheckers = true;
	    turn = black;
	    for (int i = 0; i < board.length; i++) {
		for (int n = 0; n < board[0].length; n++) {
		    if ((i + n) % 2 == 1) {
			if (n < 3) {
			    board[i][n] = new Checker(true, n + (i * 8));
			} else if (n > 4) {
			    board[i][n] = new Checker(false, n + (i * 8));
			}
		    }
		}
	    }
	} else {
	    ChessPiece.threefoldRepetition = true;
	    SnapshotStorage.initialize(true);
	    ChessPiece.doubleMovePawn = null;
	    ChessPiece.pawnCaptureCount = 0;
	    gameIsCheckers = false;
	    turn = !black;
	    for (int i = 0; i < board.length; i++) {
		for (int n = 0; n < board[0].length; n++) {
		    switch (n) {
		    case 0:
			if (i == 0 || i == 7) {
			    board[i][n] = new Rook(true);
			} else if (i == 1 || i == 6) {
			    board[i][n] = new Knight(true);
			} else if (i == 2 || i == 5) {
			    board[i][n] = new Bishop(true);
			} else if (i == 3) {
			    board[i][n] = new Queen(true);
			} else if (i == 4) {
			    board[i][n] = new King(true);
			}
			break;
		    case 1:
			board[i][n] = new Pawn(true);
			break;

		    case 6:
			board[i][n] = new Pawn(false);
			break;
		    case 7:
			if (i == 0 || i == 7) {
			    board[i][n] = new Rook(false);
			} else if (i == 1 || i == 6) {
			    board[i][n] = new Knight(false);
			} else if (i == 2 || i == 5) {
			    board[i][n] = new Bishop(false);
			} else if (i == 3) {
			    board[i][n] = new Queen(false);
			} else if (i == 4) {
			    board[i][n] = new King(false);
			}
			break;
		    }
		}
	    }
	}
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
	if (msg instanceof EventMessage) {
	    EventMessage event = (EventMessage) msg;
	}
    }

    public void start(String host, int port) {

    }

    public void select(Point point, Piece selector) {
	select(point.x, point.y, selector);
    }

    public void select(int x, int y, Piece selector) {
	board[x][y].possible = true;
	board[x][y].selector = selector;
    }

    public void moveChecker(int x, int y, Checker checker) {
	// Reset possible spots for the next piece to move
	Chess_Checkers.client.unselectAll();
	// Change the turn to the opposite player
	Chess_Checkers.blackTurn = !Chess_Checkers.blackTurn;
	for (JumpTree tree : checker.lastValidLocations) {
	    // Find which jumptree we're actually following
	    if (x == tree.getEndJump().getEndPoint().x && y == tree.getEndJump().getEndPoint().y) {
		ArrayList<Jump> midJumpsAL = tree.getMidJumps();
		Jump[] midJumps = new Jump[0];
		// Convert the mid jumps to an array for easier handling
		midJumps = midJumpsAL.toArray(midJumps);
		for (Jump jump : midJumps) {
		    if (jump != null) {
			// Make the piece(s) we jumped over's place(s) empty
			board[jump.getMidPoint().x][jump.getMidPoint().y] = new Empty();
		    }
		}
		// if we are jumping the piece
		if (tree.getEndJump().getMidPoint() != null) {
		    // Clear the last piece to be jumped over in the train
		    board[tree.getEndJump().getMidPoint().x][tree.getEndJump().getMidPoint().y] = new Empty();
		}
		// Find our current square in the board and make it empty
		findSelfLoop: for (int i = 0; i < 8; i++) {
		    for (int n = 0; n < 8; n++) {
			if (board[i][n].equals(checker)) {
			    board[i][n] = new Empty();
			    break findSelfLoop;
			}
		    }
		}
		// Check if we should be kinged
		if (black && y == 7) {
		    checker.kinged = true;
		} else if (!black && y == 0) {
		    checker.kinged = true;
		}
		// Actually put ourselves on the board in the new place
		board[x][y] = checker;
		// Check if the other team has any possible moves
		if (Utilities.isArrayEmpty(CheckersPiece.checkJumps(!black, true))) {
		    Chess_Checkers.gameOver = true;
		    String winner = black ? "black" : "red";
		    JOptionPane.showMessageDialog(null,
			    "Congratulations, " + winner
				    + " wins. Exit this message and click on the board to restart.",
			    "A Champion has been decided!", JOptionPane.INFORMATION_MESSAGE);
		}
		// End the search for the jumptree we took and end the method
		break;
	    }
	}
    }

    public void moveChess(int x, int y, ChessPiece piece) {
	Chess_Checkers.client.unselectAll();
	Chess_Checkers.blackTurn = !Chess_Checkers.blackTurn;
	findSelfLoop: for (int i = 0; i < 8; i++) {
	    for (int n = 0; n < 8; n++) {
		if (board[i][n].equals(piece)) {
		    board[i][n] = new Empty();
		    break findSelfLoop;
		}
	    }
	}
	if (piece instanceof King) {
	    King pieceK = (King) piece;
	    if (!pieceK.moved) {
		pieceK.moved = true;
		if (x == 2) {
		    board[3][y] = board[0][y];
		    board[0][y] = new Empty();
		} else if (x == 6) {
		    board[5][y] = board[7][y];
		    board[7][y] = new Empty();
		}
		piece = pieceK;
	    }
	} else if (piece instanceof Rook) {
	    Rook pieceR = (Rook) piece;
	    if (!pieceR.moved) {
		pieceR.moved = true;
	    }
	}
	if (piece instanceof Pawn) {
	    Pawn pieceP = (Pawn) piece;
	    ChessPiece.pawnCaptureCount = 0;
	    int yOffset = black ? -1 : 1;
	    if (board[x][y + yOffset].equals(ChessPiece.doubleMovePawn)) {
		board[x][y + yOffset] = new Empty();
	    }
	    ChessPiece.doubleMovePawn = pieceP;
	    if (y == 0 || y == 7) {
		PawnPromotion promoter = new PawnPromotion();
		String promotion = promoter.result;
		switch (promotion) {
		case "Queen":
		    piece = new Queen(black);
		    break;
		case "Rook":
		    piece = new Rook(black);
		    break;
		case "Knight":
		    piece = new Knight(black);
		    break;
		case "Bishop":
		    piece = new Bishop(black);
		    break;
		}
	    }
	} else if (board[x][y] instanceof Empty) {
	    ChessPiece.pawnCaptureCount++;
	} else {
	    ChessPiece.pawnCaptureCount = 0;
	}
	ChessPiece.doubleMovePawn = null;
	board[x][y] = piece;
	ChessPiece.endGameCheck();
    }

    public void unselectAll() {
	for (Piece[] row : board) {
	    for (Piece piece : row) {
		piece.possible = false;
		piece.selected = false;
	    }
	}
    }

    public Piece[][] getBoard() {
	return board;
    }

    public boolean getTurn() {
	return turn;
    }
}