package Huffman;

import java.io.File;
import java.util.ArrayList;
import gui.Row;
import hash.HashEntery;
import hash.HashMap;
import io.stream.BitInputStream;
import io.stream.BitOutputStream;

public class DecompressMethods {

	/** in : used to read from a file as bytes */
	private BitInputStream in;

	/** out : is used to write on a file as bytes */
	private BitOutputStream out;

	private int numberOfCharInOriginalFile;

	private int countDistinctInOriginal;

	private int countHeader;

	private int countCharInCompressedFile;

	private int[] count = new int[256];

	private StringBuilder encodedOfHeader = new StringBuilder();

	private ArrayList<Row> rows = new ArrayList<Row>();

	private String extensionOfOriginalFile = new String("");

	@SuppressWarnings("unused")
	private File compressedFile;
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	public DecompressMethods(File compressedFile) {

		this.compressedFile = compressedFile;
		in = new BitInputStream(compressedFile);
		numberOfCharInOriginalFile = in.readBits(32);

		int numberOfExtensionCharacters = in.readBits(8);

		for (int i = 0; i < numberOfExtensionCharacters; i++) {

			char character = (char) in.readBits(8);
			this.extensionOfOriginalFile += character;
		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 1st Step of Decompression Process
	 * 
	 * This method is used to decompress Header of a file to its original form as
	 * huffman tree to use it to encoding a compressed file
	 * 
	 * @param compressedFile
	 * @return Original Huffman tree
	 */
	/*private Node decodingHeader() {
		
		Stack<Node> treeBuilder = new Stack<Node>();

		for (;;) {

			int bit = in.readBits(1);
			if (bit == 0) {

				encodedOfHeader.append('0');

				countHeader++;

				if (treeBuilder.size() == 1) {

					return treeBuilder.pop();

				} else {

					Node right = treeBuilder.pop();
					Node left = treeBuilder.pop();
					Node newNode = new Node(256, 0, left, right);
					treeBuilder.push(newNode);

				}

			} else {

				countHeader += 2;

				int character = in.readBits(8);

				encodedOfHeader.append('1');
				encodedOfHeader.append((char) character);

				Node node = new Node(character, 0, null, null);
				treeBuilder.push(node);

			}

		}

	}*/

	
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 1st Step of Decompression Process
	 * 
	 * This method is used to decompress Header of a file to its original form as
	 * huffman tree to use it to encoding a compressed file
	 * 
	 * @param compressedFile
	 * @return Original Huffman tree
	 */
	private Node decodingHeader() {


			return decodingHelper();


	}

	/**
	 * This method is consider to be part of 1st step of decompressed a file.
	 * 0:internal leaf , so we need to read a left and a right node 1:leaf node , so
	 * we need to read a character
	 * 
	 * @return node either internal or leaf for each invoking and the last invoking
	 *         will be a root of a huffman tree.
	 * 
	 */
	private Node decodingHelper() {

		int bit = in.readBits(1);

		if (bit == 0) {

			countHeader++;
			Node internalNode = new Node(256, 0, null, null);
			internalNode.setLeft(decodingHelper());
			internalNode.setRight(decodingHelper());
			return internalNode;

		} else {

			countHeader+=9;
			int character = in.readBits(8);
			Node leaf = new Node(character, 0, null, null);
			return leaf;

		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to decompress an encoded message to its original form
	 * using a huffman tree and print it on new File.
	 * 
	 * @param huffmanTree
	 * @param DecompressedFile
	 */
	private void decompressMessage(Node huffmanTree, File DecompressedFile) {

		Node current = huffmanTree;
		int bit = 0;
		out = new BitOutputStream(DecompressedFile);

		int numberOfCharInOriginalFileTemp = this.numberOfCharInOriginalFile;
		int countNumberOfBits = 0;

		while (numberOfCharInOriginalFileTemp != 0) {

			bit = in.readBits(1);

			countNumberOfBits++;

			current = (bit != 0) ? current.getRight() : current.getLeft();

			if (current.isLeaf()) {

				int codePoint = current.getCharacter();
				out.writeBits(8, codePoint);
				current = huffmanTree;
				numberOfCharInOriginalFileTemp--;

				count[codePoint]++;
				if (count[codePoint] == 1) {

					countDistinctInOriginal++;
				}
			}

		}

		// #bytes or a character of a message in compressed file
		this.countCharInCompressedFile = (countNumberOfBits / 8);

		if (countNumberOfBits % 8 > 0) {

			this.countCharInCompressedFile++;
		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to decompress A compressed file to its original form in
	 * new File
	 * 
	 * @param compressedFile
	 * @param newFile
	 * 
	 */
	public void decompres(File newFile) {

		Node huffmanTree = decodingHeader();// 1st
		decompressMessage(huffmanTree, newFile);// 2nd
		buildTable(huffmanTree);
		out.flush();
		out.close();
		in.close();

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 3rd step of Huffman compression Process These two method is used to read a
	 * Huffman tree in the form of a single HuffNode to build a table Using HashMap
	 * data structure contains all of the Huffman codes you will need to compress
	 * the body of the file. Rather than querying the tree and extracting a code for
	 * every character in the file.
	 * 
	 * @param root         : Huffman tree .
	 * @param huffmanCode.
	 * @param table        : Table that represent a codepoint of character and its
	 *                     huffman code.
	 * 
	 */
	private void taversal(Node root, String huffmanCode, HashMap<Integer, String> table) {

		if (root != null) {

			taversal(root.getLeft(), huffmanCode + "0", table);

			if (root.isLeaf()) {

				// When it reach a leaf it will read a code point and put it in a table.

				table.put(root.getCharacter(), huffmanCode);

			}

			taversal(root.getRight(), huffmanCode + "1", table);

		}

	}

	/**
	 * This method will create a huffman table depends on Huffman tree that create
	 * in second step of compressed process and using a recursive method/travesal
	 * 
	 * @param root : Huffman tree
	 * 
	 * @return a huffman table as HashMap data structure
	 * 
	 */
	private HashMap<Integer, String> buildTable(Node root) {

		HashMap<Integer, String> table = new HashMap<>(countDistinctInOriginal);

		if (countDistinctInOriginal != 1) {

			taversal(root, "", table);

		} else {

			table.put(root.getCharacter(), "0");
		}

		fillArrayList(table);

		return table;

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to fill ArrayList Object that will be used to fill
	 * TableBiew In GUI .
	 * 
	 * @param table Huffman Table
	 * 
	 */
	private void fillArrayList(HashMap<Integer, String> table) {

		HashEntery<Integer, String>[] tableTemp = table.getHashTable();

		for (int i = 0; i < table.sizeOfData(); i++) {

			Character character = new Character((char) tableTemp[i].getKey().intValue());
			String code = tableTemp[i].getValue();
			Integer length = code.length();
			Integer frequency = count[tableTemp[i].getKey()];

			Row row = new Row(character, code, length, frequency);

			rows.add(row);

		}

	}

//////////////////////////////////////////////////////////////////////////////////////////////////

	public int getNumberOfCharInOriginalFile() {
		return numberOfCharInOriginalFile;
	}

	public int getCountDistinctInOriginal() {
		return countDistinctInOriginal;
	}

	public int getCountHeader() {
		return countHeader;
	}

	public int getCountCharInCompressedFile() {
		return countCharInCompressedFile;
	}

	public StringBuilder getEncodedOfHeader() {
		return encodedOfHeader;
	}

	public ArrayList<Row> getRows() {
		return rows;
	}

	public String getExtensionOfOriginalFile() {
		return "." + extensionOfOriginalFile;
	}

}
