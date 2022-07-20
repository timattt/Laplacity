package steelUnicorn.laplacity.ui;


import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class LevelButton extends TextButton {
	private String lvlImgPath;
	private int levelNumber;

	public static ChangeListener listener = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			LevelButton btn = (LevelButton) actor;
			GameProcess.levelNumber = btn.levelNumber;
			GameProcess.initLevel(Globals.assetManager.get(btn.lvlImgPath, Texture.class));
			//Globals.game.setScreen(Globals.gameScreen);
			Globals.game.getScreenManager().pushScreen(Globals.nameGameScreen, Globals.nameSlideOut);
		}
	};
	public LevelButton(String text, Skin skin, String lvlImgPath, int levelNumber) {
		super(text, skin);
		this.lvlImgPath = lvlImgPath;
		this.levelNumber = levelNumber;
	}
}
