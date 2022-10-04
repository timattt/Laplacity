package steelUnicorn.laplacity.tutorial;

import steelUnicorn.laplacity.GameProcess;

public class TutorialManager {

	private static Tutorial currentTutorial;
	public static Pointer pointer = new Pointer();
	
	public static void initLevel() {
		// SELECT
		// first
		if (GameProcess.levelNumber == 1 && GameProcess.sectionNumber == 1) {
			currentTutorial = new Tutorial1();
		}
		// second
		if (GameProcess.levelNumber == 2 && GameProcess.sectionNumber == 1) {
			currentTutorial = new Tutorial2();
		}
		// third
		if (GameProcess.levelNumber == 3 && GameProcess.sectionNumber == 1) {
			currentTutorial = new Tutorial3();
		}
		// forth
		if (GameProcess.levelNumber == 4 && GameProcess.sectionNumber == 1) {
			currentTutorial = new Tutorial4();
		}
		// fifth
		if (GameProcess.levelNumber == 5 && GameProcess.sectionNumber == 1) {
			currentTutorial = new Tutorial5();
		}
		
		if (currentTutorial != null) {
			currentTutorial.init();
		}
	}
	
	public static void draw() {
		pointer.draw();
	}
	
	public static void update() {
		pointer.update();
		if (currentTutorial != null) {
			currentTutorial.update();
		}
	}
	
	public static void cleanup() {
		pointer.hide();
		if (currentTutorial != null) {
			currentTutorial.cleanup();
			currentTutorial = null;
		}
	}

}
