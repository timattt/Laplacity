package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;

/**
 * Диалоговое всплывающее окно подтверждения выхода на главное меню.
 *
 * Окно содержит описание и кнопки
 * Yes No
 */
public class ReturnDialog extends Dialog {
	private static final String title = "Warning";
	private static final float btnWidth = Globals.UI_WORLD_WIDTH * 0.1f;
	private static final float btnHeight = Globals.UI_WORLD_HEIGHT * 0.08f;
	private static final float padSize = 30;

	public ReturnDialog(String title, Skin skin) {
		super(title, skin);
	}

	public ReturnDialog(Skin skin) {
		this(title, skin);
		Color color = getColor();
		color.a = 0;
		setColor(color);
		//initialize
		text("Return to main menu");
		getContentTable();
		getButtonTable().pad(padSize);
		getButtonTable().pad(padSize).defaults()
						.width(btnWidth)
						.height(btnHeight)
						.pad(padSize);

		button("No", false);
		button("Yes", true);
	}

	@Override
	protected void result(Object object) {
		LaplacityAssets.playSound(LaplacityAssets.clickSound);
		if ((Boolean) object) {
			LaplacityAssets.changeTrack("music/main theme_drop.ogg");
			Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen, null);
		}
	}
}
