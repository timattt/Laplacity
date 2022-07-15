package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import steelUnicorn.laplacity.Globals;
import steelUnicorn.laplacity.LaplacityAssets;
import steelUnicorn.laplacity.ui.GameInterface;
import com.badlogic.gdx.input.GestureDetector;

public class GameScreen extends ScreenAdapter {
	private GameInterface gi;

	public GameScreen() {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(gameUI);
		super.show();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		super.hide();
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

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new GestureDetector(controller));
		super.show();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		super.hide();
	}
	
}
