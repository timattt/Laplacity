package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;

public class ModeDirichlet extends GameMode {

	boolean isSoundLooping = false;

	public ModeDirichlet() {
		super("Dirichlet");
	}

	private void makeSpray(float x, float y) {
		LaplacityField.fillCircleWithRandomDensity(x, y, BRUSH_RADIUS, MAX_DENSITY);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void tap(float x, float y) {
		LaplacityAssets.playSound(LaplacityAssets.spraySound);
		makeSpray(x, y);
	}

	@Override
	public void pan(float x, float y, float dx, float dy) {
		if (!isSoundLooping) {
			LaplacityAssets.loopSound(LaplacityAssets.spraySound);
			isSoundLooping = true;
		}
		makeSpray(x, y);
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
		LaplacityAssets.stopSound(LaplacityAssets.spraySound);
		FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

	@Override
	public void panStop(float x, float y) {
		isSoundLooping = false;
		LaplacityAssets.stopSound(LaplacityAssets.spraySound);
		FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}

}
