package steelUnicorn.laplacity.core;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.screens.GameScreen;
import steelUnicorn.laplacity.screens.LevelsScreen;
import steelUnicorn.laplacity.screens.LoadingScreen;
import steelUnicorn.laplacity.screens.MainMenuScreen;
import steelUnicorn.laplacity.screens.WinScreen;
import steelUnicorn.laplacity.ui.CatFood;
import steelUnicorn.laplacity.utils.AdHandler;
import steelUnicorn.laplacity.utils.LevelsParser;
import steelUnicorn.laplacity.utils.PlayerProgress;
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
		catFood = new CatFood();
		progress = new PlayerProgress();

		CameraManager.init();
		game = this;
		guiViewport = new ExtendViewport(UI_WORLD_WIDTH, UI_WORLD_HEIGHT);
		gameViewport = CameraManager.createViewport();
		gameScreen = new GameScreen();
		mainMenuScreen = new MainMenuScreen();
		winScreen = new WinScreen();
		levelsScreen = new LevelsScreen();
		loadingScreen = new LoadingScreen();
		shapeRenderer = new ShapeRenderer();
		inputMultiplexer = new InputMultiplexer();
		
		// transition
		this.transitionBatch = new SpriteBatch();
		BlendingTransition slideIn = new BlendingTransition(transitionBatch, 1f);
		BlendingTransition slideOut = new BlendingTransition(transitionBatch, 1f);
		this.screenManager.addScreen(nameGameScreen, gameScreen);
		this.screenManager.addScreen(nameMainMenuScreen, mainMenuScreen);
		this.screenManager.addScreen(nameWinScreen, winScreen);
		this.screenManager.addScreen(nameLevelsScreen, levelsScreen);
		this.screenManager.addScreen(nameLoadingScreen, loadingScreen);
		this.screenManager.addScreenTransition(nameSlideIn, slideIn);
		this.screenManager.addScreenTransition(nameSlideOut, slideOut);

		LaplacityAssets.changeTrack("music/main theme_drop.ogg");
		this.screenManager.pushScreen(nameMainMenuScreen, null);
	}
	
	private void loadRec(String path, Class<?> cl) {
		FileHandle[] sections = Gdx.files.internal(path).list();
		for (FileHandle fh : sections) {
			if (fh.isDirectory()) {
				loadRec(fh.path(), cl);
			} else {
				assetManager.load(fh.path(), cl);
			}
		}
	}
	
	private void loadAssets() {
		assetManager = new AssetManager();

		// levels
		//Создаем мапу где ключи - номер секции, а значения - пути до уровней
		LevelsParser.loadAssets(assetManager);
		
		// ui
		assetManager.load("ui/uiskin.json", Skin.class);
		assetManager.load("ui/gameicons/icons.atlas", TextureAtlas.class);
		
		// textures
		loadRec("textures/", Texture.class);
		
		// rigid objects
		assetManager.load("rigidObjects/gift.png", Texture.class);
		
		// sounds
		loadRec("sounds/", Sound.class);
		
		// music
		LaplacityAssets.levelTracks = Gdx.files.internal("music/levels/").list();

		// finish loading
		assetManager.finishLoading();
		LaplacityAssets.repackAssets();
	}
	
	@Override
	public void render () {
		shapeRenderer.setProjectionMatrix(CameraManager.camMat());
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
		catFood.dispose();
		progress.dispose();
	}
	
	public void showInterstitial() {
		if (adHandler != null) {
			adHandler.showOrLoadInterstital();
		}
	}
	
	public void showRewarded() {
		if (adHandler != null) {
			adHandler.showOrLoadRewarded();
		}
	}
	
}
