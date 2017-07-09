package blackop778.chess_checkers.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import blackop778.chess_checkers.Chess_Checkers;

public class TabManager {
    private final JPanel background;
    private final JPanel tabsPanel;
    private final JPanel content;
    private HashMap<String, AbstractTab> tabs;
    private AbstractTab currentTab;
    private final JFrame frame;

    public TabManager(JPanel superBackground, JFrame frame) {
	this.frame = frame;
	background = new JPanel();
	background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
	tabsPanel = new JPanel();
	background.add(tabsPanel);
	content = new JPanel();
	background.add(content);
	tabs = new HashMap<>();
    }

    public void addTab(AbstractTab tab) {
	tabs.put(tab.getName(), tab);
	JButton tabHeader = new JButton(tab.getName());
	tabHeader.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		currentTab.onTabDeactivated(content);
		Object source = event.getSource();
		((AbstractTab) source).onTabActivated(content);
		frame.pack();
	    }
	});
	tabsPanel.add(tabHeader);
	if (tabs.size() == 1) {
	    Chess_Checkers.debugLog("Activating tab");
	    tab.onTabActivated(content);
	    currentTab = tab;
	    frame.pack();
	}
    }

    public static abstract class AbstractTab {
	public abstract void onTabActivated(JPanel content);

	public abstract void onTabDeactivated(JPanel content);

	public abstract String getName();
    }
}
