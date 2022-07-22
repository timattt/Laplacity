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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.gameModes.GameMode;


/**
 * @brief Класс создающий ui и обработчик жество на экране
 *
 * При нажатии на иконку мода, нужный мод устанавливается в GameProcess.currentGameMode
 * @author Elveg, timat
 */
public class GameInterface extends Stage implements GestureListener {
	private ReturnDialog returnDialog;
	private TextureAtlas icons;
	private Image curModeImg;
	private static final float iconSize = UI_WORLD_HEIGHT / 10;
	private static final float iconSpace = iconSize * 0.1f;

	Cell flightCell;
	Button flightBtn;
	Button editBtn;

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

		//mode img
		curModeImg = new Image();
		curModeImg.setVisible(false);
		root.add(curModeImg).expand().left().top()
				.size(iconSize, iconSize).pad(iconSpace);

		//Icons Table
		Table guiTable = new Table();
		root.add(guiTable).right();
		guiTable.defaults()
				.width(iconSize)
				.height(iconSize)
				.space(iconSpace);

		icons = Globals.assetManager.get("ui/gameicons/icons.atlas", TextureAtlas.class);

		//reload and return buttons
		guiTable.add(createIcon("Return", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				returnDialog.show(GameInterface.this);
			}
		}));
		guiTable.row();

		//modes
		flightBtn = createModeIcon("Flight", GameMode.FLIGHT);
		editBtn = createModeIcon("Edit", GameMode.NONE);
		flightCell = guiTable.add(flightBtn);
		guiTable.row();

		guiTable.add(createIcon("Clear", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameProcess.clearLevel();
			}
		}));
		guiTable.row();

		guiTable.add(createModeIcon("Eraser", GameMode.ERASER));
		guiTable.row();
		guiTable.add(createModeIcon("Electrons", GameMode.ELECTRONS));
		guiTable.row();
		guiTable.add(createModeIcon("Protons", GameMode.PROTONS));
		guiTable.row();
		guiTable.add(createModeIcon("Dirichlet", GameMode.DIRICHLET));
	}

	/**
	 * Функция вызывается в GameProcess.changeGameMode при изменении мода
	 * для изменения вида gui
	 */
	public void updateCurModeImg() {
		if (currentGameMode != GameMode.NONE) {
			curModeImg.setVisible(true);
			curModeImg.setDrawable(
					new TextureRegionDrawable(
							icons.findRegion(currentGameMode.getName())));
		} else {
			curModeImg.setVisible(false);
		}

		if (currentGameMode == GameMode.FLIGHT) {
			flightCell.setActor(editBtn);
		} else {
			flightCell.setActor(flightBtn);
		}
	}
	/**
	 * Функция для создания кнопки иконки
	 *
	 * @param name - название мода
	 * @param listener - обработчик события
	 */
	private Button createIcon(String name, ChangeListener listener) {
		Button btn = new Button(new TextureRegionDrawable(icons.findRegion(name)));
		btn.setName(name);
		btn.addListener(listener);
		return btn;
	}

	/**
	 * Функция создающая иконку мода
	 *
	 * @param name - название мода
	 * @param mode - включаемый мод
	 */
	private Button createModeIcon(String name, GameMode mode) {
		return createIcon(name, new ChangeListener() {
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
		
		currentGameMode.tap(TMP3.x, TMP3.y);
		
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
		if (TrajectoryRenderer.changingDir) {
			return false;
		}
		
		//
		TMP3.set(x, y, 0);
		camera.unproject(TMP3);
		
		currentGameMode.pan(TMP3.x, TMP3.y, deltaX / Gdx.graphics.getWidth() * SCREEN_WORLD_WIDTH, deltaY / Gdx.graphics.getWidth() * SCREEN_WORLD_WIDTH);
		
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
			return true;
		} else {
			TrajectoryRenderer.changingDir = false;
		}
		
		//
		TMP3.set(screenX, screenY, 0);
		camera.unproject(TMP3);
		currentGameMode.touchDown(TMP3.x, TMP3.y);
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		TrajectoryRenderer.changingDir = false;
		
		//
		TMP3.set(screenX, screenY, 0);
		camera.unproject(TMP3);
		currentGameMode.touchUp(TMP3.x, TMP3.y);
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (currentGameMode == GameMode.FLIGHT) {
			TrajectoryRenderer.changingDir = false;
		}
		if (TrajectoryRenderer.changingDir) {
			getlen2ToMainParticle(screenX, screenY);
			mainParticle.setSlingshot(TMP3.x, TMP3.y);
			TrajectoryRenderer.updateTrajectory();
			return true;
		}
		
		//
		TMP3.set(screenX, screenY, 0);
		camera.unproject(TMP3);
		currentGameMode.touchDragged(TMP3.x, TMP3.y);		
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
		
		currentGameMode.pinch(dx1, dx2);
		
		return false;
	}

	@Override
	public void pinchStop() {
	}
	
}
