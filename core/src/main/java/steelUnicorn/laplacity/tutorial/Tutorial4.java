package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.ParticleMover;
import steelUnicorn.laplacity.ui.handler.ButtonNames;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;

public class Tutorial4 extends Tutorial {

	private enum Status {
		press_food,
		drag_sling,
		press_flight,
		flight
	};
	
	private Status currentStatus = null;
	
	@Override
	public void init() {
		currentStatus = Status.press_food;
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		hand.slingshotHandler.setLocked(true);
		hand.lockAllButtons();
		hand.unlockBtn(ButtonNames.SPEED_UP);
		hand.unlockBtn(ButtonNames.HOME);
		hand.unlockBtn(ButtonNames.SETTINGS);

		GameProcess.gameUI.catFI.update(Globals.catFood.addLaunches(-4));

		hand.unlockBtn(ButtonNames.REWARD);
		hand.startFlashing(ButtonNames.REWARD, 0, 0.25f, 10000);
		hand.unlockBtn(ButtonNames.REWARD);
		GameProcess.gameUI.showMessage("Every flight consumes food.\nPress the flashing button to refill it.");
		ParticleMover.setLocked(true);
	}

	@Override
	public void update() {
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		
		switch (currentStatus) {
		case press_food:
			if (hand.wasButtonPressed(ButtonNames.INTER) || hand.wasButtonPressed(ButtonNames.REWARD)) {
				currentStatus = Status.drag_sling;
				hand.stopFlashing();
				hand.slingshotHandler.setLocked(false);
				TutorialManager.pointer.linearAnimation(GameProcess.cat.getX(), GameProcess.cat.getY(), GameProcess.cat.getX() - 17f, GameProcess.cat.getY() - 10f, 2000);
				GameProcess.gameUI.changeMessageText("As you can see the food restores!\nNow drag the slingshot\nto set start direction!");
			}
			break;
		case drag_sling:
			if (hand.slingshotHandler.wasSlingshotSet()) {
				currentStatus = Status.press_flight;
				hand.unlockBtn(ButtonNames.FLIGHT);
				hand.startFlashing(ButtonNames.FLIGHT, 0, 0.25f, 10000);
				hand.slingshotHandler.changeSlingshot(-20, -10);
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
		ParticleMover.setLocked(false);
	}

}
