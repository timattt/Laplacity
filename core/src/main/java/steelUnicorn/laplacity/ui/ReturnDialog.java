package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import steelUnicorn.laplacity.Globals;

/**
 * Dialog popup to confirm return to menu.
 *
 * Contains warning text and 2 buttons
 * 1. NO - just close popup
 * 2. YES - return to main menu
 *
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

		//initialize
		text("Return to main menu");
		getContentTable().pad(padSize);
		getButtonTable().pad(padSize);
		getButtonTable().defaults()
						.width(btnWidth)
						.height(btnHeight)
						.pad(padSize);

		button("No", false);
		button("Yes", true);
	}

	@Override
	protected void result(Object object) {
		if ((Boolean) object) {
			Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen, Globals.nameSlideIn);
		}
	}
}
