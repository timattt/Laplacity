package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	private static final float btnWidth = Globals.UI_WORLD_WIDTH * 0.2f;
	private static final float btnHeight = Globals.UI_WORLD_HEIGHT * 0.15f;
	private static final float padSize = 30;

	public ReturnDialog(Skin skin) {
		super("", skin);
		Color color = getColor();
		color.a = 0;
		setColor(color);
		//initialize
		Label textLabel = new Label("RETURN TO LEVELS", skin, "noback");
		textLabel.setScale(2);
		text(textLabel);
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
			Globals.game.getScreenManager().pushScreen(Globals.nameLevelsScreen, null);
		}
	}
}
