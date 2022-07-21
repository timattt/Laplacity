package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldPotentialCalculator;

public class ModeDirichlet extends GameMode {

	public ModeDirichlet() {
		super("Dirichlet");
	}

	private void makeSpray(float x, float y) {
		LaplacityField.fillCircleWithRandomDensity(x, y, BRUSH_RADIUS, MAX_DENSITY);
		FieldPotentialCalculator.calculateFieldPotential(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void tap(float x, float y) {
		makeSpray(x, y);
	}

	@Override
	public void pan(float x, float y, float dx, float dy) {
		makeSpray(x, y);
	}

	@Override
	public void pinch(float dx1, float dx2) {
		moveCamera(dx1 + dx2);
	}

}
