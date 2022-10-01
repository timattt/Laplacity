package steelUnicorn.laplacity.ui;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.clickSound;
import static steelUnicorn.laplacity.core.LaplacityAssets.lightClickSound;
import static steelUnicorn.laplacity.ui.handler.ButtonNames.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.ui.dialogs.CatDialog;
import steelUnicorn.laplacity.ui.dialogs.ClearDialog;
import steelUnicorn.laplacity.ui.dialogs.ReturnDialog;
import steelUnicorn.laplacity.ui.dialogs.SettingsDialog;
import steelUnicorn.laplacity.ui.handler.GameInterfaceHandler;
import steelUnicorn.laplacity.utils.Settings;


/**
 * Сцена для игрового интерфейса.
 * Создает кнопки для управления игровым процессом.
 * @author Elveg, timat
 */
public class GameInterface extends Stage implements GestureListener {
	private static final float iconSize = UI_WORLD_WIDTH * 0.075f;
	private static final float settingsSize = iconSize * 0.9f;
	private static final float iconSpace = iconSize * 0.08f;

	private Table centerLayout;

	private ReturnDialog returnDialog;
	private SettingsDialog settingsDialog;
	private CatDialog catDialog;
	private ClearDialog clearDialog;
	private MessageBox msgBox;

	public CatFoodInterface catFI;

	private Table modes;
	/** Кнопка для выбора мода. */
	private ImageButton selectedMode;

	private Cell<ImageButton> flightCell;
	private ImageButton flightBtn;
	private ImageButton pauseBtn;
	/** Массив с скрываемыми элементами при запуске. */
	private Array<Actor> visibleActors;

	/** кнопка ускорения времени в режиме полета */
	private TextButton speedUpBtn;


	/** Управление интерфейсом (флаги) */
	public GameInterfaceHandler guiHandler;


	/**
	 * Создает сцену и интерфейс.
	 * @param viewport вьюпорт сцены.
	 */
	public GameInterface(Viewport viewport) {
		super(viewport);
		guiHandler = new GameInterfaceHandler();
		createInterface(TEXSKIN);
	}

	/**
	 * Инициализирует игровой интерфейс. Создает кнопки для изменения инструментов, запуска и
	 * остановки полета, пополнение еды.
	 * Кнопки выхода и настроек открывают соответствующие диалоговые окна.
	 * При попытке запуска без еды, открывается диалоговое окно предлагающее посмотреть рекламу.
	 *
	 * @param skin скин с текстурами виджетов.
	 * @see SettingsDialog
	 * @see CatDialog
	 * @see ReturnDialog
	 */
	private void createInterface(Skin skin) {
		visibleActors = new Array<>();
		//Dialogs initialize
		returnDialog = new ReturnDialog(skin);
		settingsDialog = new SettingsDialog(skin);
		settingsDialog.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actor instanceof CheckBox) {
					CheckBox box = (CheckBox) actor;
					if (Objects.equals(box.getName(), "skipCheckbox")) {
						Actor skip = centerLayout.findActor("skipBtn");
						if (skip != null) {
							skip.setVisible(box.isChecked());
						}
					}
				}
			}
		});
		catDialog = new CatDialog(skin, "cat_hungry");
		clearDialog = new ClearDialog(skin);
		msgBox = new MessageBox("", skin, "MessageBoxLabel");
		//FpsCounter
		if (Laplacity.isDebugEnabled()) {
			FpsCounter fpsCounter = new FpsCounter(skin, "noback");
			addActor(fpsCounter);
		}

		//interface initialize
		Table root = new Table();
		root.setFillParent(true);
		addActor(root);

		//left layout
		Table leftLayout = new Table();
		leftLayout.add(createIcon(skin, HOME, new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(LaplacityAssets.popupSound);
				updateCurMode();
				guiHandler.pressButton(HOME);
				returnDialog.show(GameInterface.this);
			}
		})).expandY().top().size(iconSize).space(iconSpace);

		leftLayout.add(createIcon(skin, CLEAR, new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(LaplacityAssets.annihilationSound);
				clearDialog.show(GameInterface.this);
				guiHandler.pressButton(CLEAR);
			}
		})).expandY().top().size(iconSize).space(iconSpace);

		leftLayout.row();
		leftLayout.add(createIcon(skin, SETTINGS, new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(LaplacityAssets.popupSound);
				updateCurMode();
				guiHandler.pressButton(SETTINGS);
				settingsDialog.show(GameInterface.this);
			}
		})).expandY().bottom().size(settingsSize);

		root.add(leftLayout).expand().fillY().left().pad(iconSpace).uniform();

		//center layout
		centerLayout = new Table();
		root.add(centerLayout).growY();

		catFI = new CatFoodInterface(skin);
		centerLayout.add(catFI).expandY().top();
		catFood.timer.setCurrentInterface(catFI);
		visibleActors.add(catFI);
		guiHandler.putButton(catFI.interstitialBtn);
		guiHandler.putButton(catFI.rewardedBtn);


		if (Laplacity.isDebugEnabled()) {
			TextButton skip = new TextButton("Skip", skin);
			skip.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					LaplacityAssets.playSound(clickSound);
					GameProcess.skipLevel();
				}
			});
			skip.setName("skipBtn");
			skip.setVisible(Settings.isShowSkip());
			visibleActors.add(skip);
			centerLayout.row();
			centerLayout.add(skip).padBottom(iconSpace);
		}

		//right layout
		Table rightLayout = new Table();
		root.add(rightLayout).expandX().growY().right().uniform().pad(iconSpace);

		//flight and pause buttons
		flightBtn = createIcon(skin, FLIGHT, new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(LaplacityAssets.lightClickSound);
				if (catFood.getLaunches() > 0) {
					changeGameMode(GameMode.FLIGHT);
					catFI.update(catFood.launch());
				} else {
					catDialog.show(GameInterface.this);
					changeGameMode(GameMode.NONE);
				}
				guiHandler.pressButton(FLIGHT);
			}
		});
		pauseBtn = createModeIcon(skin, PAUSE, GameMode.NONE, LaplacityAssets.lightClickSound);
		flightCell = rightLayout.add(flightBtn)
				.size(iconSize).space(iconSpace);


		rightLayout.row();
		selectedMode = new ImageButton(skin, "SquareNone");
		selectedMode.setName(MODE_SELECTOR);
		guiHandler.putButton(selectedMode);
		selectedMode.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				LaplacityAssets.playSound(clickSound);
				modes.setVisible(!modes.isVisible());
				guiHandler.pressButton(MODE_SELECTOR);
			}
		});

		rightLayout.add(selectedMode).width(iconSize)
				.height(skin.getDrawable("square_btn").getMinHeight()
						/ skin.getDrawable("square_btn").getMinWidth() * iconSize);
		visibleActors.add(selectedMode);
		//modes table
		modes = new Table();
		rightLayout.row();
		rightLayout.add(modes).right().expandY().top().space(iconSpace);
		modes.defaults().size(iconSize).space(iconSpace);

		modes.row();
		modes.add(createModeIcon(skin, PROTONS, GameMode.PROTONS, LaplacityAssets.genStartSound));
		modes.row();
		modes.add(createModeIcon(skin, ELECTRONS, GameMode.ELECTRONS, LaplacityAssets.genStartSound));
		modes.row();
		modes.add(createModeIcon(skin, DIRICHLET, GameMode.DIRICHLET, LaplacityAssets.sprayStartSound));
		modes.row();
		modes.add(createModeIcon(skin, ERASER, GameMode.ERASER, LaplacityAssets.lightClickSound));

		rightLayout.row();
		speedUpBtn = new TextButton("x2", skin, "checked");
		speedUpBtn.setVisible(false);
		speedUpBtn.setName(SPEED_UP);
		guiHandler.putButton(speedUpBtn);
		speedUpBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				LaplacityAssets.playSound(lightClickSound);
				timeSpeedUp = speedUpBtn.isChecked() ? 2 : 1;
				guiHandler.pressButton(SPEED_UP);
			}
		});


		rightLayout.add(speedUpBtn).expandY().bottom();
	}

	/**
	 * Обновляет интерфейс при изменении мода.
	 * Меняет кнопку запуска на паузу, скрывая остальные элементы, и наоборот.
	 * Меняет вид кнопки выбора мода, и открывает выбор, если мод не выбран.
	 *
	 * @see #selectedMode
	 * @see #visibleActors
	 */
	public void updateCurMode() {
		if (currentGameMode != GameMode.FLIGHT) {
			selectedMode.setStyle(selectedMode.getSkin().get("Square" + currentGameMode.getName(),
					ImageButton.ImageButtonStyle.class));
		}
		modes.setVisible(currentGameMode == GameMode.NONE);	//сразу видимы если не выбран мод

		if (currentGameMode == GameMode.FLIGHT) {
			flightCell.setActor(pauseBtn);
			for (Actor actor : visibleActors) {
				actor.setVisible(false);
			}
			speedUpBtn.setVisible(true);
		} else {
			flightCell.setActor(flightBtn);
			for (Actor actor : visibleActors) {
				actor.setVisible(actor.getName() != "skipBtn" || Settings.isShowSkip());
			}

			speedUpBtn.setVisible(false);
			speedUpBtn.setChecked(false);
			timeSpeedUp = 1;
		}
	}
	/**
	 * Создает иконку ImageButtons с заданным обработчиком событий.
	 *
	 * @param skin скин с текстурой кнопки.
	 * @param styleName название стиля в скине.
	 * @param listener обработчик событий.
	 * @see ImageButton
	 */
	private ImageButton createIcon(Skin skin, String styleName, ClickListener listener) {
		ImageButton btn = new ImageButton(skin, styleName);
		if (!styleName.equals(FLIGHT) && !styleName.equals(PAUSE)) {
			visibleActors.add(btn);
		}

		btn.setName(styleName);
		guiHandler.putButton(btn);

		btn.getImageCell().grow();
		btn.addListener(listener);
		return btn;
	}

	/**
	 * Создает иконку мода по переданному моду.
	 *
	 * @param skin скин с текстурами кнопки.
	 * @param styleName название стиля кнопки в скине
	 * @param mode мод для кнопки
	 * @param sound звук, проигрываемый при нажатии на кнопку.
	 * @see #createIcon(Skin, String, ClickListener)
	 */
	private ImageButton createModeIcon(Skin skin, String styleName, GameMode mode, Sound sound) {
		return createIcon(skin, styleName, new ClickListener(){
			@Override
			public void clicked (InputEvent event, float x, float y) {
				LaplacityAssets.playSound(sound);

				Actor targetActor = event.getTarget();
				Button targetBtn = targetActor instanceof Button ? (Button) targetActor :
						targetActor.getParent() instanceof Button ? ((Button) targetActor.getParent()) : null;
				guiHandler.pressButton(targetBtn == null ? null : targetBtn.getName());

				if (currentGameMode == mode) {
					changeGameMode(GameMode.NONE);
				} else {
					changeGameMode(mode);
				}
			}
		});
	}

	/**
	 * Показывает сообщение с текстом text.
	 * @param text текст сообщения
	 */
	public void showMessage(String text) {
		msgBox.show(this, text);
	}
	/**
	 * Показывает сообщение с текстом text на showTime секунд.
	 * @param text текст сообщения
	 * @param showTime время показа
	 */
	public void showMessage(String text, float showTime) {
		msgBox.show(this, text, showTime);
	}

	/**
	 * Меняет текст сообщения.
	 * @param text новый текст сообщения
	 */
	public void changeMessageText(String text) {
		msgBox.setText(text);
	}
	/**
	 * Скрывает сообщение.
	 */
	public void hideMessage() {
		msgBox.hide();
	}

	public void resize() {
		msgBox.stageResized(this);
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

		if (len2 < 4 * PARTICLE_SIZE * PARTICLE_SIZE && !guiHandler.slingshotHandler.getLocked()) {
			TrajectoryRenderer.changingDir = true;
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
		if (TrajectoryRenderer.changingDir) {
			guiHandler.slingshotHandler.setSlingshot(cat, cat.getSlingshotX(), cat.getSlingshotY());
			TrajectoryRenderer.changingDir = false;
		}
		
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
