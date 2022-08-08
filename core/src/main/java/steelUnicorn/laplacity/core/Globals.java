package steelUnicorn.laplacity.core;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import steelUnicorn.laplacity.screens.GameScreen;
import steelUnicorn.laplacity.screens.LevelsScreen;
import steelUnicorn.laplacity.screens.LoadingScreen;
import steelUnicorn.laplacity.screens.MainMenuScreen;
import steelUnicorn.laplacity.screens.WinScreen;
import steelUnicorn.laplacity.ui.CatFood;

/**
 * Класс, который содержит глобальные переменные для всей программы.
 * @author timat
 *
 */
public class Globals {

	// GLOBAL INSTANCES
	public static Laplacity game;
	public static ExtendViewport guiViewport;
	public static ExtendViewport gameViewport;
	public static ShapeRenderer shapeRenderer;
	public static InputMultiplexer inputMultiplexer;
	
	// Screen names
	public static final String nameMainMenuScreen = "mainMenu";
	public static final String nameGameScreen = "game";
	public static final String nameWinScreen = "winScreen";
	public static final String nameLevelsScreen = "levelsScreen";
	public static final String nameLoadingScreen = "loadingScreen";

	// Tramsition names
	public static final String nameSlideIn = "slideIn";
	public static final String nameSlideOut = "slideOut";

	// UI Viewport. Default world parameters for 16:9 ratio
	public static final float UI_WORLD_WIDTH = 1920;
	public static final float UI_WORLD_HEIGHT = 1080;

	// Ingame viewport
	/**
	 * Сколько приходится игровых единиц на ширину экрана
	 */
	public static final float SCREEN_WORLD_WIDTH = 160;
	/**
	 * Сколько приходится игровых единиц на высоту экрана
	 */
	public static final float SCREEN_WORLD_HEIGHT = 90;
	
	// epsilon
	public static final float EPSILON_PRECISION = 0.0001f;
	
	// screens
	public static GameScreen gameScreen;
	public static MainMenuScreen mainMenuScreen;
	public static WinScreen winScreen;
	public static LevelsScreen levelsScreen;
	public static LoadingScreen loadingScreen;

	// Resource manager
	public static AssetManager assetManager;

	// TMP
	public static final Vector2 TMP1 = new Vector2();
	public static final Vector2 TMP2 = new Vector2();
	public static final Vector3 TMP3 = new Vector3();
	public static final Vector3 TMP4 = new Vector3();
	
	// Total levels loaded
	public static int TOTAL_LEVELS_AVAILABLE;
	public static final int LEVELS_PER_SECTION = 10;

	//cat Food
	public static CatFood catFood;
}
