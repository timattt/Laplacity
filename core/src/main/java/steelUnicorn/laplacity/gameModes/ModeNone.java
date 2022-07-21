package steelUnicorn.laplacity.gameModes;

public class ModeNone extends GameMode {

	public ModeNone() {
		super("None");
	}

	@Override
	public void pan(float x, float y, float dx, float dy) {
		moveCamera(-dx);
	}
	
}
