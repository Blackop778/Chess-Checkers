package blackop778.chess_checkers.checkers;

import java.util.ArrayList;

import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.pieces.Checker;

public class JumpTree
{
	private Jump endJump;
	private ArrayList<Jump> midJumps;
	private int direction;

	// The values for direction
	public static final int NONE = 0;
	public static final int NE = 2;
	public static final int SE = 4;
	public static final int SW = 6;
	public static final int NW = 8;

	public JumpTree()
	{
		endJump = null;
		midJumps = new ArrayList<Jump>();
		direction = NONE;
	}

	public Jump getEndJump()
	{
		return new Jump(endJump);
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

	public void setDirection(int direction)
	{
		if(direction == NE || direction == SE || direction == SW || direction == NW)
		{
			this.direction = direction;
		}
		else
		{
			this.direction = NONE;
		}
	}

	public int getDirection()
	{
		return new Integer(direction);
	}

	static JumpTree[] makeTree(Checker checker, int x, int y)
	{
		JumpTree[] trees = new JumpTree[0];

		if(!Utilities.isArrayEmpty(checker.getJumpablePlaces(x, y)))
		{
			Jump[] places = checker.getJumpablePlaces(x, y);
			trees = extendArray(trees, places.length);
			for(int i = 0; i < places.length; i++)
			{
				trees[i] = new JumpTree();
				trees[i].addMidJump(new Jump(places[i].getEndPoint()));
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
						tree[i].getMidJumps().get(tree[i].getMidJumps().size() - 1).getEndPoint().y);
				if(Utilities.isArrayEmpty(places))
				{
					tree[i].finalize();
				}
				else
				{
					done = false;
					if(places.length > 1)
						for(int n = 0; n < places.length; n++)
						{

						}
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
