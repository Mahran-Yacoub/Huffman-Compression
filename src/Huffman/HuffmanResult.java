package Huffman;

import hash.HashMap;

public class HuffmanResult {

	private Node huffmanTree;
	private HashMap<Integer, String> table;


	public HuffmanResult(HashMap<Integer, String> table, Node huffmanTree) {

		this.table = table;
		this.huffmanTree = huffmanTree;
	}

	public Node getHuffmanTree() {
		return huffmanTree;
	}

	public void setHuffmanTree(Node huffmanTree) {
		this.huffmanTree = huffmanTree;
	}

	public HashMap<Integer, String> getTable() {
		return table;
	}

	public void setTable(HashMap<Integer, String> table) {
		this.table = table;
	}

	
	
	

}
