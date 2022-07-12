package steelUnicorn.laplacity.field;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import steelUnicorn.laplacity.field.tiles.BarrierTile;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.field.tiles.FieldTile;
import steelUnicorn.laplacity.field.tiles.FinishTile;
import steelUnicorn.laplacity.field.tiles.WallTile;

public class LaplacityField extends Group {

	// Tiles
	private FieldTile[][] tiles;
	
	// Sizes
	private int fieldWidth;
	private int fieldHeight;
	
	private float tileSize;
	
	public void init(Texture tileMap) {
		fieldWidth = tileMap.getWidth();
		fieldHeight = tileMap.getHeight();
		tileSize = SCREEN_WORLD_HEIGHT / fieldHeight;
		tiles = new FieldTile[fieldWidth][fieldHeight];
		
		tileMap.getTextureData().prepare();
		Pixmap pxmap = tileMap.getTextureData().consumePixmap();
		
		for (int i = 0; i < fieldWidth; i++) {
			for (int k = 0; k < fieldHeight; k++) {
				int j = fieldHeight - k - 1;
				int c = pxmap.getPixel(i, k);
				// black
				if (c == 255) {
					tiles[i][j] = new WallTile(i, j);
				}
				// green
				if (c == 16711935) {
					tiles[i][j] = new FinishTile(i, j);
				}
				// blue
				if (c == 65535) {
					tiles[i][j] = new BarrierTile(i, j);
				}
				// red
				if (c == -16776961) {
					tiles[i][j] = new DeadlyTile(i, j);
				}
			}
		}
		
		tileMap.getTextureData().disposePixmap();
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
		switch (currentGameMode) {
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
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	public void fromGridToWorldCoords(int gridX, int gridY, Vector2 res) {
		res.set((gridX - fieldWidth / 2 + 0.5f) * tileSize, (gridY - fieldHeight / 2 + 0.5f) * tileSize);
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
