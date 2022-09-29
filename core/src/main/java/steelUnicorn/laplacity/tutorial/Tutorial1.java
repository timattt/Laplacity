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
	
	private Status currentStatus = null;
	
	@Override
	public void init() {
		currentStatus = Status.drag_sling;
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;

		hand.lockAllButtons();
		hand.unlockBtn(ButtonNames.HOME);
		hand.slingshotHandler.setLocked(false);
		TutorialManager.pointer.linearAnimation(GameProcess.cat.getX() + 3f, GameProcess.cat.getY(), GameProcess.cat.getX() + 22f, GameProcess.cat.getY(), 2000);
		GameProcess.gameUI.showMessage("Drag the slingshot\nto set start direction!");
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
				hand.slingshotHandler.changeSlingshot(20, 0);
				hand.slingshotHandler.setLocked(true);
				TutorialManager.pointer.hide();
				GameProcess.gameUI.changeMessageText("Press the flashing button to start flying!");
			}
			break;
		case press_flight:
			if (hand.wasButtonPressed(ButtonNames.FLIGHT)) {
				currentStatus = Status.flight;
				GameProcess.gameUI.hideMessage();
			}
			break;
		case flight:
			break;
		}
	}

	@Override
	public void cleanup() {
		GameProcess.gameUI.hideMessage();
	}

}
