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
		CameraManager.moveTo(GameProcess.mainParticle.getX(),GameProcess.mainParticle.getY());
	}

	@Override
	public void replaced() {
		CameraManager.moveTo(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y);
	}

}
