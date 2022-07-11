package steelUnicorn.laplacity.field;

import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import steelUnicorn.laplacity.field.tiles.FieldTile;

public class LaplacityField extends Group {

	// Tiles
	private FieldTile[][] tiles;
	
	// Sizes
	private int fieldWidth;
	private int fieldHeight;
	
	private float tileSize;
	
	public LaplacityField(Texture tileMap, Stage stage, World world) {
		loadTilemap(tileMap, stage);
		createBox2dObjects(world);
	}
	
	private void loadTilemap(Texture tileMap, Stage stage) {
		fieldWidth = tileMap.getWidth();
		fieldHeight = tileMap.getHeight();
		tileSize = SCREEN_WORLD_HEIGHT / fieldHeight;
		tiles = new FieldTile[fieldWidth][fieldHeight];
		
		tileMap.getTextureData().prepare();
		Pixmap pxmap = tileMap.getTextureData().consumePixmap();
		
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				int c = pxmap.getPixel(i, j);
				// black
				if (c == 255) {
					tiles[i][j] = new FieldTile(i, j, this);
				}
				
				if (tiles[i][j] != null) {
					stage.addActor(tiles[i][j]);
				}
			}
		}
		
		tileMap.getTextureData().disposePixmap();
	}
	
	private void createBox2dObjects(World world) {
		// TODO create box2d objects
	}
	
	private void updateModeFlight() {
		
	}
	
	private void updateModeNone() {
		
	}
	
	private void updateModeDirichlet() {
		
	}
	
	private void updateModeEraser() {
		
	}

	private void updateModeProtons() {
		
	}
	
	private void updateModeElectrons() {
		
	}

	@Override
	public void act(float delta) {
		
		// TODO make camera move
		camera.position.x = fieldWidth / 2 * tileSize - SCREEN_WORLD_WIDTH / 2;
		camera.update();
		
		super.act(delta);
		switch (gameScreen.getCurrentGameMode()) {
		case none:
			updateModeNone();
			break;
		case flight:
			updateModeFlight();
			break;
		case dirichlet:
			updateModeDirichlet();
			break;
		case eraser:
			updateModeEraser();
			break;
		case protons:
			updateModeProtons();
			break;
		case electrons:
			updateModeElectrons();
			break;
		}
	}

	public int getFieldWidth() {
		return fieldWidth;
	}

	public int getFieldHeight() {
		return fieldHeight;
	}

	public float getTileSize() {
		return tileSize;
	}

	public FieldTile[][] getTiles() {
		return tiles;
	}
}
