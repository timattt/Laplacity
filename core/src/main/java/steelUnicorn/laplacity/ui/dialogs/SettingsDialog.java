package steelUnicorn.laplacity.ui.dialogs;


import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;

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

        settingsTable = new SettingsTab(TEXSKIN);
        getCell(getContentTable()).padLeft(sidepad).padRight(sidepad);
        getContentTable().add(settingsTable.settings);

        Button exitBtn = new Button(TEXSKIN.get("ExitBtn", Button.ButtonStyle.class));
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