package hash;

/**
 * The class that used to create hash table for String data
 * 
 * We implement this class to create closed hashing , and solve The collision by
 * Quadratic probing
 * 
 * The hashing is used to make an insert , find , delete as constant time in
 * average and O(n) in worst case if we have many collision in the same position
 * Or reHashing
 * 
 * @author Mahran Yacoub
 *
 */
public class HashMap<K, V> {

	private HashEntery<K, V>[] hashTable;

	private int sizeOfHashTable;

	private int currentSize = 0;

	/**
	 * A constructor that used to make a hash table with input size
	 * 
	 * @param size The size of hash Table
	 * 
	 */
	@SuppressWarnings("unchecked")
	public HashMap(int size) {

		this.sizeOfHashTable = size;
		hashTable = new HashEntery[size];

	}

	public HashEntery<K, V>[] getHashTable() {
		return hashTable;
	}

	/**
	 * This method is used to add a string data to hash table
	 * 
	 * @param key The data that will be added to hash Table
	 * 
	 */
	public void put(K key, V value) {

		if (sizeOfData() < sizeOfHashTable) {

			int index = key.hashCode() % sizeOfHashTable;

			for (int i = 0; i < sizeOfHashTable; i++) {

				int pos = (index + i) % sizeOfHashTable;

				if (hashTable[pos] == null) {

					hashTable[pos] = new HashEntery<K, V>(key, value, 1);
					currentSize++;
					break;
				}
			}
		} else {

			reHashing();
			put(key, value);
		}

	}

	/**
	 * This method is used to remove a string data from hash table
	 * 
	 * @param key The data that will be removed from hash Table
	 * 
	 */
	public boolean remove(K key) {

		if (isContains(key)) {

			int index = key.hashCode() % sizeOfHashTable;

			for (int i = 0; i < sizeOfHashTable; i++) {

				int pos = (index + i) % sizeOfHashTable;

				if (hashTable[pos] != null && hashTable[pos].getStatus() == 1 && hashTable[pos].getKey().equals(key)) {

					hashTable[pos].setKey(null);
					hashTable[pos].setValue(null);
					hashTable[pos].setStatus(2);
					currentSize--;
					return true;

				} else if (hashTable[pos] == null) {

					return false;
				} else {
					continue;
				}
			}

			return false;

		} else {
			return false;
		}

	}

	/**
	 * This method is used to check if a string data is exist in hash table or not
	 * 
	 * @param key The data that will be searched in hash Table
	 * 
	 * @return true:exist false:not exist
	 * 
	 */
	public boolean isContains(K key) {

		int index = key.hashCode() % sizeOfHashTable;

		for (int i = 0; i < sizeOfHashTable; i++) {
			int pos = (index + i) % sizeOfHashTable;
			if (hashTable[pos] != null && hashTable[pos].getStatus() == 1 && hashTable[pos].getKey().equals(key)) {

				return true;

			} else if (hashTable[pos] == null) {

				return false;
			} else {
				continue;
			}
		}

		return false;

	}

	/**
	 * This method is used to print hash table with double dash for empty location
	 */
	public void printHashTable() {

		for (int i = 0; i < hashTable.length; i++) {

			if (hashTable[i] != null && hashTable[i].getStatus() == 1) {

				System.out.println(hashTable[i].getKey().toString() +" ---> "+ hashTable[i].getValue());

			}

		}

	}

	/**
	 *
	 * @return the size of hash table
	 */
	public int sizeOfHashTable() {

		return hashTable.length;
	}

	/**
	 * 
	 * @return ths size of data (full locations) in hash table
	 */
	public int sizeOfData() {

		return currentSize;

	}

	/**
	 * This method is used to clear the data from hash table
	 */
	@SuppressWarnings("unchecked")
	public void clear() {

		hashTable = new HashEntery[sizeOfHashTable];
		currentSize = 0;
	}

	/**
	 * This method is used to make
	 * 
	 * rehashing : refill the data in new Hash table with large size that the first
	 * one
	 * 
	 * 
	 */
	public void reHashing() {

		int size = hashTable.length * 2;

		HashMap<K, V> h = new HashMap<K, V>(size);

		for (int i = 0; i < hashTable.length; i++) {

			if (hashTable[i] != null) {

				h.put(hashTable[i].getKey(), hashTable[i].getValue());

			}
		}

		hashTable = h.hashTable;
		this.sizeOfHashTable = hashTable.length;

	}


	/**
	 * This method is used to find the position of input data we make this method to
	 * use after chech if the data is exist in hash table
	 * 
	 * @param key the data that will find its position in hash table
	 * 
	 * @return the position of input data in hash table
	 * 
	 */
	public V find(K key) {

		int index = key.hashCode() % sizeOfHashTable;

		Integer pos = null;

		for (int i = 0; i < sizeOfHashTable; i++) {

			pos = (index + i) % sizeOfHashTable;
			 
			if (hashTable[pos] != null && hashTable[pos].getStatus() == 1 && hashTable[pos].getKey().equals(key)) {

				return hashTable[pos].getValue();

			}
		}

		return null;
	}


}
