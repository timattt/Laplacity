package steelUnicorn.laplacity.ui.mainmenu.tabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;
import steelUnicorn.laplacity.utils.Settings;

/**
 * Вкладка настроек в главном меню.
 * Использует класс Settings подгружающий настройки и сохраняющий при закрытии игры.
 *
 * Таблица с настройками {@link #settings} так же используется в
 * {@link steelUnicorn.laplacity.ui.dialogs.SettingsDialog}.
 *
 * @see Settings
 */
public class SettingsTab extends MainMenuTab {
    private static final float cbSize = Globals.UI_WORLD_HEIGHT * 0.04f;
    private static final float cbPad = cbSize * 0.2f;

    public Table settings;

    /**
     * Создает интерфейс вкладки с настройками.
     *
     * @param skin скин с текстурами кнопок и чекбоксов.
     */
    public SettingsTab(Skin skin) {
        super();

        addReturnButton(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                MainMenu stage = (MainMenu) SettingsTab.this.getStage();
                stage.returnMainMenu();
            }
        }).expand().uniform().left().top().padLeft(MainMenu.menuLeftSpace);

        Table setTable = new Table();
        setTable.setBackground(skin.getDrawable("label_back"));

        Label description = new Label("Settings", skin, "noback");
        description.setOrigin(Align.center);
        description.setScale(descriptionScale);
        description.setFontScale(descriptionScale);
        setTable.add(description).pad(tabSpace);
        setTable.row();

        createSettings(skin);
        setTable.add(settings);

        add(setTable).space(tabSpace).top().padTop(MainMenu.menuTopPad);

        add().expand().uniform();
    }

    /**
     * Создает виджеты с настройками.
     * Содержит
     * <ul>
     *     <li>звуковые эффекты</li>
     *     <li>музыка</li>
     *     <li>освещение</li>
     *     <li>счетчик фпс - в дебаг режиме</li>
     *     <li>кнопку открытия всех уровней - в дебаг режиме</li>
     * </ul>
     * @param skin скин с текстурами кнопок и чекбоксов.
     */
    private void createSettings(Skin skin) {
        settings = new Table();
        settings.defaults().left().space(tabSpace);

        //Sound and music
        addCheckbox(settings, "Enable sound", skin,
                Settings.getSoundVolume() == Settings.VOLUME.ON.ordinal(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        LaplacityAssets.playSound(LaplacityAssets.clickSound);
                        CheckBox box = (CheckBox) actor;
                        Settings.setSoundVolume(box.isChecked() ? Settings.VOLUME.ON.ordinal() : Settings.VOLUME.OFF.ordinal());
                    }
                }, "soundCheckbox");

        settings.row();

        addCheckbox(settings, "Enable music", skin,
                Settings.getMusicVolume() == Settings.VOLUME.ON.ordinal(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        LaplacityAssets.playSound(LaplacityAssets.clickSound);
                        CheckBox box = (CheckBox) actor;
                        Settings.setMusicVolume(box.isChecked() ? Settings.VOLUME.ON.ordinal() : Settings.VOLUME.OFF.ordinal());
                        LaplacityAssets.syncMusicVolume();
                    }
                }, "musicCheckbox");

        settings.row();
        addCheckbox(settings, "Enable lighting", skin, Settings.isLightingEnabled(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        LaplacityAssets.playSound(LaplacityAssets.clickSound);
                        CheckBox box = (CheckBox) actor;
                        Settings.setLighting(box.isChecked());
                    }
                }, "lightingCheckbox");

        settings.row();
        addCheckbox(settings, "Hide food bar", skin, Settings.isHideFoodBar(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        LaplacityAssets.playSound(LaplacityAssets.clickSound);
                        CheckBox box = (CheckBox) actor;
                        Settings.setHideFoodBar(box.isChecked());
                        if (GameProcess.gameUI != null &&
                                (!box.isChecked() || GameProcess.gameUI.catFI.isShown)) {
                            //Если не скрывать, то нужно вызвать showHide чтобы показать панель
                            //Если скрывать, то если панель показана, то нужно вызвать чтобы скрыть
                            GameProcess.gameUI.catFI.showHide();
                        }
                    }
        }, "hideFoodCheckbox");


        if (Laplacity.isDebugEnabled()) {
            settings.row();
            addCheckbox(settings, "Show fps", skin, Settings.isShowFps(),
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            LaplacityAssets.playSound(LaplacityAssets.clickSound);
                            CheckBox box = (CheckBox) actor;
                            Settings.setShowFps(box.isChecked());
                        }
                    }, "fpsCheckbox");

            settings.row();
            addCheckbox(settings, "Show grid", skin, Settings.isShowGrid(),
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            LaplacityAssets.playSound(LaplacityAssets.clickSound);
                            CheckBox box = (CheckBox) actor;
                            Settings.setShowGrid(box.isChecked());
                        }
                    }, "gridCheckbox");
            settings.row();
            addCheckbox(settings, "Show skip button", skin, Settings.isShowSkip(),
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            LaplacityAssets.playSound(LaplacityAssets.clickSound);
                            CheckBox box = (CheckBox) actor;
                            Settings.setShowSkip(box.isChecked());
                        }
                    }, "skipCheckbox");


            settings.row();
            TextButton openLevels = new TextButton("Open levels", skin);
            openLevels.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LaplacityAssets.playSound(LaplacityAssets.clickSound);
                    Globals.levelsScreen.levelsTab.openLevels();
                }
            });
            settings.add(openLevels).center();
        }
    }

    /**
     * Добавление чекбокса в настройки.
     * @param table таблица куда добавляется чекбокс
     * @param label надпись рядом с чекбоксом
     * @param skin скин
     * @param isChecked логическое выражения для установки режима чекбокса
     * @param listener действия при нажатии
     * @param name имя чекбокса в таблице
     */
    private void addCheckbox(Table table, String label, Skin skin,
                             boolean isChecked, ChangeListener listener,
                             String name) {
        CheckBox checkBox = new CheckBox(label, skin);
        checkBox.getImageCell().padRight(cbPad);

        checkBox.setName(name);
        checkBox.setChecked(isChecked);
        checkBox.addListener(listener);
        table.add(checkBox);
    }

    //Вызывается при показывании настроек, для синхронизации интерфейса
    public void show() {
        ((CheckBox)settings.findActor("soundCheckbox"))
                .setChecked(Settings.getSoundVolume() == Settings.VOLUME.ON.ordinal());
        ((CheckBox)settings.findActor("musicCheckbox"))
                .setChecked(Settings.getMusicVolume() == Settings.VOLUME.ON.ordinal());
        ((CheckBox)settings.findActor("lightingCheckbox"))
                .setChecked(Settings.isLightingEnabled());
        ((CheckBox)settings.findActor("hideFoodCheckbox"))
                .setChecked(Settings.isHideFoodBar());
        if (Laplacity.isDebugEnabled()) {
            ((CheckBox) settings.findActor("fpsCheckbox"))
                    .setChecked(Settings.isShowFps());
            ((CheckBox) settings.findActor("gridCheckbox"))
                    .setChecked(Settings.isShowGrid());
            ((CheckBox) settings.findActor("skipCheckbox"))
                    .setChecked(Settings.isShowSkip());
        }
    }
}
