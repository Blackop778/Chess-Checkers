package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;
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
		if(kinged)
		{
			g.setColor(Color.WHITE);
			g.fillRect(x + 15, y + 50, 60, 15);
			for(int i = 0; i < 3; i++)
			{
				g.fillPolygon(new int[] {x + 15 + (i * 20), x + 25 + (i * 20), x + 35 + (i * 20)},
						new int[] {y + 50, y + 20, y + 50}, 3);
				g.fillOval(x + 20 + (i * 20), y + 20, 10, 10);
			}
		}
	}

	@Override
	public JumpTree[] getValidLocations(int x, int y)
	{
		JumpTree[] treeArray = JumpTree.makeTree(this, x, y);
		return treeArray;
	}

	@Override
	public void select(int x, int y)
	{
		if(black == Chess_Checkers.blackTurn)
		{
			Chess_Checkers.unselectAll();
			boolean successful = false;
			JumpTree[] jumps = getValidLocations(x, y);
			lastValidLocations = jumps;
			for(JumpTree tree : jumps)
			{
				if(tree != null)
				{
					successful = true;
					Chess_Checkers.board[tree.getEndJump().getEndPoint().x][tree.getEndJump()
							.getEndPoint().y].possible = true;
					Chess_Checkers.board[tree.getEndJump().getEndPoint().x][tree.getEndJump()
							.getEndPoint().y].selector = this;
				}
			}

			if(successful)
				selected = true;
		}
	}

	@Override
	public void move(int x, int y)
	{
		Chess_Checkers.unselectAll();
		Chess_Checkers.blackTurn = Utilities.opposite(Chess_Checkers.blackTurn);
		for(JumpTree tree : lastValidLocations)
		{
			if(x == tree.getEndJump().getEndPoint().x && y == tree.getEndJump().getEndPoint().y)
			{
				ArrayList<Jump> midJumpsAL = tree.getMidJumps();
				Jump[] midJumps = new Jump[0];
				midJumps = midJumpsAL.toArray(midJumps);
				for(Jump jump : midJumps)
				{
					if(jump != null)
						Chess_Checkers.board[jump.getMidPoint().x][jump.getMidPoint().y] = new Empty();
				}
				if(tree.getEndJump().getMidPoint() != null)
				{
					Chess_Checkers.board[tree.getEndJump().getMidPoint().x][tree.getEndJump()
							.getMidPoint().y] = new Empty();
				}
				outerLoop: for(int i = 0; i < 8; i++)
				{
					for(int n = 0; n < 8; n++)
					{
						if(Chess_Checkers.board[i][n].equals(this))
						{
							Chess_Checkers.board[i][n] = new Empty();
							break outerLoop;

						}
					}
				}
				if(black && y == 7)
					kinged = true;
				else if(!black && y == 0)
					kinged = true;
				Chess_Checkers.board[x][y] = this;
				break;
			}
		}
	}

	@Override
	public Jump[] getJumpablePlaces(int x, int y)
	{
		return getJumpablePlaces(x, y, JumpTree.NONE);
	}

	public Jump[] getJumpablePlaces(int x, int y, int previousDirection)
	{
		ArrayList<Jump> places = new ArrayList<Jump>();

		if((black || kinged) && y < 6)
		{
			if(x > 1)
			{
				if(Chess_Checkers.board[x - 1][y + 1].black != black
						&& !(Chess_Checkers.board[x - 1][y + 1] instanceof Empty))
				{
					if(Chess_Checkers.board[x - 2][y + 2] instanceof Empty)
					{
						if(Math.abs(previousDirection - JumpTree.SW) != 4)
							places.add(new Jump(new Point(x - 1, y + 1), new Point(x - 2, y + 2), JumpTree.SW));
					}
				}
			}
			if(x < 6)
			{
				if(Chess_Checkers.board[x + 1][y + 1].black != black
						&& !(Chess_Checkers.board[x + 1][y + 1] instanceof Empty))
				{
					if(Chess_Checkers.board[x + 2][y + 2] instanceof Empty)
					{
						if(Math.abs(previousDirection - JumpTree.SE) != 4)
							places.add(new Jump(new Point(x + 1, y + 1), new Point(x + 2, y + 2), JumpTree.SE));
					}
				}
			}
		}
		if((!black || kinged) && y > 1)
		{
			if(x > 1)
			{
				if(Chess_Checkers.board[x - 1][y - 1].black != black
						&& !(Chess_Checkers.board[x - 1][y - 1] instanceof Empty))
				{
					if(Chess_Checkers.board[x - 2][y - 2] instanceof Empty)
					{
						if(Math.abs(previousDirection - JumpTree.NW) != 4)
							places.add(new Jump(new Point(x - 1, y - 1), new Point(x - 2, y - 2), JumpTree.NW));
					}
				}
			}
			if(x < 6)
			{
				if(Chess_Checkers.board[x + 1][y - 1].black != black
						&& !(Chess_Checkers.board[x + 1][y - 1] instanceof Empty))
				{
					if(Chess_Checkers.board[x + 2][y - 2] instanceof Empty)
					{
						if(Math.abs(previousDirection - JumpTree.NE) != 4)
							places.add(new Jump(new Point(x + 1, y - 1), new Point(x + 2, y - 2), JumpTree.NE));
					}
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
			if(x > 0)
			{
				if(Chess_Checkers.board[x - 1][y + 1] instanceof Empty)
				{
					places.add(new Jump(new Point(x - 1, y + 1), JumpTree.SW));
				}
			}
			if(x < 7)
			{
				if(Chess_Checkers.board[x + 1][y + 1] instanceof Empty)
				{
					places.add(new Jump(new Point(x + 1, y + 1), JumpTree.SE));
				}
			}
		}
		if((!black || kinged) && y != 0)
		{
			if(x > 0)
			{
				if(Chess_Checkers.board[x - 1][y - 1] instanceof Empty)
				{
					places.add(new Jump(new Point(x - 1, y - 1), JumpTree.NW));
				}
			}
			if(x < 7)
			{
				if(Chess_Checkers.board[x + 1][y - 1] instanceof Empty)
				{
					places.add(new Jump(new Point(x + 1, y - 1), JumpTree.NE));
				}
			}
		}

		Jump[] toReturn = new Jump[0];
		if(!Utilities.isArrayEmpty(CheckersPiece.checkJumps(black)))
			places.clear();
		places.trimToSize();
		return places.toArray(toReturn);
	}
}