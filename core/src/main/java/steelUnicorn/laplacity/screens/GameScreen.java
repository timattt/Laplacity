package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.ScreenAdapter;

import steelUnicorn.laplacity.LaplacityAssets;

public class GameScreen extends ScreenAdapter {
	
	public GameScreen() {
		initLevel(LaplacityAssets.LEVEL1_TILEMAP);
	}
	
	@Override
	public void render(float delta) {
		if (!isPlaying()) {
			return;
		}
		updateLevel(delta);
		super.render(delta);
	}

	@Override
	public void dispose() {
		disposeLevel();
		super.dispose();
	}
	
}
