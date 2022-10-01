package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;
import static steelUnicorn.laplacity.core.Globals.progress;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.handler.FlashingBtnHandler;
import steelUnicorn.laplacity.utils.PlayerProgress.SectionProgress;


/**
 * Секция с уровнями.
 *
 * Используется в {@link LevelsTab} для таблицы выбора уровня.
 * Хранит в себе прогресс секции для быстрого доступа.
 *
 * @see LevelsTab
 * @see steelUnicorn.laplacity.utils.PlayerProgress.SectionProgress
 */
public class LevelSection extends Table {
    public static float levelSpace = UI_WORLD_HEIGHT * 0.05f;
    private static final float lockBtnHeight = UI_WORLD_HEIGHT * 0.12f;
    private static final float lockBtnWidth = UI_WORLD_WIDTH * 0.3f;
    private static final float lockBtnPadTop = UI_WORLD_HEIGHT * 0.08f;
    private static final float lockLabelPad = UI_WORLD_HEIGHT * 0.05f;
    private static final float lockFontScale = 1.3f;
    private static final float starScale = 0.7f;

    private static final int levelsRow = 5;
    public int secSize;

    private Table sectionLayout;
    private Table levelButtons;
    private TextButton lockButton;
    private static final float lockFlashingInterval = 0.5f;
    private static final int lockFlashingRepeat = (int) (10 / lockFlashingInterval);



    private final int sectionNumber;
    private final SectionProgress sectionProgress;

    private FlashingBtnHandler flashingBtnHandler;

    /**
     * Инициализирует секцию.
     * @param sectionNumber номер секции
     * @param skin скин с текстурами кнопок
     */
    public LevelSection(int sectionNumber, Skin skin) {
        this.sectionNumber = sectionNumber;
        sectionProgress = progress.getSectionProgress(sectionNumber);
        addLevels(skin);
    }

    /**
     * Создает таблицу кнопок выбора уровней LevelWidget.
     * В ряду {@link #levelsRow} кнопок. Всего в секции 10 уровней.
     * @param skin скин с текстурами кнопок.
     *
     * @see LevelWidget
     */
    private void createLevelsTable(Skin skin) {
        levelButtons = new Table();

        if (sectionNumber - 1 < sectionLevels.size) {
            secSize = sectionLevels.get(sectionNumber - 1).size;

            for (int lvlNumber = 1; lvlNumber <= secSize; lvlNumber++) {
                LevelWidget lvlWidget = new LevelWidget(skin,
                        sectionNumber, lvlNumber, progress.getProgress(sectionNumber, lvlNumber));

                levelButtons.add(lvlWidget).space(levelSpace);
                //new Row
                if (lvlNumber % levelsRow == 0 && lvlNumber != secSize) {
                    levelButtons.row();
                }
            }
        } else {
            Gdx.app.error("section creation", "section number error");
        }
    }

    /**
     * Создает таблицу с уровнями и кнопку открытия секции.
     *
     * @param skin скин с текстурами виджетов.
     */
    private Cell addLevels(Skin skin) {
        sectionLayout = new Table();

        createLevelsTable(skin);

        if (!sectionProgress.isOpened()) {
            lockButton = new TextButton(progress.starsCollected +
                    "/" + sectionProgress.getStarsToOpen(), skin);
            lockButton.setName("lock");
            lockButton.getLabel().setFontScale(lockFontScale);

            lockButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LaplacityAssets.playSound(LaplacityAssets.clickSound);
                    //progress
                    sectionProgress.setOpened(true);
                    //buttons
                    sectionLayout.findActor("lock").setVisible(false);
                    ((LevelWidget) levelButtons.findActor("level1"))
                            .setDisabled(false);
                }
            });

            lockButton.getLabelCell().expand(false, false).padRight(lockLabelPad);

            Image starImg = new Image(skin, "label_star");
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

    /**
     * Открывает уровень в секции (т.е делает кнопку активной).
     * @param level номер уровня
     */
    public void openLevel(int level) {
        Actor actor = levelButtons.findActor("level" + level);
        if (actor != null) {
            LevelWidget wg = (LevelWidget) actor;
            wg.setDisabled(false);
            wg.updateStars(wg.getSkin(), progress.getProgress(sectionNumber, level));
        }
    }

    /**
     * Обновляет кнопку уровня после прохождения (чтобы изменить звезды над уровнем).
     * @param level номер уровня
     */
    public void updateLevel(int level) {
        Actor actor = levelButtons.findActor("level" + level);
        if (actor != null) {
            LevelWidget lvlWidget = (LevelWidget) actor;
            lvlWidget.updateStars(lvlWidget.getSkin(), progress.getProgress(sectionNumber, level));
        }
    }

    /**
     * Меняет количество набранных звезд на лейбле открытия секции.
     */
    public void show() {
        //в mainCell лежит levelButtons если секция открыта. Если закрыта, проверяем что надпись в порядке
        if (sectionLayout.findActor("lock") != null) {
            lockButton.setDisabled(progress.starsCollected < sectionProgress.getStarsToOpen());
            lockButton.setText(progress.starsCollected +
                    "/" + sectionProgress.getStarsToOpen());
        }

        if (!sectionProgress.isOpened() &&
                progress.starsCollected >= sectionProgress.getStarsToOpen()) {
            if (flashingBtnHandler == null) {
                flashingBtnHandler = new FlashingBtnHandler();
            }
            flashingBtnHandler.setBtn(lockButton);
            flashingBtnHandler.startFlashing(0, lockFlashingInterval, lockFlashingRepeat);
        }
    }

    /**
     * Открывает все уровни. Вспомогательная функция.
     */
    public void openLevels() {
        for (int level = 1; level <= secSize; level++) {
            openLevel(level);
        }
    }
}
