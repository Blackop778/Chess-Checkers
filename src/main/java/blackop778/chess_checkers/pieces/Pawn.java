package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import blackop778.chess_checkers.Chess_Checkers;

public class Pawn extends ChessPiece
{
	private boolean moved;

	public Pawn(boolean black)
	{
		this.black = black;
		this.selected = false;
		this.selector = null;
		this.possible = false;
		this.moved = false;
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
			if((black && !blackKingInCheck) || (!black && !whiteKingInCheck))
			{
				validLocations.add(new Point(x, y + yOffset));
			}
			else
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
		}

		Point[] array = new Point[0];
		return validLocations.toArray(array);
	}

	@Override
	public void select(int x, int y)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void move(int x, int y)
	{
		// TODO Auto-generated method stub

	}
}
