package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.ParticleMover;
import steelUnicorn.laplacity.ui.handler.ButtonNames;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;
import steelUnicorn.laplacity.utils.CatFood;

public class Tutorial {
	//Сохраненное значение еды
	private int savedLaunches;
	private int savedTimerValue;

	public Tutorial() {
		//Food values saving
		savedLaunches = -1;
		savedTimerValue = -1;
		saveFoodInfo();
	}

	public void saveFoodInfo() {
		if (Globals.catFood != null && savedLaunches == -1 && savedTimerValue == -1) {
			savedLaunches = Globals.catFood.getLaunches();
			savedTimerValue = Globals.catFood.timer.getTime();

			if (GameProcess.gameUI != null) {
				GameProcess.gameUI.catFI.update(
						Globals.catFood.addLaunches(CatFood.MAX_LAUNCHES));
			}
		}
	}

	public void restoreFoodInfo() {
		if (Globals.catFood != null && savedLaunches >= 0 && savedTimerValue >= 0) {
			Globals.catFood.addLaunches(-(CatFood.MAX_LAUNCHES - savedLaunches));
			Globals.catFood.timer.setTime(savedTimerValue);
			if (GameProcess.gameUI != null) {
				GameProcess.gameUI.catFI.update(Globals.catFood.getLaunches());
			}

			savedLaunches = -1;
			savedTimerValue = -1;
		}
	}
	
	public void update(float delta) {
		
	}
	
	public void init() {
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		
		hand.lockAllButtons();
		hand.unlockBtn(ButtonNames.HOME);
		hand.unlockBtn(ButtonNames.SETTINGS);
		
		ParticleMover.setLocked(true);
	}
	
	public void cleanup() {
		GameProcess.gameUI.hideMessage();
		ParticleMover.setLocked(false);

		restoreFoodInfo();
	}

}
