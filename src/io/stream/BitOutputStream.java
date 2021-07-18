package io.stream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitOutputStream {

	/** A buufer that we used to write */
	private BufferedOutputStream out;

	/** index onside a byte 0-7 */
	private int bitIndex = 7;

	/** A byte value that we write from a buffer at a time */
	private int byteValue;

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * A constructor that take a file that will be write on
	 * 
	 * @param source
	 */
	public BitOutputStream(File Destination) {

		try {

			out = new BufferedOutputStream(new FileOutputStream(Destination));

		} catch (IOException e) {

			System.out.println("BitOutputStream Costructor");
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will initialize some data Fields and write a byte fon a buffer
	 * 
	 */
	private void initialize() {

		try {

			this.bitIndex = 7;
			out.write(byteValue);
			this.byteValue = 0;

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to write specific number of bits from 1 - 32
	 * 
	 * @param numberOfBits
	 * 
	 * 
	 */
	public void writeBits(int numberOfBits, int value) {

		if (numberOfBits < 1 || numberOfBits > 32) {
			throw new IllegalArgumentException("Enter number between [1-32]");
		}

		int shift = numberOfBits - 1;

		for (int i = 0; i < numberOfBits; i++) {

			int bit = value & (1 << shift);
			shift--;
			bit = (bit == 0) ? 0 : 1;
			byteValue |= (bit << bitIndex);
			bitIndex--;
			if (bitIndex < 0) {

				initialize();
			}

		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will be used to write a buffer on a file The buffer will flush
	 * automatic if fill all , but you have to flush it manually if not fill all
	 * 
	 */
	public void flush() {

		try {

			out.flush();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void close() {

		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
