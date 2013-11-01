package puzzle;

import datastructures.Graph;
//import datastructures.Config;

public class Game extends Graph {

	public final static int N_TILES = Config.BOARD_SIZE * Config.BOARD_SIZE;
	public final static int INIT_CAPACITY = (int) (util.Math.factorial(N_TILES) / 0.75 + 1);

	public Game(boolean populateGraphMap) {
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
		super(INIT_CAPACITY);
		if (!populateGraphMap) {
			return;
		}

		int[][] board = new int[Config.BOARD_SIZE][Config.BOARD_SIZE];
		for (int i = 0; i < Config.BOARD_SIZE; i++) {
			for (int j = 0; j < Config.BOARD_SIZE; j++) {
				// TODO board[i][j] = ;
			}
		}

		/*
		 * TODO Criar todos os nós (9!) - permutações de 9 números Criar as
		 * relações deles no map
		 */
	}

	public void dfs() {
		boolean empty = this.isEmpty();
		if (empty) {
			// Empty graph. Method 'dfs' will build it.

			super.traverseReset();
			// TODO
		} else {
			super.dfs();
		}
	}
}
