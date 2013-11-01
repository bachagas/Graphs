package datastructures;

//A Node representing a game configuration with a label identifier
public class Node {
	
	protected String label;
	
	public Node(String nodeLabel) {
		this.label = nodeLabel;
	}
	
	public boolean equals(Node n2) {
		return this.label.equals(n2.label);
	}
	
	public String toString() {
		return this.label;
	}
	
}
