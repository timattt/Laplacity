package steelUnicorn.laplacity.screens;

import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import steelUnicorn.laplacity.field.GameMode;
import steelUnicorn.laplacity.field.LaplacityField;

public class GameScreen extends ScreenAdapter {

	// Tilemap
	private LaplacityField field;
	
	// Stage and world
	private Stage stage;
	private World world;

	// Mode
	private GameMode currentGameMode;
	
	public GameScreen() {
		
	}

	public void load(Texture level) {
		stage = new Stage(gameViewport);
		field = new LaplacityField(level);
		stage.addActor(field);
		currentGameMode = GameMode.none;
	}

	@Override
	public void render(float delta) {
		update(delta);
		super.render(delta);
	}

	private void update(float delta) {
		switch (currentGameMode) {
		case none:
			field.updateModeNone();
			break;
		case flight:
			field.updateModeFlight();
			break;
		case dirichlet:
			field.updateModeDirichlet();
			break;
		case eraser:
			field.updateModeEraser();
			break;
		case protons:
			field.updateModeProtons();
			break;
		case electrons:
			field.updateModeElectrons();
			break;
		}
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		super.dispose();
	}
	
}
