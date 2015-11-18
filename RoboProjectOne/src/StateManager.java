
public class StateManager {
	
	private static StateManager manager = new StateManager();
	private static boolean seekState;
	private static boolean searchState;
	
	private StateManager(){};
	
	public static StateManager getInstance() {
		return manager;
	}
	
	protected static void setSeekState() {
		seekState = true;
		searchState = false;
	}
	
	protected static void setSearchState() {
		seekState = false;
		searchState = true;
	}
	
	protected static boolean getSeekState() {
		return seekState;
	}
	
	protected static boolean getSearchState() {
		return searchState;
	}
}
