package test;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import puzzle.Board;
import puzzle.Game;
import datastructures.Graph;
import datastructures.Node;

public class GameTest {

	// Main tasks:
	public static void main(String[] args) {
		System.out.println("************************* Start **************************");
		Date startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Start");
		//Delay - for time measurement testing only:
		for (long i=0;i<2000000000;i++) if (i==100000) System.out.print("Testing for time measurement...");
		System.out.println("Done!");
		Date endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		// Testing for permutations generation:
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Testing permutations");
		Set<String> permutations = util.Math.permutate("0123",0);
		System.out.println("All possible permutations of \"0123\" are:");
		List<String> temp = new Vector<String>(util.Math.factorial(4));
		for (Iterator<String> it = permutations.iterator(); it.hasNext(); )
			temp.add(it.next());
		endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		Collections.sort(temp);
		System.out.println(temp);
		System.out.println();
		
		GameTest.assignmentTasks();
	}
	
	// Executes tasks of the assignment, according to the document references/TrabalhoPAA-2013-2-Grafos.pdf
	public static void assignmentTasks() {
		// Generating graph:
		System.out.println("******************* Building Game Graph ******************");
		Date startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Building graph:");
		Set<String> permutations = util.Math.permutate("012345678",0);
		int estimatedSize = util.Math.factorial(Game.N_TILES);
		System.out.println("Must have 9!=" + estimatedSize + " permutations, generated " + permutations.size() + " board configurations");
		System.out.print("Building a game graph with them all... ");
		Game game = new Game("Game");
		game.populate(permutations);
		System.out.println("Done!");
		Date endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		System.out.println(game);
		//game.println();
		System.out.println();
		
		// Tarefa 1:
		System.out.println("************************ Tarefa 1 ************************");
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Looking for connected components by DFS:");
		System.out.print("Performing DFS... "); // Note: Use -Xss1g to increase JVM stack size to avoid stack overflow errors (use test/GameTest.bat)
		game.dfs(game.getNodeById("123456780"));
		System.out.println("Done!");
		endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		System.out.println("Found " + game.getNumberOfCCs() + " connected components:");
		//Iterator<Graph> CCs = game.getCCs().iterator();
		//while (CCs.hasNext()) {
		for (Iterator<Graph> CCs = game.getCCs().iterator(); CCs.hasNext(); )
			System.out.println(CCs.next());
		System.out.println();
		
		// Tarefa 2:
		System.out.println("************************ Tarefa 2 ************************");
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Looking for maximum distance to solution by BFS:");
		// This is to eliminate half of the graph, since solution can only be reached from its own connected component:
		Graph halfGame = null;
		Board solution = (Board) game.getNodeById(Board.SOLUTION);
		for (Iterator<Graph> CCs = game.getCCs().iterator(); CCs.hasNext(); ) {
			halfGame = CCs.next();
			if ( halfGame.containsNode(solution) ) {
				System.out.println("Solution is in Connected Component " + halfGame);
				break;
			}
		}
		// Test for the the nodes reachable from solution and pick any node from the last level of the BFS:
		// it will be at maximum distance to/from the solution
		// Note: the graph is undirected, so paths "to" and "from" any pair of nodes are exactly the same.
		System.out.print("Performing BFS... "); // Note: Use -Xms2g to increase JVM heap size for a better performance"
		Vector<Set<Node>> bfsLevels = halfGame.bfs(solution);
		System.out.println("Done!");
		endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		Board aFarAwayNode = (Board) bfsLevels.lastElement().iterator().next();
		System.out.println("Most far away node is:");
		aFarAwayNode.println();
		System.out.println("and it is at distance " + (bfsLevels.size()-1) + " to solution.");
		System.out.println("Path from it to solution is the following steps:");
		// Prints the path in reverse direction, since we started from node "solution"
		LinkedList<Node> maxPath = halfGame.getShortestPath(solution, aFarAwayNode);
		for (Iterator<Node> pathNodes = maxPath.descendingIterator(); pathNodes.hasNext(); ) {
			Board bd = (Board) pathNodes.next();
			bd.printBoard();
			//System.out.println(bd);
		}
		System.out.println();
	}

}
