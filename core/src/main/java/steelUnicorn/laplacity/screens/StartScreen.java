package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.utils.Debugger;

public class StartScreen extends LoadingScreen {
    private Texture backTexture;
    private Skin skin;

    private boolean callOnce;

    public StartScreen(Viewport viewport) {
        super(viewport);

        backTexture = new Texture(Gdx.files.internal("textures/backgrounds/LOAD_BACKGROUND.png"));
        skin = new Skin(Gdx.files.internal("ui/texskin/texskin.json"));

        callOnce = true;
    }

    @Override
    protected void create() {
        addBackground(backTexture);
        addLoadingLabel(skin);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        loadingStage.draw();
        loadingStage.act();

        if (callOnce && Globals.game.assetManager.update(1000 / 60)) {
            if (Globals.game.createEntities() == Laplacity.CreatingStatus.CREATING_FINISHED) {
                callOnce = false;
                LaplacityAssets.changeTrack("music/main theme_drop.ogg");
                Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen,
                        Globals.blendTransitionName);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        backTexture.dispose();
        skin.dispose();
    }
}
