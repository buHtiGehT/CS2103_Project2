import java.util.HashMap;


public class NameStorage implements DataProvider<Integer, String> {
	private final HashMap<Integer, String> _storage;
	private int _numFetches;
	
	public NameStorage(HashMap<Integer, String> storage) {
		_storage = storage;
		_numFetches = 0;
	}
	
	public String get(Integer key) {
		_numFetches++;
		return _storage.get(key);
	}
	
	public int getNumFetches() {
		return _numFetches;
	}
}
