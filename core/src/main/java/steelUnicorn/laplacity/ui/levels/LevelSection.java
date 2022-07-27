package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import steelUnicorn.laplacity.utils.LevelsParser;

public class LevelSection extends Table {
    private static final float lvlBtnSize = UI_WORLD_WIDTH * 0.04f;
    private static final int levelsRow = 4;
    private static float tabSpace = UI_WORLD_HEIGHT * 0.03f;

    private int sectionNumber;
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
        if (sectionNumber < LevelsParser.sectionLevelsPaths.size) {
            Array<String> lvlPaths = LevelsParser.sectionLevelsPaths.get(sectionNumber);

            defaults().size(lvlBtnSize)
                    .space(tabSpace);

            for (int i = 1; i <= lvlPaths.size; i++) {
                LevelButton btn = new LevelButton(String.valueOf(i), skin, lvlPaths.get(i - 1), i);
                btn.setName("level" + i);
                btn.addListener(LevelButton.listener);

                add(btn);
                //new Row
                if (i % levelsRow == 0 && i != lvlPaths.size) {
                    row();
                }
            }
        } else {
            Gdx.app.log("section creation", "section number error");
        }
    }
}
