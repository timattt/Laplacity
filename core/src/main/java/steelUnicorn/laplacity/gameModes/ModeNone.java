package steelUnicorn.laplacity.gameModes;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.ui.ParticleMover;

public class ModeNone extends GameMode {

	public ModeNone() {
		super("None");
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
