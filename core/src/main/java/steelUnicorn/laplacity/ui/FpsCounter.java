package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import steelUnicorn.laplacity.utils.Settings;

/**
 * Label счетчик фпс. Позиция задается левого нижнего угла с помощью {@link #padBottom} и {@link #padLeft}
 * отступов.
 * Обновляется каждый кадр.
 * Показ fps настраивается в debug режиме.
 * @see Label
 * @see Settings#isShowFps()
 */
public class FpsCounter extends Label {
    public static float padBottom = 20;
    public static float padLeft = 100;

    /**
     * Инициализирует счетчик.
     * @param skin скин с текстурами интерфейса
     * @param styleName название стиля
     */
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
