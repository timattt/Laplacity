package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.mainmenu.WinInterface;

/**
 * Победный экран. Открывается при прохождении уровня и отображает
 * победную надпись
 * количество очков
 * навигационные кнопки menu replay next
 *
 * При создании используется функция loadWinScreen, принимающая количество заработанных очков.
 */
public class WinScreen extends ManagedScreen {
	private WinInterface winStage;

	public WinScreen() {
		super();
		winStage = new WinInterface(Globals.guiViewport);
	}
	
	@Override
	protected void create() {
		this.addInputProcessor(winStage);
	}

	/**
	 * Функция перестраивающая winStage, под пройденный уровень (поведение кнопок и количество очков)
	 * @param score
	 */
	public void loadWinScreen(float score) {
		winStage.buildStage(score);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		winStage.act();
		winStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		winStage.resizeBackground();
	}

	@Override
	public void dispose() {
		winStage.dispose();
	}

	@Override
	public void hide() {
	}
}
