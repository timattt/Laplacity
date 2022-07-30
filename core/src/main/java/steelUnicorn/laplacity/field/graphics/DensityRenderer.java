package steelUnicorn.laplacity.field.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.IntRect;
import steelUnicorn.laplacity.field.tiles.EmptyTile;

/**
 * Класс, который быстро умеет рисовать плотность.
 * Кроме обычных методов для рендеринга (init, render, cleanup)
 * тут есть два, необходимые для взаимодействия с tilemap.
 * 
 * updateDensity - его нужно вызвать, когда требуется перерисовать текстуру с плотностью.
 * Нужно вызывать редко. После большого кол. вызовов следующего метода.
 * setTileDensity - его нужно вызывать, когда меняется плотность одного тайла.
 * 
 * @author timat
 *
 */
public class DensityRenderer {

	private static int blockWidth;
	private static int blockHeight;
	
	private static DensityBlock[][] blocks;
	
	private static int up(int a, int b) {
		return a / b + (a % b == 0 ? 0 : 1);
	}
	
	public static void init() {
		int fieldWidth = LaplacityField.fieldWidth;
		int fieldHeight = LaplacityField.fieldHeight;
		
		blockWidth = (int) Math.sqrt(fieldWidth);
		blockHeight = (int) Math.sqrt(fieldHeight);
		
		blocks = new DensityBlock[up(fieldWidth, blockWidth)][up(fieldHeight, blockHeight)];

		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j] = new DensityBlock(new IntRect(i * blockWidth, j * blockHeight, (i + 1) * blockWidth - 1, (j + 1) * blockHeight - 1));
			}
		}
		
		updateDensity();
	}
	
	private static DensityBlock getBlock(int gridX, int gridY) {
		return blocks[gridX / blockWidth][gridY / blockHeight];
	}
	
	public static void updateDensity() {
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				if (blocks[i][j].repaintRequested) {
					blocks[i][j].repaintRequested = false;
					blocks[i][j].repaint();
				}
			}
		}
	}
	
	public static void setTileDensity(int x, int y, float val) {
		getBlock(x, y).repaintRequested = true;
	}
	
	public static void render() {
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j].render();
			}
		}
	}
	
	public static void cleanup() {
		if (blocks == null) {
			return;
		}
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j].dispose();
			}
		}
		blocks = null;
	}
	
	/**
	 * selects Texture region from LaplacityAssets.DENSITY_REGIONS.
	 * val from 0 to 1.
	 * @param val
	 * @return
	 */
	private static TextureRegion select(float val) {
		int i = (int) (val * 9);
		return LaplacityAssets.DENSITY_REGIONS[i%3][i/3];
	}
	
	private static class DensityBlock {
		
		private IntRect bounds;
		private boolean repaintRequested;
		
		private int id;
		
		public DensityBlock(IntRect bounds) {
			super();
			this.bounds = bounds;
			repaintRequested = true;

			float sz = LaplacityField.tileSize;
			
			GameProcess.gameCache.beginCache();
			for (int i = bounds.left; i <= bounds.right; i++) {
				for (int j = bounds.bottom; j <= bounds.top; j++) {
					if (i >= LaplacityField.fieldWidth || j >= LaplacityField.fieldHeight) {
						continue;
					}
					TextureRegion reg = select(0.1f);
					GameProcess.gameCache.add(reg, i * sz, j * sz, sz, sz);
				}
			}
			id = GameProcess.gameCache.endCache();
		}

		@Override
		public String toString() {
			return "DensityBlock [bounds=" + bounds + ", repaintRequested=" + repaintRequested + "]";
		}
		
		public void repaint() {
			float sz = LaplacityField.tileSize;
			
			GameProcess.gameCache.beginCache(id);
			for (int i = bounds.left; i <= bounds.right; i++) {
				for (int j = bounds.bottom; j <= bounds.top; j++) {
					if (i >= LaplacityField.fieldWidth || j >= LaplacityField.fieldHeight) {
						continue;
					}
					EmptyTile tl = LaplacityField.tiles[i][j];
					if (tl.getChargeDensity() > 0) {
						TextureRegion reg = select(tl.getChargeDensity() / (GameProcess.MAX_DENSITY + 0.1f));
						GameProcess.gameCache.add(reg, i * sz, j * sz, sz, sz);
					}
				}
			}
			GameProcess.gameCache.endCache();
		}
		
		public void render() {
			GameProcess.gameCache.setProjectionMatrix(CameraManager.camMat());
			GameProcess.gameCache.begin();
			GameProcess.gameCache.draw(id);
			GameProcess.gameCache.end();
		}
		
		public void dispose() {
		}
	}

}
