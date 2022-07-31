package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.mainmenu.WinInterface;

/**
 * ÐšÐ»Ð°Ñ�Ñ� Ð¿Ð¾Ð±ÐµÐ´Ð½Ð¾Ð³Ð¾ Ñ�ÐºÑ€Ð°Ð½Ð°
 * ÐžÑ‚Ð¾Ð±Ñ€Ð°Ð¶Ð°ÐµÑ‚ Ñ�Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ð¾ Ð¿Ñ€Ð¾Ñ…Ð¾Ð¶Ð´ÐµÐ½Ð¸Ð¸ Ð¸ ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾ Ð¾Ñ‡ÐºÐ¾Ð².
 *
 * Ð¢Ð°Ðº Ð¶Ðµ Ð½Ð° Ñ�ÐºÑ€Ð°Ð½Ðµ Ð²Ð¾Ð·Ð½Ð¸ÐºÐ°ÑŽÑ‚ 3 Ð½Ð°Ð²Ð¸Ð³Ð°Ñ†Ð¸Ð¾Ð½Ð½Ñ‹Ðµ ÐºÐ½Ð¾Ð¿ÐºÐ¸:
 * Ð·Ð°Ð¿ÑƒÑ�ÐºÐ°ÑŽÑ‰Ð¸Ð¹ Ñ�Ð»ÐµÐ´ÑƒÑŽÑ‰Ð¸Ð¹ ÑƒÑ€Ð¾Ð²ÐµÐ½ÑŒ
 * Ð¿ÐµÑ€ÐµÐ·Ð°Ð³Ñ€ÑƒÐ¶Ð°ÑŽÑ‰Ð¸Ð¹ Ñ‚ÐµÐºÑƒÑ‰Ð¸Ð¹
 * Ð¿ÐµÑ€ÐµÑ…Ð¾Ð´Ñ�Ñ‰Ð¸Ð¹ Ð² Ð¼ÐµÐ½ÑŽ
 *
 * Ð¡ Ð¿Ð¾Ð¼Ð¾Ñ‰ÑŒÑŽ Ñ„ÑƒÐ½ÐºÑ†Ð¸Ð¸ loadWinScreen Ð¿Ñ€Ð¸Ð½Ð¸Ð¼Ð°ÑŽÑ‰ÐµÐ¹ ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑ�Ñ‚Ð²Ð¾ Ð¾Ñ‡ÐºÐ¾Ð² Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»Ñ�
 * Ð¿Ð¾Ð´Ð³Ñ€ÑƒÐ¶Ð°ÐµÑ‚Ñ�Ñ� Ñ�Ð¾Ð¾Ñ‚Ð²ÐµÑ‚Ñ�Ð²ÑƒÑŽÑ‰Ð¸Ð¹ Ñ�ÐºÑ€Ð°Ð½.
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
	 * Ð¤ÑƒÐ½ÐºÑ†Ð¸Ñ� Ð¾Ñ‡Ð¸Ñ‰Ð°ÑŽÑ‰Ð°Ñ� Ñ‚Ð°Ð±Ð»Ð¸Ñ†Ñƒ Ñ�ÐºÑ€Ð°Ð½Ð° Ð¸ Ð¿Ð¾Ð´Ð³Ñ€ÑƒÐ¶Ð°ÑŽÑ‰Ð°Ñ� Ð½Ð¾Ð²ÑƒÑŽ, Ð´Ð»Ñ� Ð¾Ñ‚Ð¾Ð±Ñ€Ð°Ð¶ÐµÐ½Ð¸Ñ�
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
