package puzzle;

import datastructures.Node;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.*;

public class Config extends Node {

	// A Node representing a game configuration
    
	// Constants:
	// ==========
    public static final int BOARD_SIZE = 3;
    
    // Data definitions:
    // =================
    /* board is a BOARD_SIZE x BOARD_SIZE puzzle game:
     * 1 2 3
     * 4 5 6
     * 7 8 0 --> 0 is the empty square */
    private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    
    // Node id is the linearized configuration of the board as a string, for example: "123456780" 
    
    // emptyPos are the X and Y coordinates of the empty square
    private Pair<Integer,Integer> emptyPos;
    
    /* Example: Node("123456780") is the solution of the game
     * 1 2 3
     * 4 5 6
     * 7 8 0 */
    public static final Config SOLUTION = new Config("123456780");
    
    // Functions:
    // ==========
    
    // Creates a graph node representing a game configuration in a given time
    public Config(String _id) { //throws Exception {
    	super(_id);
        //TODO: usar assert ou Expcetion???
        //if (_id.length() != 9) throw new Exception("");
        assert (_id.length() != 9);
        this.label = new String(_id);
        for (int i=0; i<BOARD_SIZE; i++) {
            for (int j=0; j<BOARD_SIZE; j++) {
                board[i][j] = Integer.parseInt(this.label.substring(i*BOARD_SIZE+j, i*BOARD_SIZE+j+1));
                if (board[i][j]==0) {
                    emptyPos = new ImmutablePair<Integer,Integer>(i,j);
                }
            }
        }
    }
    
    public String getId() {
    	return new String(this.label);
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public Pair<Integer,Integer> getEmptyPos() {
        return emptyPos;
    }
    
    public String stringfy() {
        return ArrayUtils.toString(board);
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
        System.out.println("Node label is \"" + this.label + "\"");
        System.out.println("Board is: ");
        printBoard();
        System.out.println("Empty square is at position " + emptyPos);
    }
    
    public Set<Node> generateNeighbors() {
            Set<Node> neighbors = new HashSet<Node>();
            Node newNeighbor = new Node("012345678");
            neighbors.add(newNeighbor);
            return neighbors;
    }
	
	public static void main(String[] args) {
		// Basic unit test "check-expects":
		Config node1 = new Config("087654321");
		node1.println();
		node1.printBoard();
		System.out.println(node1);
		System.out.println("\n**************************");
		System.out.println("Solution for the " + BOARD_SIZE + " x " + BOARD_SIZE + " game is:");
		SOLUTION.println();
	}

}
