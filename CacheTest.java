import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
	private DataProvider<Integer, String> _provider;
	
	
	
	@Before
	public void setUp() {
		setUpProviders();
	}
	
	private void setUpProviders() {
		HashMap<Integer, String> storage1 = new HashMap<Integer, String>();
		storage1.put(1, "Albert");
		storage1.put(2, "Brandon");
		storage1.put(3, "Charlie");
		storage1.put(4, "Dane");
		storage1.put(5, null);
		storage1.put(6, "Fred");
		storage1.put(7, "Gregor");
		storage1.put(8, "Harry");
		storage1.put(9, "Isaac");
		storage1.put(10, "Jeremy");
		
		// A DataProvider of which holds 10 key value combinations in Integer, String format
		// Keys:   1      2       3       4    5    6    7      8     9     10
		// Values: Albert Brandon Charlie Dane null Fred Gregor Harry Isaac Jeremy
		_provider = new NameStorage(storage1);
	}
	
	@Test
	public void cacheDoesNotCallProvider() {
		DataProvider<Integer,String> provider = _provider;
		Cache<Integer, String> cache = new LRUCache<Integer, String>(provider, 3);
		
		cache.get(9);
		cache.get(7);
		cache.get(1);
		assertEquals(((NameStorage) _provider).getNumFetches(), 3);
		
		cache.get(7);
		cache.get(1);
		cache.get(1);
		cache.get(9);
		assertEquals(((NameStorage) _provider).getNumFetches(), 3);
	}
	
	@Test
	public void getProperValue() {
		DataProvider<Integer,String> provider = _provider;
		Cache<Integer, String> cache = new LRUCache<Integer, String>(provider, 3);
		
		assertEquals(cache.get(1), "Albert");
		assertEquals(cache.get(2), "Brandon");
		assertNull(cache.get(5));
		
		//re-call key 1
		assertEquals(cache.get(1), "Albert");
		
		//evict key 2
		assertEquals(cache.get(3), "Charlie");
		assertEquals(cache.get(2), "Brandon");
	}
	
	@Test
	public void leastRecentlyUsedIsCorrect () {
		DataProvider<Integer,String> provider = _provider;
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		
		//Add 5 objects to cache
		cache.get(3);
		cache.get(6);
		cache.get(8);
		cache.get(9);
		cache.get(1);
		assertEquals(cache.getNumMisses(), 5);
		
		
		//3 gets evicted
		cache.get(2);
		//6 gets evicted, 3 should not hit
		cache.get(3);
		assertEquals(cache.getNumMisses(), 7);
		
		//re-get 8, no added to miss
		cache.get(8);
		//evict 9, not 8
		cache.get(7);
		//call 8 again to make sure it was not evicted
		cache.get(8);
		assertEquals(cache.getNumMisses(), 8);
		
		//call 9 to prove 
		cache.get(9);
		assertEquals(cache.getNumMisses(), 9);
		
		
		//contains a null value for key 5, should still work properly
		cache.get(5);
		cache.get(5);
		assertEquals(cache.getNumMisses(), 10);
	}
}
