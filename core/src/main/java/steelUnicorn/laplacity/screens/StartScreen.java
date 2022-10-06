package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.core.Globals;

public class StartScreen extends LoadingScreen {
    private Texture backTexture;
    private Skin skin;

    //TODO async task for creation entities

    public StartScreen(Viewport viewport) {
        super(viewport);

        backTexture = new Texture(Gdx.files.internal("textures/backgrounds/LOAD_BACKGROUND.png"));
        skin = new Skin(Gdx.files.internal("ui/texskin/texskin.json"));
    }

    @Override
    protected void create() {
        super.create();

        addBackground(backTexture);

        addLoadingLabel(skin);
    }
    private float check = 0;
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        loadingStage.draw();
        loadingStage.act();

        //TODO checking for loading finishing and transition
    }

    @Override
    public void dispose() {
        super.dispose();
        backTexture.dispose();
        skin.dispose();
    }
}
