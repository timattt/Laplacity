package steelUnicorn.laplacity.ui;

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
import steelUnicorn.laplacity.utils.LevelsParamsParser;

/**
 * Сцена для победного экрана.
 * Создает интерфейс с заработанными звездами и навигационными кнопками:
 * menu, replay, next.
 */
public class WinInterface extends Stage {
    //Константы размеров
    private static final float btnSpace = Globals.UI_WORLD_WIDTH * 0.02f;
    private static final float starSpace = Globals.UI_WORLD_HEIGHT * 0.03f;
    private static final float starsSpace = Globals.UI_WORLD_HEIGHT * 0.07f;
    private static final float edgeStarPad = Globals.UI_WORLD_HEIGHT * 0.05f;
    private static final float starScale = 1.5f;
    private static final float btnScale = 1.5f;

    private final Table root;
    private final Image background;

    /**
     * Создает интерфейс победного экрана.
     * <ul>Добавляет на экран:
     *  <li>Фон EARTH_BACKGROUND</li>
     *  <li>Счетчик фпс, если включен режим debug</li>
     *  <li>Сам интерфейс</li>
     * </ul>
     * @param viewport вьюпорт сцены
     * @see FpsCounter
     */
    public WinInterface(Viewport viewport) {
        super(viewport);
        //back
        background = new Image(EARTH_BACKGROUND);
        background.setSize(background.getPrefWidth() / background.getPrefHeight() * viewport.getWorldHeight(),
                viewport.getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + viewport.getWorldWidth() / 2 , 0);
        addActor(background);
        //fps
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
     * Очищает интерфейс перед его сборкой.
     * @see #buildStage(int, Skin)
     */
    private void clearStage() {
        if (root.hasChildren()) {
            Table buttons = root.findActor("buttonsTable");
            buttons.clearChildren();
            root.clearChildren();
        }
    }

    /**
     * Собирает интерфейс на победном экране.
     * На интерфейсе отображается 3 звезды, заполненные в зависимости от того сколько их собрано
     * с помощью {@link #getStarRows(int, Skin)}.
     * И под ними навигационные кнопки menu, replay, next.
     *
     * @param starsScored - количество заработанных звезд.
     * @see #getStarRows(int, Skin)
     */
    public void buildStage(int starsScored, Skin skin) {
        clearStage();

        //stars
        root.row();
        root.add(getStarRows(starsScored, skin)).padBottom(starsSpace);

        root.row();
        //buttons
        Table buttons = new Table();
        buttons.setName("buttonsTable");
        root.add(buttons);

        buttons.defaults().space(btnSpace);

        addButton(buttons, skin, "Home", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                LaplacityAssets.changeTrack("music/main theme_drop.ogg");
                Globals.game.getScreenManager().pushScreen(Globals.nameLevelsScreen, Globals.blendTransitionName);
            }
        });

        addButton(buttons, skin, "Replay", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                GameProcess.initLevel(sectionLevels.get(GameProcess.sectionNumber - 1)
                                .get(GameProcess.levelNumber - 1));

            }
        });

        //В последнем уровне кнопку не надо отображать
        if (!(GameProcess.sectionNumber == sectionLevels.size &&
                GameProcess.levelNumber == sectionLevels.get(GameProcess.sectionNumber - 1).size)) {
            ChangeListener listener;
            if (GameProcess.sectionNumber < sectionLevels.size &&
                    GameProcess.levelNumber == sectionLevels.get(GameProcess.sectionNumber - 1).size) {
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

                        GameProcess.levelParams = LevelsParamsParser.getParams(section, level);
                        GameProcess.initLevel(sectionLevels.get(section - 1).get(level - 1));
                        LaplacityAssets.setLevelTrack();
                    }
                };
            }

            addButton(buttons, skin, "Next", listener);
        }
    }

    /**
     * Создает и добавляет кнопку к таблице.
     * @param table таблица
     * @param skin скин с текстурами и стилями кнопок
     * @param styleName название стиля кнопки
     * @param listener обработчик событий
     * @return клетку в таблице с элементом
     */
    private Cell<ImageButton> addButton(Table table, Skin skin,
                                        String styleName, ChangeListener listener) {
        ImageButton btn = new ImageButton(skin, styleName);
        btn.getImageCell().grow();
        btn.addListener(listener);

        return table.add(btn).size(btn.getPrefWidth() * btnScale, btn.getPrefHeight() * btnScale);
    }

    /**
     * Меняет размеры фона при изменении размеров приложения.
     */
    public void resizeBackground() {
        background.setSize(background.getPrefWidth() / background.getPrefHeight()
                        * this.getViewport().getWorldHeight(),
                this.getViewport().getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + this.getViewport().getWorldWidth() / 2 , 0);
    }

    /**
     * Создает таблицу с собранными на уровне звездами для отображения в интерфейсе.
     * @param starsScored количество собранных звезд
     * @return виджет со звездами
     */
    private Table getStarRows(int starsScored, Skin skin) {
        Table stars = new Table();
        StringBuilder starName = new StringBuilder("star00");

        for (int i = 1; i <= GameProcess.MAX_STAR; i++) {
            starName.setCharAt(4, starsScored >= i ? '1' : '0');
            starName.setCharAt(5, Character.forDigit(i , 10));

            Image star = new Image(skin, starName.toString());
            stars.add(star).size(star.getPrefWidth() * starScale,
                            star.getPrefHeight() * starScale)
                    .space(starSpace).padTop(i != 2 ? edgeStarPad : 0).top();
        }

        return stars;
    }
}
