package org.weipf.android.adbdump;

import org.weipf.android.adbdump.views.MainPanel;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		
		this.setLayout(new BorderLayout());
		
		add(new MainPanel());
		
		this.setTitle("Adb dump");
		this.setBounds(50, 50, Contants.DEFAULT_FRAME_WIDTH, Contants.DEFAULT_FRAME_HEIGHT);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String [] args) {
		new MainFrame();
	}
}
