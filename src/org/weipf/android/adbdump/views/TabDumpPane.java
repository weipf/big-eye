package org.weipf.android.adbdump.views;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public class TabDumpPane extends JTabbedPane{

	public TabDumpPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		
		addTab("Activities", new ActivitiesDumpPanel());
		
		addTab("Services", new JLabel("Comming soon..."));
		
		addTab("Processes", new JLabel("Comming soon..."));
		
		addTab("MemInfo", new JLabel("Comming soon..."));
	}
	
}
