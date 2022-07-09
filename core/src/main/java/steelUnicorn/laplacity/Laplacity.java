package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import steelUnicorn.laplacity.screens.GameScreen;
import steelUnicorn.laplacity.screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Laplacity extends Game {
	
	@Override
	public void create() {
		loadAssets();
		
		game = this;
		camera = new OrthographicCamera(100, 100);
		guiViewport = new ScreenViewport();
		gameViewport = new ExtendViewport(100, 100, camera);
		guiViewport.setUnitsPerPixel(0.5f);
		gameScreen = new GameScreen();
		mainMenuScreen = new MainMenuScreen();
		
		camera.position.x = camera.position.y = 0;
		
		setScreen(mainMenuScreen);
	}
	
	private void loadAssets() {
		assetManager = new AssetManager();

		
		assetManager.finishLoading();
	}
	
	@Override
	public void render () {
		ScreenUtils.clear(0, 0.6f, 0, 1);
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		guiViewport.update(width, height, true);
		gameViewport.update(width, height, false);
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		super.dispose();
		assetManager.dispose();
	}
	
}