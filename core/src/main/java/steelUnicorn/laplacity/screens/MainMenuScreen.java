package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;

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
