package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.mainmenu.WinInterface;

/**
 * Класс победного экрана
 * Отображает сообщение о прохождении и количество очков.
 *
 * Так же на экране возникают 3 навигационные кнопки:
 * запускающий следующий уровень
 * перезагружающий текущий
 * переходящий в меню
 *
 * С помощью функции loadWinScreen принимающей количество очков пользователя
 * подгружается соответсвующий экран.
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
	 * Функция очищающая таблицу экрана и подгружающая новую, для отображения
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
	}

	@Override
	public void dispose() {
		winStage.dispose();
	}

	@Override
	public void hide() {
	}
}
