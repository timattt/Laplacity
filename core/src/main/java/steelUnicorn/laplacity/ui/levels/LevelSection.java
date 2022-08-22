package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.progress;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.utils.PlayerProgress.SectionProgress;


/**
 * Класс секции уровней
 *
 * В консрукторе по номеру секции создает таблицу с уровнями используя
 * LevelParser.sectionLevelsPaths
 *
 * Используется в классе LevelsTab для отображения уровней для выбора
 */
public class LevelSection extends Table {
    private Stack sectionStack;
    private Table levelButtons;
    private TextButton lockButton;

    private static final float lockButtonTextScale = 2;
    private static final float lockPad = UI_WORLD_HEIGHT * 0.1f;
    private static final float lockPadTop = UI_WORLD_HEIGHT * 0.15f;
    private static final float lockLabelPad = UI_WORLD_HEIGHT * 0.05f;

    public int secSize;
    private static final int levelsRow = 5;
    public static float levelSpace = UI_WORLD_HEIGHT * 0.05f;

    private final int sectionNumber;
    private SectionProgress sectionProgress;



    public LevelSection(int sectionNumber, Skin skin) {
        this.sectionNumber = sectionNumber;
        sectionProgress = progress.getSectionProgress(sectionNumber);
        addLevels(skin);
        secSize = levelButtons.getChildren().size;
    }


    private void createLevelsTable(Skin skin) {
        levelButtons = new Table();
        levelButtons.setName("levelsTable");
        //Подтягивание массива путей по номеру секции
        if (sectionNumber - 1 < sectionLevels.size) {
            Array<Texture> lvlImages = sectionLevels.get(sectionNumber - 1);

            for (int i = 1; i <= lvlImages.size; i++) {
                LevelWidget lvlWidget = new LevelWidget(skin,
                        i, sectionNumber, progress.getProgress(sectionNumber, i));

                levelButtons.add(lvlWidget).space(levelSpace);
                //new Row
                if (i % levelsRow == 0 && i != lvlImages.size) {
                    levelButtons.row();
                }
            }
        } else {
            Gdx.app.log("section creation", "section number error");
        }
    }

    /**
     * Функция подгружающая уровни из папки levels/ и создающая для каждого уровня кнопку.
     *
     * В качестве кнопки используется класс LevelButton
     * @param skin - ui skin
     */
    private Cell addLevels(Skin skin) {
        sectionStack = new Stack();

        createLevelsTable(skin);
        sectionStack.add(levelButtons);

        if (!sectionProgress.isOpened() && !Globals.LEVEL_DEBUG) {
            lockButton = new TextButton(progress.starsCollected +
                    "/" + sectionProgress.getStarsToOpen(), skin, "LockSection");
            lockButton.getLabel().setFontScale(lockButtonTextScale);
            lockButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    sectionProgress.setOpened(true);
                    sectionStack.findActor("lock").setVisible(false);
                }
            });
            lockButton.getLabelCell().expand(false, true).padRight(lockLabelPad);

            lockButton.add(new Image(TEXSKIN, "label_star"));


            lockButton.setDisabled(progress.starsCollected < sectionProgress.getStarsToOpen());
            Table overlay = new Table();
            overlay.add(lockButton).grow().pad(lockPadTop,
                    lockPad, lockPad, lockPad);
            overlay.setName("lock");

            sectionStack.add(overlay);
        }

        return add(sectionStack);
    }

    public void openLevel(int level) {
        Actor actor = levelButtons.findActor("level" + level);
        if (actor != null) {
            LevelWidget wg = (LevelWidget) actor;
            wg.setDisabled(false);
            wg.updateStars(TEXSKIN, progress.getProgress(sectionNumber, level));
        }
    }

    public void updateLevel(int level) {
        Actor actor = levelButtons.findActor("level" + level);
        if (actor != null) {
            ((LevelWidget) actor).updateStars(TEXSKIN, progress.getProgress(sectionNumber, level));
        }
    }

    public void show() {
        //в mainCell лежит levelButtons если секция открыта. Если закрыта, проверяем что надпись в порядке
        if (sectionStack.findActor("lock") != null) {
            lockButton.setDisabled(progress.starsCollected < sectionProgress.getStarsToOpen());
            lockButton.setText(progress.starsCollected +
                    "/" + sectionProgress.getStarsToOpen());
        }
    }
}
