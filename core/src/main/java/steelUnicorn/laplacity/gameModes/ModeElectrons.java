package steelUnicorn.laplacity.gameModes;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.chargedParticles.ChargedParticle;
import steelUnicorn.laplacity.chargedParticles.Electron;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.ui.ParticleMover;

public class ModeElectrons extends GameMode {

	public ModeElectrons() {
		super("Electrons");
	}

	@Override
	public void tap(float x, float y) {
		ChargedParticle particle = new Electron(x, y);
		if (tryToAddStaticParticle(particle)) {
			gameUI.guiHandler.particleHandler.placeParticle(particle);
		}
		FieldCalculator.initPotentialCalculation(LaplacityField.tiles);
		TrajectoryRenderer.updateTrajectory();
	}
	
	@Override
	public void pan(float x, float y, float dx, float dy) {
		if (!ParticleMover.isMoving()) {
			CameraManager.move(-dx, dy);
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
	public void touchDown(float x, float y) {
		
	}

	@Override
	public void touchUp(float x, float y) {
		ParticleMover.stopMoving(x, y);
	}

	@Override
	public void touchDragged(float x, float y) {
		ParticleMover.tryToMove(x, y);
	}

}
