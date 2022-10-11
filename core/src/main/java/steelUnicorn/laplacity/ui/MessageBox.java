package steelUnicorn.laplacity.ui;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Timer;

import steelUnicorn.laplacity.core.Globals;

/**
 * Лейбл для сообщений. Может показывать сообщение, показывать на несколько секунд
 * и закрывать его.
 */
public class MessageBox extends Label {
    private static final float boxWidth = Globals.UI_WORLD_WIDTH * 0.62f;
    private static final float boxHeight = Globals.UI_WORLD_HEIGHT * 0.23f;
    private static final float padBottom = Globals.UI_WORLD_HEIGHT * 0.03f;
    private static final float fontScale = 1.5f;
    private static final float fadeTime = 0.1f;

    private boolean isShown;
    private boolean autoHide;

    /**
     * Timer.Task для закрытия сообщения.
     * @see Timer.Task
     */
    private final Timer.Task hideTask = new Timer.Task() {
        @Override
        public void run() {
            hide();
        }
    };

    public MessageBox(String text, Skin skin, String wsName) {
        super(text, skin, wsName);
        setFontScale(fontScale);
        setAlignment(Align.center);
        setSize(boxWidth, boxHeight);

        Color color = getColor();
        color.a = 0;
        setColor(color);

        isShown = false;
        autoHide = false;
    }

    private Label show(Stage stage, @Null Action action) {
        clearActions();

        stage.addActor(this);
        if (action != null && !isShown) addAction(action);

        isShown = true;
        autoHide = false;
        return this;
    }

    /**
     * Показывает сообщение с переданным текстом.
     * @param stage сцена, на которой отображается сообщение
     * @param text текст сообщения
     * @return диалоговое окно
     */
    public Label show(Stage stage, String text) {
        setText(text);
        show(stage, sequence(Actions.alpha(0), Actions.fadeIn(fadeTime, Interpolation.fade)));
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), padBottom);

        if (!autoHide) {
            hideTask.cancel();
        }
        return this;
    }

    /**
     * Делает то же самое что и {@link #show(Stage, String)} только на showTime секунд.
     * @param stage сцена, на которой отображается сообщение
     * @param text текст сообщения
     * @param showTime время отображения сообщения
     * @return диалоговое окно
     */
    public Label show(Stage stage, String text, float showTime) {
        show(stage, text);
        if (hideTask.isScheduled()) {
            hideTask.cancel();
        }
        Timer.schedule(hideTask, showTime);
        autoHide = true;
        return this;
    }

    private void hide(Action action) {
        if (action != null) {
            addAction(sequence(action, Actions.removeActor()));
        } else {
            remove();
        }

        isShown = false;
    }

    public void hide() {
        hide(fadeOut(fadeTime, Interpolation.fade));
    }

    public void stageResized(Stage stage) {
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), padBottom);
    }
}
