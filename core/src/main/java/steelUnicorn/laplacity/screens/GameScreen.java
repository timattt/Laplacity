package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import de.eskalon.commons.screen.ManagedScreen;

public class GameScreen extends ManagedScreen {

	public GameScreen() {
	}

	@Override
	public void create() {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMultiplexer);
		super.show();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void resize(int width, int height){
	}

	@Override
	public void render(float delta) {
		if (!isPlaying()) {
			return;
		}
		updateLevel(delta);
	}

	@Override
	public void dispose() {
		disposeLevel();
	}

}
