package steelUnicorn.laplacity.ui.mainmenu;

import static steelUnicorn.laplacity.core.LaplacityAssets.EARTH_BACKGROUND;
import static steelUnicorn.laplacity.core.LaplacityAssets.SKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
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
    private static final float btnWidth = Globals.UI_WORLD_WIDTH * 0.1f;
    private static final float btnHeight = Globals.UI_WORLD_HEIGHT * 0.1f;
    private static final float starSize = Globals.UI_WORLD_HEIGHT * 0.08f;
    private static final float fontScale = 1.5f;

    private Table root; //<< Корневая таблица для позиционирования

    private Image background;


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
        Label done = new Label("Done", SKIN);
        done.setAlignment(Align.center);
        done.setName("doneLabel");
        done.setFontScale(fontScale);
        done.setColor(Color.WHITE);
        root.add(done).space(spaceSize);
        //stars
        if (score > 0) {
            root.row();
            Table stars = getStarRows(score);
            root.add(stars);
        }
        root.row();
        //buttons
        Table buttons = new Table();
        buttons.setName("buttonsTable");
        root.add(buttons).space(spaceSize);

        buttons.defaults()
                .space(spaceSize)
                .size(btnWidth, btnHeight);

        addButton(buttons, "Exit", SKIN, "exitBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                LaplacityAssets.changeTrack("music/main theme_drop.ogg");
                Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen, Globals.nameSlideIn);
            }
        });

        addButton(buttons, "Replay", SKIN, "replayBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                GameProcess.initLevel(sectionLevels.get(GameProcess.sectionNumber - 1)
                                .get(GameProcess.levelNumber - 1));
            }
        });

        //Если текущий уровень максимален, кнопки max не будет
        if ((GameProcess.sectionNumber - 1) * Globals.LEVELS_PER_SECTION
                + GameProcess.levelNumber < Globals.TOTAL_LEVELS_AVAILABLE) {
            addButton(buttons, "Next", SKIN, "nextBtn", new ChangeListener() {
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

    private void addButton(Table table, String text, Skin skin,
                           String name, ChangeListener listener) {
        TextButton btn = new TextButton(text, skin);
        btn.setName(name);
        btn.addListener(listener);

        table.add(btn);
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
        Array<Table> rows = new Array<>();
        int n = 3;  //количество звезд в основании
        if (score <= n) {
            rows.add(getStarRow(score));
        } else {
            while (!(n * (n - 1) / 2 < score && score <= n * (n + 1) / 2)) {
                n++;
            }

            for (int rowStars = n;  score > 0; rowStars--) {
                rows.add(getStarRow((score - rowStars < 0) ? score : rowStars));
                score -= rowStars;
            }
        }

        rows.reverse();
        Table stars = new Table();
        for (Table row : rows) {
            stars.add(row);
            stars.row();
        }

        return stars;
    }

    /**
     * 1 строчка с score звезд
     * @param score - количество звезд
     * @return строчка таблицы
     */
    private Table getStarRow(int score) {
        Table row = new Table();
        for (int i = score; i > 0; i--) {
            row.add(new Image(LaplacityAssets.STAR_REGIONS[0])).size(starSize);
        }

        return row;
    }
}
