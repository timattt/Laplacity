package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    private Image background;

    public LevelsScreen() {
        levelStage = new Stage(Globals.guiViewport);
        //background
        background = new Image(assetManager.get("backgrounds/MAIN_MENU_BACKGROUND.png", Texture.class));
        background.setSize(background.getPrefWidth() / background.getPrefHeight() * Globals.guiViewport.getWorldHeight(),
                Globals.guiViewport.getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + Globals.guiViewport.getWorldWidth() / 2 , 0);
        levelStage.addActor(background);

        Skin skin = Globals.assetManager.get("ui/uiskin.json", Skin.class);

        Table root = new Table();
        root.setFillParent(true);
        levelStage.addActor(root);

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

    public void resizeBackground() {
        background.setSize(background.getPrefWidth() / background.getPrefHeight()
                        * levelStage.getViewport().getWorldHeight(),
                levelStage.getViewport().getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + levelStage.getViewport().getWorldWidth() / 2 , 0);
    }

    @Override
    public void resize(int width, int height) {
        resizeBackground();
    }

    @Override
    public void dispose() {
    }
}
