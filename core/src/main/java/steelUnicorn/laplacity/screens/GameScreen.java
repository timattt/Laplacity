package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;

import de.eskalon.commons.screen.ManagedScreen;

/**
 * Класс экрана игры.
 * @author timat
 *
 */
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
		disposeLevel();
	}

	@Override
	public void resize(int width, int height){
		gameUI.resize();
	}

	private static final Color back = new Color(46f/255f, 46f/255f, 46f/255f, 1f);
	
	@Override
	public Color getClearColor() {
		return back;
	}

	@Override
	public void render(float delta) {
		updateLevel(delta);
	}

	@Override
	public void dispose() {
		disposeLevel();
	}

}
