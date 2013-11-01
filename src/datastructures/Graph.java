package datastructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * A graph stored as an adjacency list with all nodes and edges of the graph.
 * Provides methods for traversing the graph with depth-first search (DFS) and breadth-first search (BFS).
 */
public class Graph {

	// The main adjacency list of the graph:
	// keys of the map are the graph's nodes and its values are each a set of neighbors of that key-node.
	protected String label;
	private Map<Node, Set<Node>> graphMap;

	// Indicates graph mode: undirected (default) or directed
	private boolean undirected = true;
	
	// Quantity of nodes (n) and edges (m) of the graph
	private int nNodes = 0;
	private int mEdges = 0;

	// Creates an empty graph
	public Graph(String _label) {
		this.label = new String(_label);
		this.graphMap = new HashMap<Node, Set<Node>>();
		this.nNodes = 0;
		this.mEdges = 0;
	}

	// Creates an empty graph of estimated size, what may improve the performance of the HashMap
	public Graph( String _label, int size ) {
		this.label = new String(_label);
		this.graphMap = new HashMap<Node, Set<Node>>( size );
		this.nNodes = 0;
		this.mEdges = 0;
	}
	
	// Creates a directed empty graph
	public Graph( String _label, boolean isDirected ) {
		this.label = new String(_label);
		this.graphMap = new HashMap<Node, Set<Node>>();
		this.nNodes = 0;
		this.mEdges = 0;
		this.undirected = !isDirected;
	}
	
	// Creates a graph from a Map structure that is its adjacency list
	/*public Graph( Map<Node, Set<Node>> graphMap ) {
		this.graphMap = graphMap;
		// Setting n and m:
		this.nNodes = graphMap.size();
		Iterator<Node> it = graphMap.keySet().iterator();		
		for( Node nextNode = it.next(); nextNode != null; nextNode = it.next() ) {
			this.mEdges += graphMap.get(nextNode).size();
		}
	}*/
	
	// Adds a node to the graph, if it is not already in it
	public void addNode( Node newNode ) {
		//TODO: For some reason, it is not working as expected, and will return false if two nodes have the same id
		//      but are different objects, what would be not desirable...
		boolean contains = this.graphMap.containsKey(newNode);
		if ( !contains ) {
			this.graphMap.put(newNode, null);
			this.nNodes++;
		}
	}
	
	// Adds an edge to the graph. Creates new nodes "from" and "to" if needed.
	public void addEdge( Node fromNode, Node toNode ) {
		this.addNode(fromNode);
		Set<Node> fromNeighbors = this.graphMap.get(fromNode);
		if( fromNeighbors == null ) { // fromNode has no neighbor yet			
			fromNeighbors = new HashSet<Node>();
			this.graphMap.put(fromNode, fromNeighbors);
		}
		this.addNode(toNode);
		// Insert 'from'-'to' edge into dfsTree
		fromNeighbors.add(toNode);
		this.mEdges++;
		if (this.undirected) { // creates the reverse edge
			Set<Node> toNeighbors = this.graphMap.get(toNode);
			if( toNeighbors == null ) { // toNode has no neighbor yet
				toNeighbors = new HashSet<Node>();
				this.graphMap.put(toNode, toNeighbors);
			}
			toNeighbors.add(fromNode);
		}
	}
	
	// Prints a human-readable text representing the graph
	public void println() {
		System.out.println("Graph " + this.label + " is " + this.graphMap.toString() + " with " + this.getN() + " node(s) and " + this.getM() + " edge(s).");
	}

	// Produces a human-readable string representing the graph
	public String toString() {
		return this.label + "=" + this.graphMap.toString();
	}
	
	// Returns the graph amount of vertices in the graph
	public int getN() {
		return this.nNodes;
	}
	
	// Returns the graph amount of edges in the graph
	public int getM() {
		return this.mEdges;
	}

	// Returns a set with all the nodes of the graph
	public Set<Node> getNodes() {
		Set<Node> keySet = this.graphMap.keySet();
		return keySet;
	}
	
	// Returns a set with all the neighbors of a given node
	public Set<Node> getNeighbors(Node node) {
		Set<Node> neighbors = this.graphMap.get(node);
		return neighbors;
	}
	
	// Returns the number of connected components of the graph or -1 if the graph has never been traversed
	public int getNumberOfCCs() {
		if ( !this.visitedSet.isEmpty() ) {
			return this.ccNumber;
		} else return -1;
	}
	
	// Produces true if graph is empty	
	public boolean isEmpty() {
		Set<Node> nodeSet = this.getNodes();
		boolean empty = nodeSet.isEmpty();
		return empty;
	}

	// Produces true if graph contains a given node, false otherwise
	public boolean contains(Node node) {
		return this.graphMap.containsKey(node);
	}
	
	// ************** Traversal Algorithms structures and functions: **************
	
	// Temporary structure to control already visited nodes during a traversal algorithm (DFS and BFS)
	private Set<Node> visitedSet = null;
	
	// Count for the connected Components
	int ccNumber = -1; // only valid after the graph is traversed
	
	// Produces true if given node has already been visited in a graph traversal
	private boolean isVisited(Node node) {
		boolean contains = this.visitedSet.contains(node);
		return contains;
	}

	// Mark a given node as "visited" in a graph traversal
	private void markVisited(Node node) {
		this.visitedSet.add(node);
	}
	
	// Performs a Depth-First Search (DFS) in the graph and returns the DFS-Forest.
	// The DFS-Forest produced is as a List of other Graphs, which are the disconnected DFS-Trees produced by the traversal
	public List<Graph> dfs(Node s) {		
		//Initialize and reset traversal control structures:
		List<Graph> dfsForest = new LinkedList<Graph>();
		this.visitedSet = new HashSet<Node>();
		this.ccNumber = 0;

		// Iterate through all nodes in the graph, starting from "s"
		Set<Node> nodeSet = this.getNodes();
		Iterator<Node> it = nodeSet.iterator();
		while ( it.hasNext() ){
			Node next = null;
			if (ccNumber == 0) next = s; // this is to force start from "s"
			else next = it.next();
			//System.out.println(next);
			boolean visited = this.isVisited(next);
			if( !visited ) {
				this.ccNumber++;
				Graph dfsTree = new Graph("CC"+ccNumber, true); // Obs: DFS-Tree is directed for the purpose of clarity
				dfsVisit(next, dfsTree);
				//System.out.println(dfsTree);
				if ( !dfsTree.isEmpty() ) dfsForest.add(dfsTree);
			}
		}
		return dfsForest;
	}
	
	private void dfsVisit(Node node, Graph dfsTree) {		
		this.markVisited(node);
		
		Set<Node> neighbors = this.getNeighbors(node);
		if ( neighbors != null ) {
			Iterator<Node> it = neighbors.iterator();
			while ( it.hasNext() ) {
				Node next = it.next();
				boolean visited = this.isVisited(next);
				if( !visited ) {
					// Insert 'node'-'next' edge into dfsTree
					dfsTree.addEdge(node, next);
					
					// Visit next node
					dfsVisit(next, dfsTree);
				}
			}
		} else dfsTree.addNode(node);
	}
	
	
	// Basic unit test "check-expects":
	public static void main(String[] args) {
		System.out.println("*** Tests for a single node: ***");
		Node node1 = new Node("A");
		System.out.println(node1.toString());
		System.out.println(new Node("B").equals(node1));
		System.out.println(new Node("A").equals(node1));
		System.out.println();
		
		System.out.println("*** Tests for a simple graph example: ***");
		Graph myGraph = new Graph("G");
		myGraph.addNode(node1);
		Node node2 = new Node("B");
		Node node3 = new Node("C");
		Node node4 = new Node("D");
		myGraph.addEdge(node1, node2);
		myGraph.addEdge(node2, node3);
		myGraph.addEdge(node3, node1);
		myGraph.addEdge(node3, node4);
		Node node5 = new Node("E");
		Node node6 = new Node("F");
		myGraph.addEdge(node5, node6);
		//myGraph.addEdge(node5, new Node("F")); --> TODO: Fix the insertion of a repeated node in the graph in this case
		System.out.println(myGraph);
		myGraph.println();
		System.out.println();
		
		System.out.println("*** Tests for DFS traversal: ***");
		Set<Node> allNodes = myGraph.getNodes();
		Iterator<Node> itNodes = allNodes.iterator();
		while ( itNodes.hasNext() ){
			Node v = itNodes.next();
			List<Graph> dfsForest = myGraph.dfs(v);
			
			System.out.println("DFS starting at node " + v + " result is:");
			Iterator<Graph> it = dfsForest.iterator();
			while (it.hasNext()) {
				Graph dfsTree = it.next();
				System.out.println(dfsTree);
			}
		}
		System.out.println("Found " + myGraph.getNumberOfCCs() + " connected components.");
		System.out.println("Each one has:");
		List<Graph> dfsForest = myGraph.dfs(node5);
		Iterator<Graph> it = dfsForest.iterator();
		while (it.hasNext()) {
			Graph dfsTree = it.next();
			//System.out.println(dfsTree.getN() + " node(s) and " + dfsTree.getM() + " edge(s).");
			dfsTree.println();
		}
		System.out.println();
	}
	
}
