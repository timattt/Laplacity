package steelUnicorn.laplacity.ui.dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.mainmenu.tabs.SettingsTab;

/**
 * Всплывающее окно для регулирования настроек во время игрового процесса.
 */
public class SettingsDialog extends Dialog {
    private SettingsTab settingsTable;

    private static final float sidePad = Globals.UI_WORLD_WIDTH * 0.05f;

    /**
     * Инициализирует класс SettingsDialog.
     * @param skin скин с текстурами фона диалоги и кнопок.
     */
    public SettingsDialog(Skin skin) {
        super("", skin);
        initializeSettings(skin);

        Color color = getColor();
        color.a = 0;
        setColor(color);
    }

    /**
     * Инициализирует интерфейс диалога. Создает SettingsTab, чтобы взять настройки оттуда.
     * @param skin скин с текстурами виджетов.
     * @see SettingsTab
     */
    private void initializeSettings(Skin skin) {
        settingsTable = new SettingsTab(skin);
        getCell(getContentTable()).padLeft(sidePad).padRight(sidePad);
        getContentTable().add(settingsTable.settings);

        Button exitBtn = new Button(skin, "ExitBtn");
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
            }
        });
        Table buttonTable = getButtonTable();
        getButtonTable().add(exitBtn).size(buttonTable.getMinWidth() * 0.6f,
                buttonTable.getMinHeight() * 0.6f);
        setObject(exitBtn, null);
    }

    @Override
    public Dialog show(Stage stage) {
        settingsTable.show();
        return super.show(stage);
    }
}
