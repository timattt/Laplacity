package steelUnicorn.laplacity.gameModes;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;

public class ModeFlight extends GameMode {

	public ModeFlight() {
		super("Flight");
	}

	@Override
	public void update() {
		CameraManager.moveToX(GameProcess.mainParticle.getX());
	}

	@Override
	public void replaced() {
		CameraManager.moveToX(LaplacityField.electronStartPos.x);
	}

}
