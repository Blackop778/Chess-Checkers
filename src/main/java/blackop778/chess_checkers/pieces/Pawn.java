package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.chess.PawnPromotion;

public class Pawn extends ChessPiece
{
	protected Boolean lastMoveDouble;

	public Pawn(boolean black)
	{
		this.black = black;
		this.selected = false;
		this.selector = null;
		this.possible = false;
		this.lastMoveDouble = null;
	}

	@Override
	public void drawSelf(Graphics g, int x, int y)
	{
		if(possible)
		{
			g.setColor(Color.YELLOW);
			g.fillRect(x + 2, y + 2, 86, 86);
		}
		String color = black ? "Black" : "White";
		File image = new File("resources\\" + color + "Pawn.png");
		try
		{
			g.drawImage(ImageIO.read(image), x, y, null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Point[] getValidLocations(int x, int y)
	{
		ArrayList<Point> validLocations = new ArrayList<Point>();

		int yOffset = black ? 1 : -1;

		if(Chess_Checkers.board[x][y + yOffset] instanceof Empty)
		{
			Piece replacingPiece = Chess_Checkers.board[x][y + yOffset];
			Chess_Checkers.board[x][y + yOffset] = this;
			Chess_Checkers.board[x][y] = new Empty();
			if(!isKingInCheck(black))
			{
				validLocations.add(new Point(x, y + yOffset));
			}
			Chess_Checkers.board[x][y + yOffset] = replacingPiece;
			Chess_Checkers.board[x][y] = this;
		}
		if(x - 1 > -1)
		{
			if(Chess_Checkers.board[x - 1][y + yOffset] instanceof ChessPiece
					&& Chess_Checkers.board[x - 1][y + yOffset].black != black)
			{
				Piece replacingPiece = Chess_Checkers.board[x - 1][y + yOffset];
				Chess_Checkers.board[x - 1][y + yOffset] = this;
				Chess_Checkers.board[x][y] = new Empty();
				if(!isKingInCheck(black))
				{
					validLocations.add(new Point(x - 1, y + yOffset));
				}
				Chess_Checkers.board[x - 1][y + yOffset] = replacingPiece;
				Chess_Checkers.board[x][y] = this;
			}
		}
		if(x + 1 < 8)
		{
			if(Chess_Checkers.board[x + 1][y + yOffset] instanceof ChessPiece
					&& Chess_Checkers.board[x + 1][y + yOffset].black != black)
			{
				Piece replacingPiece = Chess_Checkers.board[x + 1][y + yOffset];
				Chess_Checkers.board[x + 1][y + yOffset] = this;
				Chess_Checkers.board[x][y] = new Empty();
				if(!isKingInCheck(black))
				{
					validLocations.add(new Point(x + 1, y + yOffset));
				}
				Chess_Checkers.board[x + 1][y + yOffset] = replacingPiece;
				Chess_Checkers.board[x][y] = this;
			}

		}
		if(lastMoveDouble == null)
		{
			if(Chess_Checkers.board[x][y + yOffset] instanceof Empty
					&& Chess_Checkers.board[x][y + (yOffset * 2)] instanceof Empty)
			{
				Piece replacingPiece = Chess_Checkers.board[x][y + (yOffset * 2)];
				Chess_Checkers.board[x][y + (yOffset * 2)] = this;
				Chess_Checkers.board[x][y] = new Empty();
				if(!isKingInCheck(black))
				{
					validLocations.add(new Point(x, y + (yOffset * 2)));
				}
				Chess_Checkers.board[x][y + (yOffset * 2)] = replacingPiece;
				Chess_Checkers.board[x][y] = this;
			}
		}
		Integer sides = canEnPassantCapture(x, y);
		if(sides != null)
		{
			validLocations.add(new Point(x + sides, y + yOffset));
		}

		Point[] array = new Point[0];
		validLocations.trimToSize();
		return validLocations.toArray(array);
	}

	public Integer canEnPassantCapture(int x, int y)
	{
		// null means no captures possible, negative means left side, positive
		// means right side
		Integer sides = null;
		int yOffset = black ? 1 : -1;

		if(x + 1 < 8)
		{
			if(Chess_Checkers.board[x + 1][y] instanceof Pawn && Chess_Checkers.board[x + 1][y].black != black)
			{
				if(Chess_Checkers.board[x + 1][y + yOffset] instanceof Empty)
				{
					Pawn pawn = (Pawn) Chess_Checkers.board[x + 1][y];
					if(pawn.lastMoveDouble == true)
					{
						sides = new Integer(1);
					}
				}
			}
		}
		if(x - 1 > -1)
		{
			if(Chess_Checkers.board[x - 1][y] instanceof Pawn && Chess_Checkers.board[x - 1][y].black != black)
			{
				if(Chess_Checkers.board[x - 1][y + yOffset] instanceof Empty)
				{
					Pawn pawn = (Pawn) Chess_Checkers.board[x - 1][y];
					if(pawn.lastMoveDouble == true)
					{
						sides = new Integer(-1);
					}
				}
			}
		}

		return sides;
	}

	@Override
	public void move(int x, int y)
	{
		Chess_Checkers.unselectAll();
		Chess_Checkers.blackTurn = !Chess_Checkers.blackTurn;
		pawnCaptureCount = 0;
		if(doubleMovePawn != null)
		{
			Pawn pawn = (Pawn) Chess_Checkers.board[doubleMovePawn.x][doubleMovePawn.y];
			pawn.lastMoveDouble = false;
			doubleMovePawn = null;
		}
		if(Chess_Checkers.board[x][y] instanceof Empty)
		{
			if(black)
			{
				if(Chess_Checkers.board[x][y - 1] instanceof Pawn && Chess_Checkers.board[x][y - 1].black != black)
				{
					Chess_Checkers.board[x][y - 1] = new Empty();
				}
			}
			else
			{
				if(Chess_Checkers.board[x][y + 1] instanceof Pawn && Chess_Checkers.board[x][y + 1].black != black)
				{
					Chess_Checkers.board[x][y + 1] = new Empty();
				}
			}
		}
		findSelfLoop: for(int i = 0; i < 8; i++)
		{
			for(int n = 0; n < 8; n++)
			{
				if(Chess_Checkers.board[i][n].equals(this))
				{
					Chess_Checkers.board[i][n] = new Empty();
					if(n - y == 2 || y - n == 2)
					{
						lastMoveDouble = new Boolean(true);
						doubleMovePawn = new Point(x, y);
					}
					else
					{
						lastMoveDouble = new Boolean(false);
					}
					break findSelfLoop;
				}
			}
		}
		if(y != 8 && y != 0)
			Chess_Checkers.board[x][y] = this;
		else
		{
			PawnPromotion promoter = new PawnPromotion();
			String promotion = promoter.result;
			switch(promotion)
			{
				case "Queen":
					Chess_Checkers.board[x][y] = new Queen(black);
					break;
				case "Rook":
					Chess_Checkers.board[x][y] = new Rook(black);
					break;
				case "Knight":
					Chess_Checkers.board[x][y] = new Knight(black);
					break;
				case "Bishop":
					Chess_Checkers.board[x][y] = new Bishop(black);
					break;
			}
		}
		endGameCheck();
	}
}
