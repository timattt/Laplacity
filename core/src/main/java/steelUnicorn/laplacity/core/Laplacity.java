package steelUnicorn.laplacity.core;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import steelUnicorn.laplacity.screens.GameScreen;
import steelUnicorn.laplacity.screens.MainMenuScreen;
import steelUnicorn.laplacity.screens.WinScreen;
import steelUnicorn.laplacity.utils.AdHandler;
import steelUnicorn.laplacity.utils.Settings;

/** Класс запуска игры. Инициализируем поля из класса Globals. Загружаем assets. */
public class Laplacity extends ManagedGame<ManagedScreen, ScreenTransition> {
	
	private SpriteBatch transitionBatch;
	private AdHandler adHandler;
	
	public Laplacity(AdHandler adHand) {
		adHandler = adHand;
	}

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
		guiViewport = new ExtendViewport(UI_WORLD_WIDTH, UI_WORLD_HEIGHT);
		gameViewport = new ExtendViewport(SCREEN_WORLD_WIDTH, SCREEN_WORLD_HEIGHT, camera);
		gameScreen = new GameScreen();
		mainMenuScreen = new MainMenuScreen();
		winScreen = new WinScreen();
		shapeRenderer = new ShapeRenderer();
		inputMultiplexer = new InputMultiplexer();
		
		camera.position.x = SCREEN_WORLD_WIDTH / 2;
		camera.position.y = SCREEN_WORLD_HEIGHT / 2;
		
		// transition
		this.transitionBatch = new SpriteBatch();
		BlendingTransition slideIn = new BlendingTransition(transitionBatch, 1f);
		BlendingTransition slideOut = new BlendingTransition(transitionBatch, 1f);
		this.screenManager.addScreen(nameGameScreen, gameScreen);
		this.screenManager.addScreen(nameMainMenuScreen, mainMenuScreen);
		this.screenManager.addScreen(nameWinScreen, winScreen);
		this.screenManager.addScreenTransition(nameSlideIn, slideIn);
		this.screenManager.addScreenTransition(nameSlideOut, slideOut);

		this.screenManager.pushScreen(nameMainMenuScreen, null);
	}
	
	private void loadAssets() {
		assetManager = new AssetManager();

		FileHandle[] lvls = Gdx.files.internal("levels/").list();
		TOTAL_LEVELS_AVAILABLE = lvls.length;
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
		transitionBatch.dispose();
		assetManager.dispose();
		Settings.saveSettings();
	}
	
	public void showInterstitial() {
		if (adHandler != null) {
			adHandler.showOrLoadInterstital();
		}
	}
	
}
