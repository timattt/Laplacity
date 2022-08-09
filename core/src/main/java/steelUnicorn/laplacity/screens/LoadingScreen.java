package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.game;
import static steelUnicorn.laplacity.core.Globals.guiViewport;
import static steelUnicorn.laplacity.core.Globals.nameGameScreen;
import static steelUnicorn.laplacity.core.LaplacityAssets.EARTH_BACKGROUND;
import static steelUnicorn.laplacity.core.LaplacityAssets.SKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.ui.FpsCounter;

/**
 * Экран загрузки. Используется при включении уровня. Отображает надпись Loading в течении
 * loadingDuration секунд, с анимацией точек ./../... каждые loadingRate секунд
 */
public class LoadingScreen extends ManagedScreen {
    private Stage loadingStage;
    private Label loadingLabel;

    private final String[] loadingText = new String[]{"Loading", "Loading.", "Loading..", "Loading..."};
    private float loadingRate = 0.25f;
    private float loadingDuration = 3f;
    private int dot = 0;
    private static final float scale = 2;

    private float elapsedTime = 0;
    Timer.Task loadingAnimation;

    private Image background;

    public LoadingScreen() {
        loadingStage = new Stage(guiViewport);

        background = new Image(EARTH_BACKGROUND);
        background.setSize(background.getPrefWidth() / background.getPrefHeight() * guiViewport.getWorldHeight(),
                guiViewport.getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + guiViewport.getWorldWidth() / 2 , 0);
        loadingStage.addActor(background);

        //fpsCounter
        FpsCounter fpsCounter = new FpsCounter(SKIN);
        loadingStage.addActor(fpsCounter);

        loadingLabel = new Label("Loading", SKIN);
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
    protected void create() {
        addInputProcessor(loadingStage);
    }

    @Override
    public void hide() {
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
        background.setSize(background.getPrefWidth() / background.getPrefHeight()
                        * loadingStage.getViewport().getWorldHeight(),
                loadingStage.getViewport().getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + loadingStage.getViewport().getWorldWidth() / 2 , 0);
    }

    @Override
    public void resize(int width, int height) {
        resizeBackground();
        loadingLabel.setPosition(loadingStage.getWidth() / 2 - loadingLabel.getWidth() * scale / 2,
                loadingStage.getHeight() / 2 - loadingLabel.getHeight() * scale / 2);
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
