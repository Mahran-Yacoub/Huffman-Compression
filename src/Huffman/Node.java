package Huffman;

public class Node implements Comparable<Node> {

	private int frequency;
	private Node left;
	private Node right;
	private int character = 256;

	public Node(int character, int frequency, Node left, Node rigth) {

		this.character = character;
		this.frequency = frequency;
		this.left = left;
		this.right = rigth;

	}

	public Node(int frequency, Node left, Node rigth) {

		this.frequency = frequency;
		this.left = left;
		this.right = rigth;

	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}


	public int getCharacter() {
		return character;
	}

	public void setCharacter(int character) {
		this.character = character;
	}

	public boolean isLeaf() {

		return this.left == null && this.right == null;
	}

	@Override
	public int compareTo(Node o) {

		if (frequency > o.frequency) {

			return 1;

		} else if (frequency < o.frequency) {

			return -1;

		} else {

			return 0;

		}

	}

	@Override
	public String toString() {

		if (this.left != null && this.right != null) {

			return "[ " + this.character + ", " + this.frequency + " ] \n" + this.left.toString()
					+ this.right.toString();

		} else {

			return "[" + this.character + ", " + this.frequency + " ] \n" ;
		}

	}

}
