package Huffman;

import java.io.File;
import java.util.ArrayList;

import gui.Row;
import hash.HashEntery;
import hash.HashMap;
import heap.HeapEntery;
import heap.PriorityQueu;
import io.stream.BitInputStream;
import io.stream.BitOutputStream;

public class CompressMethods {

	/**
	 * An array that is used to store frequencies of a character/bytes in original
	 * file
	 */
	static int[] count;

	/** Number of Distinct char in an original file */
	private int countDistinct;

	/** in : used to read from a file as bytes */
	private BitInputStream in;

	/** out : is used to write on a file as bytes */
	private BitOutputStream out;

	private int countNumberOfCharInOriginalFile;

	private int countNumberOfCharInHeader;

   private int numberOfCharInEncodedMessage;

	private StringBuilder encodedOfHeader = new StringBuilder();

	/** This ArrayList will store rows of TableView */
	private ArrayList<Row> rows = new ArrayList<Row>();

	private String extensionOfOriginalFile;

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to count frequencies of char in original file and store
	 * it in an frequencies array (in above data fields )
	 *
	 * @param original : Original File that we want to compress
	 *
	 **/
	private void countEeachCharacter(File original) {

		count = new int[256];

		// Initialize an in to original file
		in = new BitInputStream(original);

		int character = 0;

		/*
		 * The loop will terminate when a file ends / reach -1
		 * 
		 * The read(number of bits) return int type that represent unsigned of read
		 * bytes (8 bits as input)
		 * 
		 */
		while ((character = in.readBits(8)) != -1) {

			count[character]++;

			if (count[character] == 1) {

				countDistinct++;

			}

			countNumberOfCharInOriginalFile++;

		}

		// This method will return in stream to read from a begin of a file
		in.reset();

		// read and save extension of original file
		int indexOfDot = original.getAbsolutePath().lastIndexOf('.');
		String extension = original.getAbsolutePath().substring(indexOfDot + 1);
		this.extensionOfOriginalFile = extension;

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * This method will use to create a huffman tree used a priority queu (Minimun
	 * Heap) Depends on frequency array that fill in first step of compression
	 * process.
	 * 
	 * Huffman tree : will store each character in int type (code points / unsigned
	 * bytes) and frequency of each character in leaves and the remains nodes will
	 * be store 256 as character code point and 0 as frequency.
	 * 
	 * we will add an additional node to a tree
	 * 
	 * @param count : requency array that fill in first step of compression process.
	 * 
	 * @return The node that represent a huffman tree
	 * 
	 */
	private Node buildHuffmanTree(int[] count) {

		PriorityQueu<Node> huffmanTree = new PriorityQueu<>(countDistinct);

		// Add each character in Orginal file in Heap as Node
		for (int i = 0; i < count.length; i++) {

			if (count[i] > 0) {

				int item = count[i];
				HeapEntery<Node> element = new HeapEntery<Node>();
				Node node = new Node(i, item, null, null);
				element.setKey(node);
				huffmanTree.insert(element);
			}

		}

		/*
		 * A loop #nodes -1 (countDistinct +1 -1 = countDistinct) to build huffman tree
		 * depends of the first loop.
		 * 
		 * we pop two nodes and make new node with left: first pop , right :second pop
		 * each iteration
		 * 
		 * 
		 */
		for (int i = 0; i < countDistinct - 1; i++) {

			HeapEntery<Node> item1 = huffmanTree.delete();
			Node node1 = item1.getKey();
			HeapEntery<Node> item2 = huffmanTree.delete();
			Node node2 = item2.getKey();

			HeapEntery<Node> item3 = new HeapEntery<Node>();
			Node node3 = new Node(node1.getFrequency() + node2.getFrequency(), node1, node2);
			item3.setKey(node3);

			huffmanTree.insert(item3);

		}

		return huffmanTree.delete().getKey();

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

		HashMap<Integer, String> table = new HashMap<>(countDistinct);

		if (countDistinct != 1) {

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

	/**
	 * This step will be a 4th step of a compreesion process it is to write a header
	 * that will be used to decompress a file
	 * 
	 * We will write it in preorder root - left - right
	 * 
	 * @param huffmanTree : Huffman tree
	 * 
	 * 
	 */
	private void encodingHeader(Node huffmanTree) {

		if (huffmanTree != null) {


			if (huffmanTree.isLeaf()) {

				out.writeBits(1, 1);
				countNumberOfCharInHeader ++;
				out.writeBits(8, huffmanTree.getCharacter());
				countNumberOfCharInHeader += 8;
				encodedOfHeader.append('1');
				encodedOfHeader.append((char) huffmanTree.getCharacter());

			} else {

				out.writeBits(1, 0);
				countNumberOfCharInHeader++;
				encodedOfHeader.append('0');

			}

			encodingHeader(huffmanTree.getLeft());

			encodingHeader(huffmanTree.getRight());
		}

	}

	/**
	 * This method will use to print flag to begin of header on compressed file and
	 * flag of end of header on compressed file and header it self between two flag
	 * using help recursive method encodingHeader
	 * 
	 * @param huffmanTree
	 * @param compreesedFile
	 * 
	 */
	private void printHeaderOnFile(Node huffmanTree, File compreesedFile) {

		out = new BitOutputStream(compreesedFile);

		out.writeBits(32, countNumberOfCharInOriginalFile);

		out.writeBits(8, this.extensionOfOriginalFile.length());

		for (int i = 0; i < extensionOfOriginalFile.length(); i++) {

			out.writeBits(8, extensionOfOriginalFile.charAt(i));
		}

		encodingHeader(huffmanTree);

		//out.writeBits(1, 0); // Ends Of Tree Encoding

		//countNumberOfCharInHeader++;

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 5th step of huffman compression process This method is used to print encoding
	 * of original file contents based on huffman table by read each byte/character
	 * and print its huffman code in compressed file
	 * 
	 * @param originalFile
	 * @param compressedFile
	 * @param huffmanTable
	 */
	private void pritnEncodingMessage(File originalFile, File compressedFile, HashMap<Integer, String> huffmanTable) {

		int character = 0;
		int count = 0;
		while ((character = in.readBits(8)) != -1) {

			String value = huffmanTable.find(character);
			out.writeBits(value.length(), Integer.parseInt(value, 2));
			count += value.length();

		}

		// #bytes or a character of a message in compressed file
		numberOfCharInEncodedMessage = (count / 8);

		if (count % 8 > 0) {

			numberOfCharInEncodedMessage++;
		}

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to do 1st , 2nd , 3rd , 4th ,5th steps of compression
	 * process (All Process)
	 * 
	 * @param originalFile
	 * @param compressedFile
	 * 
	 */
	public void compress(File originalFile, File compressedFile) {

		countEeachCharacter(originalFile);// 1st
		Node huffmanTree = buildHuffmanTree(count);// 2nd
		HashMap<Integer, String> huffmanTable = buildTable(huffmanTree);// 3rd
		printHeaderOnFile(huffmanTree, compressedFile);// 4th
		pritnEncodingMessage(originalFile, compressedFile, huffmanTable); // 5th;
		out.flush();
		out.close();
		in.close();

	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getCountDistinct() {
		return countDistinct;
	}

	public int getCountNumberOfCharInOriginalFile() {
		return countNumberOfCharInOriginalFile;
	}

	public int getCountNumberOfCharInHeader() {
		return countNumberOfCharInHeader;
	}

	public int getNumberOfCharInEncodedMessage() {
		return numberOfCharInEncodedMessage;
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
