package datastructures;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/*
 * A Graph stored as an adjacency list with all nodes and edges of the graph.
 * Provides methods for traversing the graph with depth-first search (DFS) and breadth-first search (BFS).
 */
public class Graph {

	// Data definitions:
    // =================
	protected String label; // a name for the graph
	
	// The main adjacency list of the graph:
	// keys of the map are the graph's nodes and its values are each a set of neighbors of that key-node. 
	private Map<Node, Set<Node>> graphMap;
	private Map<String, Node> nodesMap; //auxiliary structure to check and prevent nodes with same Id's

	// Indicates graph mode: undirected (default) or directed
	private boolean undirected = true;
	
	// Quantity of nodes (n) and edges (m) of the graph
	private int nNodes = 0;
	private int mEdges = 0;

    // Functions:
    // ==========
	// Creates an empty graph
	public Graph(String _label) {
		this.label = new String(_label);
		this.graphMap = new HashMap<Node, Set<Node>>();
		this.nodesMap = new HashMap<String, Node>();
		this.nNodes = 0;
		this.mEdges = 0;
	}

	// Creates an empty graph of estimated size, what may improve the performance of the HashMap
	public Graph( String _label, int size ) {
		this.label = new String(_label);
		this.graphMap = new HashMap<Node, Set<Node>>( size );
		this.nodesMap = new HashMap<String, Node>( size );
		this.nNodes = 0;
		this.mEdges = 0;
	}
	
	// Creates a directed empty graph
	public Graph( String _label, boolean directed ) {
		this.label = new String(_label);
		this.graphMap = new HashMap<Node, Set<Node>>();
		this.nodesMap = new HashMap<String, Node>();
		this.nNodes = 0;
		this.mEdges = 0;
		this.undirected = !directed;
	}
	
	// Adds a node to the graph, if it is not already in it. Checks for duplicate nodes by its id and 
	// returns new node added, or existent if there was already a node with the same id in the graph.
	public Node addNode( Node newNode ) {
		if ( !this.contains(newNode) ) {
			this.graphMap.put(newNode, new HashSet<Node>());
			this.nodesMap.put(newNode.getId(), newNode);
			this.nNodes++;
			return newNode;
		}
		else return this.nodesMap.get(newNode.getId());
	}
	
	// Adds an edge to the graph, if it is not already in it; ignores new edge otherwise.
	// Creates new nodes "from" and "to" if needed.
	public void addEdge( Node fromNode, Node toNode ) {
		if ( !this.contains(fromNode,toNode) ) {
			fromNode = this.addNode(fromNode);
			toNode = this.addNode(toNode);
			Set<Node> fromNeighbors = this.graphMap.get(fromNode);
			if ( !fromNeighbors.contains(toNode) ) {
				fromNeighbors.add(toNode);
				this.mEdges++;
				if (this.undirected) { // creates also the reverse edge for undirected graphs
					Set<Node> toNeighbors = this.graphMap.get(toNode);
					toNeighbors.add(fromNode);
				}
			}
		}
	}
	
	// Prints a human-readable text representing the graph
	public void println() {
		System.out.println(this.stringfy());
	}

	// Produces an human-readable string with global useful information about the graph
	public String toString() {
		return this.getLabel() + "={" + this.getN() + " node(s)," + this.getM() + " edge(s)}";
	}
	
	// Produces a human-readable string representing the graph
	public String stringfy() {
		return this.label + "=" + this.graphMap.toString();
	}
	
	// Returns the label of the graph
	public String getLabel() {
		return this.label;
	}
	
	// Returns the graph amount of vertices in the graph
	public int getN() {
		return this.nNodes;
	}
	
	// Returns the graph amount of edges in the graph
	public int getM() {
		return this.mEdges;
	}

	// Returns an specific node of the graph by its id or null if not existent 
	public Node getNodeById(String id) {
			return nodesMap.get(id);
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

	// Produces true if graph contains a given node, false otherwise
	public boolean contains(Node aNode) {
		return this.nodesMap.containsKey(aNode.getId());
	}
	
	// Produces true if graph contains given edge, false otherwise
	private boolean contains(Node fromNode, Node toNode) {
		if ( this.nodesMap.containsKey(fromNode.getId()) ) {
			fromNode = this.nodesMap.get(fromNode.getId());
			if ( this.nodesMap.containsKey(toNode.getId()) ) {
				toNode = this.nodesMap.get(toNode.getId());
				Set<Node> neighbors = this.graphMap.get(fromNode);
				if ( neighbors != null ) return neighbors.contains(toNode);
				else return false;
			} else return false;
		} else return false;
	}
	
	// Traversal Algorithms - Functions and auxiliary structures:
	// ==========================================================
	// Temporary structure to control already visited nodes during a traversal algorithm (DFS and BFS)
	private Set<Node> visitedSet = null;
	
	// Connected Components structures
	int ccNumber = -1; // only valid after the graph is traversed by a complete DFS or BFS
	private Vector<Graph> CCs = null;
	
	// Returns the number of connected components of the graph computed by a complete traversal or -1 if the graph has never been traversed
	public int getNumberOfCCs() {
		if ( !this.visitedSet.isEmpty() ) {
			return this.ccNumber+1;
		} else return -1;
	}
	
	// Returns the connected components of the graph computed by a complete traversal (null if the graph has never been traversed)
	public Vector<Graph> getCCs() {
		return this.CCs;
	}
	
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
		if (s == null || !this.contains(s)) return null;
		//Initialize and reset traversal control structures:
		List<Graph> dfsForest = new LinkedList<Graph>();
		this.visitedSet = new HashSet<Node>();
		this.ccNumber = -1;
		this.CCs = new Vector<Graph>();

		// Iterate through all nodes in the graph, starting from "s"
		for (Iterator<Node> it = this.getNodes().iterator(); it.hasNext();  ) {
			Node next = null;
			if (ccNumber == -1) next = s; // this is to force start from "s"
			else next = it.next();
			boolean visited = this.isVisited(next);
			if( !visited ) {
				// Initialize a new Connected Component
				this.ccNumber++;
				this.CCs.add(new Graph("CC"+ccNumber, !this.undirected)); // Note: Connected components are sub-graphs of original graph and of the same type as it 
				Graph dfsTree = new Graph("dfsTree"+(this.ccNumber+1), true); // Note: DFS-Tree is directed for the purpose of clarity only
				dfsVisit(next, dfsTree);
				dfsForest.add(dfsTree);
			}
		}
		return dfsForest;
	}
	
	private void dfsVisit(Node node, Graph dfsTree) {		
		this.markVisited(node);
		dfsTree.addNode(node);
		for (Iterator<Node> neighbors = this.getNeighbors(node).iterator(); neighbors.hasNext();  ) {
			Node next = neighbors.next();
			// Always insert edge 'node'-'next' into current Connected Component
			this.CCs.get(this.ccNumber).addEdge(node,next);
			boolean visited = this.isVisited(next);
			if( !visited ) {
				// Insert edge 'node'-'next' into dfsTree
				dfsTree.addEdge(node, next);
				
				// Visit next node
				dfsVisit(next, dfsTree);
			}
		}
	}
	
	// Performs a Breadth-First Search (BFS) in the graph and returns the BFS-Forest.
	// The BFS-Forest produced is as a List of other Graphs, which are the disconnected BFS-Trees produced by the traversal
	public List<Graph> bfs(Node s) {
		if (s == null || !this.contains(s)) return null;
		//Initialize and reset traversal control structures:
		List<Graph> bfsForest = new LinkedList<Graph>();
		this.visitedSet = new HashSet<Node>();
		this.ccNumber = -1;
		this.CCs = new Vector<Graph>();
		Deque<Node> queue = new LinkedList<Node>();

		// Iterate through all nodes in the graph, starting from "s"
		for (Iterator<Node> it = this.getNodes().iterator(); it.hasNext();  ) {
			Node next = null;
			if (ccNumber == -1) next = s; // this is to force to start from "s"
			else next = it.next();
			boolean visited = isVisited(next);
			if( !visited ) {
				this.markVisited(next);
				queue.add(next);
				// Initialize a new Connected Component
				this.ccNumber++;
				this.CCs.add(new Graph("CC"+ccNumber, !this.undirected)); // Note: Connected components are sub-graphs of original graph and of the same type as it
				Graph bfsTree = new Graph("bfsTree"+(ccNumber+1), true); // Note: BFS-Tree is directed for the purpose of clarity only	
				while ( !queue.isEmpty() ) {
					Node u = queue.remove();
					bfsTree.addNode(u);
					for (Iterator<Node> neighbors = this.getNeighbors(u).iterator(); neighbors.hasNext();  ) {
						Node v = neighbors.next();
						// Always insert edge 'u'-'v' into current Connected Component
						this.CCs.get(this.ccNumber).addEdge(u,v);
						boolean explored = isVisited(v);
						if( !explored ) {
							// Insert edge 'u'-'v' into bfsTree
							bfsTree.addEdge(u, v);
							this.markVisited(v);
							queue.add(v);
						}
					}
				}
				bfsForest.add(bfsTree);
			}
		}
		return bfsForest;
	}

	// Finds the minimum path between nodes start and end performing a Breadth-First Search (BFS).
	// Returns a List of Nodes which are the path, or null if there is no path between the two given nodes.
	// Obs: The size of the List produced - 1 will be the distance between the two nodes.
	public LinkedList<Node> bfsPath(Node start, Node end) {
		if (start == null || !this.contains(start) || end == null || !this.contains(end)) return null;
		//Initialize and reset traversal control structures:
		this.visitedSet = new HashSet<Node>();
		Map<Node,Node> fathers = new HashMap<Node,Node>();
		Deque<Node> queue = new LinkedList<Node>();
		boolean found = false;

		this.markVisited(start);
		fathers.put(start,null);
		queue.add(start);	
		while ( !queue.isEmpty() && !found ) {
			Node u = queue.remove();
			if ( u == end ) found = true;
			else
				for (Iterator<Node> neighbors = this.getNeighbors(u).iterator(); neighbors.hasNext();  ) {
					Node v = neighbors.next();
					if( !isVisited(v) ) {
						fathers.put(v,u);
						this.markVisited(v);
						queue.add(v);
					}
				}
		}
		if ( found ) { // builds the result list going backwards through node parents list
			LinkedList<Node> result = new LinkedList<Node>();
			Node node = end;
			while ( node != null ) {
				result.push(node);
				node = fathers.get(node);
			}
			return result;
		} else return null;
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
		//Should not replicate nodes and edges:
		myGraph.addNode(new Node("F"));
		myGraph.addEdge(node5, node6);
		myGraph.addEdge(node5, new Node("F"));
		System.out.println(myGraph);
		myGraph.println();
		System.out.println();
		
		System.out.println("*** Tests for DFS traversal: ***");
		Set<Node> allNodes = myGraph.getNodes();
		for (Iterator<Node> it = allNodes.iterator(); it.hasNext(); ) { 
			Node v = it.next();
			List<Graph> dfsForest = myGraph.dfs(v);
			
			System.out.println("DFS starting at node " + v + " result is:");
			for (Iterator<Graph> it2 = dfsForest.iterator(); it2.hasNext();  ) {
				Graph dfsTree = it2.next();
				dfsTree.println();
			}
		}
		System.out.println("Found " + myGraph.getNumberOfCCs() + " connected components:");
		for (Iterator<Graph> it = myGraph.getCCs().iterator(); it.hasNext();  ) {
			Graph cc = it.next();
			System.out.println(cc);
			cc.println();
		}
		System.out.println();
		
		System.out.println("*** Tests for BFS traversal: ***");
		for (Iterator<Node> it = allNodes.iterator(); it.hasNext(); ) {
			Node v = it.next();
			List<Graph> bfsForest = myGraph.bfs(v);
			
			System.out.println("BFS starting at node " + v + " result is:");
			for (Iterator<Graph> it2 = bfsForest.iterator(); it2.hasNext();  ) {
				Graph bfsTree = it2.next();
				bfsTree.println();
			}
		}
		System.out.println("Found " + myGraph.getNumberOfCCs() + " connected components:");
		for (Iterator<Graph> it = myGraph.getCCs().iterator(); it.hasNext();  ) {
			Graph cc = it.next();
			System.out.println(cc);
			cc.println();
		}
		System.out.println();
		
		System.out.println("*** Tests for Path finding and distance through BFS: ***");
		for (Iterator<Node> it1 = allNodes.iterator(); it1.hasNext(); ) {
			Node u = it1.next();
			for (Iterator<Node> it2 = allNodes.iterator(); it2.hasNext(); ) {
				Node v = it2.next();
				List<Node> path = myGraph.bfsPath(u,v);
				if ( path == null ) {
					System.out.println("Node " + u + " cannot reach " + v + "!");
				} else {
					System.out.println("Distance from " + u + " to " + v + " is " + (path.size()-1) + ":");
					for (Iterator<Node> it3 = path.iterator(); it3.hasNext(); ) {
						Node tempNode = it3.next();
						System.out.println(tempNode);
					}
				}
			}
		}
		System.out.println();
	}
	
}
