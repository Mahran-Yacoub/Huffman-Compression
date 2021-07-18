package gui;

public class Row {

	private Character character;
	private String huffmanCode;
	private Integer length;
	private Integer frequency;

	public Row() {

	}

	public Row(Character character, String huffmanCode, Integer length, Integer frequency) {

		this.character = character;
		this.huffmanCode = huffmanCode;
		this.length = length;
		this.frequency = frequency;

	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public String getHuffmanCode() {
		return huffmanCode;
	}

	public void setHuffmanCode(String huffmanCode) {
		this.huffmanCode = huffmanCode;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
	

}
