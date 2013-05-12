package org.weipf.android.adbdump.model;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreeNodeInfo {
	
	private Map<String, String> descriptionMap = new HashMap<String, String>();
	
	private java.util.List<String> descriptionList = new ArrayList<String>();
	
	private String nodeName = null;
	
	public TreeNodeInfo(String nodeName) {
		this.nodeName = nodeName;		
	}

	public void addDescription(String description, String seprate) {
		if (description == null) {
			return;
		}
		if (seprate == null || seprate.isEmpty()) {
			seprate = "=";
		}
		String [] keyValue = description.split(seprate);
		if (!descriptionMap.containsKey(keyValue[0])) {
			descriptionMap.put(keyValue[0], keyValue[1]);
		}
	}
	
	public void addDescription(String description) {
		if (description == null) {
			return;
		}
		if (descriptionList == null) {
			descriptionList = new ArrayList<String>();
		}
		descriptionList.add(description);
	}
	
	public java.util.List<String> getDescription() {
		return descriptionList;		
	}
	@Override
	public String toString() {
		return this.nodeName;
	}
}
