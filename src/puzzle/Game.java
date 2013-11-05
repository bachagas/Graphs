package puzzle;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import datastructures.Graph;
import datastructures.Node;

// A board puzzle game modeled as a graph:
// - Each node is a board configuration;
// - There is an edge between nodes u,v if there is a valid single move that can take you from board u to board v.
public class Game extends Graph {
	
	// Constants:
	// ==========
	public final static int N_TILES = Board.SIZE * Board.SIZE;
	public final static int INIT_CAPACITY = (int) (util.Math.factorial(N_TILES) / 0.75 + 1);

	// Data Definitions:
	// =================
	//private Graph gameGraph;
	
	// Functions:
	// ==========
	public Game(String aName) {
		/*
		 * From the HashMap class JavaDoc: As a general rule, the default load
		 * factor (.75) offers a good tradeoff between time and space costs.
		 * Higher values decrease the space overhead but increase the lookup
		 * cost (reflected in most of the operations of the HashMap class,
		 * including get and put). The expected number of entries in the map and
		 * its load factor should be taken into account when setting its initial
		 * capacity, so as to minimize the number of rehash operations. If the
		 * initial capacity is greater than the maximum number of entries
		 * divided by the load factor, no rehash operations will ever occur.
		 * 
		 * This means, our optimal initial capacity should be N_TILES!/0.75 + 1
		 */
		super(aName,INIT_CAPACITY);
	}
	
	// Populates the game graph with the given list of boards to be built
	public void populate(HashSet<String> permutations) {
		Iterator<String> it = permutations.iterator();
		while (it.hasNext()) {
			Board currentBoard = (Board) this.addNode(new Board(it.next()));
			//Add all the edges to neighbors of the current board
			Iterator<Board> neighbors = currentBoard.nextPossibleBoards().iterator();
			while ( neighbors.hasNext() ) {
				Board next = neighbors.next();
				//if ( permutations.contains(next.getId()) ) // only add neighbors explicitly asked in the given entry list
						this.addEdge(currentBoard, next);
			}
		}
	}
	
	// Produces a list of all the permutations of the characters in a given string
	private static HashSet<String> permutate(String chars, int pointer) {
		HashSet<String> result = new HashSet<String>();
		if (pointer == chars.length()) {
	        //stop-condition: returns list with only the input string
			result.add(chars);
	        return result;
	    }
	    for (int i = pointer; i < chars.length(); i++) {
	    	char[] permutation = chars.toCharArray();
	        permutation[pointer] = chars.charAt(i);
	        permutation[i] = chars.charAt(pointer);
	        result.addAll(permutate(new String(permutation), pointer + 1));
	    }
        return result;
	}
	
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
		HashSet<String> permutations = Game.permutate("0123",0);
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
		
		// Generating graph:
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Building graph:");
		permutations = Game.permutate("012345678",0);
		int estimatedSize = util.Math.factorial(Game.N_TILES);
		System.out.println("Must have 9!=" + estimatedSize + " permutations, generated " + permutations.size() + " board configurations");
		System.out.print("Building a game graph with them all... ");
		Game game = new Game("Game");
		game.populate(permutations);
		System.out.println("Done!");
		endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		System.out.println(game);
		//game.println();
		System.out.println();
		
		// Tarefa 1:
		System.out.println("************************ Tarefa 1 ************************");
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Looking for connected components by DFS:");
		System.out.print("Performing DFS..."); // Note: Use -Xss1g to increase JVM stack size to avoid stack overflow errors
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
		
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Looking for connected components by BFS (just double-checking):");
		System.out.print("Performing BFS..."); // Obs: Use -Xms2g to increase JVM heap size for a better performance)
		game.bfs(game.getNodeById("123456780"));
		System.out.println("Done!");
		endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		System.out.println("Found " + game.getNumberOfCCs() + " connected components:");
		for (Iterator<Graph> CCs = game.getCCs().iterator(); CCs.hasNext(); )
			System.out.println(CCs.next());
		System.out.println();
		
		// Tarefa 2:
		System.out.println("************************ Tarefa 2 ************************");
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Looking for maximum distance to solution by BFS:");
		System.out.println("Looking for path with the maximum distance..."); // Obs: Use -Xms<maximum your machine will permit>g to increase JVM heap size for a better performance"
		Graph halfGame = null;
		Board solution = (Board) game.getNodeById(Board.SOLUTION.getId());
		for (Iterator<Graph> CCs = game.getCCs().iterator(); CCs.hasNext(); ) {
			halfGame = CCs.next();
			if ( halfGame.contains(solution) ) {
				System.out.println("Solution is in Connected Component " + halfGame);
				break;
			}
		}
		Iterator<Node> nodes = halfGame.getNodes().iterator();
		LinkedList<Node> maxPath = new LinkedList<Node>(); int count = 0;
		while ( nodes.hasNext() ) {
			LinkedList<Node> path = halfGame.bfsPath(nodes.next(),solution);
			if (path != null)
				if ( path.size() > maxPath.size() )
					maxPath = (LinkedList<Node>) path;
			if ((count++)%(100) == 0)
				System.out.println(new Date() + "(" + new Date().getTime() + ")> " + count + " nodes checked");
		}
		System.out.println("Done!");
		endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		System.out.println("Tha maximum path to solution has distance " + (maxPath.size()-1) + " and starts at node:");
		System.out.println((Board) maxPath.getFirst());
		System.out.println("Steps:");
		for (Iterator<Node> pathNodes = maxPath.iterator(); pathNodes.hasNext(); ) {
			Board bd = (Board) maxPath.remove();
			bd.printBoard();
			//System.out.println(bd);
		}
		System.out.println();
	}
}
