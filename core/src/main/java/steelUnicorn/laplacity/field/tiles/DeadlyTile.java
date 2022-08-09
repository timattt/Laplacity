package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.core.LaplacityAssets;

public class DeadlyTile extends SolidTile {

	public DeadlyTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(4);
	}

	private int animIndex = 0;
	
	@Override
	public TextureRegion getAnimatedRegion(float[] angle) {
		boolean top = tiles[gridX][gridY + 1] instanceof DeadlyTile;
		boolean bottom = tiles[gridX][gridY - 1] instanceof DeadlyTile;
		boolean right = tiles[gridX + 1][gridY] instanceof DeadlyTile;
		boolean left = tiles[gridX - 1][gridY] instanceof DeadlyTile;
		
		animIndex++;
		animIndex %= 3;
		
		// wires
		if (top && bottom && !left && !right) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[6 + (gridX + gridY + animIndex) % 3][0];
		}
		if (!top && !bottom && left && right) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[6 + (gridX + gridY + animIndex) % 3][0];
		}
		
		return null;
	}

	@Override
	public TextureRegion getRegion(float[] angle) {
		boolean top = tiles[gridX][gridY + 1] instanceof DeadlyTile;
		boolean bottom = tiles[gridX][gridY - 1] instanceof DeadlyTile;
		boolean right = tiles[gridX + 1][gridY] instanceof DeadlyTile;
		boolean left = tiles[gridX - 1][gridY] instanceof DeadlyTile;
		
		// 1
		if (top && !bottom && !left && !right) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[3][1];
		}
		if (!top && bottom && !left && !right) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[3][1];
		}
		if (!top && !bottom && left && !right) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[3][1];
		}
		if (!top && !bottom && !left && right) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[3][1];
		}
		
		// 2
		if (!top && bottom && right && !left) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[5][3];
		}
		if (!top && bottom && !right && left) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[5][3];
		}
		if (top && !bottom && !right && left) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[5][3];
		}
		if (top && !bottom && right && !left) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[5][3];
		}
		// wires
		if (top && bottom && !left && !right) {
			return null;
		}
		if (!top && !bottom && left && right) {
			return null;
		}
		
		// 3
		if (!top && bottom && right && left) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[4][0];
		}
		if (top && !bottom && right && left) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[4][0];
		}
		if (top && bottom && !right && left) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[4][0];
		}
		if (top && bottom && right && !left) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[4][0];
		}
	
		// 4
		if (top && bottom && right && left) {
			return LaplacityAssets.DEADLY_REGIONS[5][2];
		}
		
		return LaplacityAssets.DEADLY_REGIONS[0][0];
	}
}
