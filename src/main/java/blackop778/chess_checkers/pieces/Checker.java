package blackop778.chess_checkers.pieces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;
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
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.RED);
		g.fillOval(x, y, 90, 90);
	}

	@Override
	public JumpTree[] getValidLocations(int x, int y)
	{
		ArrayList<JumpTree> trees = new ArrayList<JumpTree>();
		JumpTree[] treeArray = {new JumpTree()};

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
				Chess_Checkers.board[tree.getEndPoint().x][tree.getEndPoint().y].possible = true;
		}
	}

	@Override
	public void move(int x, int y)
	{
		Chess_Checkers.unselectAll();
	}

	@Override
	public boolean canJumpPiece(int x, int y)
	{
		if((black || kinged) && y != 7)
		{
			if(Chess_Checkers.board[x - 1][y + 1].black != black
					&& !(Chess_Checkers.board[x - 1][y + 1] instanceof Piece))
			{
				if(Chess_Checkers.board[x - 2][y + 2] instanceof Empty)
					return true;
			}
			if(Chess_Checkers.board[x + 1][y + 1].black != black
					&& !(Chess_Checkers.board[x + 1][y + 1] instanceof Piece))
			{
				if(Chess_Checkers.board[x + 2][y + 2] instanceof Empty)
					return true;
			}
		}
		if((!black || kinged) && y != 0)
		{
			if(Chess_Checkers.board[x - 1][y - 1].black != black
					&& !(Chess_Checkers.board[x - 1][y - 1] instanceof Piece))
			{
				if(Chess_Checkers.board[x - 2][y - 2] instanceof Empty)
					return true;
			}
			if(Chess_Checkers.board[x + 1][y - 1].black != black
					&& !(Chess_Checkers.board[x + 1][y - 1] instanceof Piece))
			{
				if(Chess_Checkers.board[x + 2][y - 2] instanceof Empty)
					return true;
			}
		}

		return false;
	}

	@Override
	public boolean canMovePiece(int x, int y)
	{
		if((black || kinged) && y != 7)
		{
			if(Chess_Checkers.board[x - 1][y + 1] instanceof Empty)
				return true;
			if(Chess_Checkers.board[x + 1][y + 1] instanceof Empty)
				return true;
		}
		if((!black || kinged) && y != 0)
		{
			if(Chess_Checkers.board[x - 1][y - 1] instanceof Empty)
				return true;
			if(Chess_Checkers.board[x + 1][y - 1] instanceof Empty)
				return true;
		}

		return false;
	}

	@Override
	public JumpTree extendJumpTree(JumpTree tree, int x, int y)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
