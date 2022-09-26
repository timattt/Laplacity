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
		hand.lockBtn(ButtonNames.PAUSE);
	}
	
	@Override
	public void update() {
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		
		switch (currentStatus) {
		case drag_sling:
			if (hand.slingshotHandler.wasSlingshotSet()) {
				currentStatus = Status.press_flight;
				hand.unlockBtn(ButtonNames.FLIGHT);
				hand.startFlashing(ButtonNames.FLIGHT, 0, 0.25f, 10000);
				hand.slingshotHandler.changeSlingshot(10, 0);
			}
			break;
		case press_flight:
			if (hand.wasButtonPressed(ButtonNames.FLIGHT)) {
				currentStatus = Status.flight;
			}
			break;
		case flight:
			break;
		}
	}

}
