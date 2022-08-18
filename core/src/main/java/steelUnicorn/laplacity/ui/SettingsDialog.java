package steelUnicorn.laplacity.ui;


import static steelUnicorn.laplacity.core.LaplacityAssets.SKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.mainmenu.tabs.SettingsTab;


public class SettingsDialog extends Dialog {
    private SettingsTab settingsTable;

    private static final float sidepad = Globals.UI_WORLD_WIDTH * 0.05f;

    public SettingsDialog(WindowStyle ws) {
        super("", ws);
        initializeSettings();
    }

    public SettingsDialog(Skin skin) {
        this(skin.get(WindowStyle.class));

    }

    public SettingsDialog(Skin skin, String wsName) {
        this(skin.get(wsName, WindowStyle.class));
    }

    private void initializeSettings() {
        //flickering bug fix
        Color color = getColor();
        color.a = 0;
        setColor(color);

        settingsTable = new SettingsTab(SKIN);
        getCell(getContentTable()).padLeft(sidepad).padRight(sidepad);
        getContentTable().add(settingsTable.settingsPane);

        Button exitBtn = new Button(TEXSKIN.get("ExitBtn", Button.ButtonStyle.class));
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
