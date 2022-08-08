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
    public static float padSize = 10;

    public FpsCounter(Skin skin) {
        super(String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps",  skin);
        setPosition(padSize, padSize);
        setColor(Color.PURPLE);
    }

    private void updateFps() {
        setVisible(Settings.isShowFps());
        setText(String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps");
    }

    public void act(float delta) {
        super.act(delta);
        updateFps();
    }
}
