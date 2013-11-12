package test;

import java.util.Iterator;

import puzzle.Board;

public class BoardTest {
	// Basic unit test "check-expects":
	public static void main(String[] args) {
		Board node = new Board("087654321");
		node.println();
		node.printBoard();
		System.out.println(node);
		System.out.println("\n**************************");
		System.out.println("Solution for the " + Board.SIZE + " x " + Board.SIZE + " game is:");
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
