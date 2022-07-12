package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScreen extends ScreenAdapter {
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
		levelsTab.add(description).spaceBottom(20);
		levelsTab.row();

		Table levels = new Table();
		levelsTab.add(levels);

		int levelCount = 10; //TODO - получить количество уровней
		int levelsRow = 4;

		for (int i = 1; i <= levelCount; i++) {
			TextButton btn = new TextButton(String.valueOf(i), skin);
			btn.setName(String.valueOf(i) + "_level");
			btn.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub
					game.setScreen(gameScreen);
				}
			});
			levels.add(btn).space(20);
			if (i % levelsRow == 0 && i != levelCount) {
				levels.row();
			}
		}
	}

	/**
	 * Create options table
	 *
	 * TODO add options functionality
	 * @param skin
	 */
	private void createOptions(Skin skin) {
		optionsTab = new Table();
		optionsTab.setName("tab");

		Label description = new Label("Options:", skin);
		optionsTab.add(description).spaceBottom(20);
		optionsTab.row();

		Table options = new Table();
		optionsTab.add(options).spaceBottom(20);

		CheckBox checkBox = new CheckBox("Enable sound", skin);
		checkBox.setName("sound_checkbox");
		options.add(checkBox);

		options.row();
		checkBox = new CheckBox("Enable Music", skin);
		checkBox.setName("music_checkbox");
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
		super.render(delta);
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.95f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		menuStage.draw();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		Gdx.input.setInputProcessor(menuStage);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		Gdx.input.setInputProcessor(null);
	}
}
