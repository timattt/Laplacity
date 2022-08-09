package steelUnicorn.laplacity.ui.mainmenu;

import static steelUnicorn.laplacity.core.Globals.*;
import static steelUnicorn.laplacity.core.LaplacityAssets.MAIN_MENU_BACKGROUND;
import static steelUnicorn.laplacity.core.LaplacityAssets.SKIN;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.CatFoodInterface;
import steelUnicorn.laplacity.ui.FpsCounter;
import steelUnicorn.laplacity.ui.mainmenu.tabs.CreditsTab;
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

    public static final float menuWidth = UI_WORLD_WIDTH * 0.2f;	// << menu button width ratio
    public static final float menuHeight = UI_WORLD_HEIGHT * 0.06f; // << menu button height ratio
    public static final float menuSpaceSize = UI_WORLD_HEIGHT * 0.03f; // << space between elements

    private SettingsTab settingsTab;
    private CreditsTab creditsTab;

    private CatFoodInterface catFI;

    private Cell<MainMenuTab> tabCell;

    private Image background;
    /**
     * Конструктор главного меню.
     * Собирает каждую вкладку и главное меню.
     *
     * @param viewport - вьюпорт сцены
     */
    public MainMenu(Viewport viewport) {
        super(viewport);

        //background
        background = new Image(MAIN_MENU_BACKGROUND);
        background.setSize(background.getPrefWidth() / background.getPrefHeight() * viewport.getWorldHeight(),
                viewport.getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + viewport.getWorldWidth() / 2 , 0);
        addActor(background);

        //fpsCounter
        FpsCounter fpsCounter = new FpsCounter(SKIN);
        addActor(fpsCounter);
        //CatFood
        catFI = new CatFoodInterface(catFood.getTotalLaunchesAvailable(), SKIN);
        catFI.setPosition(this.getWidth() / 2, this.getHeight() - catFI.getPrefHeight() / 2);
        addActor(catFI);
        catFood.timer.setCurrentInterface(catFI);

        //MainMenu
        Table root = new Table();
        root.setFillParent(true);
        addActor(root);

        settingsTab = new SettingsTab(SKIN);
        creditsTab = new CreditsTab(SKIN);

        createMainMenu(root, SKIN);
    }

    //Функция вызывается при открытии главного меню, чтобы обновить параметры!
    public void show() {
        catFI.update(catFood.getTotalLaunchesAvailable());
        catFood.timer.setCurrentInterface(catFI);
        tabCell.setActor(null);
    }

    /**
     * Функция создающая кнопки главного меню и их поведение (переключение нужных вкладок)
     *
     * @param root - корневая таблица сцены
     * @param skin - скин
     */
    @SuppressWarnings("unchecked")
	private void createMainMenu(Table root, Skin skin) {
        Table mainMenu = new Table();
        root.add(mainMenu).expandX().uniform();

        tabCell = root.add().growX().uniform();

        mainMenu.defaults()
                .width(menuWidth)
                .height(menuHeight)
                .space(menuSpaceSize);
        //play
        addMenuButton(mainMenu, "Play", skin, "playBtn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                game.getScreenManager().pushScreen(nameLevelsScreen, nameSlideOut);
            }
        });

        //options
        mainMenu.row();
        addMenuButton(mainMenu, "Settings", skin, "settingsBtn", settingsTab.settingsPane);

        //credits
        mainMenu.row();
        addMenuButton(mainMenu, "Credits", skin, "creditsBtn", creditsTab);

        //testads
        mainMenu.row();
        addMenuButton(mainMenu, "Test ad", skin, "testAd", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                Globals.game.showRewarded();
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

    private void addMenuButton(Table table, String text, Skin skin, String name, Actor tabActor) {
        addMenuButton(table, text, skin, name, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                tabCell.setActor(tabActor);
            }
        });
    }

    public void resizeBackground() {
        background.setSize(background.getPrefWidth() / background.getPrefHeight()
                        * this.getViewport().getWorldHeight(),
                this.getViewport().getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + this.getViewport().getWorldWidth() / 2 , 0);
    }
}
