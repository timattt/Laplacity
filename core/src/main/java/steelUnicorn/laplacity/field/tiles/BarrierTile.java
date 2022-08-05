package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.core.LaplacityAssets;

public class BarrierTile extends SolidTile {
	
	public BarrierTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(3);
	}

	@Override
	public TextureRegion getRegion(float[] angle) {	
		boolean top = tiles[gridX][gridY + 1] instanceof BarrierTile;
		boolean bottom = tiles[gridX][gridY - 1] instanceof BarrierTile;
		boolean right = tiles[gridX + 1][gridY] instanceof BarrierTile;
		boolean left = tiles[gridX - 1][gridY] instanceof BarrierTile;
		
		boolean topRight = tiles[gridX + 1][gridY + 1] instanceof BarrierTile;
		boolean bottomRight = tiles[gridX + 1][gridY - 1] instanceof BarrierTile;
		boolean topLeft = tiles[gridX - 1][gridY + 1] instanceof BarrierTile;
		boolean bottomLeft = tiles[gridX - 1][gridY - 1] instanceof BarrierTile;
		
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
