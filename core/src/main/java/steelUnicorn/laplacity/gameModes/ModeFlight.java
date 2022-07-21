package steelUnicorn.laplacity.gameModes;

import steelUnicorn.laplacity.GameProcess;

public class ModeFlight extends GameMode {

	public ModeFlight() {
		super("Flight");
	}

	@Override
	public void update() {
		setCamera(GameProcess.mainParticle.getX());
	}

}
