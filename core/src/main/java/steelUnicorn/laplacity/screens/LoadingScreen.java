package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.game;
import static steelUnicorn.laplacity.core.Globals.nameGameScreen;
import static steelUnicorn.laplacity.core.LaplacityAssets.LOAD_BACKGROUND;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.ui.FpsCounter;

/**
 * Экран загрузки. Используется при включении уровня. Отображает надпись Loading в течении
 * loadingDuration секунд, с анимацией точек ./../... каждые loadingRate секунд
 */
public class LoadingScreen extends ManagedScreen {
    protected Stage loadingStage;
    private Label loadingLabel;
    private final Viewport viewport;

    private static final String[] loadingText = new String[]{"Loading", "Loading.", "Loading..", "Loading..."};
    private static final float loadingRate = 0.25f;
    private static final float loadingDuration = 3f;
    private int dot = 0;
    private static final float scale = 2;

    private float elapsedTime = 0;
    private Timer.Task loadingAnimation;

    private Image background;

    public LoadingScreen(Viewport viewport) {
        this.viewport = viewport;
        loadingStage = new Stage(viewport);
    }

    @Override
    protected void create() {
        addBackground(LOAD_BACKGROUND);
        //fpsCounter
        if (Laplacity.isDebugEnabled()) {
            FpsCounter fpsCounter = new FpsCounter(TEXSKIN, "noback");
            loadingStage.addActor(fpsCounter);
        }

        addLoadingLabel(TEXSKIN);

        addInputProcessor(loadingStage);
    }

    protected void addBackground(Texture backTexture) {
        background = new Image(backTexture);
        background.setSize(background.getPrefWidth() / background.getPrefHeight() * viewport.getWorldHeight(),
                viewport.getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + viewport.getWorldWidth() / 2 , 0);
        loadingStage.addActor(background);
    }

    protected void addLoadingLabel(Skin skin) {
        loadingLabel = new Label("Loading", skin, "noback");
        loadingLabel.setScale(scale);
        loadingLabel.setFontScale(scale);
        loadingLabel.setColor(Color.WHITE);
        loadingLabel.setPosition(loadingStage.getWidth() / 2 - loadingLabel.getWidth() * scale / 2,
                loadingStage.getHeight() / 2 - loadingLabel.getHeight() * scale / 2);
        loadingStage.addActor(loadingLabel);

        loadingAnimation = new Timer.Task() {
            @Override
            public void run() {
                loadingLabel.setText(loadingText[dot % loadingText.length]);
                dot++;
            }
        };
    }

    @Override
    public void hide() {
        loadingAnimation.cancel();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        loadingStage.draw();
        loadingStage.act();

        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime > loadingDuration) {
            game.getScreenManager().pushScreen(nameGameScreen, null);
        }
    }

    public void resizeBackground() {
        if (background != null) {
            background.setSize(background.getPrefWidth() / background.getPrefHeight()
                            * viewport.getWorldHeight(),
                    viewport.getWorldHeight());
            background.setPosition(-background.getWidth() / 2 + viewport.getWorldWidth() / 2, 0);
        }
    }

    @Override
    public void resize(int width, int height) {
        resizeBackground();
        if (loadingLabel != null) {
            loadingLabel.setPosition(loadingStage.getWidth() / 2 - loadingLabel.getWidth() * scale / 2,
                    loadingStage.getHeight() / 2 - loadingLabel.getHeight() * scale / 2);
        }
    }

    @Override
    public void show() {
        super.show();
        dot = 0;
        elapsedTime = 0;
        Timer.schedule(loadingAnimation, 0, loadingRate,
                (int) (loadingDuration / loadingRate));
    }

    @Override
    public void dispose() {
        loadingStage.dispose();
    }
}
