package test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import datastructures.Graph;
import datastructures.Node;

public class GraphTest {
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
			
			System.out.println("Articulation nodes:");
			Set<Node> articulationNodes = myGraph.getArticulationNodes();
			if( articulationNodes.isEmpty() ) {
				System.out.println("There are no articulation nodes.");
			}
			else {
				System.out.print("{");
				for( Iterator<Node> itNodes = articulationNodes.iterator(); itNodes.hasNext(); ) {
					Node next = itNodes.next();
					System.out.print(next.getId() + ", ");
				}
				System.out.println("}");
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
			Vector<Set<Node>> bfsLevels = myGraph.bfs(v);
			System.out.println("BFS starting at node " + v + " result is:");
			for (Iterator<Set<Node>> it2 = bfsLevels.iterator(); it2.hasNext();  ) {
				System.out.println(it2.next());
			}
			
			for (Iterator<Node> it2 = allNodes.iterator(); it2.hasNext(); ) {
				Node u = it2.next();
				List<Node> path = myGraph.getShortestPath(v,u);
				if ( path == null ) {
					System.out.println("Node " + v + " cannot reach " + u + "!");
				} else {
					System.out.println("Distance from " + v + " to " + u + " is " + (path.size()-1) + ":");
					for (Iterator<Node> it3 = path.iterator(); it3.hasNext(); ) {
						Node tempNode = it3.next();
						System.out.println(tempNode);
					}
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
