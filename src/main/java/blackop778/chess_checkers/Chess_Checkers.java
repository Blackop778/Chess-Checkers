package blackop778.chess_checkers;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import blackop778.chess_checkers.graphics.Chess_CheckersFrame;
import blackop778.chess_checkers.net.Client;
import blackop778.chess_checkers.net.Server;

public abstract class Chess_Checkers {
    public static Client client;
    public static boolean offerSurrender;
    public static boolean gameOver = false;
    public static String gameType;

    public static void main(String[] args) {
	setupGame();

	Chess_CheckersFrame frame = new Chess_CheckersFrame();
	frame.setTitle("Chess-Checkers");
	frame.setPreferredSize(new Dimension(727, 794));
	frame.pack();
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }

    public static void setupGame() {
	offerSurrender = false;

	String gameType = JOptionPane.showInputDialog(null,
		"Enter 'chess' to play chess or 'checkers' to play checkers.", "Which game?",
		JOptionPane.QUESTION_MESSAGE);
	if (gameType == null)
	    System.exit(0);
	while (!gameType.equalsIgnoreCase("chess") && !gameType.equalsIgnoreCase("checkers")) {
	    gameType = JOptionPane.showInputDialog(null, "Invalid gameType. Enter either 'chess' or 'checkers'.",
		    "Ohoes noes", JOptionPane.ERROR_MESSAGE);
	    if (gameType == null)
		gameType = "null";
	}
	Thread serverT = new Thread(new Runnable() {

	    @Override
	    public void run() {
		Server server = new Server(false, !Chess_Checkers.gameType.equalsIgnoreCase("checkers"));
		server.startServer(778);
	    }
	});
	client = new Client(false, true);
	client.start("127.0.0.1", 778);

    }
}
