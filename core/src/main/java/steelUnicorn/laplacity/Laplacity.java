package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.Globals.*;
import static steelUnicorn.laplacity.LaplacityAssets.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import steelUnicorn.laplacity.screens.GameScreen;
import steelUnicorn.laplacity.screens.MainMenuScreen;
import steelUnicorn.laplacity.utils.Settings;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Laplacity extends Game {
	
	@Override
	public void create() {
		loadAssets();
		Settings.loadSettings();
		//logging for checking saving
		Gdx.app.log("Settings Sound", String.valueOf(Settings.getSoundVolume()));
		Gdx.app.log("Settings Music", String.valueOf(Settings.getMusicVolume()));
		
		game = this;
		camera = new OrthographicCamera(SCREEN_WORLD_WIDTH, SCREEN_WORLD_HEIGHT);
		guiViewport = new ScreenViewport();
		gameViewport = new ExtendViewport(SCREEN_WORLD_WIDTH, SCREEN_WORLD_HEIGHT, camera);
		guiViewport.setUnitsPerPixel(0.5f);
		gameScreen = new GameScreen();
		mainMenuScreen = new MainMenuScreen();
		
		camera.position.x = camera.position.y = 0;
		
		setScreen(mainMenuScreen);
	}
	
	private void loadAssets() {
		assetManager = new AssetManager();
		assetManager.load("levels/testLevel.png", Texture.class);
		assetManager.load("ui/uiskin.json", Skin.class);
		assetManager.finishLoading();
		
		LEVEL1_TILEMAP = assetManager.get("levels/testLevel.png", Texture.class);
	}
	
	@Override
	public void render () {
		shapeRenderer.setProjectionMatrix(camera.combined);
		ScreenUtils.clear(1, 1, 1, 1);
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
		Settings.saveSettings();
	}
	
}
