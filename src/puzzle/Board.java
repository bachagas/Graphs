package puzzle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import datastructures.Node;

// A graph node representing a game board and its configuration:
// Each position contains a number from 1..(SIZE*SIZE-1) + one empty square denoted by "0"
public class Board extends Node {

	// Constants:
	// ==========
    public static final int SIZE = 3;
    
    // Data definitions:
    // =================
    /* board is a SIZE x SIZE puzzle game with:
     * - an id (inherited from Node): the linearized version of the board configurations as a string
     * - a board: a two-dimensional matrix
     * - an empty pos: a pair with the (x,y) coordinates of the empty square position
     * Ex: id = "123456780" is the board
     * 1 2 3
     * 4 5 6
     * 7 8 0 --> 0 is the empty square */
    private int[][] board = new int[SIZE][SIZE];
    private Pair<Integer,Integer> emptyPos;
    
    /* Example: Node("123456780") is the solution of the game
     * 1 2 3
     * 4 5 6
     * 7 8 0 */
    public static final String SOLUTION = "123456780";
    
    // Functions:
    // ==========    
    // Creates a graph node representing a board configuration in a given time from an string representing the board
    public Board(String aBoard) { //throws Exception {
    	super(aBoard);
        //TODO: use assert or Exception???
        //if (_id.length() != 9) throw new Exception("");
        assert (aBoard.length() != 9);
        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                board[i][j] = Integer.parseInt(this.id.substring(i*SIZE+j, i*SIZE+j+1));
                if (board[i][j]==0) {
                    emptyPos = new ImmutablePair<Integer,Integer>(i,j);
                }
            }
        }
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
    
    // Prints a board as a SIZE x SIZE matrix
    public void printBoard() {
        String temp = "";
        for (int i=0; i<SIZE; i++) {
            if (i==0) temp = "[";
            else temp += " ";
            for (int j=0; j<SIZE; j++) {
                temp += board[i][j];
                if (i==2 && j==2) temp += "]";
                else temp += " ";
            }
            if (i<2) temp += "\n";
        }
        System.out.println( temp );
    }
    
    // Prints all the attributes of the board in an human-readable format 
    public void println() {
        System.out.println("Node label is \"" + this.id + "\"");
        System.out.println("Board is: ");
        printBoard();
        System.out.println("Empty square is at position " + emptyPos);
    }

    // Produces true if board is a neighbor of a given one
    public boolean isNeighbor(Board aBoard) {
    	if (aBoard != null)
	    	return ( (Math.abs(this.emptyPos.getLeft() - aBoard.emptyPos.getLeft()) <= 1) &&
	    			 (Math.abs(this.emptyPos.getRight() - aBoard.emptyPos.getRight()) == 0) ) ||
	    			 	( (Math.abs(this.emptyPos.getRight() - aBoard.emptyPos.getRight()) <= 1) &&
	    	    			 (Math.abs(this.emptyPos.getLeft() - aBoard.emptyPos.getLeft()) == 0) );
    	else return false;
    }
    
    // Produces a new board exchanging positions p1 with p2, if possible; null if p1 or p2 is out of bounds
    public Board exchange(int p1, int p2) {
    	if ( (0<=p1)&&(p1<SIZE*SIZE)&&(0<=p2)&&(p2<SIZE*SIZE) ) {
    		char[] temp = this.id.toCharArray();
	        temp[p1] = this.id.charAt(p2);
	        temp[p2] = this.id.charAt(p1);
	    	return new Board(new String(temp));
    	} else return null;
    }
    
    // Produces all possible next boards from current board
    public Set<Board> nextPossibleBoards() {
            Set<Board> neighbors = new HashSet<Board>();
            //Can move the empty square one position at a time, according to its current position:
            int emptyPointer = this.emptyPos.getLeft()*SIZE + this.emptyPos.getRight();
            Board newBd = null;
            // Move down if possible:
            if ( this.emptyPos.getLeft() < SIZE-1 ) {
            	newBd = this.exchange(emptyPointer, emptyPointer+SIZE);
            	if ( newBd != null) neighbors.add(newBd);
            }
            // Move up if possible:
            if ( this.emptyPos.getLeft() > 0 ) {
            	newBd = this.exchange(emptyPointer, emptyPointer-SIZE);
            	if ( newBd != null) neighbors.add(newBd);
            }
            // Move right if possible:
            if ( this.emptyPos.getRight() < SIZE-1 ) {
	            newBd = this.exchange(emptyPointer, emptyPointer+1);
	            if ( newBd != null) neighbors.add(newBd);
            }
            // Move left if possible:
            if ( this.emptyPos.getRight() > 0 ) {
            	newBd = this.exchange(emptyPointer, emptyPointer-1);
            	//if ( newBd != null) 
            	neighbors.add(newBd);
            }
            return neighbors;
    }
	
	// Basic unit test "check-expects":
	public static void main(String[] args) {
		Board node = new Board("087654321");
		node.println();
		node.printBoard();
		System.out.println(node);
		System.out.println("\n**************************");
		System.out.println("Solution for the " + SIZE + " x " + SIZE + " game is:");
		new Board(Board.SOLUTION).println();
		
		System.out.println("\n**************************");
		System.out.println("Original node is:");
		node.printBoard();
		System.out.println("Moving right is:");
		node.exchange(0, 1).printBoard();
		System.out.println("Moving down is:");
		node.exchange(0, 1).exchange(1, 4).printBoard();
		
		System.out.println("\n**************************");
		System.out.println("Original node is:");
		node.printBoard();
		boolean test = true;
		System.out.println("Possible moves are:");
		for (Iterator<Board> it = node.nextPossibleBoards().iterator(); it.hasNext(); ) { 
			Board bd = it.next();
			bd.printBoard();
			test = test && node.isNeighbor(bd);
		}
		System.out.println("Ok=" + test);
		System.out.println();
		
		node = new Board("123405678");
		System.out.println("Original node is:");
		node.printBoard();
		System.out.println("Possible moves are:");
		test = true;
		for (Iterator<Board> it = node.nextPossibleBoards().iterator(); it.hasNext(); ) { 
			Board bd = it.next();
			bd.printBoard();
			test = test && node.isNeighbor(bd);
		}
		System.out.println("Ok=" + test);
		System.out.println();
		
		node = new Board("102345678");
		System.out.println("Original node is:");
		node.printBoard();
		System.out.println("Possible moves are:");
		for (Iterator<Board> it = node.nextPossibleBoards().iterator(); it.hasNext(); ) {
			Board bd = it.next();
			bd.printBoard();
			test = test && node.isNeighbor(bd);
		}
		System.out.println("Ok=" + test);
		System.out.println();
		
		//Check for error in node "627018453"
		node = new Board("627018453");
		System.out.println("Original node is:");
		node.printBoard();
		System.out.println("Possible moves are:");
		for (Iterator<Board> it = node.nextPossibleBoards().iterator(); it.hasNext(); ) {
			Board bd = it.next();
		bd.printBoard();
		test = test && node.isNeighbor(bd);
		}
		System.out.println("Ok=" + test);
		System.out.println();
	}
}
