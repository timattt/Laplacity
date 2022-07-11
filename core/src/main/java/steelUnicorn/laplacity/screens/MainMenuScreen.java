package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class MainMenuScreen extends ScreenAdapter {
	Stage menuStage;

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

		buildMainMenu(root, skin);
	}

	/**
	 * Function builds main menu and add listeners to buttons
	 *
	 * @param root main table of menuStage
	 * @param skin of ui elements
	 */
	private void buildMainMenu(Table root, Skin skin) {
		Table mainMenu = new Table();
		mainMenu.setName("mainMenu");
		root.add(mainMenu).expandX().left().padLeft(50);

		TextButton button = new TextButton("Play", skin);
		button.setName("playBtn");
		mainMenu.add(button).width(120).space(20);

		mainMenu.row();
		button = new TextButton("Options", skin);
		button.setName("optionsBtn");
		mainMenu.add(button).width(120).space(20);

		mainMenu.row();
		button = new TextButton("Credits", skin);
		button.setName("creditsBtn");
		mainMenu.add(button).width(120).space(20);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
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
