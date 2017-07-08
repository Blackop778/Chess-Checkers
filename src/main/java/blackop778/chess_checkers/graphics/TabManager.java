package blackop778.chess_checkers.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

public class TabManager {
    private final JPanel background;
    private final JPanel tabsPanel;
    private final JPanel content;
    private HashMap<String, AbstractTab> tabs;
    private AbstractTab currentTab;

    public TabManager() {
	background = new JPanel();
	tabsPanel = new JPanel();
	content = new JPanel();
	tabs = new HashMap<String, AbstractTab>();
    }

    public void AddTab(AbstractTab tab) {
	tabs.put(tab.getName(), tab);
	JButton tabHeader = new JButton(tab.getName());
	tabHeader.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		currentTab.OnTabDeactivated(content);
	    }
	});
	tabsPanel.add(tabHeader);
	if (tabs.size() == 1) {
	    tab.OnTabActivated(content);
	    currentTab = tab;
	}
    }

    public static abstract class AbstractTab {
	public abstract void OnTabActivated(JPanel content);

	public abstract void OnTabDeactivated(JPanel content);

	public abstract String getName();
    }
}
