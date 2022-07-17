package steelUnicorn.laplacity.field;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.tiles.BarrierTile;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.FinishTile;
import steelUnicorn.laplacity.field.tiles.WallTile;
import steelUnicorn.laplacity.particles.ChargedParticle;

public class LaplacityField extends Group {

	// Tiles
	private EmptyTile[][] tiles;
	
	// Rendering visible density tiles
	private Pixmap densityPixmap;
	private Texture densityTexture;

	// Sizes
	private int fieldWidth;
	private int fieldHeight;
	
	private float tileSize;
	
	public void init(Texture tileMap) {
		fieldWidth = tileMap.getWidth();
		fieldHeight = tileMap.getHeight();
		tileSize = SCREEN_WORLD_HEIGHT / fieldHeight;
		tiles = new EmptyTile[fieldWidth][fieldHeight];
		
		tileMap.getTextureData().prepare();
		Pixmap pxmap = tileMap.getTextureData().consumePixmap();
		
		for (int i = 0; i < fieldWidth; i++) {
			for (int k = 0; k < fieldHeight; k++) {
				int j = fieldHeight - k - 1;
				int c = pxmap.getPixel(i, k);
				// black
				if (c == 255) {
					tiles[i][j] = new WallTile(i, j);
				} else
				// green
				if (c == 16711935) {
					tiles[i][j] = new FinishTile(i, j);
				} else
				// blue
				if (c == 65535) {
					tiles[i][j] = new BarrierTile(i, j);
				} else
				// red
				if (c == -16776961) {
					tiles[i][j] = new DeadlyTile(i, j);
				}
				
				// empty
				else {
					tiles[i][j] = new EmptyTile(i, j);
				}
			}
		}
		
		densityPixmap = new Pixmap(fieldWidth, fieldHeight, Format.RGBA8888);
		densityPixmap.setBlending(Blending.None);
		densityPixmap.setColor(Color.CLEAR);
		densityPixmap.fill();
		densityTexture = new Texture(densityPixmap);

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

	public void updateDensityTexture() {
		Color chargeColor = new Color(Color.VIOLET);
		float chargeDensity = 0.0f;
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				if ((chargeDensity = tiles[i][j].getChargeDensity()) != 0) {
					chargeColor.a = chargeDensity / GameProcess.MAX_CHARGE_DENSITY;
					densityPixmap.drawPixel(i, j, Color.rgba8888(chargeColor));
				}
			}
		}
		densityTexture.draw(densityPixmap, 0, 0);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	public void fromGridToWorldCoords(int gridX, int gridY, Vector2 res) {
		res.set((gridX - fieldWidth / 2 + 0.5f) * tileSize, (gridY - fieldHeight / 2 + 0.5f) * tileSize);
	}
	
	public EmptyTile getTileFromWorldCoords(float x, float y) {
		int i = (int) (x / tileSize + fieldWidth / 2);
		int j = (int) (y / tileSize + fieldHeight / 2);
		if (i >= 0 && j >= 0 && i < fieldWidth && j < fieldHeight) {
			return tiles[i][j];
		} else {
			return null;
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

	public EmptyTile[][] getTiles() {
		return tiles;
	}

	public Texture getDensityTexture() {
		return densityTexture;
	
	public void fillCircleWithRandomDensity(float x, float y, float r, float val) {
		EmptyTile center = getTileFromWorldCoords(x, y);

		if (center == null) {
			return;
		}
		
		int i = center.getGridX();
		int j = center.getGridY();
		int side = (int) (r / tileSize + 1);
		
		for (int p = -side; p < side; p++) {
			for (int q = -side; q < side; q++) {
				int u = p + i;
				int v = q + j;
				
				fromGridToWorldCoords(u, v, TMP1);
				TMP1.sub(center.getX(), center.getY());
				
				if (u >= 0 && v >= 0 && u < fieldWidth && v < fieldHeight && TMP1.len2() < r * r && Math.random() < DIRICHLET_SPRAY_TILE_PROBABILITY) {
					EmptyTile tile = tiles[u][v];
					tile.setChargeDensity((float) (Math.random() * val));
				}
			}
		}
	}
	
	public void clearCircleDensity(float x, float y, float r) {
		EmptyTile center = getTileFromWorldCoords(x, y);
		
		int i = center.getGridX();
		int j = center.getGridY();
		int side = (int) (r / tileSize + 1);
		
		for (int p = -side; p < side; p++) {
			for (int q = -side; q < side; q++) {
				int u = p + i;
				int v = q + j;
				
				fromGridToWorldCoords(u, v, TMP1);
				TMP1.sub(center.getX(), center.getY());
				
				if (u >= 0 && v >= 0 && u < fieldWidth && v < fieldHeight && TMP1.len2() < r * r) {
					EmptyTile tile = tiles[u][v];
					tile.setChargeDensity(0);
				}
			}
		}
		
		for (int k = 0; k < particles.size; k++) {
			ChargedParticle pt = particles.get(k);
			TMP1.set(center.getX(), center.getY());
			TMP1.sub(pt.getX(), pt.getY());
			if (TMP1.len2() < r * r) {
				deleteStaticParticle(pt);
				k--;
			}
		}
	}
}
