package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.tutorial.PathDragManager;

public class ModeDirichlet extends GameMode {

	boolean isSoundLooping = false;

	public ModeDirichlet() {
		super("Dirichlet");
	}

	private boolean makeSpray(float x, float y) {
		if (!PathDragManager.isTouchedCorrect(x, y)) {
			return false;
		}
		float totalChange = LaplacityField.fillCircleWithRandomDensity(x, y, BRUSH_RADIUS, BRUSH_DENSITY_POWER);
		TrajectoryRenderer.updateTrajectory();
		return totalChange > 0.001f;
	}
	
	@Override
	public void tap(float x, float y) {
		LaplacityAssets.playSound(LaplacityAssets.spraySound);
		makeSpray(x, y);
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

	private void tryToStopSoundLooping() {
		if (isSoundLooping) {
			isSoundLooping = false;
			LaplacityAssets.stopSound(LaplacityAssets.spraySound);
		}
	}
	
	private void tryToStartSoundLooping() {
		if (!isSoundLooping) {
			LaplacityAssets.loopSound(LaplacityAssets.spraySound);
			isSoundLooping = true;
		}
	}

	@Override
	public void pan(float x, float y, float dx, float dy) {
		boolean changedSomething = makeSpray(x, y);
		
		if (changedSomething) {
			tryToStartSoundLooping();
		} else {
			tryToStopSoundLooping();
		}
	}

	@Override
	public void pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		CameraManager.processPinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override
	public void pinchStop() {
		CameraManager.stopPinching();
	}

	@Override
	public void touchUp(float x, float y) {
		tryToStopSoundLooping();
		TrajectoryRenderer.updateTrajectory();
	}

	@Override
	public void panStop(float x, float y) {
		tryToStopSoundLooping();
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

	@Override
	public void replaced() {
		tryToStopSoundLooping();
	}

}
