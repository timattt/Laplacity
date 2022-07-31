package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.assetManager;
import static steelUnicorn.laplacity.core.Globals.game;
import static steelUnicorn.laplacity.core.Globals.guiViewport;
import static steelUnicorn.laplacity.core.Globals.nameGameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;

import de.eskalon.commons.screen.ManagedScreen;

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

    public LoadingScreen() {
        loadingStage = new Stage(guiViewport);

        Skin skin = assetManager.get("ui/uiskin.json", Skin.class);

        Image background = new Image(assetManager.get("backgrounds/EARTH_BACKGROUND.png", Texture.class));
        background.setSize(background.getPrefWidth() / background.getPrefHeight() * guiViewport.getWorldHeight(),
                guiViewport.getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + guiViewport.getWorldWidth() / 2 , 0);
        loadingStage.addActor(background);

        loadingLabel = new Label("Loading", skin);
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

    @Override
    public void resize(int width, int height) {

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
