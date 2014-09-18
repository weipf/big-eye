package org.weipf.android.adbdump;

public class Debug {

	
	public static void main(String [] args) {
	    
	    String PATTERN_TREE_ROOT = "(Main stack:|Activity stack:)";
	    String treeRoot = "Activity stack:";
		
		String PATTERN_TASK_NODE = "\\W*\\* TaskRecord\\{[\\w\\W]*\\}";
		String taskNode = "  * TaskRecord{418d93f8 #2 A com.sonyericsson.home}";
		
		String PATTERN_ACTIVITY_NODE = "\\W*\\* Hist #\\d{1,2}: ActivityRecord\\{[\\w\\W]*\\}";
		String activityNode ="     * Hist #3: ActivityRecord{4173ab20 com.sonyericsson.home/.HomeActivity}";
		String activityNode2 = "* Hist #0: HistoryRecord{b65f3e80 com.android.launcher/com.android.launcher2.Launcher}";
		
//		System.out.println(taskNode + " -> " + taskNode.matches(PATTERN_TASK_NODE));
		
//		System.out.println(activityNode + " -> " + activityNode.matches(PATTERN_ACTIVITY_NODE));
		
		System.out.println(treeRoot + " -> " + treeRoot.matches(PATTERN_TREE_ROOT));
	}
}
