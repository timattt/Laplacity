package steelUnicorn.laplacity.gameModes;

import com.badlogic.gdx.math.Vector2;

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
	public void pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		CameraManager.processPinch(initialPointer1, initialPointer2, pointer1, pointer2);
	}

	@Override
	public void pinchStop() {
		CameraManager.stopPinching();
	}

	@Override
	public void replaced() {
		CameraManager.moveTo(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y);
	}

}
