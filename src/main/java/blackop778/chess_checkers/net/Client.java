package blackop778.chess_checkers.net;

import blackop778.chess_checkers.chess.SnapshotStorage;
import blackop778.chess_checkers.pieces.Bishop;
import blackop778.chess_checkers.pieces.Checker;
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

    public boolean isTurn() {
	return turn;
    }
}