package steelUnicorn.laplacity.ui;


import steelUnicorn.laplacity.Globals;
import steelUnicorn.laplacity.GameProcess;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class LevelButton extends TextButton {
	private String lvlImgPath;

	public static ChangeListener listener = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			// TODO Auto-generated method stub
			if (GameProcess.field != null) {
				GameProcess.disposeLevel();
			}

			GameProcess.initLevel(Globals.assetManager.get(((LevelButton) actor).lvlImgPath, Texture.class));
			Globals.game.setScreen(Globals.gameScreen);
		}
	};
	public LevelButton(String text, Skin skin, String lvlImgPath) {
		super(text, skin);
		this.lvlImgPath = lvlImgPath;
	}
}
