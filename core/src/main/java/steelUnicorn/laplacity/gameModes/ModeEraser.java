package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;

public class ModeEraser extends GameMode {

	boolean isSoundLooping = false;

	public ModeEraser() {
		super("Eraser");
	}
	
	private void makeErase(float x, float y) {
		LaplacityField.clearCircleDensity(x, y, BRUSH_RADIUS);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void tap(float x, float y) {
		LaplacityAssets.playSound(LaplacityAssets.eraserSweepSound);
		makeErase(x, y);
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

	@Override
	public void pan(float x, float y, float dx, float dy) {
		if (!isSoundLooping) {
			LaplacityAssets.loopSound(LaplacityAssets.eraserLoopSound);
			isSoundLooping = true;
		}
		makeErase(x, y);
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
		isSoundLooping = false;
		LaplacityAssets.stopSound(LaplacityAssets.eraserLoopSound);
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

	@Override
	public void panStop(float x, float y) {
		isSoundLooping = false;
		LaplacityAssets.stopSound(LaplacityAssets.eraserLoopSound);
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

}
