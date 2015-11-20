
public class StateManager {
	
	private static StateManager manager = new StateManager();
	private int state;

	
	private StateManager(){
		state = 1;
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
			
	protected int getState() {
		return manager.state;
	}
		
}
