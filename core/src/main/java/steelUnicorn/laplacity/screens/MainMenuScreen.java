package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.*;

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
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.LevelButton;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;
import steelUnicorn.laplacity.utils.Settings;

/**
 * Класс скрин для главного меню. Создает и отображает MainMenu на экране.
 */
public class MainMenuScreen extends ManagedScreen {
	MainMenu menuStage;

	public MainMenuScreen() {
		menuStage = new MainMenu(guiViewport);
	}

	@Override
	public void create() {
		this.addInputProcessor(menuStage);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		menuStage.act();
		menuStage.draw();
	}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		menuStage.dispose();
	}
}
