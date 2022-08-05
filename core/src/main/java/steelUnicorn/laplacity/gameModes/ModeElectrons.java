package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.particles.Electron;
import steelUnicorn.laplacity.ui.ParticleMover;

public class ModeElectrons extends GameMode {

	public ModeElectrons() {
		super("Electrons");
	}

	@Override
	public void tap(float x, float y) {
		LaplacityAssets.playSound(LaplacityAssets.placeSound);;
		addStaticParticle(new Electron(x, y));
		FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void pan(float x, float y, float dx, float dy) {
		if (!ParticleMover.isMoving()) {
			CameraManager.moveX(-dx);
		}
	}

	@Override
	public void touchDown(float x, float y) {
		ParticleMover.tryToStartMoving(x, y);
	}

	@Override
	public void touchUp(float x, float y) {
		ParticleMover.stopMoving();
	}

	@Override
	public void touchDragged(float x, float y) {
		ParticleMover.tryToMove(x, y);
	}

}
