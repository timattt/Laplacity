package steelUnicorn.laplacity.ui;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.clickSound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Laplacity;
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
	private SettingsDialog settingsDialog;
	public CatFoodInterface catFI;
	private static final float iconSize = UI_WORLD_WIDTH * 0.075f;
	private static final float settingsSize = iconSize * 0.9f;
	private static final float iconSpace = iconSize * 0.08f;

	Cell<ImageButton> flightCell;
	ImageButton flightBtn;
	ImageButton pauseBtn;
	Table modes;
	Array<Actor> visibleActors;

	ImageButton selectedMode;
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
		visibleActors = new Array<>();
		//Dialogs initialize
		returnDialog = new ReturnDialog(TEXSKIN);
		settingsDialog = new SettingsDialog(TEXSKIN);
		//FpsCounter
		if (Laplacity.isDebugEnabled()) {
			FpsCounter fpsCounter = new FpsCounter(TEXSKIN, "noback");
			addActor(fpsCounter);
		}

		//interface intitialize
		Table root = new Table();
		root.setFillParent(true);
		addActor(root);

		//return button
		Table leftLayout = new Table();
		leftLayout.add(createIcon("Home", new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(LaplacityAssets.popupSound);
				updateCurMode();
				returnDialog.show(GameInterface.this);
			}
		})).expandY().top().size(iconSize).space(iconSpace);

		leftLayout.add(createIcon("Clear", new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(LaplacityAssets.annihilationSound);
				GameProcess.clearLevel();
			}
		})).expandY().top().size(iconSize).space(iconSpace);

		leftLayout.row();
		leftLayout.add(createIcon("settings", new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(LaplacityAssets.popupSound);
				updateCurMode();
				Gdx.app.log("GameInterface", "settings pressed");
				settingsDialog.show(GameInterface.this);
			}
		})).expandY().bottom().size(settingsSize);

		root.add(leftLayout).expand().fillY().left().pad(iconSpace).uniform();

		//cat interface
		Table centerLayout = new Table();
		root.add(centerLayout).growY();

		catFI = new CatFoodInterface(TEXSKIN);
		centerLayout.add(catFI).expandY().top();
		catFood.timer.setCurrentInterface(catFI);
		visibleActors.add(catFI);

		if (Laplacity.isDebugEnabled()) {
			TextButton skip = new TextButton("Skip", TEXSKIN);
			skip.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					LaplacityAssets.playSound(clickSound);
					GameProcess.skipLevel();
				}
			});
			visibleActors.add(skip);
			centerLayout.row();
			centerLayout.add(skip).padBottom(iconSpace);
		}

		//Icons Table
		Table rightLayout = new Table();
		root.add(rightLayout).expandX().growY().right().uniform().pad(iconSpace);

		//modes
		flightBtn = createIcon("Flight", new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(LaplacityAssets.lightClickSound);
				if (catFood.getLaunches() > 0) {
					changeGameMode(GameMode.FLIGHT);

					catFI.update(catFood.launch());
				} else {
					catFI.showHungry(GameInterface.this);
					changeGameMode(GameMode.NONE);
				}
			}
		});
		pauseBtn = createModeIcon("Pause", GameMode.NONE, LaplacityAssets.lightClickSound);
		flightCell = rightLayout.add(flightBtn)
				.size(iconSize).space(iconSpace);

		rightLayout.row();
		rightLayout.add(createModeSelector(TEXSKIN, "SquareNone")).width(iconSize)
				.height(TEXSKIN.getDrawable("square_btn").getMinHeight()
						/ TEXSKIN.getDrawable("square_btn").getMinWidth() * iconSize);
		visibleActors.add(selectedMode);
		//modes table
		modes = new Table();
		rightLayout.row();
		rightLayout.add(modes).right().expandY().top().space(iconSpace);
		modes.defaults().size(iconSize).space(iconSpace);

		modes.row();
		modes.add(createModeIcon("Protons", GameMode.PROTONS, LaplacityAssets.genStartSound));
		modes.row();
		modes.add(createModeIcon("Electrons", GameMode.ELECTRONS, LaplacityAssets.genStartSound));
		modes.row();
		modes.add(createModeIcon("Dirichlet", GameMode.DIRICHLET, LaplacityAssets.sprayStartSound));
		modes.row();
		modes.add(createModeIcon("Eraser", GameMode.ERASER, LaplacityAssets.lightClickSound));
	}

	/**
	 * Функция вызывается в GameProcess.changeGameMode при изменении мода
	 * для изменения вида gui
	 */
	public void updateCurMode() {
		if (currentGameMode != GameMode.FLIGHT) {
			selectedMode.setStyle(TEXSKIN.get("Square" + currentGameMode.getName(),
					ImageButton.ImageButtonStyle.class));
		}
		modes.setVisible(currentGameMode == GameMode.NONE);	//сразу видимы если не выбран мод

		if (currentGameMode == GameMode.FLIGHT) {
			flightCell.setActor(pauseBtn);
			for (Actor actor : visibleActors) {
				actor.setVisible(false);
			}
		} else {
			flightCell.setActor(flightBtn);
			for (Actor actor : visibleActors) {
				actor.setVisible(true);
			}
		}
	}
	/**
	 * Функция для создания кнопки иконки
	 *
	 * @param name - название мода
	 * @param listener - обработчик события
	 */
	private ImageButton createIcon(String name, ClickListener listener) {
		ImageButton btn = new ImageButton(TEXSKIN.get(name, ImageButton.ImageButtonStyle.class));
		if (!name.equals("Flight") && !name.equals("Pause")) {
			Gdx.app.log("visible", "added " + name);

			visibleActors.add(btn);
		}

		btn.getImageCell().grow();
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
		return createIcon(name, new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(sound);
				if (currentGameMode == mode) {
					changeGameMode(GameMode.NONE);
				} else {
					changeGameMode(mode);
				}
			}
		});
	}

	private ImageButton createModeSelector(Skin skin, String name) {
		selectedMode = new ImageButton(skin, name);
		selectedMode.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LaplacityAssets.playSound(clickSound);
				if (modes.isVisible()) {
					modes.setVisible(false);
				} else  {
					modes.setVisible(true);
				}
			}
		});
		return selectedMode;
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
		
		currentGameMode.pan(TMP1.x, TMP1.y, deltaX * CameraManager.getCamera().zoom / Gdx.graphics.getWidth() * SCREEN_WORLD_WIDTH, deltaY * CameraManager.getCamera().zoom/ Gdx.graphics.getWidth() * SCREEN_WORLD_WIDTH);
		
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
		TMP1.sub(cat.getX(), cat.getY());
		return TMP1.len2();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		float len2 = getlen2ToMainParticle(screenX, screenY);

		if (len2 < 4 * PARTICLE_SIZE * PARTICLE_SIZE) {
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
			cat.setSlingshot(TMP1.x, TMP1.y);
			TrajectoryRenderer.updateTrajectory();
			return true;
		}
		
		//
		CameraManager.getCameraWorldPos(screenX, screenY, TMP1);
		currentGameMode.touchDragged(TMP1.x, TMP1.y);		
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		currentGameMode.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
		return false;
	}

	@Override
	public void pinchStop() {
		currentGameMode.pinchStop();
	}
	
	public void setPauseButtonEnabled(boolean val) {
		pauseBtn.setVisible(val);
	}
}
