package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

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
	public void pinch(float dx1, float dx2) {
		CameraManager.moveX(dx1 + dx2);
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
