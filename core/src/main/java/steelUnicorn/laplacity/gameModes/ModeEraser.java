package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;

public class ModeEraser extends GameMode {

	private boolean isSoundLooping = false;

	public ModeEraser() {
		super("Eraser");
	}
	
	private boolean makeErase(float x, float y) {
		boolean encounteredSpray = LaplacityField.clearCircleDensity(x, y, BRUSH_RADIUS);
		TrajectoryRenderer.updateTrajectory();
		return  encounteredSpray;
	}
	
	@Override
	public void tap(float x, float y) {
		if (makeErase(x, y))
			LaplacityAssets.playSound(LaplacityAssets.eraserSweepSound);
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

	@Override
	public void pan(float x, float y, float dx, float dy) {
		if (makeErase(x, y) && (!isSoundLooping)) {
			LaplacityAssets.loopSound(LaplacityAssets.eraserLoopSound);
			isSoundLooping = true;
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
		LaplacityAssets.stopSound(LaplacityAssets.eraserLoopSound);
		isSoundLooping = false;
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

	@Override
	public void panStop(float x, float y) {
		LaplacityAssets.stopSound(LaplacityAssets.eraserLoopSound);
		isSoundLooping = false;
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void replaced() {
		LaplacityAssets.stopSound(LaplacityAssets.eraserLoopSound);
		isSoundLooping = false;
	}

}
