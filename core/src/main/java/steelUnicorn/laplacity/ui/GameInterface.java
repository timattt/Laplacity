package steelUnicorn.laplacity.ui;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.gameModes.GameMode;;


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
	private CatFoodInterface catFI;
	private static final float iconSize = UI_WORLD_HEIGHT / 10;
	private static final float iconSpace = iconSize * 0.1f;

	Cell<Button> flightCell;
	ImageButton flightBtn;
	ImageButton editBtn;

	Skin skin;
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
		skin = Globals.assetManager.get("ui/uiskin.json");
		returnDialog = new ReturnDialog(skin);

		//FpsCounter
		FpsCounter fpsCounter = new FpsCounter(skin);
		addActor(fpsCounter);

		//interface intitialize
		Table root = new Table();
		root.setFillParent(true);
		addActor(root);

		//mode img
		curModeImg = new Image();
		curModeImg.setVisible(false);
		root.add(curModeImg).expand().left().top()
				.size(iconSize, iconSize).pad(iconSpace).uniform();

		//cat interface
		catFI = new CatFoodInterface(catFood.getTotalLaunchesAvailable(), skin);
		root.add(catFI).expand().top().uniform();
		catFood.timer.setCurrentInterface(catFI);
		catFI.setBackground(skin.newDrawable("white", Color.valueOf("120A39FF")));

		//Icons Table
		Table guiTable = new Table();
		root.add(guiTable).expand().right().pad(iconSpace).uniform();
		guiTable.defaults()
				.width(iconSize)
				.height(iconSize)
				.space(iconSpace);

		icons = Globals.assetManager.get("ui/gameicons/icons.atlas", TextureAtlas.class);

		//reload and return buttons
		guiTable.add(createIcon("Return", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LaplacityAssets.playSound(LaplacityAssets.popupSound);
				returnDialog.show(GameInterface.this);
			}
		}));
		guiTable.row();

		//modes
		flightBtn = createIcon("Flight", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LaplacityAssets.playSound(LaplacityAssets.lightClickSound);
				if (catFood.getTotalLaunchesAvailable() > 0) {
					changeGameMode(GameMode.FLIGHT);    //no need in NONE because of editBtn

					catFI.update(catFood.launch());
				} else {
					CatFoodInterface.showHungry(GameInterface.this);
				}
			}
		});
		editBtn = createModeIcon("Edit", GameMode.NONE, LaplacityAssets.lightClickSound);
		flightCell = guiTable.add(flightBtn);
		guiTable.row();

		guiTable.add(createIcon("Clear", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LaplacityAssets.playSound(LaplacityAssets.annihilationSound);
				GameProcess.clearLevel();
			}
		}));
		guiTable.row();

		guiTable.add(createModeIcon("Eraser", GameMode.ERASER, LaplacityAssets.lightClickSound));
		guiTable.row();
		guiTable.add(createModeIcon("Electrons", GameMode.ELECTRONS, LaplacityAssets.genStartSound));
		guiTable.row();
		guiTable.add(createModeIcon("Protons", GameMode.PROTONS, LaplacityAssets.genStartSound));
		guiTable.row();
		guiTable.add(createModeIcon("Dirichlet", GameMode.DIRICHLET, LaplacityAssets.sprayStartSound));
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
	private ImageButton createIcon(String name, ChangeListener listener) {
		ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(
				skin.newDrawable("white", Color.valueOf("837d7eff")),
				skin.newDrawable("white", Color.valueOf("505251ff")),
				null,
				new TextureRegionDrawable(icons.findRegion(name)),
				null,
				null);
		ImageButton btn = new ImageButton(style);
		Gdx.app.log("GameInterface", name);
		btn.setColor(Color.WHITE);
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
	private ImageButton createModeIcon(String name, GameMode mode, Sound sound) {
		return createIcon(name, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LaplacityAssets.playSound(sound);
				if (currentGameMode == mode) {
					changeGameMode(GameMode.NONE);
				} else {
					changeGameMode(mode);
				}
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
		
		CameraManager.getCameraWorldPos(x, y, TMP1);
		
		currentGameMode.tap(TMP1.x, TMP1.y);
		
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
		CameraManager.getCameraWorldPos(x, y, TMP1);
		
		currentGameMode.pan(TMP1.x, TMP1.y, deltaX / Gdx.graphics.getWidth() * SCREEN_WORLD_WIDTH, deltaY / Gdx.graphics.getWidth() * SCREEN_WORLD_WIDTH);
		
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		//
		CameraManager.getCameraWorldPos(x, y, TMP1);
		
		currentGameMode.panStop(TMP1.x, TMP1.y);
		
		return true;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	private float getlen2ToMainParticle(float scX, float scY) {
		CameraManager.getCameraWorldPos(scX, scY, TMP1);
		TMP1.sub(mainParticle.getX(), mainParticle.getY());
		return TMP1.len2();
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
		CameraManager.getCameraWorldPos(screenX, screenY, TMP1);
		currentGameMode.touchDown(TMP1.x, TMP1.y);
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		TrajectoryRenderer.changingDir = false;
		
		//
		CameraManager.getCameraWorldPos(screenX, screenY, TMP1);
		currentGameMode.touchUp(TMP1.x, TMP1.y);
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (currentGameMode == GameMode.FLIGHT) {
			TrajectoryRenderer.changingDir = false;
		}
		if (TrajectoryRenderer.changingDir) {
			getlen2ToMainParticle(screenX, screenY);
			mainParticle.setSlingshot(TMP1.x, TMP1.y);
			TrajectoryRenderer.updateTrajectory();
			return true;
		}
		
		//
		CameraManager.getCameraWorldPos(screenX, screenY, TMP1);
		currentGameMode.touchDragged(TMP1.x, TMP1.y);		
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
