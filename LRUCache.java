import java.util.HashMap;



/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {

	private DataProvider<T, U> thisProvider;
	private int thisCapacity;
	private int numMisses;
	private HashMap<T, U> cacheMap = new HashMap<>();
	private LinkedList cacheLL = new LinkedList();

	class Node{
		U data;
		Node previous;
		Node next;
		Node(U d){
			data = d;
			next = null;
			previous = null;
			
		}
	}
	class LinkedList{
		Node head;
	}

	/**
	 * @param provider
	 *            the data provider to consult for a cache miss
	 * @param capacity
	 *            the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache(DataProvider<T, U> provider, int capacity) {
		thisProvider = provider;
		thisCapacity = capacity;
		numMisses = 0;
		cacheLL.head = new Node(null);
		

	}

	/**
	 * Returns the value associated with the specified key.
	 * 
	 * @param key
	 *            the key
	 * @return the value associated with the key
	 */
	public U get(T key) {
		
		return thisProvider.get(key); // TODO -- implement!
	}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * 
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses() {
		return numMisses;
	}
}
