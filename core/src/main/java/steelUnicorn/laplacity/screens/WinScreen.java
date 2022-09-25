package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.WinInterface;

/**
 * Победный экран, который открывается при прохождении уровня.
 * Отображает интерфес, создаваемый классом WinInterface
 *
 * При создании используется функция loadWinScreen, принимающая количество собранных звезд.
 *
 * @see WinInterface
 */
public class WinScreen extends ManagedScreen {
	private final WinInterface winStage;

	/**
	 * Инициализирует winStage.
	 */
	public WinScreen() {
		super();
		winStage = new WinInterface(Globals.guiViewport);
	}
	
	@Override
	protected void create() {
		this.addInputProcessor(winStage);
	}

	/**
	 * Собирает интерфейс для отображения на экране.
	 * @param starsScored количество собранных звезд
	 * @see WinInterface#buildStage(int, Skin)
	 */
	public void loadWinScreen(int starsScored) {
		winStage.buildStage(starsScored, LaplacityAssets.TEXSKIN);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
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
