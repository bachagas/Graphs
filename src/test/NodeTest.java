package test;

import datastructures.Node;

public class NodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO verificar funcionamento assert
		Node node1 = new Node("087654321");
		node1.println();
		System.out.println("\n**************************");
		System.out.println("Solution for the " + Node.BOARD_SIZE + " x " + Node.BOARD_SIZE + " game is:");
		Node.SOLUTION.println();
	}

}
