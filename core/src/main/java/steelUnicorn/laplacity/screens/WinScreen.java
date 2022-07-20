package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;

/**
 * Class for winning screen.
 * 
 * This class contain and build stage for winning screen.
 * In the stage there is a score, and 3 buttons
 * 	1. Exit - return user to main menu
 *  2. Replay - reload level
 *  3. Next - load next level
 */
public class WinScreen extends ManagedScreen {
	private Stage winStage;
	private Table root;
	
	private static final float spaceSize = Globals.UI_WORLD_HEIGHT * 0.03f;
	private static final float btnWidth = Globals.UI_WORLD_WIDTH * 0.1f;
	private static final float btnHeight = Globals.UI_WORLD_HEIGHT * 0.1f;
	private static final float contentWidth = Globals.UI_WORLD_WIDTH * 0.2f;
	private static final float contentHeight = Globals.UI_WORLD_HEIGHT * 0.2f;
	private static final float fontScale = 1.5f;
	/**
	 * Constructor initialize stage and root table
	 */
	public WinScreen() {
		super();
		winStage = new Stage(Globals.guiViewport);

		root = new Table();
		root.setFillParent(true);
		root.setName("rootTable");
		winStage.addActor(root);
	}
	
	@Override
	protected void create() {
		this.addInputProcessor(winStage);
	}
	
	/**
	 * buildStage function brings label and buttons together.
	 * 
	 * 
	 * @note When implement, don't forget to use clearStage because this functions allocate new memory. 
	 * 
	 * @param score - amount of score that user earned. This score goes into label initialization.
	 */
	public void buildStage(float score) {		
		Skin skin = Globals.assetManager.get("ui/uiskin.json", Skin.class);
		//label
		Label done = new Label("Done\n"
				+ "Score " + String.valueOf(score), skin);
		done.setAlignment(Align.center);
		done.setName("doneLabel");
		done.setFontScale(fontScale);
		done.setColor(Color.BLACK);
		root.add(done).space(spaceSize).size(contentWidth, contentHeight);
		
		root.row();
		//buttons
		Table buttons = new Table();
		buttons.setName("buttonsTable");
		root.add(buttons).space(spaceSize);

		buttons.defaults()
				.space(spaceSize)
				.size(btnWidth, btnHeight);

		TextButton btn = new TextButton("Exit", skin);
		btn.setName("exitBtn");
		btn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen, Globals.nameSlideIn);
			}
		});
		buttons.add(btn);
		
		
		btn = new TextButton("Replay", skin);
		btn.setName("replayBtn");
		btn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameProcess.disposeLevel();
				GameProcess.initLevel(Globals.assetManager.get("levels/level" +
						GameProcess.levelNumber + ".png", Texture.class));
				Globals.game.getScreenManager().pushScreen(Globals.nameGameScreen, Globals.nameSlideOut);
			}
		});
		buttons.add(btn);
		
		
		if (GameProcess.levelNumber < Globals.TOTAL_LEVELS_AVAILABLE) {	//For max level there is no need in next button
			btn = new TextButton("Next", skin);
			btn.setName("nextBtn");
			btn.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					GameProcess.disposeLevel();
					GameProcess.initLevel(Globals.assetManager.get("levels/level" +
							(++GameProcess.levelNumber) + ".png", Texture.class));
					Globals.game.getScreenManager().pushScreen(Globals.nameGameScreen, Globals.nameSlideOut);
				}
			});
			buttons.add(btn);
		}
	}
	
	/**
	 * Clear root table from children to use buildStage.
	 */
	public void clearStage() {
		if (root.hasChildren()) {
			Table buttons = root.findActor("buttonsTable");
			buttons.clearChildren();
			root.clearChildren();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		winStage.act();
		winStage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void dispose() {
		winStage.dispose();
	}

	@Override
	public void hide() {
		
	}
}
