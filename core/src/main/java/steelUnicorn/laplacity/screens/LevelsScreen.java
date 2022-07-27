package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.ui.levels.LevelsTab;

/**
 * Класс LevelsScreen
 * Подгружает LevelsTab для отображения уровней
 * 
 */
public class LevelsScreen extends ManagedScreen {
    private Stage levelStage;
    private LevelsTab levelsTab;

    public LevelsScreen() {
        levelStage = new Stage(Globals.guiViewport);
        Skin skin = Globals.assetManager.get("ui/uiskin.json", Skin.class);

        Table root = new Table();
        root.setFillParent(true);
        levelStage.addActor(root);
        root.setDebug(true);

        levelsTab = new LevelsTab(skin);

        root.add(levelsTab);
    }

    @Override
    protected void create() {
        this.addInputProcessor(levelStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        levelStage.act();
        levelStage.draw();
    }

    @Override
    public void hide() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
    }
}
