package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.ui.handler.ButtonNames;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;

public class Tutorial2 extends Tutorial {

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
		currentStatus = Status.select_particle;
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;

		hand.lockAllButtons();
		hand.unlockBtn(ButtonNames.HOME);
		hand.unlockBtn(ButtonNames.PROTONS);
		hand.slingshotHandler.setLocked(true);
		hand.startFlashing(ButtonNames.PROTONS, 0, 0.25f, 100000);
		
		GameProcess.gameUI.showMessage("Press the flashing button\nselect particle placer mode!");
	}

	@Override
	public void update() {
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		
		switch (currentStatus) {
		case select_particle:
			if (hand.wasButtonPressed(ButtonNames.PROTONS)) {
				currentStatus = Status.place_particle;
				hand.lockBtn(ButtonNames.PROTONS);
				hand.stopFlashing();
				TutorialManager.pointer.linearAnimation(100, 44, 110, 54, 1000);
				GameProcess.gameUI.changeMessageText("Now put the particle in the specified place\nso that the cat reaches the exit!");
			}
			break;
		case place_particle:
			if (hand.particleHandler.wasParticlePlaced()) {
				currentStatus = Status.drag_sling;
				GameProcess.changeGameMode(GameMode.NONE);
				GameProcess.tryToMoveStaticParticle(hand.particleHandler.particle, 100, 44);
				FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
				hand.slingshotHandler.setLocked(false);
				TutorialManager.pointer.hide();
				TutorialManager.pointer.linearAnimation(GameProcess.cat.getX(), GameProcess.cat.getY(),
						GameProcess.cat.getX() - 10, GameProcess.cat.getY() + 10, 1000);
				GameProcess.gameUI.changeMessageText("Drag the sling to set cat's\nstart flight direction!");
			}
			break;
		case drag_sling:
			if (hand.slingshotHandler.wasSlingshotSet()) {
				TutorialManager.pointer.hide();
				currentStatus = Status.press_flight;
				hand.slingshotHandler.setLocked(true);
				hand.slingshotHandler.changeSlingshot(-2.49f, 5f);
				
				hand.unlockBtn(ButtonNames.FLIGHT);	
				hand.startFlashing(ButtonNames.FLIGHT, 0, 0.25f, 100000);
				GameProcess.gameUI.changeMessageText("Press the flight button to start flight!");
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

	@Override
	public void cleanup() {
		GameProcess.gameUI.hideMessage();
	}

}
