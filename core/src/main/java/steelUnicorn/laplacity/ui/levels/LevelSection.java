package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;
import static steelUnicorn.laplacity.core.Globals.progress;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    private Table sectionLayout;
    private Table levelButtons;
    private TextButton lockButton;

    private static final float lockBtnHeight = UI_WORLD_HEIGHT * 0.12f;
    private static final float lockBtnWidth = UI_WORLD_WIDTH * 0.3f;
    private static final float lockBtnPadTop = UI_WORLD_HEIGHT * 0.08f;
    private static final float lockLabelPad = UI_WORLD_HEIGHT * 0.05f;
    private static final float lockFontScale = 1.3f;
    private static final float starScale = 0.7f;

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
        sectionLayout = new Table();

        createLevelsTable(skin);

        if (!sectionProgress.isOpened() && !Globals.LEVEL_DEBUG) {
            lockButton = new TextButton(progress.starsCollected +
                    "/" + sectionProgress.getStarsToOpen(), skin);
            lockButton.setName("lock");
            lockButton.getLabel().setFontScale(lockFontScale);

            lockButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //progress
                    sectionProgress.setOpened(true);
                    sectionProgress.openLevel(1);
                    //buttons
                    sectionLayout.findActor("lock").setVisible(false);
                    ((LevelWidget) levelButtons.findActor("level1"))
                            .setDisabled(false);
                }
            });

            lockButton.getLabelCell().expand(false, false).padRight(lockLabelPad);

            Image starImg = new Image(TEXSKIN, "label_star");
            lockButton.add(starImg).size(starImg.getHeight() * starScale,
                    starImg.getHeight() * starScale);

            lockButton.setDisabled(progress.starsCollected < sectionProgress.getStarsToOpen());

            sectionLayout.add(lockButton).width(lockBtnWidth).height(lockBtnHeight).padTop(lockBtnPadTop);
        } else {
            sectionLayout.add().height(lockBtnHeight).padTop(lockBtnPadTop);
        }

        sectionLayout.row();
        sectionLayout.add(levelButtons);

        return add(sectionLayout);
    }

    public void openLevel(int level) {
        Actor actor = levelButtons.findActor("level" + level);
        if (actor != null) {
            LevelWidget wg = (LevelWidget) actor;
            wg.setDisabled(level == 1 && !sectionProgress.isOpened());
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
        if (sectionLayout.findActor("lock") != null) {
            lockButton.setDisabled(progress.starsCollected < sectionProgress.getStarsToOpen());
            lockButton.setText(progress.starsCollected +
                    "/" + sectionProgress.getStarsToOpen());
        }
    }
}
