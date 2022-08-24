package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import steelUnicorn.laplacity.utils.Settings;

/**
 * Строчка fps которая обновляется каждый кадр. Позиция задается слева снизу с помощь padSize.
 */
public class FpsCounter extends Label {
    public static float padBottom = 20;
    public static float padLeft = 100;

    public FpsCounter(Skin skin) {
        super(Gdx.graphics.getFramesPerSecond() + " fps",  skin);
        initialize();
    }

    public FpsCounter(Skin skin, String styleName) {
        super(Gdx.graphics.getFramesPerSecond() + " fps", skin, styleName);
        initialize();
    }

    private void initialize() {
        setPosition(padLeft, padBottom);
    }

    private void updateFps() {
        setVisible(Settings.isShowFps());
        setText(Gdx.graphics.getFramesPerSecond() + " fps");
    }

    public void act(float delta) {
        super.act(delta);
        updateFps();
    }
}
