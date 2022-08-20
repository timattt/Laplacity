package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.LEVEL_DEBUG;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.progress;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    public int secSize;
    public static final float lvlBtnScale = 0.6f;
    private static final int levelsRow = 5;
    public static float levelSpace = UI_WORLD_HEIGHT * 0.05f;

    private final int sectionNumber;  //section number greater than pathsSize by 1
    public LevelSection(int sectionNumber, Skin skin) {
        this.sectionNumber = sectionNumber;
        addLevels(skin);
        secSize = getChildren().size;
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

            for (int i = 1; i <= lvlImages.size; i++) {
                LevelButton btn = new LevelButton(String.valueOf(i), skin, "Level",
                        lvlImages.get(i - 1), i, sectionNumber);
                btn.setName("level" + i);
                btn.addListener(LevelButton.listener);
                btn.setDisabled(progress.getProgress(sectionNumber, i) == -1 && !LEVEL_DEBUG);

                add(btn).space(levelSpace)
                        .size(btn.getPrefWidth() * lvlBtnScale, btn.getPrefHeight() * lvlBtnScale);
                //new Row
                if (i % levelsRow == 0 && i != lvlImages.size) {
                    row();
                }
            }
        } else {
            Gdx.app.log("section creation", "section number error");
        }
    }

    public void openLevel(int level) {
        Actor actor = findActor("level" + level);
        if (actor != null) {
            ((LevelButton) actor).setDisabled(false);
        }
    }
}
