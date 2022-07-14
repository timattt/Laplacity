package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.ScreenAdapter;

import steelUnicorn.laplacity.Globals;
import steelUnicorn.laplacity.LaplacityAssets;
import steelUnicorn.laplacity.ui.GameInterface;

public class GameScreen extends ScreenAdapter {
	GameInterface gi;
	
	public GameScreen() {
		initLevel(LaplacityAssets.LEVEL1_TILEMAP);
		gi = new GameInterface(Globals.guiViewport);
	}
	
	@Override
	public void render(float delta) {
		if (!isPlaying()) {
			return;
		}
		updateLevel(delta);
		gi.draw();
		super.render(delta);
	}

	@Override
	public void dispose() {
		disposeLevel();
		gi.dispose();
		super.dispose();
	}
	
}
