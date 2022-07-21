package steelUnicorn.laplacity.ui.mainmenu.tabs;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import steelUnicorn.laplacity.ui.mainmenu.LevelButton;

/**
 * Класс LevelTab при создании создает вкладку с уровнями в главном меню.
 * Вкладка начинается с названия, и под названием табилца с уровнями.
 * Уровни подгружаются из папки /assets/levels/
 */
public class LevelsTab extends MainMenuTab {
    private static final float lvlBtnSize = UI_WORLD_WIDTH * 0.04f;
    private static final int levelsRow = 4;

    public LevelsTab(Skin skin) {
        super();

        addDescription("Levels:", skin);
        row();
        addLevels(skin);
    }

    /**
     * Функция подгружающая уровни из папки levels/ и создающая для каждого уровня кнопку.
     *
     * В качестве кнопки используется класс LevelButton
     * @param skin - ui skin
     */
    private void addLevels(Skin skin) {
        Table levels = new Table();
        add(levels);

        FileHandle[] lvlImages = Gdx.files.internal("levels/").list();

        levels.defaults()
                .width(lvlBtnSize).height(lvlBtnSize)
                .space(tabSpace);

        for (int i = 1; i <= lvlImages.length; i++) {
            LevelButton btn = new LevelButton(String.valueOf(i), skin, lvlImages[i - 1].path(), i);
            btn.setName("level" + i);
            btn.addListener(LevelButton.listener);

            levels.add(btn);
            //new Row
            if (i % levelsRow == 0 && i != lvlImages.length) {
                levels.row();
            }
        }
    }
}
