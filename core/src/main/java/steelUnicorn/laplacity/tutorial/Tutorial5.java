package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.ui.handler.ButtonNames;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;

public class Tutorial5 extends Tutorial {

	private enum Status {
		select_spray,
		spread_spray,
		drag_sling,
		press_flight,
		flight
	};
	
	private Status currentStatus;

	@Override
	public void init() {
		super.init();
		currentStatus = Status.select_spray;
		
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
		
		// buttons
		hand.unlockBtn(ButtonNames.SPEED_UP);
		hand.unlockBtn(ButtonNames.DIRICHLET);
		hand.startFlashing(ButtonNames.DIRICHLET, 0, 0.25f, 100000);
		
		// sligshot
		hand.slingshotHandler.setLocked(true);
		
		// start message
		GameProcess.gameUI.showMessage("Select spray!\nIt will create a cloud that attracts the cat!");
	}	
	
	private static float[][] targets = new float[][] {
		{88, 16},
		{88, 15},
		{88, 14},
		{88, 13},
		{88, 12},
		{87, 11},
		{86, 9},
		{85, 8},
		{84, 7},
		{82, 6},
		{80, 6},
		{78, 6},
		{76, 6},
		{74, 6},
		{72, 6},
		{70, 6},
		{68, 6},
		{66, 6},
		{64, 7},
		{62, 7},
		{60, 7},
		{58, 7},
		{56, 7},
		{54, 7},
		{52, 7},
		{50, 7},
		{49, 8},
		{48, 8},
		{46, 9},
		{44, 10},
		{42, 11},
		{40, 12},
		{38, 13},
		{37, 14},
		{36, 15},
		{36, 16},
		{36, 17},
		{34, 18},
		{32, 19},
		{30, 20},
		{28, 19},
		{26, 21},
		{24, 22},
		{23, 23},
		{22, 24},
		{21, 25},
		{21, 26},
		{21, 27},
	}; 
	
	@Override
	public void update() {
		GameInterfaceHandler hand = GameProcess.gameUI.guiHandler;
			
		switch (currentStatus) {
		case select_spray:
			if (hand.wasButtonPressed(ButtonNames.DIRICHLET)) {
				currentStatus = Status.spread_spray;
				hand.lockBtn(ButtonNames.DIRICHLET);
				hand.stopFlashing();
				PathDragManager.newTask(targets);
				GameProcess.gameUI.showMessage("Now you may spread some!", 2f);
			}
			break;
		case spread_spray:
			PathDragManager.update();
			if (PathDragManager.isCompleted()) {
				GameProcess.clearLevel();
				for (float[] p : targets) {
					LaplacityField.fillCircleWithRandomDensity(p[0] * LaplacityField.tileSize, p[1]* LaplacityField.tileSize, 3f*LaplacityField.tileSize, 2*GameProcess.MAX_DENSITY);
				}
				FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
				TrajectoryRenderer.updateTrajectory();
				currentStatus = Status.drag_sling;
				PathDragManager.cleanup();
				
				GameProcess.changeGameMode(GameMode.NONE);
				hand.slingshotHandler.setLocked(false);
				TutorialManager.pointer.hide();
				TutorialManager.pointer.linearAnimation(GameProcess.cat.getX(), GameProcess.cat.getY(),
						GameProcess.cat.getX() - 5, GameProcess.cat.getY() - 10, 1000);
				TutorialManager.pointer.setFlipped(true);
				GameProcess.gameUI.showMessage("Set cat's start direction!");
			}
			break;
		case drag_sling:
			if (hand.slingshotHandler.wasSlingshotSet()) {
				hand.slingshotHandler.setLocked(true);
				TutorialManager.pointer.hide();
				hand.slingshotHandler.changeSlingshot(-6.5f, -2f);
				currentStatus = Status.press_flight;
				GameProcess.gameUI.changeMessageText("Press the flight button!");
				hand.unlockBtn(ButtonNames.FLIGHT);
				hand.startFlashing(ButtonNames.FLIGHT, 0, 0.25f, 100000);
				GameProcess.gameUI.changeMessageText("Press the flight button!");
			}
			break;
		case press_flight:
			/*
			if (true) {
			GameProcess.clearLevel();
			hand.slingshotHandler.changeSlingshot(-6.5f, -2f);
			
			for (float[] p : targets) {
				LaplacityField.fillCircleWithRandomDensity(p[0] * LaplacityField.tileSize, p[1]* LaplacityField.tileSize, 3f*LaplacityField.tileSize, 2*GameProcess.MAX_DENSITY);
			}
			}
			
			FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
			for (int i = 0; FieldCalculator.isCalculating(); i++) {
				FieldCalculator.iterate();
				
			}
			
			TrajectoryRenderer.updateTrajectory();
			*/
			if (hand.wasButtonPressed(ButtonNames.FLIGHT)) {
				currentStatus = Status.flight;
				GameProcess.gameUI.hideMessage();
			}
			break;
		default:
			break;
		}
	}

}
