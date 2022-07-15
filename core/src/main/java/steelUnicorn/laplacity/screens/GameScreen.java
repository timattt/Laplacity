package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.input.GestureDetector;

public class GameScreen extends ScreenAdapter {
	
	public GameScreen() {
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
