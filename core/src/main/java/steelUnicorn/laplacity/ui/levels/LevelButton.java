package steelUnicorn.laplacity.ui.mainmenu;


import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Класс LevelButton текстовая кнопка содержащая путь до файла с уровнем, и номером уровня.
 *
 * При нажатии на уровень запускается инициализация уровня из нужной текстуры и включается
 * игровой экран.
 *
 */
public class LevelButton extends TextButton {
	private String lvlImgPath;
	private int levelNumber;

	/**
	 * Обработчик событий. Т.к. все кнопки одинаковые, разница лишь в подгружаемом уровне
	 * создается один обработчик на все уровни!
	 */
	public static ChangeListener listener = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			LaplacityAssets.playSound(LaplacityAssets.lightClickSound);
			LevelButton btn = (LevelButton) actor;
			GameProcess.levelNumber = btn.levelNumber;
			GameProcess.initLevel(Globals.assetManager.get(btn.lvlImgPath, Texture.class));

			LaplacityAssets.setLevelTrack();;
			Globals.game.getScreenManager().pushScreen(Globals.nameGameScreen, Globals.nameSlideOut);
		}
	};

	public LevelButton(String text, Skin skin, String lvlImgPath, int levelNumber) {
		super(text, skin);
		this.lvlImgPath = lvlImgPath;
		this.levelNumber = levelNumber;
	}
}