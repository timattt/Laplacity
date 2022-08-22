package steelUnicorn.laplacity.ui.mainmenu;

import static steelUnicorn.laplacity.core.LaplacityAssets.EARTH_BACKGROUND;
import static steelUnicorn.laplacity.core.LaplacityAssets.SKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.FpsCounter;
import steelUnicorn.laplacity.utils.LevelsParser;

/**
 * Класс сцена для победного экрана.
 * Создает сцену из сообщения о победе с подсчетом очков
 * и 3х навигационных кнопок: Menu Replay Next
 */
public class WinInterface extends Stage {
    //Константы размеров
    private static final float spaceSize = Globals.UI_WORLD_HEIGHT * 0.03f;
    private static final float btnScale = 0.8f;
    private static final float edgeStarPad = Globals.UI_WORLD_HEIGHT * 0.05f;

    private Table root; //<< Корневая таблица для позиционирования

    private Image background;

    private static final String[] msg = new String[]{"Done...", "Acceptable.", "Good !", "Excellent !!!"};

    public WinInterface(Viewport viewport) {
        super(viewport);

        background = new Image(EARTH_BACKGROUND);
        background.setSize(background.getPrefWidth() / background.getPrefHeight() * viewport.getWorldHeight(),
                viewport.getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + viewport.getWorldWidth() / 2 , 0);
        addActor(background);
        //fpsCounter
        FpsCounter fpsCounter = new FpsCounter(SKIN);
        addActor(fpsCounter);

        root = new Table();
        root.setFillParent(true);
        root.setName("rootTable");
        addActor(root);
    }

    /**
     * Функция очищающая корневую таблицу
     */
    private void clearStage() {
        if (root.hasChildren()) {
            Table buttons = root.findActor("buttonsTable");
            buttons.clearChildren();
            root.clearChildren();
        }
    }

    /**
     * Функция собирающая описание и кнопки вместе
     *
     * @param score - количество заработанных очков
     */
    public void buildStage(int score) {
        clearStage();

        //label
        Label done = new Label(msg[score], TEXSKIN);
        done.setAlignment(Align.center);
        done.setName("doneLabel");
        root.add(done).space(spaceSize);
        //stars
        root.row();
        Table stars = getStarRows(score);
        root.add(stars);

        root.row();
        //buttons
        Table buttons = new Table();
        buttons.setName("buttonsTable");
        root.add(buttons).space(spaceSize);

        buttons.defaults()
                .space(spaceSize);

        addButton(buttons, TEXSKIN, "ExitBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                LaplacityAssets.changeTrack("music/main theme_drop.ogg");
                Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen, Globals.nameSlideIn);
            }
        });

        addButton(buttons, TEXSKIN, "ReplayBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                GameProcess.initLevel(sectionLevels.get(GameProcess.sectionNumber - 1)
                                .get(GameProcess.levelNumber - 1));
            }
        });

        //Если текущий уровень максимален, кнопки max не будет
        if (checkNextLevel()) {
            addButton(buttons, TEXSKIN, "NextBtn", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LaplacityAssets.playSound(LaplacityAssets.clickSound);
                    int nextSection;
                    int nextLevel;
                    if (GameProcess.sectionNumber < sectionLevels.size &&
                        GameProcess.levelNumber
                                == sectionLevels.get(GameProcess.sectionNumber - 1).size) {
                        nextSection = ++GameProcess.sectionNumber;
                        nextLevel = GameProcess.levelNumber = 1;
                    } else {
                        nextSection = GameProcess.sectionNumber;
                        nextLevel = ++GameProcess.levelNumber;
                    }
                    GameProcess.levelParams = LevelsParser.getParams(nextSection, nextLevel);
                    GameProcess.initLevel(sectionLevels.get(nextSection - 1).get(nextLevel - 1));
                    LaplacityAssets.setLevelTrack();
                }
            });
        }
    }

    private static boolean checkNextLevel() {
        return GameProcess.levelNumber < sectionLevels.get(GameProcess.sectionNumber - 1).size ||
                (GameProcess.levelNumber == sectionLevels.get(GameProcess.sectionNumber - 1).size &&
                        GameProcess.sectionNumber < sectionLevels.size
                        && Globals.progress.getSectionProgress(GameProcess.sectionNumber + 1).isOpened());
    }

    private Cell<Button> addButton(Table table, Skin skin,
                                   String name, ChangeListener listener) {
        Button btn = new Button(skin, name);
        btn.setName(name);
        btn.addListener(listener);

        return table.add(btn).size(btn.getPrefWidth() * btnScale, btn.getPrefHeight() * btnScale);
    }

    public void resizeBackground() {
        background.setSize(background.getPrefWidth() / background.getPrefHeight()
                        * this.getViewport().getWorldHeight(),
                this.getViewport().getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + this.getViewport().getWorldWidth() / 2 , 0);
    }

    /**
     * функция создания пирамиды
     * @param score - количество очков
     * @return - таблица с пирамидой
     */
    private Table getStarRows(int score) {
        Table stars = new Table();
        StringBuilder starName = new StringBuilder("star00");
        for (int i = 1; i <= GameProcess.MAX_STAR; i++) {
            starName.setCharAt(4, score >= i ? '1' : '0');
            starName.setCharAt(5, Character.forDigit(i , 10));

            Image star = new Image(TEXSKIN, starName.toString());
            star.setName("star" + i);
            stars.add(star).padTop(i != 2 ? edgeStarPad : 0);
        }

        return stars;
    }
}
