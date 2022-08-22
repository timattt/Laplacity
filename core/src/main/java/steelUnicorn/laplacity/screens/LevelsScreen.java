package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.nameMainMenuScreen;
import static steelUnicorn.laplacity.core.Globals.nameSlideOut;
import static steelUnicorn.laplacity.core.Globals.progress;
import static steelUnicorn.laplacity.core.LaplacityAssets.LEVEL_BACKGROUND;
import static steelUnicorn.laplacity.core.LaplacityAssets.SKIN;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.FpsCounter;
import steelUnicorn.laplacity.ui.levels.LevelsTab;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;

/**
 * Класс LevelsScreen
 * Подгружает LevelsTab для отображения уровней
 * 
 */
public class LevelsScreen extends ManagedScreen {
    private Stage levelStage;
    public LevelsTab levelsTab;

    private Image background;

    private Label starsCollected;
    private static final float starsPad = 10;
    private static final float starImgScale = 0.5f;

    public LevelsScreen() {
        levelStage = new Stage(Globals.guiViewport);
        //background
        background = new Image(LEVEL_BACKGROUND);
        background.setSize(background.getPrefWidth() / background.getPrefHeight() * Globals.guiViewport.getWorldHeight(),
                Globals.guiViewport.getWorldHeight());
        background.setPosition(- background.getWidth() / 2 + Globals.guiViewport.getWorldWidth() / 2 , 0);
        levelStage.addActor(background);


        //fpsCounter
        FpsCounter fpsCounter = new FpsCounter(SKIN);
        levelStage.addActor(fpsCounter);

        //root
        Table root = new Table();
        root.setFillParent(true);
        levelStage.addActor(root);

        //return button
        ImageButton btn = new ImageButton(TEXSKIN, "Home");
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                Globals.game.getScreenManager().pushScreen(nameMainMenuScreen, nameSlideOut);
            }
        });
        root.add(btn).expand().uniform().top().left().padLeft(MainMenu.menuLeftSpace);

        //Levels Tab
        levelsTab = new LevelsTab(TEXSKIN);

        root.add(levelsTab).grow().top();
        //star collected label
        Table starsWidget = new Table();
        starsWidget.setBackground(TEXSKIN.getDrawable("label_back"));

        starsCollected = new Label("" + progress.starsCollected, TEXSKIN, "noback");
        starsWidget.add(starsCollected).padRight(starsPad);
        Image starImg = new Image(TEXSKIN, "label_star");
        starsWidget.add(starImg)
                .size(starImg.getPrefWidth() * starImgScale, starImg.getPrefHeight() * starImgScale);

        root.add(starsWidget).expand().top().right().uniform().pad(starsPad)
                .width(starsWidget.getPrefWidth() * 0.7f);
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
    public void show() {
        super.show();
        starsCollected.setText("" + progress.starsCollected);
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
