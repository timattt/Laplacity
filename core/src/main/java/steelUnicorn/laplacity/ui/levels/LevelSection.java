package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.progress;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
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
    private Table levelButtons;
    private TextButton lockButton;
    private Cell<Actor> mainCell;

    public int secSize;
    private static final int levelsRow = 5;
    public static float levelSpace = UI_WORLD_HEIGHT * 0.05f;

    private final int sectionNumber;


    private SectionProgress sectionProgress;


    public LevelSection(int sectionNumber, Skin skin) {
        this.sectionNumber = sectionNumber;
        sectionProgress = progress.getSectionProgress(sectionNumber);
        mainCell = addLevels(skin);
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
    private Cell<Actor> addLevels(Skin skin) {
        createLevelsTable(skin);

        if (sectionProgress.isOpened()) {
            return add(levelButtons);
        } else {
            lockButton = new TextButton("Open section " + progress.starsCollected +
                    "/" + sectionProgress.getStarsToOpen(), skin);

            lockButton.setDisabled(progress.starsCollected < sectionProgress.getStarsToOpen());
            lockButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    sectionProgress.setOpened(true);
                    mainCell.setActor(levelButtons);
                }
            });

            return add(lockButton);
        }
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
        if (mainCell.getActor() != levelButtons && lockButton != null) {
            lockButton.setDisabled(progress.starsCollected < sectionProgress.getStarsToOpen());
            lockButton.setText("Open section " + progress.starsCollected +
                    "/" + sectionProgress.getStarsToOpen());
        }
    }
}
