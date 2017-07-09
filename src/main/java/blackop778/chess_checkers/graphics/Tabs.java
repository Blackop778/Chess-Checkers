package blackop778.chess_checkers.graphics;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import blackop778.chess_checkers.graphics.TabManager.AbstractTab;

public abstract class Tabs {
    public static class MoveLogs extends AbstractTab {

	private final JScrollPane textZone;
	private final JTextArea moves;

	public MoveLogs() {
	    textZone = new JScrollPane();
	    moves = new JTextArea();

	    moves.setEditable(false);

	    textZone.setViewportView(moves);
	    textZone.setPreferredSize(new Dimension(140, 720));
	}

	@Override
	public void onTabActivated(JPanel content) {
	    content.add(textZone);
	}

	@Override
	public void onTabDeactivated(JPanel content) {
	    content.remove(textZone);
	}

	@Override
	public String getName() {
	    return "Move Logs";
	}

	public void setText(String text) {
	    moves.setText(text);
	}

    }

    public static class Graveyard extends AbstractTab {

	@Override
	public void onTabActivated(JPanel content) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onTabDeactivated(JPanel content) {
	    // TODO Auto-generated method stub

	}

	@Override
	public String getName() {
	    // TODO Auto-generated method stub
	    return null;
	}

    }

    public static class Chat extends AbstractTab {

	@Override
	public void onTabActivated(JPanel content) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onTabDeactivated(JPanel content) {
	    // TODO Auto-generated method stub

	}

	@Override
	public String getName() {
	    // TODO Auto-generated method stub
	    return null;
	}

    }
}
