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

	public ReturnDialog(String title, Skin skin) {
		super(title, skin);
	}

	public ReturnDialog(Skin skin) {
		this(title, skin);
		//initialize
		text("Return to main menu");
		button("No", false);
		button("Yes", true);

		getContentTable().pad(10);
		getButtonTable().pad(10);
	}

	@Override
	protected void result(Object object) {
		if ((Boolean) object) {
			Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen, Globals.nameSlideIn);
		}
	}
}
