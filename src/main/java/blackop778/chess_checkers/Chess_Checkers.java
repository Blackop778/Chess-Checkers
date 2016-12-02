package blackop778.chess_checkers;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import blackop778.chess_checkers.graphics.Chess_CheckersFrame;
import blackop778.chess_checkers.net.Client;

public abstract class Chess_Checkers {
    public static Client client;
    public static boolean offerSurrender;
    public static boolean gameOver = false;

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

	String input = JOptionPane.showInputDialog(null, "Enter 'chess' to play chess or 'checkers' to play checkers.",
		"Which game?", JOptionPane.QUESTION_MESSAGE);
	if (input == null)
	    System.exit(0);
	while (!input.equalsIgnoreCase("chess") && !input.equalsIgnoreCase("checkers")) {
	    input = JOptionPane.showInputDialog(null, "Invalid input. Enter either 'chess' or 'checkers'.",
		    "Ohoes noes", JOptionPane.ERROR_MESSAGE);
	    if (input == null)
		input = "null";
	}

	client = new Client(false, input.equalsIgnoreCase("checkers"));
    }
}
