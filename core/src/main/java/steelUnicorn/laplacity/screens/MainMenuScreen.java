package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.ui.LevelButton;
import steelUnicorn.laplacity.utils.Settings;

public class MainMenuScreen extends ManagedScreen {
	Stage menuStage;

	private Table levelsTab;
	private Table optionsTab;
	private Table creditsTab;

	/**
	 * Constructor of mainMenuScreen that construct ui
	 *
	 * UI
	 * - Play
	 * 	- levels table.
	 * - Options
	 * 	- sound and music checkbox.
	 * - Credits
	 * 	- label with description.
	 */
	public MainMenuScreen() {
		menuStage = new Stage(guiViewport);

		Skin skin = assetManager.get("ui/uiskin.json", Skin.class);

		Table root = new Table();
		root.setFillParent(true);
		menuStage.addActor(root);

		createLevels(skin);
		createOptions(skin);
		createCredits(skin);

		createMainMenu(root, skin);
	}

	@Override
	public void create() {
		this.addInputProcessor(menuStage);
	}

	@Override
	public void resize(int width, int height) {
	}

	/**
	 * Function Create levels table with buttons
	 * Each button has a listener. After click the listener choose right level
	 *
	 * TODO level choosing, lvl uploading
	 *
	 * @param skin
	 */
	private void createLevels(Skin skin) {
		levelsTab = new Table();
		levelsTab.setName("tab");

		Label description = new Label("Levels:", skin);
		description.setColor(Color.BLACK);
		levelsTab.add(description).spaceBottom(20);
		levelsTab.row();

		Table levels = new Table();
		levelsTab.add(levels);

		final int levelsRow = 4;
		FileHandle[] lvlImages = Gdx.files.internal("levels/").list();

		for (int i = 0; i < lvlImages.length; i++) {
			LevelButton btn = new LevelButton(String.valueOf(i + 1), skin, lvlImages[i].path(), i + 1);
			btn.setName("level" + String.valueOf(i));
			btn.addListener(LevelButton.listener);

			levels.add(btn).space(20);
			//new Row
			if ((i + 1) % levelsRow == 0 && (i + 1) != lvlImages.length) {
				levels.row();
			}
		}
	}

	/**
	 * Create options table
	 *
	 * TODO add options functionality
	 *
	 * Buttons change values in Settings.
	 *
	 * @param skin
	 */
	private void createOptions(Skin skin) {
		optionsTab = new Table();
		optionsTab.setName("tab");

		Label description = new Label("Options:", skin);
		description.setColor(Color.BLACK);
		optionsTab.add(description).spaceBottom(20);
		optionsTab.row();

		Table options = new Table();
		optionsTab.add(options).spaceBottom(20);
		//Sound and music
		CheckBox checkBox = new CheckBox("Enable sound", skin);
		checkBox.setName("sound_checkbox");
		checkBox.setChecked(Settings.getSoundVolume() == Settings.VOLUME.ON.ordinal());
		checkBox.getLabel().setColor(Color.BLACK);
		checkBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CheckBox box = (CheckBox) actor;
				Settings.setSoundVolume(box.isChecked() ? Settings.VOLUME.ON.ordinal() : Settings.VOLUME.OFF.ordinal());
				Gdx.app.log("SoundVolumeChanged", String.valueOf(Settings.getSoundVolume())); //logging for check changes
			}
		});
		options.add(checkBox);

		options.row();
		checkBox = new CheckBox("Enable Music", skin);
		checkBox.setName("music_checkbox");
		checkBox.setChecked(Settings.getMusicVolume() == Settings.VOLUME.ON.ordinal());
		checkBox.getLabel().setColor(Color.BLACK);
		checkBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CheckBox box = (CheckBox) actor;
				Settings.setMusicVolume(box.isChecked() ? Settings.VOLUME.ON.ordinal() : Settings.VOLUME.OFF.ordinal());
				Gdx.app.log("MusicVolumeChanged", String.valueOf(Settings.getMusicVolume())); //logging for check changes
			}
		});
		options.add(checkBox);
	}

	/**
	 * Create credits with label string.
	 *
	 * @param skin
	 */
	private void createCredits(Skin skin) {
		creditsTab = new Table();
		creditsTab.setName("tab");

		Label label = new Label("Credits:\n"
				+ "Made by Steel Unicorn\n"
				+ "steel-unicorn.org", skin);
		label.setColor(Color.BLACK);
		label.setName("credits_label");
		creditsTab.add(label);
	}

	/**
	 * Function builds main menu and add listeners to buttons
	 *
	 * @param root main table of menuStage
	 * @param skin of ui elements
	 */
	private void createMainMenu(Table root, Skin skin) {
		Table mainMenu = new Table();
		mainMenu.setName("mainMenu");
		root.add(mainMenu).expandX().left().padLeft(100);

		Table tmpTab = new Table();
		tmpTab.setName("tab");
		root.add(tmpTab).expandX();

		//play
		TextButton button = new TextButton("Play", skin);
		button.setName("playBtn");
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				root.getCell(root.findActor("tab")).setActor(levelsTab);
			}
		});
		mainMenu.add(button).width(120).space(20);

		//options
		mainMenu.row();
		button = new TextButton("Options", skin);
		button.setName("optionsBtn");
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				root.getCell(root.findActor("tab")).setActor(optionsTab);
			}
		});
		mainMenu.add(button).width(120).space(20);

		//credits
		mainMenu.row();
		button = new TextButton("Credits", skin);
		button.setName("creditsBtn");
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				root.getCell(root.findActor("tab")).setActor(creditsTab);
			}
		});
		mainMenu.add(button).width(120).space(20);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		menuStage.draw();
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void hide() {
		// Don't needed
	}

	@Override
	public void dispose() {
	}
}
