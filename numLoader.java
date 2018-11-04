import java.util.*;
public class numLoader implements DataProvider<Integer , String> {
	private final ArrayList<String>  _stringRootDirectory;
	//private final int _givenLocationToLoad = 0;
	public numLoader(ArrayList<String> stringRootDirectory) {
		_stringRootDirectory= stringRootDirectory;
	}
	
	public String loadStringFromDiskAtDirectory(Integer key, ArrayList<String> value){
		return value.get(key);
	}

	public String get(Integer key) {
		return loadStringFromDiskAtDirectory(key, _stringRootDirectory);
	}
}