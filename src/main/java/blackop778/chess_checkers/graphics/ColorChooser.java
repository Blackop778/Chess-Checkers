package blackop778.chess_checkers.graphics;

import javax.swing.JDialog;

public class ColorChooser extends JDialog {
    private static final long serialVersionUID = -2937891388510429098L;
    private boolean black;

    public ColorChooser() {

    }

    public boolean getBlack() {
	return new Boolean(black);
    }
}
