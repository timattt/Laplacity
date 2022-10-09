package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.ui.ParticleMover;
import steelUnicorn.laplacity.ui.handler.ButtonNames;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;

public class Tutorial {
	
	public Tutorial() {
	}
	
	public void update() {
		
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
	}

}
