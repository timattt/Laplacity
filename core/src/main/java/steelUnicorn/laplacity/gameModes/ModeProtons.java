package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.particles.Proton;

public class ModeProtons extends GameMode {

	public ModeProtons() {
		super("Protons");
	}

	@Override
	public void tap(float x, float y) {
		addStaticParticle(new Proton(x, y));
		FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void pan(float x, float y, float dx, float dy) {
		moveCamera(-dx);
	}
	
}