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
import steelUnicorn.laplacity.screens.MainMenuScreen;
import steelUnicorn.laplacity.screens.WinScreen;
import steelUnicorn.laplacity.ui.CatFood;
import steelUnicorn.laplacity.utils.AdHandler;
import steelUnicorn.laplacity.utils.LevelsParser;
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

		CameraManager.init();
		game = this;
		guiViewport = new ExtendViewport(UI_WORLD_WIDTH, UI_WORLD_HEIGHT);
		gameViewport = CameraManager.createViewport();
		gameScreen = new GameScreen();
		mainMenuScreen = new MainMenuScreen();
		winScreen = new WinScreen();
		levelsScreen = new LevelsScreen();
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
		this.screenManager.addScreenTransition(nameSlideIn, slideIn);
		this.screenManager.addScreenTransition(nameSlideOut, slideOut);

		LaplacityAssets.changeTrack("music/main_menu.mp3");
		this.screenManager.pushScreen(nameMainMenuScreen, null);
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
		FileHandle[] sections = Gdx.files.internal("textures/").list();
		for (FileHandle section : sections) {
			FileHandle[] backs = section.list();
			for (FileHandle back : backs) {
				assetManager.load(back.path(), Texture.class);
			}
		}
		
		// objects
		assetManager.load("rigidObjects/gift.png", Texture.class);
		
		// sounds
		FileHandle[] snds = Gdx.files.internal("sounds/").list();
		for (FileHandle snd : snds) {
			assetManager.load(snd.path(), Sound.class);
		}
		
		// music
		LaplacityAssets.levelTracks = Gdx.files.internal("music/levels/").list();
		
		// backgrounds
		sections = Gdx.files.internal("backgrounds/").list();
		for (FileHandle section : sections) {
			FileHandle[] backs = section.list();
			for (FileHandle back : backs) {
				assetManager.load(back.path(), Texture.class);
			}
		}
		assetManager.load("backgrounds/SPACE_BACKGROUND.png", Texture.class);
		
		// finish loading
		assetManager.finishLoading();
		LaplacityAssets.getAssets();
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
