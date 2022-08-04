package steelUnicorn.laplacity.field;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.graphics.DensityRenderer;
import steelUnicorn.laplacity.field.physics.TilesBodyHandler;
import steelUnicorn.laplacity.field.structures.AcceleratorStructure;
import steelUnicorn.laplacity.field.structures.BladesStructure;
import steelUnicorn.laplacity.field.structures.FieldStructure;
import steelUnicorn.laplacity.field.structures.FlimsyStructure;
import steelUnicorn.laplacity.field.structures.HatchStructure;
import steelUnicorn.laplacity.field.structures.HingeStructure;
import steelUnicorn.laplacity.field.structures.MovingWallStructure;
import steelUnicorn.laplacity.field.structures.rigid.Gift;
import steelUnicorn.laplacity.field.tiles.BarrierTile;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.FinishTile;
import steelUnicorn.laplacity.field.tiles.StructureTile;
import steelUnicorn.laplacity.field.tiles.TrampolineTile;
import steelUnicorn.laplacity.field.tiles.WallTile;
import steelUnicorn.laplacity.particles.ChargedParticle;

/**
 * Класс со статическими полями и функциями, связанными с tilemap. Тут есть размеры поля, массив тайлов,
 * размер тайла. Еще тут есть функции для перехода из координат сетки в мировые координаты и наоборот.
 * И тут еще есть функции для рисования и очистки плотности заряда. Еще этот класс отвечает за поддержку структур. 
 * @author timat
 *
 */
public class LaplacityField extends Group {

	// Tiles
	public static EmptyTile[][] tiles;
	
	// Structures
	public static final Array<FieldStructure> structures = new Array<FieldStructure>();

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
				
				// to ensure that structures will be on empty tiles.
				FieldStructure structOnTile = null;
				for (FieldStructure fs : structures) {
					if (fs.contains(i, j)) {
						structOnTile = fs;		
						break;
					}
				}
				if (structOnTile != null) {
					tiles[i][j] = new StructureTile(i, j, structOnTile);
					continue;
				}
				
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
				} else
				// moving wall
				if (c == 16777215 || c == -16711681) {
					FieldStructure str = new MovingWallStructure(i, j, pxmap);
					structures.add(str);
					tiles[i][j] = new StructureTile(i, j, str);
				} else
				// blades
				if (c == 1684301055) {
					BladesStructure str = new BladesStructure(i, j, pxmap);
					structures.add(str);
					tiles[i][j] = new StructureTile(i, j, str);
				} else
				// accelerator and moderator
				if (c == 1677721855 || c == 6553855) {
					AcceleratorStructure str = new AcceleratorStructure(i, j, pxmap, c);
					structures.add(str);
					tiles[i][j] = new StructureTile(i, j, str);
				} else
				// trampoline
				if (c == 25855) {
					tiles[i][j] = new TrampolineTile(i, j);
				} else
				// hinge
				if (c == 13107455 || c == -926365441) {
					HingeStructure str = new HingeStructure(i, j, pxmap);
					structures.add(str);
					tiles[i][j] = new StructureTile(i, j, str);
				} else
				// flimsy
				if (c == -1778384641) {
					FlimsyStructure str = new FlimsyStructure(i, j, pxmap);
					structures.add(str);
					tiles[i][j] = new StructureTile(i, j, str);
				} else
				// rigid
				if (c == 2021130495) {
					Gift str = new Gift(i, j, pxmap);
					structures.add(str);
					tiles[i][j] = new StructureTile(i, j, str);
				} else
				// hatch
				if (c == -2105409281) {
					HatchStructure str = new HatchStructure(i, j, pxmap);
					structures.add(str);
					tiles[i][j] = new StructureTile(i, j, str);
				}
				
				// empty
				else {
					if (c != -1 && c != -65281)
						Gdx.app.log("unknown tile color", c + "");
					tiles[i][j] = new EmptyTile(i, j);
				}
				
				// yellow
				if (c == -65281) {
					LaplacityField.fromGridToWorldCoords(i, j, electronStartPos);
				}
			}
		}
		
		// structures
		for (FieldStructure fs : structures) {
			fs.register();
		}
		
		TilesBodyHandler.createBodies(tiles);
		Gdx.app.log("field", "bodies created");
		tileMap.getTextureData().disposePixmap();
	}
	
	public static void renderStructuresCached(float timeFromStart) {
		for (FieldStructure fs : structures) {
			fs.renderCached(timeFromStart);
		}
	}
	
	public static void renderStructuresBatched(float timeFromStart) {
		for (FieldStructure fs : structures) {
			fs.renderBatched(timeFromStart);
		}
	}
	
	public static void cleanupStructures() {
		for (FieldStructure fs : structures) {
			fs.cleanup();
		}
		structures.clear();
	}
	
	public static void resetStructures() {
		for (FieldStructure fs : structures) {
			fs.reset();
		}
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
				TMP1.sub(center.getCenterX(), center.getCenterY());
				
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
				TMP1.sub(center.getCenterX(), center.getCenterY());
				
				if (u >= 0 && v >= 0 && u < fieldWidth && v < fieldHeight && TMP1.len2() < r * r) {
					EmptyTile tile = tiles[u][v];
					tile.setChargeDensity(0);
				}
			}
		}
		
		for (int k = 0; k < particles.size; k++) {
			ChargedParticle pt = particles.get(k);
			TMP1.set(center.getCenterX(), center.getCenterY());
			TMP1.sub(pt.getX(), pt.getY());
			if (TMP1.len2() < r * r) {
				LaplacityAssets.playSound(LaplacityAssets.annihilationSound);
				// TODO очень не нравится вызывать звук отсюда
				deleteStaticParticle(pt);
				k--;
			}
		}
		
		DensityRenderer.updateDensity();
	}

	/**
	 * Удаляет всё электростатическое поле с клеток, включая точечные заряды.
	 * Не забудьте обновить текстуру плотности и удалить физические тела
	 * точечных зарядов.
	 */
	public static void clearElectricField() {
		for (int i = 0; i < fieldWidth; i++)
			for (int j = 0; j < fieldHeight; j++) {
				tiles[i][j].setChargeDensity(0.0f);
				tiles[i][j].setInvisibleDensity(0.0f);
				tiles[i][j].setPotential(0.0f);
			}
	}

	public static void saveStructuresState() {
		for (FieldStructure fs : structures)
			fs.savePosition();
	}
}
