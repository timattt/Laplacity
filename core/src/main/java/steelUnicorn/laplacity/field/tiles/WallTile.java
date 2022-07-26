package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.graphics.g2d.SpriteCache;

import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;

public class WallTile extends SolidTile {

	public WallTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(6);
	}
	
	@Override
	public void constantDraw(SpriteCache sc) {
		float sz = LaplacityField.tileSize;
		
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
