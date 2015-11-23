
public class StateManager {
	
	private static StateManager manager = new StateManager();
	private int state;

	
	private StateManager(){
		// looking around for stuff
		state = 2;
	};
	
	public synchronized static StateManager getInstance() {
		if (manager == null)
			manager = new StateManager();
		return manager;
	}
	
	protected void setState() {
		if (manager.state == 0)
			manager.state = 1;
		else
			manager.state = 0;
	}
	protected void setState(int s) {
			manager.state = s;
	}
			
	protected int getState() {
		return manager.state;
	}
		
}
