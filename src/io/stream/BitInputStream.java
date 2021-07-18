package io.stream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BitInputStream {

	/** A buufer that we used to read */
	private BufferedInputStream in;

	/** index onside a byte 0-7 */
	private int bitIndex = 7;

	/** A byte value that we read from a buffer at a time */
	private int byteValue;

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * A constructor that take a file that will be read
	 * 
	 * @param source
	 */
	public BitInputStream(File source) {

		try {

			in = new BufferedInputStream(new FileInputStream(source));
			in.mark(Integer.MAX_VALUE);
			byteValue = in.read();

		} catch (IOException e) {

			System.out.println("BitInputStream Costructor");
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will initialize some data Fields and read a next byte from a
	 * buffer
	 * 
	 */
	private void initialize() {

		try {

			this.bitIndex = 7;
			this.byteValue = in.read();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to read specific number of bits from 1 - 32
	 * 
	 * @param numberOfBits
	 * 
	 * @return The bits in int type and -1 if there exist no bits
	 * 
	 */
	public int readBits(int numberOfBits) {

		if (numberOfBits < 1 || numberOfBits > 32) {
			throw new IllegalArgumentException("Enter number between [1-32]");
		}

		// This will save a return int
		int value = 0;

		int valueIndex = 31;

		// The number of bits that will be read it will be from 0 to enter number
		int avaliableBits = 0;

		for (int i = 0; i < numberOfBits; i++) {

			if (byteValue == -1) {

				break;
			}

			int bit = byteValue & (1 << bitIndex);
			bit = (bit == 0) ? 0 : 1;
			bitIndex--;

			if (bitIndex < 0) {

				initialize();
			}

			value |= (bit << valueIndex);
			valueIndex--;
			avaliableBits++;

		}

		if (avaliableBits == 0) {

			value = -1;

		} else {

			value >>>= (32 - avaliableBits);

		}

		return value;

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method is used to reset the poistion of read from a buffer to mark position
	 */
	public void reset() {

		try {
			
			in.reset();
			byteValue = in.read();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void close() {
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
