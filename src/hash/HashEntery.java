package hash;

/**
 * The Class will be used to make the hash table array 
 * It is contains data and status as Data field
 * 
 * @author Mahran Yacoub
 *
 */
public class HashEntery <K,V> {

	private K key ;
	private V value ;
	private int status ;
	
	public HashEntery() {
		
		
	}
	
	public HashEntery(K key ,V value ,int status) {
		this.key = key ;
		this.value = value ;
		this.status = status ;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	

	
	
}
