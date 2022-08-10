package steelUnicorn.laplacity.ui.mainmenu.tabs;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;
import steelUnicorn.laplacity.utils.Settings;

/**
 * Класс вкладки настроек в главном меню.
 * На данный момент есть:
 * - Включение звуков
 * - Включение музыки
 * - Включение освещения
 *
 * Использует класс Settings подгружающий настройки и сохраняющий при закрытии игры.
 */
public class SettingsTab extends MainMenuTab {
    public ScrollPane settingsPane;
    private static float cbSize = Globals.UI_WORLD_HEIGHT * 0.04f;
    private static float cbPad = cbSize * 0.2f;

    public SettingsTab(Skin skin) {
        super();

        addReturnButton(skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                MainMenu stage = (MainMenu) SettingsTab.this.getStage();
                stage.returnMainMenu();
            }
        });
        row();
        addDescription("Settings:", skin);
        row();
        addSettings(skin);

        settingsPane = new ScrollPane(this);
        settingsPane.validate();
        settingsPane.setName("tab");
        settingsPane.setFadeScrollBars(false);
    }

    /**
     * Функция добавляет настройки во вкладку.
     */
    private void addSettings(Skin skin) {
        Table settings = new Table();
        add(settings).space(tabSpace);
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
                        LaplacityAssets.music.setVolume(Settings.getMusicVolume());
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
        addCheckbox(settings, "Show fps", skin, Settings.isShowFps(),
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        LaplacityAssets.playSound(LaplacityAssets.clickSound);
                        CheckBox box = (CheckBox) actor;
                        Settings.setShowFps(box.isChecked());
                    }
                }, "fpsCheckbox");
    }

    /**
     * Функция для добавлегния чекбокса
     * @param table - таблица куда добавляется чекбокс
     * @param label - надпись рядом с чекбоксом
     * @param skin - скин
     * @param isChecked - логическое выражения для установки режима чекбокса
     * @param listener - действия при нажатии
     * @param name - имя чекбокса в таблице
     * @param color - цвет текста
     */
    private void addCheckbox(Table table, String label, Skin skin,
                             boolean isChecked, ChangeListener listener,
                             String name, Color color) {
        CheckBox checkBox = new CheckBox(label, skin);
        //tmp sizing
        checkBox.getImageCell().size(cbSize, cbSize).pad(cbPad);
        checkBox.getStyle().checkboxOff.setMinHeight(cbSize);
        checkBox.getStyle().checkboxOff.setMinWidth(cbSize);
        checkBox.getStyle().checkboxOn.setMinHeight(cbSize);
        checkBox.getStyle().checkboxOn.setMinWidth(cbSize);

        checkBox.setName(name);
        checkBox.setChecked(isChecked);
        checkBox.getLabel().setColor(color);
        checkBox.addListener(listener);
        table.add(checkBox);
    }

    private void addCheckbox(Table table, String label, Skin skin,
                             boolean isChecked, ChangeListener listener,
                             String name) {
        addCheckbox(table, label, skin,
                isChecked, listener,
                name, Color.WHITE);
    }
}
