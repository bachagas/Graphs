package datastructures;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.*;
//import org.javatuples.Pair;

public class Node {
	// A Node representing a game configuration
	// Constants:
	public static final int BOARD_SIZE = 3;
	// Data definitions:	
	/* board is a BOARD_SIZE x BOARD_SIZE puzzle game:
	 * 1 2 3
	 * 4 5 6
	 * 7 8 0 --> 0 is the empty square */
	private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
	// id is the linearized configuration of the board 
	private String id;
	// emptyX and emptyY are the X and Y coordinates of the empty square
	//private int emptyX;
	//private int emptyY;
	private Pair<Integer,Integer> emptyPos;
	
	/* Example: Node("123456780") is the solution of the game
	 * 1 2 3
	 * 4 5 6
	 * 7 8 0 */
	public static final Node SOLUTION = new Node("123456780");
	
	public Node(String _id) { //throws Exception {
		/*
		 * Creates a Node.
		 */
		//TODO: usar assert ou Expcetion
		//if (_id.length() != 9) throw new Exception("");
		assert (_id.length() != 9);
		this.id = new String(_id);
		for (int i=0; i<BOARD_SIZE; i++) {
			for (int j=0; j<BOARD_SIZE; j++) {
				this.board[i][j] = Integer.parseInt(this.id.substring(i*BOARD_SIZE+j, i*BOARD_SIZE+j+1));
				if (this.board[i][j]==0) {
					//this.emptyX = i;
					//this.emptyY = j;
					this.emptyPos = new ImmutablePair<Integer,Integer>(i,j);
				}
			}
		}
	}
	
	public String getId() {
		return this.id;
	}
	
	public int[][] getBoard() {
		return this.board;
	}
	
	public Pair<Integer,Integer> getEmptyPos() {
		return emptyPos;
	}
	
	public String stringfy() {
		return ArrayUtils.toString(this.board);
	}
	
	public void printBoard() {
		String temp = "";
		for (int i=0; i<BOARD_SIZE; i++) {
			if (i==0) temp = "[";
			else temp += " ";
			for (int j=0; j<BOARD_SIZE; j++) {
				temp += board[i][j];
				if (i==2 && j==2) temp += "]";
				else temp += " ";
			}
			if (i<2) temp += "\n";
		}
		System.out.println( temp );
	}
	
	public void println() {
		//String temp = "Node id is \"" + id + "\"\n";
		//temp += "Board is: " +;
		//temp += "Empty square is at position (" + emptyX + "," + emptyY + ")\n";
		//temp += "Empty square is at position " + emptyPos;
		System.out.println("Node id is \"" + id + "\"");
		System.out.println("Board is: ");
		this.printBoard();
		System.out.println("Empty square is at position " + emptyPos);
		//System.out.println( temp );
	}
	
	public Set<Node> generateNeighbors() {
		Set<Node> neighbors = new HashSet<Node>();
		Node newNeighbor = new Node("012345678");
		neighbors.add(newNeighbor);
		return neighbors;
	}
}
