package blackop778.chess_checkers.checkers;

import blackop778.chess_checkers.pieces.Checker;

public abstract class JumpTreeFactory
{
	static JumpTree[] extendArray(JumpTree[] source, int timesToExtend)
	{
		JumpTree[] toReturn = new JumpTree[source.length + timesToExtend];
		for(int i = 0; i < toReturn.length; i++)
		{
			if(i < source.length)
				toReturn[i] = source[i];
			else
				toReturn[i] = null;
		}

		return toReturn;
	}

	static JumpTree[] factorize(Checker checker, int x, int y)
	{
		JumpTree[] trees = new JumpTree[1];

		if(checker.canJumpPiece(x, y) || checker.canMovePiece(x, y))
		{

		}
		else
		{
			trees[0] = null;
		}

		return trees;
	}

}
