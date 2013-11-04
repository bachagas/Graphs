package datastructures;

//A Node representing a game configuration with a label identifier
public class Node {
	
	protected String id;
	
	public String getId() {
		return this.id;
	}
	
	public Node(String nodeLabel) {
		this.id = nodeLabel;
	}
	
	public boolean equals(Object anObject) {
		Node temp = (Node) anObject;
		return this.id.equals(temp.getId());
	}
	
	public String toString() {
		return this.id;
	}
	
}
