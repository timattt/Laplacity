package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.ui.handler.ButtonNames;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;

public class Tutorial3 extends Tutorial {

	private enum Status {
		select_particle,
		place_particle,
		drag_sling,
		press_flight,
		flight
	};
	
	private Status currentStatus = null;
	
	@Override
	public void init() {
		super.init();
		currentStatus = Status.select_particle;
		
		// buttons
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		hand.unlockBtn(ButtonNames.SPEED_UP);
		hand.unlockBtn(ButtonNames.ELECTRONS);
		hand.startFlashing(ButtonNames.ELECTRONS, 0, 0.25f, 100000);
		
		// slingshot
		hand.slingshotHandler.setLocked(true);
		
		// start message
		GameProcess.gameUI.showMessage("Red particles repels the cat!\nPress red button to select it!");
	}

	@Override
	public void update() {
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		float sz = LaplacityField.tileSize;
		
		switch (currentStatus) {
		case select_particle:
			if (hand.wasButtonPressed(ButtonNames.ELECTRONS)) {
				currentStatus = Status.place_particle;
				hand.lockBtn(ButtonNames.ELECTRONS);
				hand.stopFlashing();
				TutorialManager.pointer.linearAnimation(75*sz, 40*sz, 70*sz, 35*sz, 1000);
				TutorialManager.pointer.setFlipped(true);
				GameProcess.gameUI.changeMessageText("Now put the particle here!");
			}
			break;
		case place_particle:
			if (hand.particleHandler.wasParticlePlaced()) {
				currentStatus = Status.drag_sling;
				GameProcess.changeGameMode(GameMode.NONE);
				GameProcess.tryToMoveStaticParticle(hand.particleHandler.particle, 75*sz, 40*sz);
				FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
				hand.slingshotHandler.setLocked(false);
				TutorialManager.pointer.hide();
				TutorialManager.pointer.linearAnimation(GameProcess.cat.getX(), GameProcess.cat.getY(),
						GameProcess.cat.getX() - 10, GameProcess.cat.getY() - 2, 1000);
				GameProcess.gameUI.changeMessageText("Set cat's start direction!");
			}
			break;
		case drag_sling:
			if (hand.slingshotHandler.wasSlingshotSet()) {
				TutorialManager.pointer.hide();
				currentStatus = Status.press_flight;
				hand.slingshotHandler.setLocked(true);
				hand.slingshotHandler.changeSlingshot(-10f, -2.5f);
				
				hand.unlockBtn(ButtonNames.FLIGHT);	
				hand.startFlashing(ButtonNames.FLIGHT, 0, 0.25f, 100000);
				GameProcess.gameUI.changeMessageText("Press the flight button!");
			}
			break;
		case press_flight:
			if (hand.wasButtonPressed(ButtonNames.FLIGHT)) {
				hand.lockBtn(ButtonNames.FLIGHT);
				hand.stopFlashing();
				currentStatus = Status.flight;
				GameProcess.gameUI.hideMessage();
			}
			break;
		case flight:
			break;
		default:
			break;
		}
	}

}
