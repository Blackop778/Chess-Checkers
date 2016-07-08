package blackop778.chess_checkers;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import blackop778.chess_checkers.graphics.Chess_CheckersFrame;
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

public abstract class Chess_Checkers
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
		offerSurrender = false;
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
			System.exit(0);
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
							board[i][n] = new Checker(true, n + (i * 8));
						}
						else if(n > 4)
						{
							board[i][n] = new Checker(false, n + (i * 8));
						}
					}
				}
			}
		}
		else
		{
			ChessPiece.doubleMovePawn = null;
			ChessPiece.pawnCaptureCount = 0;
			gameIsCheckers = false;
			blackTurn = false;
			for(int i = 0; i < board.length; i++)
			{
				for(int n = 0; n < board[0].length; n++)
				{
					switch(n)
					{
						case 0:
							if(i == 0 || i == 7)
							{
								board[i][n] = new Rook(true);
							}
							else if(i == 1 || i == 6)
							{
								board[i][n] = new Knight(true);
							}
							else if(i == 2 || i == 5)
							{
								board[i][n] = new Bishop(true);
							}
							else if(i == 3)
							{
								board[i][n] = new Queen(true);
							}
							else if(i == 4)
							{
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
							if(i == 0 || i == 7)
							{
								board[i][n] = new Rook(false);
							}
							else if(i == 1 || i == 6)
							{
								board[i][n] = new Knight(false);
							}
							else if(i == 2 || i == 5)
							{
								board[i][n] = new Bishop(false);
							}
							else if(i == 3)
							{
								board[i][n] = new Queen(false);
							}
							else if(i == 4)
							{
								board[i][n] = new King(false);
							}
							break;
					}
				}
			}
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
