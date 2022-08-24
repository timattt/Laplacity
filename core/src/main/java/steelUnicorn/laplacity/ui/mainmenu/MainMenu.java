package steelUnicorn.laplacity.ui.mainmenu;

import static steelUnicorn.laplacity.core.Globals.*;
import static steelUnicorn.laplacity.core.LaplacityAssets.*;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.core.LaplacityAssets;
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

    public static final float menuTopPad = UI_WORLD_HEIGHT * 0.28f; // << menu button height ratio
    public static final float menuLeftSpace = UI_WORLD_HEIGHT * 0.06f; // << space between elements
    public static final float iconSize = UI_WORLD_HEIGHT * 0.15f;

    private SettingsTab settingsTab;
    private CreditsTab creditsTab;

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

        //MainMenu
        Table root = new Table();
        root.setFillParent(true);
        addActor(root);

        settingsTab = new SettingsTab(TEXSKIN);
        creditsTab = new CreditsTab(TEXSKIN);

        createMainMenu(root, SKIN);

        if (Laplacity.isDebugEnabled()) {
            FpsCounter fpsCounter = new FpsCounter(TEXSKIN, "noback");
            addActor(fpsCounter);
        }
    }

    //Функция вызывается при открытии главного меню, чтобы обновить параметры!
    public void show() {
        mainCell.setActor(mainMenu);
        settingsTab.show();
    }

    /**
     * Функция создающая кнопки главного меню и их поведение (переключение нужных вкладок)
     *
     * @param root - корневая таблица сцены
     * @param skin - скин
     */
	private void createMainMenu(Table root, Skin skin) {
        mainMenu = new Table();
        mainCell = root.add(mainMenu).grow();
        //left icons
        Table icons = new Table();
        mainMenu.add(icons).expand().fillY()
                .left().padLeft(menuLeftSpace).uniform();
        //options
        ImageButton icon = new ImageButton(TEXSKIN.get("credits", ImageButton.ImageButtonStyle.class));
        icon.setName("credits");
        icon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                mainCell.setActor(creditsTab);
            }
        });
        icons.add(icon).top().expandY().size(iconSize);
        //credits
        icons.row();
        icon = new ImageButton(TEXSKIN.get("settings", ImageButton.ImageButtonStyle.class));
        icon.setName("settings");
        icon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                mainCell.setActor(settingsTab);
            }
        });

        icons.add(icon).expandY().bottom().size(iconSize);

        //play
        Button btn = new Button(TEXSKIN.get("PlayBtn", Button.ButtonStyle.class));
        btn.setName("play");
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                if (progress.isNewUser()) {
                    game.getScreenManager().pushScreen(nameStoryScreen + "0", storyTransitionName);
                    progress.setNewUser(false);
                } else {
                    game.getScreenManager().pushScreen(nameLevelsScreen, blendTransitionName);
                }
            }
        });
        mainMenu.add(btn).expand().uniform();

        mainMenu.add().expand().uniform();
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
