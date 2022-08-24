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
import steelUnicorn.laplacity.screens.StoryScreen;
import steelUnicorn.laplacity.screens.WinScreen;
import steelUnicorn.laplacity.ui.CatFood;
import steelUnicorn.laplacity.utils.AdHandler;
import steelUnicorn.laplacity.utils.AnalyticsCollector;
import steelUnicorn.laplacity.utils.DebugHandler;
import steelUnicorn.laplacity.utils.PlayerProgress;
import steelUnicorn.laplacity.utils.Settings;

/** Класс запуска игры. Инициализируем поля из класса Globals. Загружаем assets. */
public class Laplacity extends ManagedGame<ManagedScreen, ScreenTransition> {
	
	public AssetManager assetManager;
	private SpriteBatch transitionBatch;
	
	// android
	public AdHandler adHandler;
	public AnalyticsCollector analyticsCollector;
	public DebugHandler debugHandler;
	
	public boolean interstitialJustShown = false;
	public boolean rewardedJustShown = false;
	
	public Laplacity(AdHandler adHand, AnalyticsCollector ac, DebugHandler db) {
		adHandler = adHand;
		analyticsCollector = ac;
		debugHandler = db;
	}

	@Override
	public void create() {
		super.create();
		game = this;
		loadAssets();
		Settings.loadSettings();
		catFood = new CatFood();
		progress = new PlayerProgress();

		CameraManager.init();
		guiViewport = new ExtendViewport(UI_WORLD_WIDTH, UI_WORLD_HEIGHT);
		gameViewport = CameraManager.createViewport();
		gameScreen = new GameScreen();
		mainMenuScreen = new MainMenuScreen();
		winScreen = new WinScreen();
		levelsScreen = new LevelsScreen();
		loadingScreen = new LoadingScreen();
		storyScreen = new StoryScreen[STORY_SIZE];
		for (int i = 0; i < STORY_SIZE; i++) {
			storyScreen[i] = new StoryScreen(i);
		}
		shapeRenderer = new ShapeRenderer();
		inputMultiplexer = new InputMultiplexer();
		
		
		// transition
		this.transitionBatch = new SpriteBatch();
		BlendingTransition blend = new BlendingTransition(transitionBatch, 1f);
		BlendingTransition story = new BlendingTransition(transitionBatch, 1f);

		this.screenManager.addScreen(nameGameScreen, gameScreen);
		this.screenManager.addScreen(nameMainMenuScreen, mainMenuScreen);
		this.screenManager.addScreen(nameWinScreen, winScreen);
		this.screenManager.addScreen(nameLevelsScreen, levelsScreen);
		this.screenManager.addScreen(nameLoadingScreen, loadingScreen);
		
		for (int i = 0; i < STORY_SIZE; i++)
			this.screenManager.addScreen(nameStoryScreen + i, storyScreen[i]);
		
		this.screenManager.addScreenTransition(blendTransitionName, blend);
		this.screenManager.addScreenTransition(storyTransitionName, story);

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
		loadRec("levels/", Texture.class);
		
		// story
		loadRec("story/", Texture.class);

		// ui
		assetManager.load("ui/uiskin.json", Skin.class);
		assetManager.load("ui/texskin/texskin.json", Skin.class);
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
		LaplacityAssets.repackAssets(assetManager);
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
	
	public void interstitialOk() {
		interstitialJustShown = true;
		debugHandler.debugMessage("From game show int OK!");
	}
	
	public void rewardedOk() {
		rewardedJustShown = true;
		debugHandler.debugMessage("From game show rew OK!");
	}
	
	public void sendLevelStats(int levelNumber, int sectionNumber, int starsCollected, int totalParticlesPlaced, int totalTry) {
		if (analyticsCollector != null) {
			analyticsCollector.levelFinished(levelNumber, sectionNumber, starsCollected, totalParticlesPlaced, totalTry);
		}
	}
	
	public static boolean isDebugEnabled() {
		if (Globals.game.debugHandler == null) {
			return true;
		}
		return Globals.game.debugHandler.isDebugModeEnabled();
	}
	
	public static boolean isPlayerCheater() {
		if (Globals.game.debugHandler == null) {
			return false;
		}
		return Globals.game.debugHandler.isPlayerCheater();
	}
	
}
