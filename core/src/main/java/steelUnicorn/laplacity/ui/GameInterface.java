package steelUnicorn.laplacity.ui;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.Globals;
import steelUnicorn.laplacity.field.GameMode;
import steelUnicorn.laplacity.particles.Electron;
import steelUnicorn.laplacity.particles.Proton;

/**
 * @brief Class that creates game ui and add listeners.
 *
 * When any icon clicked, GameProcess.currentGameMode change its state
 *
 */
public class GameInterface extends Stage implements GestureListener {
	/**
	 * Constructor create Stage and interface
	 * @param viewport
	 */
	public GameInterface(Viewport viewport) {
		super(viewport);

		createInterface();
	}
	
	/**
	 * @brief Function creates interface buttons
	 *
	 * The order is
	 * <ol>
	 * 	<li>return</li>
	 * 	<li>reload</li>
	 * 	<li>flight</li>
	 * 	<li>eraser</li>
	 * 	<li>electrons</li>
	 * 	<li>protons</li>
	 * 	<li>dirichlet</li>
	 * </ol>
	 */
	private void createInterface() {
		Table root = new Table();
		root.setFillParent(true);
		root.align(Align.right);
		addActor(root);

		TextureAtlas icons = Globals.assetManager.get("ui/gameicons/icons.atlas", TextureAtlas.class);

		//reload and return buttons
		createIcon(icons, "return", root, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("game ui", "return pressed");
				Globals.game.setScreen(Globals.mainMenuScreen);
			}
		});

		//modes
		createModeIcon(icons, "flight", root, GameMode.flight);
		createModeIcon(icons, "reload", root, GameMode.none);
		createModeIcon(icons, "eraser", root, GameMode.eraser);
		createModeIcon(icons, "electrons", root, GameMode.electrons);
		createModeIcon(icons, "protons", root, GameMode.protons);
		createModeIcon(icons, "dirichlet", root, GameMode.dirichlet);
		
	}

	/**
	 * Function that creates 1 icon button of game interface
	 *
	 * @param icons - TextureAtlas with icons in it
	 * @param name - name of TextureRegion in Atlas
	 * @param root - table where button is placed
	 * @param listener - listener that define buttons behaviour
	 */
	private void createIcon(TextureAtlas icons, String name, Table root, ChangeListener listener) {
		ImageButton btn = new ImageButton(new TextureRegionDrawable(icons.findRegion(name)));
		btn.setName(name);
		btn.addListener(listener);
		root.add(btn);
		root.row();
	}

	/**
	 * Function that creates mode button
	 *
	 * @param icons - Texture Atlas with icons of modes
	 * @param name - name of mode
	 * @param root - table where button is placed
	 * @param mode - mode that button enables
	 */
	private void createModeIcon(TextureAtlas icons, String name, Table root, GameMode mode) {
		createIcon(icons, name, root, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeGameMode(mode);
				Gdx.app.log("game ui", GameProcess.currentGameMode.toString());
			}
		});
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (changingDir) {
			return false;
		}
		
		TMP3.set(x, y, 0);
		camera.unproject(TMP3);
		if (currentGameMode == GameMode.electrons) {
			addStaticParticle(new Electron(TMP3.x, TMP3.y));
		}
		if (currentGameMode == GameMode.protons) {
			addStaticParticle(new Proton(TMP3.x, TMP3.y));
		}
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}
	
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (changingDir) {
			return false;
		}
		
		TMP3.set(x, y, 0);
		camera.unproject(TMP3);
		if (currentGameMode == GameMode.dirichlet) {
			field.fillCircleWithRandomDensity(TMP3.x, TMP3.y, BRUSH_RADIUS, MAX_DENSITY);
		} else if (currentGameMode == GameMode.eraser) {
			field.clearCircleDensity(TMP3.x, TMP3.y, BRUSH_RADIUS);
		} else {
			camera.position.x -= deltaX * Globals.SCREEN_WORLD_WIDTH / Gdx.graphics.getWidth();
			float mx = field.getFieldWidth() / 2 * field.getTileSize() - Globals.SCREEN_WORLD_WIDTH / 2;
			camera.position.x = Math.max(-mx, Math.min(mx, camera.position.x));
			camera.update();
		}
		
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	private float getlen2ToMainParticle(float scX, float scY) {
		TMP3.set(scX, scY, 0);
		camera.unproject(TMP3);
		TMP3.sub(mainParticle.getX(), mainParticle.getY(), 0);
		return TMP3.len2();
	}
	
	private boolean changingDir = false;
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float len2 = getlen2ToMainParticle(screenX, screenY);

		if (len2 < ELECTRON_SIZE * ELECTRON_SIZE) {
			changingDir = true;
		} else {
			changingDir = false;
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		changingDir = false;
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (changingDir) {
			getlen2ToMainParticle(screenX, screenY);
			mainParticle.setDir(TMP3.x, TMP3.y);
		}
		
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {
	}
	
}
