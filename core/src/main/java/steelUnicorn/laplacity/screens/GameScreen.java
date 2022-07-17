package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Color;

import de.eskalon.commons.screen.ManagedScreen;

public class GameScreen extends ManagedScreen {

	public GameScreen() {
	}

	@Override
	public void create() {
		addInputProcessor(inputMultiplexer);
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void hide() {
	}

	@Override
	public void resize(int width, int height){
	}

	@Override
	public Color getClearColor() {
		return Color.WHITE;
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
