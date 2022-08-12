package steelUnicorn.laplacity.ui.mainmenu;

import static steelUnicorn.laplacity.core.Globals.*;
import static steelUnicorn.laplacity.core.LaplacityAssets.MAIN_MENU_BACKGROUND;
import static steelUnicorn.laplacity.core.LaplacityAssets.SKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
    public static final float menuSpaceSize = UI_WORLD_HEIGHT * 0.06f; // << space between elements
    public static final float iconSize = UI_WORLD_HEIGHT * 0.15f;

    private SettingsTab settingsTab;
    private CreditsTab creditsTab;

    private CatFoodInterface catFI;

    private Table mainMenu;
    private Cell<Table> mainCell;

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
        mainCell.setActor(mainMenu);
    }

    /**
     * Функция создающая кнопки главного меню и их поведение (переключение нужных вкладок)
     *
     * @param root - корневая таблица сцены
     * @param skin - скин
     */
    @SuppressWarnings("unchecked")
	private void createMainMenu(Table root, Skin skin) {
        mainMenu = new Table();
        mainCell = root.add(mainMenu);

        //play
        Button btn = new Button(TEXSKIN.get("PlayBtn", Button.ButtonStyle.class));
        btn.setName("play");
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                game.getScreenManager().pushScreen(nameLevelsScreen, nameSlideOut);
            }
        });
        mainMenu.add(btn);

        mainMenu.row();
        Table icons = new Table();
        mainMenu.add(icons).growX().spaceTop(menuSpaceSize);
        //options
        ImageButton icon = new ImageButton(TEXSKIN.get("settings", ImageButton.ImageButtonStyle.class));
        icon.setName("settings");
        icon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                mainCell.setActor(settingsTab.settingsPane);
            }
        });
        icons.add(icon).expandX().size(iconSize);
        //credits
        icon = new ImageButton(TEXSKIN.get("credits", ImageButton.ImageButtonStyle.class));
        icon.setName("credits");
        icon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                mainCell.setActor(creditsTab);
            }
        });
        icons.add(icon).expandX().size(iconSize);
    }

    public void returnMainMenu() {
        mainCell.setActor(mainMenu);
    }

    public void resizeBackground() {
        background.setSize(background.getPrefWidth() / background.getPrefHeight()
                        * this.getViewport().getWorldHeight(),
                this.getViewport().getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + this.getViewport().getWorldWidth() / 2 , 0);
    }
}
