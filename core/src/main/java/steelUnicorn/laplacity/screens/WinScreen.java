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
import steelUnicorn.laplacity.Globals;

public class WinScreen extends ManagedScreen {
	private Stage winStage;
	private Table root;
	
	private static final float padSize = 10;
	private static final float btnWidth = 100;
	private static final float btnHeight = 50;
	private static final float contentWidth = 200;
	private static final float contentHeight = 100;
	private static final float fontScale = 2;
	
	public WinScreen() {
		super();
		winStage = new Stage();

		root = new Table();
		root.setFillParent(true);
		root.setName("rootTable");
		winStage.addActor(root);
	}
	
	@Override
	protected void create() {
		this.addInputProcessor(winStage);
	}
	
	public void buildStage(float score) {		
		Skin skin = Globals.assetManager.get("ui/uiskin.json", Skin.class);
		
		Label done = new Label("Done\n"
				+ "Score " + String.valueOf(score), skin);
		done.setAlignment(Align.center);
		done.setFontScale(fontScale);
		done.setName("doneLabel");
		done.setColor(Color.BLACK);
		root.add(done).pad(padSize).size(contentWidth, contentHeight);
		
		root.row();
		//buttons
		Table buttons = new Table();
		buttons.setName("buttonsTable");
		root.add(buttons).pad(padSize);
		
		TextButton btn = new TextButton("Exit", skin);
		btn.setName("exitBtn");
		btn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen, Globals.nameSlideIn);
			}
		});
		buttons.add(btn).pad(padSize).size(btnWidth, btnHeight);
		
		
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
		buttons.add(btn).pad(padSize).size(btnWidth, btnHeight);
		
		if (GameProcess.levelNumber < GameProcess.MAX_LEVEL) {
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
			buttons.add(btn).pad(padSize).size(btnWidth, btnHeight);
		}
	}
	
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
