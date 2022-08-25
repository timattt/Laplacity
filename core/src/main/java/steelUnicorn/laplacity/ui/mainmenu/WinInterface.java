package steelUnicorn.laplacity.ui.mainmenu;

import static steelUnicorn.laplacity.core.LaplacityAssets.EARTH_BACKGROUND;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.Laplacity;
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
    private static final float btnSpace = Globals.UI_WORLD_WIDTH * 0.02f;
    private static final float starSpace = Globals.UI_WORLD_HEIGHT * 0.03f;
    private static final float starsSpace = Globals.UI_WORLD_HEIGHT * 0.07f;
    private static final float btnScale = 1.5f;
    private static final float edgeStarPad = Globals.UI_WORLD_HEIGHT * 0.05f;
    private static final float starScale = 1.5f;

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
        if (Laplacity.isDebugEnabled()) {
            FpsCounter fpsCounter = new FpsCounter(TEXSKIN, "noback");
            addActor(fpsCounter);
        }

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

        //stars
        root.row();
        Table stars = getStarRows(score);
        root.add(stars).padBottom(starsSpace);

        root.row();
        //buttons
        Table buttons = new Table();
        buttons.setName("buttonsTable");
        root.add(buttons);

        buttons.defaults()
                .space(btnSpace);

        addButton(buttons, TEXSKIN, "Home", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                LaplacityAssets.changeTrack("music/main theme_drop.ogg");
                Globals.game.getScreenManager().pushScreen(Globals.nameLevelsScreen, Globals.blendTransitionName);
            }
        });

        addButton(buttons, TEXSKIN, "Replay", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                GameProcess.initLevel(sectionLevels.get(GameProcess.sectionNumber - 1)
                                .get(GameProcess.levelNumber - 1));

            }
        });

        //В последнем уровне кнопку не надо отображать
        if (!isLastLevel()) {
            ChangeListener listener = null;
            if (isLastSectionLevel()) {
                //Открыть levelsScreen в следующей секции
                listener = new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        LaplacityAssets.playSound(LaplacityAssets.clickSound);
                        Globals.levelsScreen.levelsTab.nav.next();  //переключение на след секцию
                        LaplacityAssets.changeTrack("music/main theme_drop.ogg");
                        Globals.game.getScreenManager().pushScreen(Globals.nameLevelsScreen,
                                Globals.blendTransitionName);
                    }
                };
            } else {
                //Следующий уровень в той же секции
                listener = new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        LaplacityAssets.playSound(LaplacityAssets.clickSound);
                        int section = GameProcess.sectionNumber;
                        int level = ++GameProcess.levelNumber;

                        GameProcess.levelParams = LevelsParser.getParams(section, level);
                        GameProcess.initLevel(sectionLevels.get(section - 1).get(level - 1));
                        LaplacityAssets.setLevelTrack();
                    }
                };
            }

            addButton(buttons, TEXSKIN, "Next", listener);
        }
    }

    private static boolean isLastLevel() {
        return GameProcess.sectionNumber == sectionLevels.size &&
                GameProcess.levelNumber == sectionLevels.get(GameProcess.sectionNumber - 1).size;
    }

    private static boolean isLastSectionLevel() {
        return GameProcess.sectionNumber < sectionLevels.size &&
                GameProcess.levelNumber == sectionLevels.get(GameProcess.sectionNumber - 1).size;
    }

    private Cell<ImageButton> addButton(Table table, Skin skin,
                                        String name, ChangeListener listener) {
        ImageButton btn = new ImageButton(skin, name);
        btn.getImageCell().grow();
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
            stars.add(star).size(star.getPrefWidth() * starScale,
                            star.getPrefHeight() * starScale)
                    .space(starSpace).padTop(i != 2 ? edgeStarPad : 0).top();
        }

        return stars;
    }
}
