package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;

public class TutorialManager {

	private static Tutorial currentTutorial;
	
	public static void initLevel() {
		// SELECT
		// first
		if (GameProcess.levelNumber == 1 && GameProcess.sectionNumber == 1) {
			currentTutorial = new Tutorial1();
		}
		
		if (currentTutorial != null) {
			currentTutorial.init();
		}
	}
	
	public static void update() {
		if (currentTutorial != null) {
			currentTutorial.update();
		}
	}
	
	public static void cleanup() {
		if (currentTutorial != null) {
			currentTutorial.cleanup();
			currentTutorial = null;
		}
	}

}
