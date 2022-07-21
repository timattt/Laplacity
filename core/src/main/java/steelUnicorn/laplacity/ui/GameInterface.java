package steelUnicorn.laplacity.ui;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.GameMode;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.particles.Electron;
import steelUnicorn.laplacity.particles.Proton;

/**
 * @brief Класс создающий ui и обработчик жество на экране
 *
 * При нажатии на иконку мода, нужный мод устанавливается в GameProcess.currentGameMode
 * @author Elveg, timat
 */
public class GameInterface extends Stage implements GestureListener {
	private ReturnDialog returnDialog;

	private static final float iconSize = UI_WORLD_HEIGHT / 10;
	private static final float iconSpace = iconSize * 0.1f;

	/**
	 * Конструктор создающий интерфейс
	 * @param viewport
	 */
	public GameInterface(Viewport viewport) {
		super(viewport);

		createInterface();
	}
	
	/**
	 * @brief Функция создающая иконки кнопок.
	 *
	 * Порядок кнопок
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
		//Dialogs initialize
		Skin skin = Globals.assetManager.get("ui/uiskin.json");
		returnDialog = new ReturnDialog(skin);

		//interface intitialize
		Table root = new Table();
		root.setFillParent(true);
		root.align(Align.right);
		root.pad(iconSpace);
		addActor(root);
		root.defaults()
				.width(iconSize)
				.height(iconSize)
				.space(iconSpace);

		TextureAtlas icons = Globals.assetManager.get("ui/gameicons/icons.atlas", TextureAtlas.class);

		//reload and return buttons
		createIcon(icons, "return", root, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				returnDialog.show(GameInterface.this);
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
	 * Функция для создания кнопки иконки
	 *
	 * @param icons - Texture Atlas с иконками
	 * @param name - название мода
	 * @param root - таблица хранящая кнопки
	 * @param listener - обработчик события
	 */
	private void createIcon(TextureAtlas icons, String name, Table root, ChangeListener listener) {
		Button btn = new Button(new TextureRegionDrawable(icons.findRegion(name)));
		btn.setName(name);
		btn.addListener(listener);
		root.add(btn);
		root.row();
	}

	/**
	 * Функция создающая иконку мода
	 *
	 * @param icons - Texture Atlas с иконками
	 * @param name - название мода
	 * @param root - таблица хранящая кнопки
	 * @param mode - включаемый мод
	 */
	private void createModeIcon(TextureAtlas icons, String name, Table root, GameMode mode) {
		createIcon(icons, name, root, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeGameMode(mode);
			}
		});
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (TrajectoryRenderer.changingDir) {
			return false;
		}
		
		TMP3.set(x, y, 0);
		camera.unproject(TMP3);
		
		actParticlePlacer(TMP3.x, TMP3.y);
		actBrush(TMP3.x, TMP3.y);
		
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
	
	private void actBrush(float x, float y) {
		if (currentGameMode == GameMode.dirichlet) {
			LaplacityField.fillCircleWithRandomDensity(x, y, BRUSH_RADIUS, MAX_DENSITY);
			FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
			TrajectoryRenderer.updateTrajectory();
		} else if (currentGameMode == GameMode.eraser) {
			LaplacityField.clearCircleDensity(x, y, BRUSH_RADIUS);
			FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
			TrajectoryRenderer.updateTrajectory();
		} 
	}
	
	private void actParticlePlacer(float x, float y) {
		if (currentGameMode == GameMode.electrons) {
			addStaticParticle(new Electron(x, y));
			FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
			TrajectoryRenderer.updateTrajectory();
		}
		if (currentGameMode == GameMode.protons) {
			addStaticParticle(new Proton(x, y));
			FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
			TrajectoryRenderer.updateTrajectory();
		}
	}
	
	private void actCameraOneFinger(float dx, float dy) {
		if (currentGameMode != GameMode.dirichlet && currentGameMode != GameMode.eraser) {
			camera.position.x -= dx * Globals.SCREEN_WORLD_WIDTH / Gdx.graphics.getWidth();
			camera.position.x = Math.max(Globals.SCREEN_WORLD_WIDTH / 2,
					Math.min(LaplacityField.fieldWidth * LaplacityField.tileSize - Globals.SCREEN_WORLD_WIDTH / 2,
							camera.position.x));
			camera.update();
		}
	}
	
	private void actCameraTwoFingers(float dx, float dy) {
		if (currentGameMode == GameMode.dirichlet || currentGameMode == GameMode.eraser) {
			camera.position.x -= dx * Globals.SCREEN_WORLD_WIDTH / Gdx.graphics.getWidth();
			camera.position.x = Math.max(Globals.SCREEN_WORLD_WIDTH / 2,
					Math.min(LaplacityField.fieldWidth * LaplacityField.tileSize - Globals.SCREEN_WORLD_WIDTH / 2,
							camera.position.x));
			camera.update();
		}
	}
	
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (TrajectoryRenderer.changingDir) {
			return false;
		}
		
		TMP3.set(x, y, 0);
		camera.unproject(TMP3);
		
		actBrush(TMP3.x, TMP3.y);
		actCameraOneFinger(deltaX, deltaY);
		
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
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float len2 = getlen2ToMainParticle(screenX, screenY);

		if (len2 < 4 * ELECTRON_SIZE * ELECTRON_SIZE) {
			TrajectoryRenderer.changingDir = true;
			touchDragged(screenX, screenY, pointer);
		} else {
			TrajectoryRenderer.changingDir = false;
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		TrajectoryRenderer.changingDir = false;
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (TrajectoryRenderer.changingDir) {
			getlen2ToMainParticle(screenX, screenY);
			mainParticle.setStartVelocity(TMP3.x, TMP3.y);
			TrajectoryRenderer.updateTrajectory();
		}
		
		return super.touchDragged(screenX, screenY, pointer);
	}

	private final Vector2 prevPtr1 = new Vector2();
	private final Vector2 prevPtr2 = new Vector2();
	
	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		float dx1 = 0;
		float dx2 = 0;
		if (initialPointer1.x == pointer1.x && initialPointer1.y == pointer1.y && initialPointer2.x == pointer2.x && initialPointer2.y == pointer2.y) {
			dx1 = 0;
			dx2 = 0;
		} else {
			dx1 = pointer1.x - prevPtr1.x;
			dx2 = pointer2.x - prevPtr2.x;
		}
		
		prevPtr1.set(pointer1);
		prevPtr2.set(pointer2);
		
		actCameraTwoFingers(dx1 + dx2, 0);
		
		return false;
	}

	@Override
	public void pinchStop() {
	}
	
}
