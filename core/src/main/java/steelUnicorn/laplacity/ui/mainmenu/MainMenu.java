package steelUnicorn.laplacity.ui.mainmenu;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;
import static steelUnicorn.laplacity.core.Globals.assetManager;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.mainmenu.tabs.CreditsTab;
import steelUnicorn.laplacity.ui.mainmenu.tabs.LevelsTab;
import steelUnicorn.laplacity.ui.mainmenu.tabs.MainMenuTab;
import steelUnicorn.laplacity.ui.mainmenu.tabs.SettingsTab;

/**
 * Класс MainMenu наследуется от Stage.
 * Меню состоит из 2 таблиц, слева отображаются главные кнопки
 * Play
 * Options
 * Credits
 * Каждая кнопка отображает справа соответсвующую вкладку
 * Levels - список уровней
 * Options - настройки, по типу включения, отключения звука, музыки
 * Credits - описание команды разработчиков
 */
public class MainMenu extends Stage {
    private static final float menuWidth = UI_WORLD_WIDTH * 0.2f;	// << menu button width ratio
    private static final float menuHeight = UI_WORLD_HEIGHT * 0.06f; // << menu button height ratio
    private static final float menuSpaceSize = UI_WORLD_HEIGHT * 0.03f; // << space between elements

    private MainMenuTab levelsTab;
    private MainMenuTab settingsTab;
    private CreditsTab creditsTab;

    /**
     * Конструктор главного меню.
     * Собирает каждую вкладку и главное меню.
     *
     * @param viewport - вьюпорт сцены
     */
    public MainMenu(Viewport viewport) {
        super(viewport);

        Skin skin = assetManager.get("ui/uiskin.json", Skin.class);
        Table root = new Table();
        root.setFillParent(true);
        addActor(root);

        levelsTab = new LevelsTab(skin);
        settingsTab = new SettingsTab(skin);
        creditsTab = new CreditsTab(skin);

        createMainMenu(root, skin);
    }

    /**
     * Функция создающая кнопки главного меню и их поведение (переключение нужных вкладок)
     *
     * @param root - корневая таблица сцены
     * @param skin - скин
     */
    private void createMainMenu(Table root, Skin skin) {
        Table mainMenu = new Table();
        root.add(mainMenu).expandX().uniform();

        MainMenuTab tmpTab = new MainMenuTab();
        root.add(tmpTab).expandX().uniform();

        mainMenu.defaults()
                .width(menuWidth)
                .height(menuHeight)
                .space(menuSpaceSize);
        //play
        addMenuButton(mainMenu, "Play", skin, "playBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                root.getCell(root.findActor("tab")).setActor(levelsTab);
            }
        });

        //options
        mainMenu.row();
        addMenuButton(mainMenu, "Settings", skin, "settingsBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                root.getCell(root.findActor("tab")).setActor(settingsTab);
            }
        });

        //credits
        mainMenu.row();
        addMenuButton(mainMenu, "Credits", skin, "creditsBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                root.getCell(root.findActor("tab")).setActor(creditsTab);
            }
        });

        //testads
        mainMenu.row();
        addMenuButton(mainMenu, "Test ad", skin, "testAd", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Globals.game.showInterstitial();
            }
        });
    }

    /**
     * Функция добавления кнопки в главное меню
     * @param table - таблица для добавления
     * @param text - текст на кнопке
     * @param skin - скин
     * @param name - имя кнопки в таблице
     * @param listener - обработчик нажатий
     */
    private void addMenuButton(Table table, String text, Skin skin, String name, ChangeListener listener) {
        TextButton btn = new TextButton(text, skin);
        btn.setName(name);
        btn.addListener(listener);
        table.add(btn);
    }
}
