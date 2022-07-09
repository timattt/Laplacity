package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.Globals.*;
import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import steelUnicorn.laplacity.LaplacityAssets;
import steelUnicorn.laplacity.field.GameMode;
import steelUnicorn.laplacity.field.LaplacityField;

public class GameScreen extends ScreenAdapter {

	// Stage and world
	private Stage stage;
	private World world;

	// Mode
	private GameMode currentGameMode;
	
	public GameScreen() {
		load(LaplacityAssets.LEVEL1_TILEMAP);
	}

	public void load(Texture level) {
		stage = new Stage(gameViewport);
		world = new World(Vector2.Zero, false);
		field = new LaplacityField(level, stage, world);
		shapeRenderer = new ShapeRenderer();
		stage.addActor(field);
		currentGameMode = GameMode.none;
	}

	@Override
	public void render(float delta) {
		if (!isPlaying()) {
			return;
		}
		update(delta);
		stage.draw();
		super.render(delta);
	}

	private void update(float delta) {
		if (!isPlaying()) {
			return;
		}
		world.step(delta, 10, 10);
		stage.act();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		super.dispose();
		field = null;
	}

	public GameMode getCurrentGameMode() {
		return currentGameMode;
	}
	
}
