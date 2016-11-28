package blackop778.chess_checkers.net;

public class EventMessage {
    public final String coordinate1;
    public final String coordinate2;
    public final boolean offerSurrender;

    private EventMessage(String coordinate1, String coordinate2, boolean offerSurrender) {
	this.coordinate1 = coordinate1;
	this.coordinate2 = coordinate2;
	this.offerSurrender = offerSurrender;
    }

    private EventMessage(String coordinate1, String coordinate2) {
	this(coordinate1, coordinate2, false);
    }

    public int getCoordinate1X() {
	switch (coordinate1.charAt(0)) {
	case 'A':
	    return 0;
	case 'B':
	    return 1;
	case 'C':
	    return 2;
	case 'D':
	    return 3;
	case 'E':
	    return 4;
	case 'F':
	    return 5;
	case 'G':
	    return 6;
	case 'H':
	    return 7;
	}
	throw new InvalidCoordinateException();
    }

    public int getCoordinate1Y() {
	int y = Integer.valueOf(new StringBuilder().append(coordinate1.charAt(1)).toString());
	if (y > -1 && y < 8)
	    return y;
	throw new InvalidCoordinateException();
    }

    /**
     * 
     * @param coordinate1
     * @param coordinate2
     * @return Will be null if invalid input
     */
    public static EventMessage instantiate(String coordinate1, String coordinate2) {
	if (coordinate1.matches("[A-H][1-8]") && coordinate2.matches("[A-H][1-8]")) {
	    return new EventMessage(coordinate1, coordinate2);
	}
	return null;
    }

    public static class InvalidCoordinateException extends RuntimeException {
	private static final long serialVersionUID = -8498929249310011469L;

    }
}
