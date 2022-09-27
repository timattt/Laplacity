package steelUnicorn.laplacity.ui.dialogs;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import steelUnicorn.laplacity.core.Globals;

/**
 * Диалоговое окно с лейблом для сообщений. Может показывать сообщение, показывать на несколько секунд
 * и закрывать его.
 */
public class MessageBoxDialog extends Dialog {
    private static final float boxWidth = Globals.UI_WORLD_WIDTH * 0.5f;
    private static final float boxHeight = Globals.UI_WORLD_HEIGHT * 0.15f;
    private static final float padBottom = Globals.UI_WORLD_HEIGHT * 0.03f;
    private static final float fontScale = 1;
    private static final float fadeTime = 0.1f;

    private Label labelText;
    /**
     * Timer.Task для закрытия сообщения.
     *
     * @see Timer.Task
     */
    private final Timer.Task hideTask = new Timer.Task() {
        @Override
        public void run() {
            hide();
        }
    };

    public MessageBoxDialog(Skin skin, String wsName) {
        super("", skin, wsName);
        initializeMessageBoxDialog();

        Color color = getColor();
        color.a = 0;
        setColor(color);
    }

    /**
     * Задает label в диалоговом окне.
     */
    public void initializeMessageBoxDialog() {
        labelText = new Label("", getSkin(), "MessageBoxLabel");
        labelText.setAlignment(Align.center);
        labelText.setFontScale(fontScale);
        getContentTable().add(labelText).size(boxWidth, boxHeight);
    }

    /**
     * Показывает сообщение с переданным текстом.
     * @param stage сцена, на которой отображается сообщение
     * @param text текст сообщения
     * @return диалоговое окно
     */
    public Dialog show(Stage stage, String text) {
        labelText.setText(text);
        show(stage, sequence(Actions.alpha(0), Actions.fadeIn(fadeTime, Interpolation.fade)));
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), padBottom);
        return this;
    }

    /**
     * Делает то же самое что и {@link #show(Stage, String)} только на showTime секунд.
     * @param stage сцена, на которой отображается сообщение
     * @param text текст сообщения
     * @param showTime время отображения сообщения
     * @return диалоговое окно
     */
    public Dialog show(Stage stage, String text, float showTime) {
        show(stage, text);
        Timer.schedule(hideTask, showTime);
        return this;
    }

    @Override
    public void hide() {
        hide(fadeOut(fadeTime, Interpolation.fade));
    }

    /**
     * Изменяет текст на лейбле.
     * @param text новое сообщение
     */
    public void setLabelText(String text) {
        labelText.setText(text);
    }

    public void stageResized(Stage stage) {
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), padBottom);
    }
}
