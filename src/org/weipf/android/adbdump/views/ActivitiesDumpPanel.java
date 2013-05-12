package org.weipf.android.adbdump.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.weipf.android.adbdump.model.AdbDump;
import org.weipf.android.adbdump.model.TreeNodeInfo;
import org.weipf.android.adbdump.model.AdbDump.DumpType;
import org.weipf.android.adbdump.model.DumpReader;

public class ActivitiesDumpPanel extends DumpPanel implements ActionListener{
	
	private static final String PATTERN_TREE_ROOT = "Main stack:";
	
	private static final String PATTERN_TASK_NODE = "\\W*\\* TaskRecord\\{[\\w\\W]*\\}";
	
	private static final String PATTERN_ACTIVITY_NODE = "\\W*\\* Hist #\\d{1,2}: ActivityRecord\\{[\\w\\W]*\\}";
	
	JToolBar mButtonBar = null;
	
		JButton mRefreshButton = null;
	
	JSplitPane mContentPane = null;
	
	    JTree mDumpTree = null;
		JList mNodeDetailList = null;
	
	DefaultTreeModel mDumpTreeModel = null;
	
	DefaultMutableTreeNode mTreeRoot = null;
	
	String mString = null;
	
	DumpReader mDumpReader = new DumpReader() {
		
		@Override
		public void readDump(InputStream inpuStream) {
			BufferedInputStream in = new BufferedInputStream(inpuStream);  
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));  
            StringBuilder sbBuilder = new StringBuilder();
            String tmp = null;
            DefaultMutableTreeNode root = null, taskNode = null, activityNode = null;
            TreeNodeInfo taskInfo = null, activityInfo = null;
            boolean isReadTask = false, isReadActivity = false;
            try {
				while ((tmp = inBr.readLine()) != null) {
					if (tmp.trim().isEmpty()) {
						continue;
					}
					if (tmp.contains(PATTERN_TREE_ROOT)) {
						// get root
						root = new DefaultMutableTreeNode(new TreeNodeInfo(tmp.trim()));
					} else if (tmp.matches(PATTERN_TASK_NODE)) {
						// get node
						taskInfo = new TreeNodeInfo(tmp.trim());
						taskNode = new DefaultMutableTreeNode(taskInfo);
						root.add(taskNode);
						isReadTask = true;
						isReadActivity = false;
					} else if (tmp.matches(PATTERN_ACTIVITY_NODE)) {
						// get activity node
						activityInfo = new TreeNodeInfo(tmp.trim());
						activityNode = new DefaultMutableTreeNode(activityInfo);
						taskNode.add(activityNode);
						isReadActivity = true;
						isReadTask = false;
					} else if (isReadTask) {
						// add task description into current task node
						taskInfo.addDescription(tmp.trim());
					} else if (isReadActivity) {
						// add activity description into current activity node
						activityInfo.addDescription(tmp.trim());
					}
//					sbBuilder.append(tmp + "\n");
				}
//				mString = sbBuilder.toString();
				mTreeRoot = root;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (root == null) {
            	mTreeRoot = new DefaultMutableTreeNode("safdjlkdsajf;ldsajf");
            }
		}

		@Override
		public void readErrorDump(InputStream inputStream) {
			System.out.println("ReadErrorDump/in");
			StringBuilder errorMessage = new StringBuilder();
			if (inputStream == null) {
				errorMessage.append("Unknown error!");
			}
			InputStreamReader reader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String tmp = null;
			try {
				while((tmp = bufferedReader.readLine()) != null) {
					errorMessage.append(tmp);
				}
			} catch (IOException e) {
				errorMessage.append("Cannot read error message! - " + e.getMessage());
			}
			System.out.println("ReadErrorDump/error message:" + errorMessage);
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(errorMessage);
			mTreeRoot = root;
		}
	};


	public ActivitiesDumpPanel() {
		this.setLayout(new BorderLayout());
		// tool bar		
		mRefreshButton = new JButton("Refresh");
		mRefreshButton.addActionListener(this);
		
		mButtonBar = new JToolBar();
		mButtonBar.add(mRefreshButton);
		
		this.add(mButtonBar, BorderLayout.NORTH);
		
//		TextArea ta = new TextArea();
//		AdbDump adbDump = AdbDump.getInstance();
//		adbDump.dump(DumpType.ACTIVITES, mDumpReader);
//		ta.setText(mString);
//		
//		add(ta, BorderLayout.CENTER);
		
		// content pane
		//   tree
		mDumpTree = new JTree();
		AdbDump adbDump = AdbDump.getInstance();
		adbDump.dump(DumpType.ACTIVITES, mDumpReader);
		mDumpTreeModel = new DefaultTreeModel(mTreeRoot);
		mDumpTree.setModel(mDumpTreeModel);
		mDumpTree.setExpandsSelectedPaths(true);
		
		DumpTreeEventListener listener = new DumpTreeEventListener();
		mDumpTree.addTreeSelectionListener(listener);
		JScrollPane jspTopPane = new JScrollPane(mDumpTree);
		
		//   tree node detail
		mNodeDetailList = new JList();
//		mNodeDetailPanel.setLayout(new GridLayout(0, 1)); // make a grid with x rows, 1 columns
		mNodeDetailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane bottomNodeDetailPane = new JScrollPane(mNodeDetailList);
		
		mContentPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jspTopPane, bottomNodeDetailPane);
		mContentPane.setDividerLocation(0.5);
		
		this.add(mContentPane, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event == null) {
			return;
		}
		if (event.getSource() == mRefreshButton) {
			refreshDump();
		}
	}
	
	private void refreshDump() {
		System.out.println("refreshDump/in");
		AdbDump adbDump = AdbDump.getInstance();
		adbDump.dump(DumpType.ACTIVITES, mDumpReader);
		mDumpTreeModel.setRoot(mTreeRoot);
	}
	
	private class DumpTreeEventListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			TreePath path = e.getPath();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
			TreeNodeInfo info = (TreeNodeInfo)selectedNode.getUserObject();
			List<String> descriptionList = info.getDescription();
			if (descriptionList == null || descriptionList.isEmpty()) {
				return;
			}
			mNodeDetailList.setListData(descriptionList.toArray());
		}
	}
	
}
