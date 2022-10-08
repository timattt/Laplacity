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
import steelUnicorn.laplacity.screens.StartScreen;
import steelUnicorn.laplacity.screens.StoryScreen;
import steelUnicorn.laplacity.screens.WinScreen;
import steelUnicorn.laplacity.utils.CatFood;
import steelUnicorn.laplacity.utils.AdHandler;
import steelUnicorn.laplacity.utils.AnalyticsCollector;
import steelUnicorn.laplacity.utils.DebugHandler;
import steelUnicorn.laplacity.utils.NotificationHandler;
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
	private NotificationHandler notificationHandler;
	
	public boolean interstitialJustShown = false;
	public boolean rewardedJustShown = false;

	public enum CreatingStatus {
		STARTED,
		ASSETS_REPACKED,
		SETTINGS_LOADED,
		PROGRESS_LOADED,
		GAME_INITIALIZED,
		MENU_INITIALIZED,
		WIN_INITIALIZED,
		LEVELS_INITIALIZED,
		LOADING_INITIALIZED,
		STORY_INITIALIZED,
		SHAPEREND_INPUTMULT,
		TRANSITIONS_CREATED,
		SCREENS_ADDED,
		CREATING_FINISHED
	}
	public CreatingStatus currentStatus;
	
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

		guiViewport = new ExtendViewport(UI_WORLD_WIDTH, UI_WORLD_HEIGHT);
		CameraManager.init();
		gameViewport = CameraManager.createViewport();

		startScreen = new StartScreen(guiViewport);
		this.screenManager.addScreen("startScreen", startScreen);
		this.screenManager.pushScreen("startScreen", null);

		currentStatus = CreatingStatus.STARTED;
	}

	public CreatingStatus createEntities() {
		switch (currentStatus) {
			case STARTED:
				LaplacityAssets.repackAssets(assetManager);
				currentStatus = CreatingStatus.ASSETS_REPACKED;
				return currentStatus;
			case ASSETS_REPACKED:
				Settings.loadSettings();
				currentStatus = CreatingStatus.SETTINGS_LOADED;
				return currentStatus;
			case SETTINGS_LOADED:
				catFood = new CatFood();
				progress = new PlayerProgress();
				currentStatus = CreatingStatus.PROGRESS_LOADED;
				return currentStatus;
			case PROGRESS_LOADED:
				gameScreen = new GameScreen();
				currentStatus = CreatingStatus.GAME_INITIALIZED;
				return currentStatus;
			case GAME_INITIALIZED:
				mainMenuScreen = new MainMenuScreen();
				currentStatus = CreatingStatus.MENU_INITIALIZED;
				return currentStatus;
			case MENU_INITIALIZED:
				winScreen = new WinScreen();
				currentStatus = CreatingStatus.WIN_INITIALIZED;
				return currentStatus;
			case WIN_INITIALIZED:
				levelsScreen = new LevelsScreen();
				currentStatus = CreatingStatus.LEVELS_INITIALIZED;
				return currentStatus;
			case LEVELS_INITIALIZED:
				loadingScreen = new LoadingScreen(guiViewport);
				currentStatus = CreatingStatus.LOADING_INITIALIZED;
				return currentStatus;
			case LOADING_INITIALIZED:
				storyScreen = new StoryScreen[STORY_SIZE];
				for (int i = 0; i < STORY_SIZE; i++) {
					storyScreen[i] = new StoryScreen(i);
				}
				currentStatus = CreatingStatus.STORY_INITIALIZED;
				return currentStatus;
			case STORY_INITIALIZED:
				shapeRenderer = new ShapeRenderer();
				inputMultiplexer = new InputMultiplexer();
				currentStatus = CreatingStatus.SHAPEREND_INPUTMULT;
				return currentStatus;
			case SHAPEREND_INPUTMULT:
				// transition
				this.transitionBatch = new SpriteBatch();
				BlendingTransition blend = new BlendingTransition(transitionBatch, 1f);
				BlendingTransition story = new BlendingTransition(transitionBatch, 1f);
				this.screenManager.addScreenTransition(blendTransitionName, blend);
				this.screenManager.addScreenTransition(storyTransitionName, story);
				currentStatus = CreatingStatus.TRANSITIONS_CREATED;
				return currentStatus;
			case TRANSITIONS_CREATED:
				this.screenManager.addScreen(nameGameScreen, gameScreen);
				this.screenManager.addScreen(nameMainMenuScreen, mainMenuScreen);
				this.screenManager.addScreen(nameWinScreen, winScreen);
				this.screenManager.addScreen(nameLevelsScreen, levelsScreen);
				this.screenManager.addScreen(nameLoadingScreen, loadingScreen);
				for (int i = 0; i < STORY_SIZE; i++)
					this.screenManager.addScreen(nameStoryScreen + i, storyScreen[i]);
				currentStatus = CreatingStatus.SCREENS_ADDED;
				return currentStatus;
			case SCREENS_ADDED:
				currentStatus = CreatingStatus.CREATING_FINISHED;
				shapeRenderer.setProjectionMatrix(CameraManager.camMat());
				return currentStatus;
			default:
				return currentStatus;
		}
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
	}
	
	@Override
	public void render () {
		if (currentStatus == CreatingStatus.CREATING_FINISHED) {
			shapeRenderer.setProjectionMatrix(CameraManager.camMat());
		}
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		if (guiViewport != null) {
			guiViewport.update(width, height, true);
		}
		if (gameViewport != null) {
			gameViewport.update(width, height, false);
		}
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
		if (catFood != null) {
			catFood.saveFoodPrefs();

			if (notificationHandler != null) {
				Gdx.app.log("Notifications",
						"Food remains: " + catFood.getLaunches() +
								"\nTimer value: " + catFood.timer.getTime());
				notificationHandler.setRestoreTime(catFood.timer.getRestoreTime());
			}
		}
	}

	@Override
	public void resume() {
		super.resume();
		if (catFood != null) {
			catFood.timer.setTime(catFood.getTimerValue());
			catFood.timer.entryUpdate(catFood.getExitTime());
		}
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

	/**
	 * Сохранение обработчика уведомлений в классе.
	 * @param notificationHandler обработчик уведомлений.
	 */
	public void setNotificationHandler(NotificationHandler notificationHandler) {
		this.notificationHandler = notificationHandler;
	}
}
