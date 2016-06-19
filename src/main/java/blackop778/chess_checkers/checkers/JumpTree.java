package blackop778.chess_checkers.checkers;

import java.util.ArrayList;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.pieces.Checker;

public class JumpTree
{
	private Jump endJump;
	private ArrayList<Jump> midJumps;
	private ArrayList<Integer> previousUIDs;

	public JumpTree()
	{
		endJump = null;
		midJumps = new ArrayList<Jump>();
		previousUIDs = new ArrayList<Integer>();
	}

	public JumpTree(JumpTree tree)
	{
		endJump = tree.getEndJump();
		midJumps = tree.getMidJumps();
		previousUIDs = tree.previousUIDs;
	}

	public Jump getEndJump()
	{
		if(endJump != null)
			return new Jump(endJump);
		else
			return null;
	}

	@Override
	public void finalize()
	{
		if(endJump == null)
		{
			midJumps.trimToSize();
			Jump last = midJumps.get(midJumps.size() - 1);
			midJumps.remove(midJumps.size() - 1);
			endJump = last;
		}
	}

	public ArrayList<Jump> getMidJumps()
	{
		midJumps.trimToSize();
		return new ArrayList<Jump>(midJumps);
	}

	public void addMidJump(Jump jump)
	{
		midJumps.add(jump);
	}

	public void trimMidJumps()
	{
		midJumps.trimToSize();
	}

	public static JumpTree[] makeTree(Checker checker, int x, int y)
	{
		JumpTree[] trees = new JumpTree[0];

		if(!Utilities.isArrayEmpty(checker.getJumpablePlaces(x, y, null)))
		{
			Jump[] places = checker.getJumpablePlaces(x, y, null);
			trees = extendArray(trees, places.length);
			for(int i = 0; i < places.length; i++)
			{
				trees[i] = new JumpTree();
				trees[i].addMidJump(new Jump(places[i].getMidPoint(), places[i].getEndPoint()));
				trees[i].previousUIDs
						.add(Chess_Checkers.board[places[i].getMidPoint().x][places[i].getMidPoint().y].UID);
			}

			trees = continueTree(trees, checker);
		}
		else if(!Utilities.isArrayEmpty(checker.getMoveablePlaces(x, y)))
		{
			Jump[] places = checker.getMoveablePlaces(x, y);
			trees = extendArray(trees, places.length);
			for(int i = 0; i < places.length; i++)
			{
				trees[i] = new JumpTree();
				trees[i].addMidJump(new Jump(places[i].getEndPoint()));
				trees[i].finalize();
			}
		}

		return trees;
	}

	private static JumpTree[] continueTree(JumpTree[] tree, Checker owner)
	{
		boolean done = true;
		int length = tree.length;
		Jump[] places;
		for(int i = 0; i < length; i++)
		{
			if(tree[i].getEndJump() == null)
			{
				tree[i].trimMidJumps();
				places = owner.getJumpablePlaces(
						tree[i].getMidJumps().get(tree[i].getMidJumps().size() - 1).getEndPoint().x,
						tree[i].getMidJumps().get(tree[i].getMidJumps().size() - 1).getEndPoint().y,
						tree[i].previousUIDs);
				if(Utilities.isArrayEmpty(places))
				{
					tree[i].finalize();
				}
				else
				{
					done = false;
					if(places.length > 1)
						tree = extendArray(tree, places.length - 1);

					for(int n = 1; n < places.length; n++)
					{
						tree[tree.length - places.length + n] = new JumpTree(tree[i]);
						tree[tree.length - places.length + n].addMidJump(places[n]);
						tree[tree.length - places.length + n].previousUIDs
								.add(Chess_Checkers.board[places[n].getMidPoint().x][places[n].getMidPoint().y].UID);
					}
					tree[i].addMidJump(places[0]);
					tree[i].previousUIDs
							.add(Chess_Checkers.board[places[0].getMidPoint().x][places[0].getMidPoint().y].UID);
				}
			}
		}

		if(done)
			return tree;
		else
			return continueTree(tree, owner);
	}

	static JumpTree[] extendArray(JumpTree[] source, int timesToExtend)
	{
		JumpTree[] toReturn = new JumpTree[source.length + timesToExtend];
		for(int i = 0; i < toReturn.length; i++)
		{
			if(i < source.length)
			{
				toReturn[i] = source[i];
			}
			else
			{
				toReturn[i] = null;
			}
		}

		return toReturn;
	}
}
