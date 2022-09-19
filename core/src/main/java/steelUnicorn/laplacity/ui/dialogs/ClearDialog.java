package steelUnicorn.laplacity.ui.dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;

/**
 * Диалог для подтверждения очистки уровня.
 */
public class ClearDialog extends Dialog {
    private static final float btnWidth = Globals.UI_WORLD_WIDTH * 0.15f;
    private static final float btnHeight = Globals.UI_WORLD_HEIGHT * 0.12f;
    private static final float padSize = 30;
    private static final float titleScale = 1.3f;

    /**
     * Инициализирует диалог.
     * @param skin скин с текстурами кнопок и окна.
     */
    public ClearDialog(Skin skin) {
        super("", skin);
        Color color = getColor();
        color.a = 0;
        setColor(color);
        //initialize
        Label textLabel = new Label("Clear all?", skin, "noback");
        textLabel.setScale(titleScale);
        textLabel.setFontScale(titleScale);
        text(textLabel);
        getButtonTable().pad(padSize);
        getButtonTable().pad(padSize).defaults()
                .width(btnWidth)
                .height(btnHeight)
                .pad(padSize);

        button("No", false);
        button("Yes", true);
    }

    @Override
    protected void result(Object object) {
        LaplacityAssets.playSound(LaplacityAssets.clickSound);
        if ((Boolean) object) {
            GameProcess.clearLevel();
        }
    }
}
