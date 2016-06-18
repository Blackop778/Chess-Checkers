package blackop778.chess_checkers;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import blackop778.chess_checkers.graphics.Chess_CheckersFrame;
import blackop778.chess_checkers.pieces.Checker;
import blackop778.chess_checkers.pieces.Empty;
import blackop778.chess_checkers.pieces.Piece;

public class Chess_Checkers
{
	public static Piece[][] board;

	public static boolean blackTurn;

	public static boolean gameOver;

	public static boolean gameIsCheckers;

	public static boolean offerSurrender;

	public static void main(String[] args)
	{
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

	public static void setupGame()
	{
		gameOver = false;
		board = new Piece[8][8];
		for(int i = 0; i < board.length; i++)
		{
			for(int n = 0; n < board[0].length; n++)
			{
				board[i][n] = new Empty();
			}
		}

		String input = JOptionPane.showInputDialog(null, "Enter 'chess' to play chess or 'checkers' to play checkers.",
				"Which game?", JOptionPane.QUESTION_MESSAGE);
		if(input == null)
			input = "null";
		while(!input.equalsIgnoreCase("chess") && !input.equalsIgnoreCase("checkers"))
		{
			input = JOptionPane.showInputDialog(null, "Invalid input. Enter either 'chess' or 'checkers'.",
					"Ohoes noes", JOptionPane.ERROR_MESSAGE);
			if(input == null)
				input = "null";
		}

		if(input.equalsIgnoreCase("checkers"))
		{
			gameIsCheckers = true;
			blackTurn = true;
			for(int i = 0; i < board.length; i++)
			{
				for(int n = 0; n < board[0].length; n++)
				{
					if((i + n) % 2 == 1)
					{
						if(n < 3)
						{
							board[i][n] = new Checker(true, false);
						}
						else if(n > 4)
						{
							board[i][n] = new Checker(false, false);
						}
					}
				}
			}
		}
		else
		{
			gameIsCheckers = false;
			blackTurn = false;
		}
	}

	public static void unselectAll()
	{
		for(Piece[] row : board)
		{
			for(Piece piece : row)
			{
				piece.possible = false;
				piece.selected = false;
			}
		}
	}
}
