package classes;

import java.util.ArrayList;

public class Node {
	String Snapshot;
	ArrayList<Node> nodeChildren;
	Node parentNode;
	
	Node(String inputSnapshot){
		this.Snapshot = inputSnapshot;
		nodeChildren = new ArrayList<Node>();
		parentNode = null;
	}
	public void setParentNode( Node parent){
		this.parentNode = parent;
	}
	
}