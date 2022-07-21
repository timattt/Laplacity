package steelUnicorn.laplacity.ui.mainmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;

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
    private static final float contentWidth = Globals.UI_WORLD_WIDTH * 0.2f;
    private static final float contentHeight = Globals.UI_WORLD_HEIGHT * 0.2f;
    private static final float fontScale = 1.5f;

    private Table root; //<< Корневая таблица для позиционирования

    public WinInterface(Viewport viewport) {
        super(viewport);

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
    public void buildStage(float score) {
        clearStage();

        Skin skin = Globals.assetManager.get("ui/uiskin.json", Skin.class);
        //label
        Label done = new Label("Done\n"
                + "Score " + String.valueOf(score), skin);
        done.setAlignment(Align.center);
        done.setName("doneLabel");
        done.setFontScale(fontScale);
        done.setColor(Color.BLACK);
        root.add(done).space(spaceSize).size(contentWidth, contentHeight);

        root.row();

        //buttons
        Table buttons = new Table();
        buttons.setName("buttonsTable");
        root.add(buttons).space(spaceSize);

        buttons.defaults()
                .space(spaceSize)
                .size(btnWidth, btnHeight);

        addButton(buttons, "Exit", skin, "exitBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen, Globals.nameSlideIn);
            }
        });

        addButton(buttons, "Replay", skin, "replayBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameProcess.initLevel(Globals.assetManager.get("levels/level" +
                        GameProcess.levelNumber + ".png", Texture.class));
                Globals.game.getScreenManager().pushScreen(Globals.nameGameScreen, Globals.nameSlideOut);
            }
        });

        //Если текущий уровень максимален, кнопки max не будет
        if (GameProcess.levelNumber < Globals.TOTAL_LEVELS_AVAILABLE) {
            addButton(buttons, "Next", skin, "nextBtn", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameProcess.initLevel(Globals.assetManager.get("levels/level" +
                            (++GameProcess.levelNumber) + ".png", Texture.class));
                    Globals.game.getScreenManager().pushScreen(Globals.nameGameScreen, Globals.nameSlideOut);
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

}
