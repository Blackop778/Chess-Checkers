package blackop778.chess_checkers;

public abstract class Utilities {
    public static boolean isArrayEmpty(Object[] array) {
	if (array != null) {
	    if (array.length == 0)
		return true;
	    else {
		for (Object object : array) {
		    if (object != null)
			return false;
		}

		return true;
	    }
	} else
	    return false;
    }

    public static boolean arrayContains(Object[] array, Object object) {
	if (array != null) {
	    for (Object piece : array) {
		if (piece != null) {
		    if (piece.equals(object))
			return true;
		} else {
		    if (object == null)
			return true;
		}
	    }
	}

	return false;
    }
}
