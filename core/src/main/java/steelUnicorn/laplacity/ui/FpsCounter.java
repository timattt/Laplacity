package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FpsCounter extends Label {
    public static float padSize = 10;

    public FpsCounter(Skin skin) {
        super(String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps",  skin);
        setPosition(padSize, padSize);
        setColor(Color.PURPLE);
    }

    private void updateFps() {
        setText(String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps");
    }

    public void act(float delta) {
        super.act(delta);
        updateFps();
    }
}
