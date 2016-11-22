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

    /**
     * 
     * @param coordinate1
     * @param coordinate2
     * @return Will be null if invalid input
     */
    public static EventMessage instantiate(String coordinate1, String coordinate2) {
	if (coordinate1.matches("[A-G][1-8]") && coordinate2.matches("[A-G][1-8]")) {
	    return new EventMessage(coordinate1, coordinate2);
	}
	return null;
    }
}
