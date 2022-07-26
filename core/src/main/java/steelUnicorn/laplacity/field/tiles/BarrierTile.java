package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.graphics.g2d.SpriteCache;

import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;

public class BarrierTile extends SolidTile {
	
	public BarrierTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(3);
	}

	@Override
	public void constantDraw(SpriteCache sc) {
		float sz = LaplacityField.tileSize;
		
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
			sc.add(LaplacityAssets.BARRIER_REGIONS[0], gridX * sz, gridY * sz, sz, sz);
			return;
		}
		if (!top && bottom && !right && left) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -90);
			return;
		}
		if (top && !bottom && !right && left) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -180);
			return;
		}
		if (top && !bottom && right && !left) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -270);
			return;
		}
		
		// sides
		if (!top && bottom && right && left) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[1], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
			return;
		}
		if (top && !bottom && right && left) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[1], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 180);
			return;
		}
		if (top && bottom && !right && left) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[1], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -90);
			return;
		}
		if (top && bottom && right && !left) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[1], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -270);
			return;
		}
		
		// internal corners
		if (top && bottom && right && left && !topRight && topLeft && bottomLeft && bottomRight) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[2], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -90);
			return;
		}
		if (top && bottom && right && left && topRight && !topLeft && bottomLeft && bottomRight) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[2], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
			return;
		}
		if (top && bottom && right && left && topRight && topLeft && !bottomLeft && bottomRight) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[2], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
			return;
		}
		if (top && bottom && right && left && topRight && topLeft && bottomLeft && !bottomRight) {
			sc.add(LaplacityAssets.BARRIER_REGIONS[2], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 180);
			return;
		}
		
		sc.add(LaplacityAssets.BARRIER_REGIONS[3], gridX * sz, gridY * sz, sz, sz);
	}

}
