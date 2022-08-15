package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;


/**
 * Класс секции уровней
 *
 * В консрукторе по номеру секции создает таблицу с уровнями используя
 * LevelParser.sectionLevelsPaths
 *
 * Используется в классе LevelsTab для отображения уровней для выбора
 */
public class LevelSection extends Table {
    public static final float lvlBtnSize = UI_WORLD_WIDTH * 0.04f;
    private static final int levelsRow = 4;
    public static float tabSpace = UI_WORLD_HEIGHT * 0.03f;

    private final int sectionNumber;  //section number greater than pathsSize by 1
    public LevelSection(int sectionNumber, Skin skin) {
        this.sectionNumber = sectionNumber;
        addLevels(skin);
    }

    /**
     * Функция подгружающая уровни из папки levels/ и создающая для каждого уровня кнопку.
     *
     * В качестве кнопки используется класс LevelButton
     * @param skin - ui skin
     */
    private void addLevels(Skin skin) {
        //Подтягивание массива путей по номеру секции
        if (sectionNumber - 1 < sectionLevels.size) {
            Array<Texture> lvlImages = sectionLevels.get(sectionNumber - 1);

            defaults().size(lvlBtnSize)
                    .space(tabSpace);

            for (int i = 1; i <= lvlImages.size; i++) {
                LevelButton btn = new LevelButton(String.valueOf(i), skin, lvlImages.get(i - 1),
                        i, sectionNumber);
                btn.setName("level" + i);
                btn.addListener(LevelButton.listener);

                add(btn);
                //new Row
                if (i % levelsRow == 0 && i != lvlImages.size) {
                    row();
                }
            }
        } else {
            Gdx.app.log("section creation", "section number error");
        }
    }
}
