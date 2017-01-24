package blackop778.chess_checkers.net;

import java.awt.Point;
import java.net.SocketAddress;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.checkers.Jump;
import blackop778.chess_checkers.checkers.JumpTree;
import blackop778.chess_checkers.chess.PawnPromotion;
import blackop778.chess_checkers.chess.PawnPromotion.Promotion;
import blackop778.chess_checkers.chess.SnapshotStorage;
import blackop778.chess_checkers.net.Message.CheckersMessage;
import blackop778.chess_checkers.net.Message.ChessMessage;
import blackop778.chess_checkers.net.Message.PawnPromotionMessage;
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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Client {

    private Piece[][] board;
    public final boolean black;
    public final boolean gameIsCheckers;
    private boolean turn;
    private ChannelHandlerContext context;
    protected final boolean localServer;

    public Client(boolean black, boolean gameIsCheckers, boolean localServer) {
	this.black = black;
	this.gameIsCheckers = gameIsCheckers;
	this.localServer = localServer;

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

    public class ClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
	    if (msg instanceof Message) {
		turn = true;
		if (msg instanceof ChessMessage) {
		    ChessMessage event = (ChessMessage) msg;
		    Point p1 = Message.ChessNotationToPoint(event.coordinate1);
		    Point p2 = Message.ChessNotationToPoint(event.coordinate2);
		    int x1 = p1.x;
		    int y1 = p1.y;
		    int x2 = p2.x;
		    int y2 = p2.y;
		    if (board[x1][y1] instanceof Pawn) {
			if (Math.abs(x2 - x1) == 1 && board[x2][y2] instanceof Empty) {
			    // Check En passant capturing
			    board[x2][y1] = new Empty();
			}
		    }
		    board[x2][y2] = board[x1][y1];
		    board[x1][y1] = new Empty();
		    if (board[x2][y2] instanceof Pawn) {
			ChessPiece.pawnCaptureCount = 0;
			if (Math.abs(y2 - y1) == 2) {
			    if (y1 == 1 || y1 == 6) {
				ChessPiece.doubleMovePawn = (Pawn) board[x2][y2];
			    }
			} else if (event instanceof PawnPromotionMessage) {
			    PawnPromotionMessage ppm = (PawnPromotionMessage) event;
			    switch (ppm.promo) {
			    case Queen:
				board[x2][y2] = new Queen(!black);
				break;
			    case Knight:
				board[x2][y2] = new Knight(!black);
				break;
			    case Rook:
				board[x2][y2] = new Rook(!black);
				break;
			    case Bishop:
				board[x2][y2] = new Bishop(!black);
				break;
			    }
			}
			// Castling
		    } else if (board[x2][y2] instanceof King) {
			King pieceK = (King) board[x2][y2];
			if (Math.abs(x2 - x1) == 2 && y1 == y2) {
			    if (!pieceK.moved) {
				boolean right = x2 - x1 == 2;
				Rook pieceR = (Rook) board[right ? 7 : 1][y1];
				if (!pieceR.moved) {
				    board[right ? 5 : 3][y1] = board[right ? 7 : 0][y1];
				    board[right ? 7 : 0][y1] = new Empty();
				}
			    }
			}
			pieceK.moved = true;
		    } else if (board[x2][y2] instanceof Rook) {
			Rook pieceR = (Rook) board[x2][y2];
			pieceR.moved = true;
		    }
		} else if (msg instanceof CheckersMessage) {
		    CheckersMessage event = (CheckersMessage) msg;
		    Point p = Message.ChessNotationToPoint(event.coordinate1);
		    int x = p.x;
		    int y = p.y;
		    for (Jump j : event.tree.getMidJumps()) {
			if (j.getMidPoint() != null) {
			    board[j.getMidPoint().x][j.getMidPoint().y] = new Empty();
			}
		    }
		    if (event.tree.getEndJump().getMidPoint() != null) {
			board[event.tree.getEndJump().getMidPoint().x][event.tree.getEndJump()
				.getMidPoint().y] = new Empty();
		    }
		    if ((event.tree.getEndJump().getEndPoint().y == 0 && !board[x][y].black)
			    || (event.tree.getEndJump().getEndPoint().y == 7 && board[x][y].black)) {
			Checker c = (Checker) board[x][y];
			c.kinged = true;
		    }
		    board[event.tree.getEndJump().getEndPoint().x][event.tree.getEndJump()
			    .getEndPoint().y] = board[x][y];
		    board[x][y] = new Empty();
		}
	    }
	    if (Chess_Checkers.panel != null) {
		Chess_Checkers.panel.repaint();
	    }
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
	    context = ctx;
	}
    }

    public void start(EventLoopGroup group, SocketAddress local) {
	if (localServer) {
	    try {
		Bootstrap b = new Bootstrap();
		b.group(group).channel(LocalChannel.class).handler(new ChannelInitializer<LocalChannel>() {
		    @Override
		    public void initChannel(LocalChannel ch) throws Exception {
			ChannelPipeline p = ch.pipeline();
			p.addLast(new LoggingHandler(LogLevel.ERROR), new ClientHandler());
		    }
		});

		// Start the client.
		ChannelFuture future = b.connect(local).sync();

		// Start GUI
		Chess_Checkers.startGUI();

		// Wait until the connection is closed.
		future.channel().closeFuture().sync();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } finally {
		// Shut down the event loop to terminate all threads.
		group.shutdownGracefully();
	    }
	}
    }

    private void passTurn(Message m) {
	turn = false;
	context.writeAndFlush(m);
	if (localServer) {
	    Client t = Chess_Checkers.client;
	    Chess_Checkers.client = Chess_Checkers.clientPartner;
	    Chess_Checkers.clientPartner = t;
	}
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
		int i;
		int n = 0;
		findSelfLoop: for (i = 0; i < 8; i++) {
		    for (n = 0; n < 8; n++) {
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
		passTurn(CheckersMessage.instantiate(Message.pointToChessNotation(i, n), tree, false));
		// End the search for the jumptree we took and end the method
		break;
	    }
	}
    }

    public void moveChess(int x, int y, ChessPiece piece) {
	Chess_Checkers.client.unselectAll();
	int i;
	int n = 0;
	findSelfLoop: for (i = 0; i < 8; i++) {
	    for (n = 0; n < 8; n++) {
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
	    }
	} else if (piece instanceof Rook) {
	    Rook pieceR = (Rook) piece;
	    if (!pieceR.moved) {
		pieceR.moved = true;
	    }
	}

	ChessMessage message = null;

	if (piece instanceof Pawn) {
	    Pawn pieceP = (Pawn) piece;
	    ChessPiece.pawnCaptureCount = 0;
	    int yOffset = black ? -1 : 1;
	    // Check En passant capturing
	    if (board[x][y + yOffset].equals(ChessPiece.doubleMovePawn)) {
		board[x][y + yOffset] = new Empty();
	    }
	    ChessPiece.doubleMovePawn = pieceP;
	    if (y == 0 || y == 7) {
		PawnPromotion promoter = new PawnPromotion();
		Promotion promotion = promoter.result;
		switch (promotion) {
		case Queen:
		    piece = new Queen(black);
		    break;
		case Knight:
		    piece = new Knight(black);
		    break;
		case Rook:
		    piece = new Rook(black);
		    break;
		case Bishop:
		    piece = new Bishop(black);
		    break;
		}

		message = PawnPromotionMessage.instantiate(Message.pointToChessNotation(i, n),
			Message.pointToChessNotation(x, y), false, promotion);
	    }
	} else {
	    if (board[x][y] instanceof Empty) {
		ChessPiece.pawnCaptureCount++;
	    } else {
		ChessPiece.pawnCaptureCount = 0;
	    }
	    ChessPiece.doubleMovePawn = null;
	}
	board[x][y] = piece;
	ChessPiece.endGameCheck();

	passTurn((message == null) ? ChessMessage.instantiate(Message.pointToChessNotation(i, n),
		Message.pointToChessNotation(x, y), false) : message);
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