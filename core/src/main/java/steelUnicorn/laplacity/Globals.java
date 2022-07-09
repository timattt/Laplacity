package steelUnicorn.laplacity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import steelUnicorn.laplacity.screens.GameScreen;
import steelUnicorn.laplacity.screens.MainMenuScreen;

public class Globals {

	// GLOBAL INSTANCES
	public static OrthographicCamera camera;
	public static Laplacity game;
	public static ScreenViewport guiViewport;
	public static ExtendViewport gameViewport;
	
	// screens
	public static GameScreen gameScreen;
	public static MainMenuScreen mainMenuScreen;

	// Resource manager
	public static AssetManager assetManager;
	
	// FIELD
	public static final float FIELD_TILE_SIZE = 1f;

}
