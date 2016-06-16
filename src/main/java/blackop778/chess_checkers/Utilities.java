package blackop778.chess_checkers;

public abstract class Utilities
{
	public static boolean isArrayEmpty(Object[] array)
	{
		if(array.length == 0)
			return true;
		else
		{
			for(Object object : array)
			{
				if(object != null)
					return false;
			}

			return true;
		}
	}
}
