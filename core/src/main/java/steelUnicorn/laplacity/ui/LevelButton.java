package steelUnicorn.laplacity.ui;

import steelUnicorn.laplacity.LaplacityAssets;
import steelUnicorn.laplacity.Globals;

import com.badlogic.gdx.Gdx;
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
			if (LaplacityAssets.LEVEL_TILEMAP != null) {
				LaplacityAssets.LEVEL_TILEMAP.dispose();
			}

			LaplacityAssets.LEVEL_TILEMAP = Globals.assetManager.get(((LevelButton) actor).lvlImgPath, Texture.class);
			Globals.game.setScreen(Globals.gameScreen);
		}
	};
	public LevelButton(String text, Skin skin, String lvlImgPath) {
		super(text, skin);
		this.lvlImgPath = lvlImgPath;
	}
}
