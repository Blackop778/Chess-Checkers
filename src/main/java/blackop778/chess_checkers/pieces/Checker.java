package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.checkers.Jump;
import blackop778.chess_checkers.checkers.JumpTree;

public class Checker extends CheckersPiece
{
	public Checker(boolean black, boolean kinged)
	{
		this.black = black;
		this.kinged = kinged;
		this.selected = false;
		this.possible = false;
	}

	@Override
	public void drawSelf(Graphics g, int x, int y)
	{
		if(possible)
		{
			g.setColor(Color.YELLOW);
			g.fillRect(x + 2, y + 2, 86, 86);
		}
		if(black)
		{
			g.setColor(Color.BLACK);
		}
		else
		{
			g.setColor(Color.RED);
		}
		g.fillOval(x, y, 90, 90);
	}

	@Override
	public JumpTree[] getValidLocations(int x, int y)
	{
		ArrayList<JumpTree> trees = new ArrayList<JumpTree>();

		JumpTree[] treeArray = new JumpTree[0];
		trees.trimToSize();
		return trees.toArray(treeArray);
	}

	@Override
	public void select(int x, int y)
	{
		selected = true;
		JumpTree[] jumps = getValidLocations(x, y);
		for(JumpTree tree : jumps)
		{
			if(tree != null)
			{
				Chess_Checkers.board[tree.getEndJump().getEndPoint().x][tree.getEndJump()
						.getEndPoint().y].possible = true;
			}
		}
	}

	@Override
	public void move(int x, int y)
	{
		Chess_Checkers.unselectAll();
	}

	@Override
	public Jump[] getJumpablePlaces(int x, int y)
	{
		ArrayList<Jump> places = new ArrayList<Jump>();
		if((black || kinged) && y != 7)
		{
			if(Chess_Checkers.board[x - 1][y + 1].black != black
					&& !(Chess_Checkers.board[x - 1][y + 1] instanceof Empty))
			{
				if(Chess_Checkers.board[x - 2][y + 2] instanceof Empty)
				{
					places.add(new Jump(new Point(x - 1, y + 1), new Point(x - 2, y + 2), JumpTree.SW));
				}
			}
			if(Chess_Checkers.board[x + 1][y + 1].black != black
					&& !(Chess_Checkers.board[x + 1][y + 1] instanceof Empty))
			{
				if(Chess_Checkers.board[x + 2][y + 2] instanceof Empty)
				{
					places.add(new Jump(new Point(x + 1, y + 1), new Point(x + 2, y + 2), JumpTree.SE));
				}
			}
		}
		if((!black || kinged) && y != 0)
		{
			if(Chess_Checkers.board[x - 1][y - 1].black != black
					&& !(Chess_Checkers.board[x - 1][y - 1] instanceof Empty))
			{
				if(Chess_Checkers.board[x - 2][y - 2] instanceof Empty)
				{
					places.add(new Jump(new Point(x - 1, y - 1), new Point(x - 2, y - 2), JumpTree.NW));
				}
			}
			if(Chess_Checkers.board[x + 1][y - 1].black != black
					&& !(Chess_Checkers.board[x + 1][y - 1] instanceof Empty))
			{
				if(Chess_Checkers.board[x + 2][y - 2] instanceof Empty)
				{
					places.add(new Jump(new Point(x + 1, y - 1), new Point(x + 2, y - 2), JumpTree.NE));
				}
			}
		}

		Jump[] toReturn = new Jump[0];
		places.trimToSize();
		return places.toArray(toReturn);
	}

	@Override
	public Jump[] getMoveablePlaces(int x, int y)
	{
		ArrayList<Jump> places = new ArrayList<Jump>();
		if((black || kinged) && y != 7)
		{
			if(Chess_Checkers.board[x - 1][y + 1] instanceof Empty)
			{
				places.add(new Jump(new Point(x - 1, y + 1), JumpTree.SW));
			}
			if(Chess_Checkers.board[x + 1][y + 1] instanceof Empty)
			{
				places.add(new Jump(new Point(x + 1, y + 1), JumpTree.SE));
			}
		}
		if((!black || kinged) && y != 0)
		{
			if(Chess_Checkers.board[x - 1][y - 1] instanceof Empty)
			{
				places.add(new Jump(new Point(x - 1, y - 1), JumpTree.NW));
			}
			if(Chess_Checkers.board[x + 1][y - 1] instanceof Empty)
			{
				places.add(new Jump(new Point(x + 1, y - 1), JumpTree.NE));
			}
		}

		Jump[] toReturn = new Jump[0];
		places.trimToSize();
		return places.toArray(toReturn);
	}
}
