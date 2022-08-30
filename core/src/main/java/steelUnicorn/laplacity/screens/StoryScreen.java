package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.*;
import static steelUnicorn.laplacity.core.LaplacityAssets.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.utils.LevelsParamsParser;

public class StoryScreen extends ManagedScreen {
	
	private Stage loadingStage;
	private Image storyImage;
	private int storyIndex;

	private TextButton nextBtn;
	private static float nextBtnPad = UI_WORLD_HEIGHT * 0.05f;
	private static float nextScaleX = 2f;
	private static float nextScaleY = 1.2f;
	private static float nextFadeDur = 2f;

	private boolean requestedChange = false;
	private long start = 0;
	
	private static final long waitTime = 2000;

	public StoryScreen(int index) {
		this.storyIndex = index;
		
		loadingStage = new Stage(guiViewport);
		storyImage = new Image(STORY[storyIndex]);

		storyImage.setSize(storyImage.getPrefWidth() / storyImage.getPrefHeight() * guiViewport.getWorldHeight(),
				guiViewport.getWorldHeight());
		storyImage.setPosition(-storyImage.getWidth() / 2 + guiViewport.getWorldWidth() / 2, 0);
		loadingStage.addActor(storyImage);

		nextBtn = new TextButton("Next", TEXSKIN);
		nextBtn.getColor().a = 0;
		nextBtn.setVisible(false);
		nextBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LaplacityAssets.playSound(clickSound);
				next();
			}
		});

		Table root = new Table();
		root.setFillParent(true);
		root.add(nextBtn).expand().right().bottom()
				.size(nextBtn.getPrefWidth() * nextScaleX, nextBtn.getPrefHeight() * nextScaleY)
				.pad(nextBtnPad);
		loadingStage.addActor(root);
	}

	@Override
	protected void create() {
		this.addInputProcessor(loadingStage);
	}
	
	private void next() {
		if (requestedChange) {
			return;
		}

		requestedChange = true;
		if (storyIndex + 1 < STORY.length) {
			game.getScreenManager().pushScreen(nameStoryScreen + (int)(storyIndex+1), storyTransitionName);
		} else {
            GameProcess.levelNumber = 1;
            GameProcess.sectionNumber = 1;
            GameProcess.levelParams = LevelsParamsParser.getParams(1, 1);
            GameProcess.initLevel(sectionLevels.get(0).get(0));
		}
	}

	@Override
	public void hide() {
		nextBtn.setVisible(false);
		nextBtn.getColor().a = 0;
		nextBtn.clearActions();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		loadingStage.draw();
		loadingStage.act();
		
		if (TimeUtils.millis() - start > waitTime) {
			nextBtn.setVisible(true);
			nextBtn.addAction(Actions.fadeIn(nextFadeDur));
		}
	}

	public void resizeBackground() {
        storyImage.setSize(storyImage.getPrefWidth() / storyImage.getPrefHeight()
                * loadingStage.getViewport().getWorldHeight(),
        loadingStage.getViewport().getWorldHeight());
        storyImage.setPosition(- storyImage.getWidth() / 2 + loadingStage.getViewport().getWorldWidth() / 2 , 0);
}

	@Override
	public void resize(int width, int height) {
		resizeBackground();
	}
	
	@Override
	public void show() {
		super.show();
		requestedChange = false;
		start = TimeUtils.millis();
	}

	@Override
	public void dispose() {
		loadingStage.dispose();
	}

}
