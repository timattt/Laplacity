package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.ui.handler.ButtonNames;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;

public class Tutorial1 extends Tutorial {

	private enum Status {
		drag_sling,
		press_flight,
		flight
	};
	
	private Status currentStatus;
	
	public Tutorial1() {
		currentStatus = Status.drag_sling;
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		
		hand.lockBtn(ButtonNames.CLEAR);
		hand.lockBtn(ButtonNames.DIRICHLET);
		hand.lockBtn(ButtonNames.ELECTRONS);
		hand.lockBtn(ButtonNames.PROTONS);
		hand.lockBtn(ButtonNames.FLIGHT);
		hand.lockBtn(ButtonNames.ERASER);
		hand.lockBtn(ButtonNames.MODE_SELECTOR);
	}
	
	@Override
	public void update() {
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		
		switch (currentStatus) {
		case drag_sling:
			if (hand.slingshotHandler.wasSlingshotSet()) {
				currentStatus = Status.drag_sling;
			}
			break;
		case press_flight:
			
			break;
		case flight:
			
			break;
		}
	}

}
