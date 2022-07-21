package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldPotentialCalculator;
import steelUnicorn.laplacity.particles.Electron;

public class ModeElectrons extends GameMode {

	public ModeElectrons() {
		super("Electrons");
	}

	@Override
	public void tap(float x, float y) {
		addStaticParticle(new Electron(x, y));
		FieldPotentialCalculator.calculateFieldPotential(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void pan(float x, float y, float dx, float dy) {
		moveCamera(-dx);
	}

}
