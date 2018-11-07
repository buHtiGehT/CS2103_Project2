import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {
	final private int _capacity;
	final private DataProvider<T, U> _provider;
	final private HashMap<T, Node<T, U>> _cache;
	private int _numMisses, _currentSize;
	private LinkedList<T, U> _LRUList;
	
	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		_provider = provider;
		_capacity = capacity;
		
		//capacity is multiplied by 3 to reduce chances of collisions
		//1.0f load capacity so hashmap never resizes
		_cache = new HashMap<T, Node<T, U>>(_capacity*3, 1.0f);
		_currentSize = _numMisses = 0;
		_LRUList = new LinkedList<T, U>();
	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		if (_cache.containsKey(key)) {
			Node<T, U> valueNode = _cache.get(key);
			U value = valueNode._data;
			bringToMRU(key, valueNode);
			return value;
		} else {
			U value = _provider.get(key);
			addToCache(key, value);	
			_numMisses++;
			return value;
		}
	}
	
	/**
	 * Tail of the list is Most Recently Used item. Head is Least Recently Used.
	 * Bring called value to Tail and sow rest of linked list around.
	 * @param value the value of the called key from get
	 */
	private void bringToMRU(T key, Node<T, U> valueNode) {
		U value = valueNode._data;
		if (_LRUList._head.equals(valueNode)) {
			_LRUList._head = valueNode._next;
		}
		valueNode = valueNode._next;
		_LRUList.add(key, value);
		//.add() adds 1 to numElements, but does not take into account 
		//the removal of a node so a subtract 1 is required
		_LRUList._numElements--;
	}
	
	/**
	 * Add the called key value pair to the cache and evict the Least Recently Used 
	 * key-value combo.
	 * @param key the key
	 * @param value the value associated with the key
	 */
	private void addToCache(T key, U value) {
		if (_currentSize < _capacity) {
			_LRUList.add(key, value);
			_currentSize++;
			_cache.put(key, _LRUList._tail);
		} else {
			_cache.remove(_LRUList._head._key);
			_LRUList._head = _LRUList._head._next;
			_LRUList.add(key, value);
			_LRUList._numElements--;
			_cache.put(key, _LRUList._tail);
		}
	}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return _numMisses;
	}
	
	private static class LinkedList<W, X> {
		private Node<W, X> _head, _tail;
		private int _numElements;
		
		public LinkedList() {
			_head = _tail = null;
			_numElements = 0;
		}
		
		public void add(W key, X data) {
			final Node<W, X> node = new Node<W, X>(key, data, null);
			if (_head == null) {
				_head = node;
				_tail = node;
			} else {
				_tail._next = node;
				_tail = node;
			}
			_numElements++;
		}
	}
	
	private static class Node<Y, Z> {
		private Y _key;
		private Z _data;
		private Node<Y, Z> _next;
		
		public Node(Y key, Z data, Node<Y, Z> next) {
			_key = key;
			_data = data;
			_next = next;
		}
	}
}
