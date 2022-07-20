package steelUnicorn.laplacity.field;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import steelUnicorn.laplacity.field.graphics.DensityRenderer;
import steelUnicorn.laplacity.field.physics.TilesBodyHandler;
import steelUnicorn.laplacity.field.tiles.BarrierTile;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.FinishTile;
import steelUnicorn.laplacity.field.tiles.WallTile;
import steelUnicorn.laplacity.particles.ChargedParticle;

/**
 * Класс со статическими полями и функциями, связанными с tilemap. Тут есть размеры поля, массив тайлов,
 * размер тайла. Еще тут есть функции для перехода из координат сетки в мировые координаты и наоборот.
 * И тут еще есть функции для рисования и очистки плотности заряда.
 * @author timat
 *
 */
public class LaplacityField extends Group {

	// Tiles
	public static EmptyTile[][] tiles;

	// Sizes
	public static int fieldWidth;
	public static int fieldHeight;
	
	public static float tileSize;
	
	// Electron start pos
	public static final Vector2 electronStartPos = new Vector2();
	
	public static void initField(Texture tileMap) {
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
				
				// yellow
				if (c == -65281) {
					LaplacityField.fromGridToWorldCoords(i, j, electronStartPos);
				}
			}
		}
		
		TilesBodyHandler.createBodies(tiles);
		Gdx.app.log("field", "bodies created");
		tileMap.getTextureData().disposePixmap();
	}

	public static void fromGridToWorldCoords(int gridX, int gridY, Vector2 res) {
		res.set((gridX + 0.5f) * tileSize, (gridY + 0.5f) * tileSize);
	}
	
	public static EmptyTile getTileFromWorldCoords(float x, float y) {
		int i = (int) (x / tileSize);
		int j = (int) (y / tileSize);
		if (i >= 0 && j >= 0 && i < fieldWidth && j < fieldHeight) {
			return tiles[i][j];
		} else {
			return null;
		}
	}

	public static void fillCircleWithRandomDensity(float x, float y, float r, float val) {
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
					tile.addChargeDensity((float) (Math.random() * val));
				}
			}
		}
		
		DensityRenderer.updateDensity();
	}
	
	public static void clearCircleDensity(float x, float y, float r) {
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
		
		DensityRenderer.updateDensity();
	}
}
