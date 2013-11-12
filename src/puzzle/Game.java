package puzzle;

import java.util.Iterator;
import java.util.Set;

import datastructures.Graph;

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
	public void populate(Set<String> permutations) {
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
}
