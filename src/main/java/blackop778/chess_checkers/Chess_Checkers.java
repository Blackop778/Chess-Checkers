package blackop778.chess_checkers;

import java.awt.Dimension;

import javax.swing.JFrame;

import blackop778.chess_checkers.graphics.Chess_CheckersFrame;
import blackop778.chess_checkers.pieces.Piece;

public class Chess_Checkers
{
	static Piece[][] board;

	public static void main(String[] args)
	{
		Chess_CheckersFrame frame = new Chess_CheckersFrame();
		frame.setTitle("Chess-Checkers");
		// frame.setSize(720, 720);
		frame.setPreferredSize(new Dimension(736, 758));
		frame.pack();
		Dimension size = frame.getContentPane().getSize();
		System.out.println(size.getWidth() + " " + size.getHeight());

		/**
		 * Dimension Dim = new Dimension(720, 720);
		 * 
		 * frame.setMaximumSize(Dim); frame.setMinimumSize(Dim);
		 * frame.setPreferredSize(Dim); frame.pack();
		 * 
		 * Dim.width = 720 + (frame.getWidth() -
		 * frame.getContentPane().getWidth()); Dim.height = 720 +
		 * (frame.getHeight() - frame.getContentPane().getHeight());
		 * 
		 * frame.setMaximumSize(Dim); frame.setMinimumSize(Dim);
		 * frame.setPreferredSize(Dim); frame.pack();
		 */
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		board = new Piece[8][8];
		for(int i = 0; i < board.length; i++)
		{
			for(int n = 0; n < board[0].length; n++)
			{
				board[i][n] = null;
			}
		}

		/**
		 * String input = JOptionPane.showInputDialog(null,
		 * "Enter 'chess' to play chess or 'checkers' to play checkers.",
		 * "Which game?", JOptionPane.QUESTION_MESSAGE); if(input == null) input
		 * = "null"; while(!input.equalsIgnoreCase("chess") &&
		 * !input.equalsIgnoreCase("checkers")) { input =
		 * JOptionPane.showInputDialog(null,
		 * "Invalid input. Enter either 'chess' or 'checkers'.", "Ohoes noes",
		 * JOptionPane.ERROR_MESSAGE); if(input == null) input = "null"; }
		 * 
		 * if(input.equalsIgnoreCase("checkers")) { for(int i = 0; i <
		 * board.length; i++) { for(int n = 0; n < board[0].length; n++) { if((i
		 * + n) % 2 == 1) { if(n < 3) { board[i][n] = new Checker(true, false);
		 * } else if(n > 4) { board[i][n] = new Checker(false, false); } } } } }
		 * else {
		 * 
		 * }
		 */
	}
}
