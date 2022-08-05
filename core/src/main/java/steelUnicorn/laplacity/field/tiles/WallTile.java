package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.core.LaplacityAssets;

public class WallTile extends SolidTile {

	public WallTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(6);
	}
	
	@Override
	public TextureRegion getRegion(float[] angle) {
		boolean top = !(gridY < fieldHeight - 1) || tiles[gridX][gridY + 1] instanceof WallTile;
		boolean bottom = !(gridY > 0) || tiles[gridX][gridY - 1] instanceof WallTile;
		boolean right = !(gridX < fieldWidth - 1) || tiles[gridX + 1][gridY] instanceof WallTile;
		boolean left = !(gridX > 0) || tiles[gridX - 1][gridY] instanceof WallTile;
		
		boolean topRight = !(gridX < fieldWidth - 1) || !(gridY < fieldHeight - 1) || tiles[gridX + 1][gridY + 1] instanceof WallTile;
		boolean bottomRight = !(gridX < fieldWidth - 1) || !(0 < gridY) || tiles[gridX + 1][gridY - 1]  instanceof WallTile;
		boolean topLeft = !(0 < gridX) || !(gridY < fieldHeight - 1) || tiles[gridX - 1][gridY + 1] instanceof WallTile;
		boolean bottomLeft = !(gridX > 0) || !(0 < gridY) || tiles[gridX - 1][gridY - 1] instanceof WallTile;
		
		// corners
		if (!top && bottom && right && !left) {
			angle[0] = 0;
			return LaplacityAssets.BARRIER_REGIONS[0];
		}
		if (!top && bottom && !right && left) {
			angle[0] = 0;
			return LaplacityAssets.BARRIER_REGIONS[5];
		}
		if (top && !bottom && !right && left) {
			angle[0] = 180;
			return LaplacityAssets.BARRIER_REGIONS[0];
		}
		if (top && !bottom && right && !left) {
			angle[0] = 180;
			return LaplacityAssets.BARRIER_REGIONS[5];
		}
		
		// sides
		if (!top && bottom && right && left) {
			angle[0] = 0;
			return LaplacityAssets.BARRIER_REGIONS[1];
		}
		if (top && !bottom && right && left) {
			angle[0] = 180;
			return LaplacityAssets.BARRIER_REGIONS[1];
		}
		if (top && bottom && !right && left) {
			angle[0] = 0;
			return LaplacityAssets.BARRIER_REGIONS[4];
		}
		if (top && bottom && right && !left) {
			angle[0] = 180;
			return LaplacityAssets.BARRIER_REGIONS[4];
		}
		
		// internal corners
		if (top && bottom && right && left && !topRight && topLeft && bottomLeft && bottomRight) {
			angle[0] = 0;
			return LaplacityAssets.BARRIER_REGIONS[6];
		}
		if (top && bottom && right && left && topRight && !topLeft && bottomLeft && bottomRight) {
			angle[0] = 0;
			return LaplacityAssets.BARRIER_REGIONS[2];
		}
		if (top && bottom && right && left && topRight && topLeft && !bottomLeft && bottomRight) {
			angle[0] = 180;
			return LaplacityAssets.BARRIER_REGIONS[6];
		}
		if (top && bottom && right && left && topRight && topLeft && bottomLeft && !bottomRight) {
			angle[0] = 180;
			return LaplacityAssets.BARRIER_REGIONS[2];
		}
		
		return LaplacityAssets.BARRIER_REGIONS[3];
	}

}
