package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;

public class ModeEraser extends GameMode {

	public ModeEraser() {
		super("Eraser");
	}
	
	private void makeErase(float x, float y) {
		LaplacityField.clearCircleDensity(x, y, BRUSH_RADIUS);
		FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void tap(float x, float y) {
		makeErase(x, y);
	}

	@Override
	public void pan(float x, float y, float dx, float dy) {
		makeErase(x, y);
	}
	
	@Override
	public void pinch(float dx1, float dx2) {
		moveCamera(dx1 + dx2);
	}

}
