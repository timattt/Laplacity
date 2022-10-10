package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.gameModes.GameMode;
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
		super.init();
		currentStatus = Status.drag_sling;
		
		// buttons
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		
		// slingshot
		hand.slingshotHandler.setLocked(false);
		TutorialManager.pointer.linearAnimation(GameProcess.cat.getX(), GameProcess.cat.getY(),
				GameProcess.cat.getX() - 10, GameProcess.cat.getY() + 10, 1000);
		
		// start message
		GameProcess.gameUI.showMessage("Strange red lines. What is this?\nLet's check!");
	}
	
	//boolean x2_pressed1 = false;
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
				
				GameProcess.gameUI.hideMessage();
				//GameProcess.gameUI.showMessage("You can press X2 button to speed up!", 3f);
				//hand.startFlashing(ButtonNames.SPEED_UP, 0, 0.25f, 10000);
			}
			break;
		case flight1:
			//if (!x2_pressed1 && hand.wasButtonPressed(ButtonNames.SPEED_UP)) {
			//	hand.stopFlashing();
			//	GameProcess.gameUI.hideMessage();
			//	x2_pressed1 = true;
			//}
			if (GameProcess.currentGameMode != GameMode.FLIGHT) {
				hand.stopFlashing();
				currentStatus = Status.select_particle;
				hand.unlockBtn(ButtonNames.PROTONS);
				hand.slingshotHandler.setLocked(true);
				hand.startFlashing(ButtonNames.PROTONS, 0, 0.25f, 100000);
				GameProcess.gameUI.showMessage("It's lasers! You need to avoid them!\n\nPress the flashing button\nto select blue particle!");
			}
			break;
		case select_particle:
			if (hand.wasButtonPressed(ButtonNames.PROTONS)) {
				currentStatus = Status.place_particle;
				hand.lockBtn(ButtonNames.PROTONS);
				hand.stopFlashing();
				TutorialManager.pointer.linearAnimation(100, 44, 110, 54, 1000);
				GameProcess.gameUI.changeMessageText("It attracts the cat.\n Place it here!");
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
				GameProcess.gameUI.changeMessageText("The trajectory has changed!\nPress the flight button to start!");
			}
			break;
		case press_flight2:
			if (hand.wasButtonPressed(ButtonNames.FLIGHT)) {
				hand.lockBtn(ButtonNames.FLIGHT);
				hand.unlockBtn(ButtonNames.SPEED_UP);
				hand.stopFlashing();
				currentStatus = Status.flight2;
				
				GameProcess.gameUI.showMessage("Press X2 button to speed up!", 2.5f);
				hand.startFlashing(ButtonNames.SPEED_UP, 0, 0.25f, 10000);
			}
			break;
		case flight2:
			if (!x2_pressed2 && hand.wasButtonPressed(ButtonNames.SPEED_UP)) {
				hand.stopFlashing();
				GameProcess.gameUI.hideMessage();
				x2_pressed2 = true;
			}
			break;
		default:
			break;
		}
	}

}
