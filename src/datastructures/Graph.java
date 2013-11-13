package datastructures;

import java.util.Collection;
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
	protected Graph( String _label, int size ) {
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
		if ( !this.containsNode(newNode) ) {
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
		if ( !this.containsEdge(fromNode,toNode) ) {
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
	public boolean containsNode(Node aNode) {
		return this.nodesMap.containsKey(aNode.getId());
	}
	
	// Produces true if graph contains given edge, false otherwise
	private boolean containsEdge(Node fromNode, Node toNode) {
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
	
	// Traversal Algorithms - Methods and auxiliary structures:
	// ==========================================================
	// Temporary structure to control already visited nodes during a traversal algorithm (DFS and BFS)
	private Set<Node> visitedSet = null;
	
	/*
	 *  Pre and Post control structures (used by DFS)
	 */
	
	// Maps nodes by their id to their pre value.
	private Map<String, Integer> preMap = null;
	// Stores the next pre value to be assigned to a visited node by DFS
	private int nextPreValue = 0;
	// Stores the lowest value between the pre value of a node u and the pre value of an ancestor w of u and there is a back-edge from some descendant of u to w.
	// Used by DFS to determine articulation nodes
	private Map<String, Integer> lowMap = null;
	//TODO private Map<String, Integer> postMap = null;
	
	
	/*
	 *  Articulation nodes set (used by DFS)
	 */
	private Set<Node> articulationNodes = null;
	
	
	/*
	 *  Connected Components structures (used by DFS)
	 */
	int ccNumber = -1; // only valid after the graph is traversed by a complete DFS or BFS
	private Vector<Graph> CCs = null;
	
	
	/*
	 *  Parents map (used by BFS and DFS)
	 *  Keys are the children and each value is their corresponding parent in a DFS or BFS tree.
	 *  Used to determine shortest paths in BFS and to determine articulation points in DFS.
	 */
	Map<Node,Node> parentsMap = null;
	
	
	// Returns the number of connected components of the graph computed by a complete DFS traversal or -1 if the graph has never been traversed by DFS
	public int getNumberOfCCs() {
		if ( !this.visitedSet.isEmpty() ) {
			return this.ccNumber+1;
		} else return -1;
	}
	
	// Returns the connected components of the graph computed by a complete DFS traversal (null if the graph has never been traversed by DFS)
	public Vector<Graph> getCCs() {
		return this.CCs;
	}
	
	// Returns the number of articulation nodes of the graph computed by a complete DFS traversal (-1 if the graph has never been traversed by DFS)
	public int getNumberOfArticulationNodes() {
		int nArtNodes = -1;
		if( this.articulationNodes != null ) {
			nArtNodes = this.articulationNodes.size();
		}
		
		return nArtNodes;
	}
	
	// Returns the set of articulation nodes of the graph computed by a complete DFS traversal (null if the graph has never been traversed by DFS)
	public Set<Node> getArticulationNodes() {
		return this.articulationNodes;
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
	
	private void setPreValue(Node node) {
		String id = node.getId();
		this.preMap.put(id, this.nextPreValue);
		this.lowMap.put(id, this.nextPreValue);
		this.nextPreValue++;
	}
	
	private int getPreValue(Node node) {
		String id = node.getId();
		int preValue = this.preMap.get(id);
		return preValue;
	}
	
	private boolean isDFSRoot(Node node) {
		// A node in a DFS tree is only a root iff it doesn't have a parent
		Node parent = this.parentsMap.get(node);
		return (parent == null);
	}
	
	/* 
	 * Used in DFS to determine articulation nodes
	 * TODO: O(n)?
	 */
	private boolean hasMoreThanOneTreeChild(Node node) {
		Collection<Node> parents = this.parentsMap.values();
		
		// Iterate through the parents' collection. If it finds 'node' more than once, return true; false otherwise.
		// In other words, if 'node' is parent to 2 or more other nodes, it has more than one child.
		Node parent = null;
		int childrenCount = 0;
		for( Iterator<Node> itParents = parents.iterator(); itParents.hasNext(); ) {
			parent = itParents.next();
			String parentId = parent.getId();
			String nodeId = node.getId();
			if( parentId.equals(nodeId) ) {
				childrenCount++;
				if( childrenCount > 1 ) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Performs a Depth-First Search (DFS) in the graph and returns the DFS-Forest.
	// The DFS-Forest produced is as a List of other Graphs, which are the disconnected DFS-Trees produced by the traversal
	public List<Graph> dfs(Node start) {
		if (start == null || !this.containsNode(start)) return null;
		//Initialize and reset traversal control structures:
		List<Graph> dfsForest = new LinkedList<Graph>();
		this.visitedSet = new HashSet<Node>();
		this.preMap = new HashMap<String, Integer>();
		this.lowMap = new HashMap<String, Integer>();
		this.articulationNodes = new HashSet<Node>();
		this.nextPreValue = 1;
		//this.postMap = new HashMap<String, Integer>();
		this.parentsMap = new HashMap<Node, Node>();
		this.ccNumber = -1;
		this.CCs = new Vector<Graph>();

		// Iterate through all nodes in the graph, starting from "s"
		for (Iterator<Node> it = this.getNodes().iterator(); it.hasNext();  ) {
			Node next = null;
			if (ccNumber == -1) next = start; // this is to force start from "s"
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
		
		/************* Articulation nodes ****************/
		/*
		this.articulationNodes = new HashSet<Node>();
		Graph tree = null;
		for( Iterator<Graph> itForest = dfsForest.iterator(); itForest.hasNext();  ) {
			tree = itForest.next();
			Set<Node> treeNodes = tree.getNodes();
			Node treeNode = null;
			for( Iterator<Node> itTree = treeNodes.iterator(); itTree.hasNext(); ) {
				treeNode = itTree.next();
				
				if( this.isDFSRoot(treeNode) && this.hasMoreThanOneTreeChild(treeNode) ) {
					// If treeNode is root and has more than one child, then it is an articulation node.
					// See references/articulation-points-or-cut-vertices-in-a-graph.pdf
					this.articulationNodes.add(treeNode);
				}
			}
		}
		*/
		/***************************************************/
		
		return dfsForest;
	}
	
	private void dfsVisit(Node node, Graph dfsTree) {		
		this.markVisited(node);
		this.setPreValue(node);
		dfsTree.addNode(node);
		
		for (Iterator<Node> neighbors = this.getNeighbors(node).iterator(); neighbors.hasNext();  ) {
			Node next = neighbors.next();
			// Always insert edge 'node'-'next' into current Connected Component
			this.CCs.get(this.ccNumber).addEdge(node,next);
			boolean visited = this.isVisited(next);
			
			String nodeId = node.getId();
			String nextId = next.getId();
			if( !visited ) {
				// Insert edge 'node'-'next' into dfsTree
				dfsTree.addEdge(node, next);
				
				//Add node as next's parent to the parents map
				this.parentsMap.put(next, node);
				
				// Visit next node
				dfsVisit(next, dfsTree);
				
				/************* Articulation nodes ****************/
				// node == u
				// next == v
				// See references/articulation-points-or-cut-vertices-in-a-graph.pdf
				
				int lowU = this.lowMap.get(nodeId);
				int lowV = this.lowMap.get(nextId); 
				int lowest = Math.min(lowU, lowV);
				this.lowMap.put(nodeId, lowest);
				
				if( this.isDFSRoot(node) && this.hasMoreThanOneTreeChild(node) ) {
					// If treeNode is root and has more than one child, then it is an articulation node.
					// See references/articulation-points-or-cut-vertices-in-a-graph.pdf
					this.articulationNodes.add(node);
				}
				
				int preValueU = this.getPreValue(node);
				if( !this.isDFSRoot(node) && lowV >= preValueU ) {
					this.articulationNodes.add(node);
				}
				/*************************************************/
			}
			else {
				Node parentU = this.parentsMap.get(node);
				if( parentU != null ) {
					String parentUId = parentU.getId();
					if( !nextId.equals(parentUId) ) {
						int lowU = this.lowMap.get(nodeId);
						int preValueV = this.getPreValue(next);
						int lowest = Math.min(lowU, preValueV);
						this.lowMap.put(nodeId, lowest);
					}
				}
			}
		}
	}
	
	// Performs a Breadth-First Search (BFS) in the graph and returns the BFS-Tree levels in the form of a vector:
	// Each level[i] is the set of nodes at distance "i" from node "start".
	public Vector<Set<Node>> bfs(Node start) {
		if (start == null || !this.containsNode(start)) return null;
		//Initialize and reset traversal control structures:
		this.visitedSet = new HashSet<Node>();
		this.parentsMap = new HashMap<Node,Node>();
		Map<Node,Integer> distances = new HashMap<Node,Integer>();
		Vector<Set<Node>> bfsLevels = new Vector<Set<Node>>();
		Deque<Node> queue = new LinkedList<Node>();
		Set<Node> level = null;

		// Iterates through all nodes in the graph, starting from "s"
		for (Iterator<Node> it = this.getNodes().iterator(); it.hasNext();  ) {
			Node next = null;
			if (level == null) next = start; // this is to force start from "s"
			else break;                      // may stop if finds another connected component that is not reachable from node "s"
			boolean visited = isVisited(next);
			if( !visited ) {
				this.markVisited(next);
				queue.add(next);
				// Initial distance and first level of BFS tree
				distances.put(next,0);
				level = new HashSet<Node>();
				level.add(next);
				bfsLevels.add(level);
				while ( !queue.isEmpty() ) {
					Node u = queue.remove();
					for (Iterator<Node> neighbors = this.getNeighbors(u).iterator(); neighbors.hasNext();  ) {
						Node v = neighbors.next();
						boolean explored = isVisited(v);
						if( !explored ) {
							this.parentsMap.put(v,u); // v's parent is u
							distances.put(v,distances.get(u)+1);
							if (bfsLevels.size()<distances.get(v)+1) bfsLevels.add(new HashSet<Node>()); // initializes a new level
							level = bfsLevels.get(distances.get(v));
							level.add(v);
							this.markVisited(v);
							queue.add(v);
						}
					}
				}
			}
		}
		return bfsLevels;
	}
	
	// Returns the shortest path from node "start" to node "end"; null if any node doesn't belong to the graph
	// or "end" it is not reachable from "start"
	public LinkedList<Node> getShortestPath(Node start, Node end) {
		if (start == null || !this.containsNode(start) ||     // node "start" doesn't belong to the graph
				end == null || !this.containsNode(end) )      // node "end" doesn't belong to the graph
			return null;
		if (this.parentsMap == null ||                                 // BFS has not been executed yet 
				((start != end)&&!this.parentsMap.containsKey(end)) )  // or it has not started from node "start"
			this.bfs(start);                                        // Note: if node "end" is not reachable from node "start" will be checked at the end!
		// Builds the result list going backwards through node parents list
		LinkedList<Node> result = new LinkedList<Node>();
		Node node = end;
		while ( node != null ) {
			result.push(node);
			node = this.parentsMap.get(node);
		}
		if ( result.getFirst() != start) return null; // there is no path from "start" to "end" --> node "end" is not reachable from node "start"!
		else return result;
	}
	
}
