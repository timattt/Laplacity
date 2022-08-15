package steelUnicorn.laplacity.ui.levels;


import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.utils.LevelsParser;

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
	private final Texture lvlImg;
	private final int levelNumber;
	private final int sectionNumber;

	/**
	 * Обработчик событий. Т.к. все кнопки одинаковые, разница лишь в подгружаемом уровне
	 * создается один обработчик на все уровни!
	 */
	public static ChangeListener listener = new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			LaplacityAssets.playSound(LaplacityAssets.clickSound);
			LevelButton btn = (LevelButton) actor;
			GameProcess.levelNumber = btn.levelNumber;
			GameProcess.sectionNumber = btn.sectionNumber;
			GameProcess.levelParams = LevelsParser.getParams(btn.sectionNumber, btn.levelNumber);
			GameProcess.initLevel(btn.lvlImg);

			LaplacityAssets.setLevelTrack();;
		}
	};

	public LevelButton(String text, Skin skin, Texture lvlImg,
					   int levelNumber, int sectionNumber) {
		super(text, skin);
		this.lvlImg = lvlImg;
		this.levelNumber = levelNumber;
		this.sectionNumber = sectionNumber;
	}
}
