package steelUnicorn.laplacity.gameModes;

import steelUnicorn.laplacity.CameraManager;

public class ModeNone extends GameMode {

	public ModeNone() {
		super("None");
	}

	@Override
	public void pan(float x, float y, float dx, float dy) {
		CameraManager.moveX(-dx);
	}
	
}
