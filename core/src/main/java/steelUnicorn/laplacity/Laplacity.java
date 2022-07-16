package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.screen.transition.impl.SlidingDirection;
import de.eskalon.commons.screen.transition.impl.SlidingInTransition;
import de.eskalon.commons.screen.transition.impl.SlidingOutTransition;
import steelUnicorn.laplacity.screens.GameScreen;
import steelUnicorn.laplacity.screens.MainMenuScreen;
import steelUnicorn.laplacity.utils.Settings;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Laplacity extends ManagedGame<ManagedScreen, ScreenTransition> {
	
	private SpriteBatch batch;

	@Override
	public void create() {
		super.create();
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
		shapeRenderer = new ShapeRenderer();
		
		camera.position.x = camera.position.y = 0;
		
		this.batch = new SpriteBatch();
		SlidingInTransition slideIn = new SlidingInTransition(batch, SlidingDirection.DOWN, 0.6f);
		SlidingOutTransition slideOut = new SlidingOutTransition(batch, SlidingDirection.UP, 0.6f);
		this.screenManager.addScreen(nameGameScreen, gameScreen);
		this.screenManager.addScreen(nameMainMenuScreen, mainMenuScreen);
		this.screenManager.addScreenTransition(nameSlideIn, slideIn);
		this.screenManager.addScreenTransition(nameSlideOut, slideOut);

		this.screenManager.pushScreen(nameMainMenuScreen, null);
	}
	
	private void loadAssets() {
		assetManager = new AssetManager();

		FileHandle[] lvls = Gdx.files.internal("levels/").list();
		for (FileHandle lvl : lvls) {
			assetManager.load(lvl.path(), Texture.class);
		}
		assetManager.load("ui/uiskin.json", Skin.class);
		assetManager.load("ui/gameicons/icons.atlas", TextureAtlas.class);
		assetManager.finishLoading();
	}
	
	@Override
	public void render () {
		shapeRenderer.setProjectionMatrix(camera.combined);
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
