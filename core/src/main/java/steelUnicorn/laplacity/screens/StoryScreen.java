package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.core.Globals.*;
import static steelUnicorn.laplacity.core.LaplacityAssets.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.utils.LevelsParser;

public class StoryScreen extends ManagedScreen {
	
	private Stage loadingStage;
	private Image storyImage;
	private int storyIndex;
	
	private boolean requestedChange = false;
	private long start = 0;
	
	private static final long waitTime = 3000;
	
	public StoryScreen(int index) {
		this.storyIndex = index;
		
		loadingStage = new Stage(guiViewport);
		storyImage = new Image(STORY[storyIndex]);

		storyImage.setSize(storyImage.getPrefWidth() / storyImage.getPrefHeight() * guiViewport.getWorldHeight(),
				guiViewport.getWorldHeight());
		storyImage.setPosition(-storyImage.getWidth() / 2 + guiViewport.getWorldWidth() / 2, 0);
		loadingStage.addActor(storyImage);
	}

	@Override
	protected void create() {
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
            GameProcess.levelParams = LevelsParser.getParams(1, 1);
            GameProcess.initLevel(sectionLevels.get(0).get(0));
		}
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
		
		if (TimeUtils.millis() - start > waitTime) {
			next();
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
		requestedChange = false;
		start = TimeUtils.millis();
	}

	@Override
	public void dispose() {
		loadingStage.dispose();
	}

}
