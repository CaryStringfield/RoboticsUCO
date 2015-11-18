
public class StateManager {
	
	private static StateManager manager = new StateManager();
	private static int state;

	
	private StateManager(){
		state = 1;
	};
	
	public static StateManager getInstance() {
		return manager;
	}
	
	protected static void setState() {
		if (state == 0)
			state = 1;
		else
			state = 0;
	}
			
	protected static int getState() {
		return state;
	}
		
}
