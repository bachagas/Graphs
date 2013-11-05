package puzzle;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import datastructures.Graph;

public class Game {
	
	// Constants:
	// ==========
	public final static int N_TILES = Board.SIZE * Board.SIZE;
	public final static int INIT_CAPACITY = (int) (util.Math.factorial(N_TILES) / 0.75 + 1);

	// Data Definitions:
	// =================
	private Graph gameGraph;
	
	// Functions:
	// ==========
	public Game(String aName) {
		gameGraph = new Graph(aName,INIT_CAPACITY);
		//Generate game Graph from all possible board permutations
		List<String> permutations = Game.permutate("012345678",0);
		Iterator<String> it = permutations.iterator();
		while (it.hasNext()) {
			Board currentBoard = new Board(it.next());
			gameGraph.addNode(currentBoard);
			//Add all the edges to neighbors of the current board
			Iterator<Board> neighbors = currentBoard.nextPossibleBoards().iterator();
			while ( neighbors.hasNext() ) {
				gameGraph.addEdge(currentBoard, neighbors.next());
			}
		}
	}
	
	// Produces a list of all the permutations of the characters in a given string
	private static List<String> permutate(String chars, int pointer) {
		LinkedList<String> result = new LinkedList<String>();
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
	
	// Returns the amount of game configurations in the graph 
	public int getNBoards() {
		return this.gameGraph.getN();
	}
	
	// Returns the amount of game moves in the graph
	public int getMMoves() {
		return this.gameGraph.getM();
	}
	
	// Produces an human-readable string with global useful information about the game
	public String toString() {
		return this.gameGraph.getLabel() + "={" + this.getNBoards() + " nodes," + this.getMMoves() + " edges}";
	}
	
	//public Game(boolean populateGraphMap) {
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
		/*super("Game", INIT_CAPACITY);
		if ( !populateGraphMap ) {
			return;
		}

		int[][] board = new int[Board.SIZE][Board.SIZE];
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				// TODO board[i][j] = ;
			}
		}*/

		/*
		 * TODO Criar todos os nós (9!) - permutações de 9 números Criar as
		 * relações deles no map
		 
	}

	public void dfs() {
		boolean empty = this.isEmpty();
		/*if (empty) {
			// Empty graph. Method 'dfs' will build it.

			super.traverseReset();
			// TODO
		} else {
			super.dfs();
		}*/
	//}
	
	// Basic unit test "check-expects":
	public static void main(String[] args) {
		Date startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Start");
		//Delay - for time measure testing only:
		for (long i=0;i<2000000000;i++) if (i==100000) System.out.println("Testing for time measurement...");
		Date endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		// Testing for permutations generation:
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Testing permutations");
		List<String> permutations = Game.permutate("0123",0);
		Iterator<String> it = permutations.iterator();
		System.out.println("All possible permutations of \"0123\" are:");
		while ( it.hasNext() ) {
			String newBoard = it.next();
			System.out.println(newBoard);
		}
		endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
		
		// Generating graph:
		startTime = new Date();
		System.out.println(startTime + " (" + startTime.getTime() + ")> Building graph");
		permutations = Game.permutate("012345678",0);
		int estimatedSize = util.Math.factorial(Game.N_TILES);
		System.out.println("Must have 9!=" + estimatedSize + " permutations, generated " + permutations.size() + " board configurations");
		System.out.print("Building graph with them all... ");
		Game game = new Game("Game");
		System.out.println("Done!");
		System.out.println(game);
		endTime = new Date();
		System.out.println(endTime + " (" + endTime.getTime() + ")> Finished!");
		System.out.println(endTime.getTime() - startTime.getTime() + " milliseconds ellapsed");
	}
}
