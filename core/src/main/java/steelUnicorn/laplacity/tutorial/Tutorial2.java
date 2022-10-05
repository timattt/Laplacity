package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.ui.ParticleMover;
import steelUnicorn.laplacity.ui.handler.ButtonNames;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;

public class Tutorial2 extends Tutorial {

	private enum Status {
		drag_sling,
		press_flight1,
		flight1,
		select_particle,
		place_particle,
		press_flight2,
		flight2
	};
	
	private Status currentStatus = null;
	
	@Override
	public void init() {
		currentStatus = Status.drag_sling;
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;

		hand.lockAllButtons();
		hand.unlockBtn(ButtonNames.HOME);
		hand.unlockBtn(ButtonNames.SETTINGS);
		hand.unlockBtn(ButtonNames.SPEED_UP);
		
		hand.slingshotHandler.setLocked(false);
		TutorialManager.pointer.linearAnimation(GameProcess.cat.getX(), GameProcess.cat.getY(),
				GameProcess.cat.getX() - 10, GameProcess.cat.getY() + 10, 1000);
		GameProcess.gameUI.showMessage("What are those strange red things?\nLets check. Drag the slingshot to fly!");
	
		ParticleMover.setLocked(true);
	}
	
	boolean x2_pressed1 = false;
	boolean x2_pressed2 = false;
	
	@Override
	public void update() {
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
			
		switch (currentStatus) {
		case drag_sling:
			if (hand.slingshotHandler.wasSlingshotSet()) {
				hand.stopFlashing();
				TutorialManager.pointer.hide();
				currentStatus = Status.press_flight1;
				hand.slingshotHandler.setLocked(true);
				hand.slingshotHandler.changeSlingshot(-2.49f, 5f);
				
				hand.unlockBtn(ButtonNames.FLIGHT);	
				hand.startFlashing(ButtonNames.FLIGHT, 0, 0.25f, 100000);
				GameProcess.gameUI.changeMessageText("Press the flight button to start flying!");
			}
			break;
		case press_flight1:
			if (hand.wasButtonPressed(ButtonNames.FLIGHT)) {
				hand.lockBtn(ButtonNames.FLIGHT);
				hand.stopFlashing();
				currentStatus = Status.flight1;
				
				// TODO ELVEG ???
				GameProcess.gameUI.showMessage("You can press X2 button to speed up!", 1f);
				hand.startFlashing(ButtonNames.SPEED_UP, 0, 0.25f, 10000);
				//
			}
			break;
		case flight1:
			if (!x2_pressed1 && hand.wasButtonPressed(ButtonNames.SPEED_UP)) {
				hand.stopFlashing();
				x2_pressed1 = true;
			}
			if (GameProcess.currentGameMode != GameMode.FLIGHT) {
				hand.stopFlashing();
				currentStatus = Status.select_particle;
				hand.unlockBtn(ButtonNames.PROTONS);
				hand.slingshotHandler.setLocked(true);
				hand.startFlashing(ButtonNames.PROTONS, 0, 0.25f, 100000);
				GameProcess.gameUI.showMessage("As you can see they are deadly.\nLet's fly around them.\nPress the flashing button\nto select particle placer mode!");
			}
			break;
		case select_particle:
			if (hand.wasButtonPressed(ButtonNames.PROTONS)) {
				currentStatus = Status.place_particle;
				hand.lockBtn(ButtonNames.PROTONS);
				hand.stopFlashing();
				TutorialManager.pointer.linearAnimation(100, 44, 110, 54, 1000);
				GameProcess.gameUI.changeMessageText("These particles attracts the cat.\n Place it in the specified place\nso that the cat reaches the exit!");
			}
			break;
		case place_particle:
			if (hand.particleHandler.wasParticlePlaced()) {
				hand.stopFlashing();
				currentStatus = Status.press_flight2;
				GameProcess.tryToMoveStaticParticle(hand.particleHandler.particle, 100, 44);
				FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
				TutorialManager.pointer.hide();
				GameProcess.changeGameMode(GameMode.NONE);
				hand.unlockBtn(ButtonNames.FLIGHT);	
				hand.startFlashing(ButtonNames.FLIGHT, 0, 0.25f, 100000);
				GameProcess.gameUI.changeMessageText("Flying trajectory has changed!\nPress the flight button to start flying!");
			}
			break;
		case press_flight2:
			if (hand.wasButtonPressed(ButtonNames.FLIGHT)) {
				hand.lockBtn(ButtonNames.FLIGHT);
				hand.stopFlashing();
				currentStatus = Status.flight2;
				
				// TODO ELVEG ???
				GameProcess.gameUI.showMessage("You can press X2 button to speed up!", 1f);
				hand.startFlashing(ButtonNames.SPEED_UP, 0, 0.25f, 10000);
				//
			}
			break;
		case flight2:
			if (!x2_pressed2 && hand.wasButtonPressed(ButtonNames.SPEED_UP)) {
				hand.stopFlashing();
				x2_pressed2 = true;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void cleanup() {
		GameProcess.gameUI.hideMessage();
		ParticleMover.setLocked(false);
	}

}
