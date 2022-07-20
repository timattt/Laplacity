package steelUnicorn.laplacity.core;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import steelUnicorn.laplacity.screens.GameScreen;
import steelUnicorn.laplacity.screens.MainMenuScreen;
import steelUnicorn.laplacity.screens.WinScreen;

/**
 * Класс, который содержит глобальные переменные для всей программы.
 * @author timat
 *
 */
public class Globals {

	// GLOBAL INSTANCES
	public static OrthographicCamera camera;
	public static Laplacity game;
	public static ScreenViewport guiViewport;
	public static ExtendViewport gameViewport;
	public static ShapeRenderer shapeRenderer;
	public static InputMultiplexer inputMultiplexer;
	
	// Screen names
	public static final String nameMainMenuScreen = "mainMenu";
	public static final String nameGameScreen = "game";
	public static final String nameWinScreen = "winScreen";

	// Tramsition names
	public static final String nameSlideIn = "slideIn";
	public static final String nameSlideOut = "slideOut";
	
	// Ingame viewport
	/**
	 * Сколько приходится игровых единиц на ширину экрана
	 */
	public static final float SCREEN_WORLD_WIDTH = 160;
	/**
	 * Сколько приходится игровых единиц на высоту экрана
	 */
	public static final float SCREEN_WORLD_HEIGHT = 90;
	
	// screens
	public static GameScreen gameScreen;
	public static MainMenuScreen mainMenuScreen;
	public static WinScreen winScreen;

	// Resource manager
	public static AssetManager assetManager;

	// TMP
	public static final Vector2 TMP1 = new Vector2();
	public static final Vector2 TMP2 = new Vector2();
	public static final Vector3 TMP3 = new Vector3();
	
	// Total levels loaded
	public static int TOTAL_LEVELS_AVAILABLE;
	
}
