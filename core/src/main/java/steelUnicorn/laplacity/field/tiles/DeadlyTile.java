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

	@Override
	public TextureRegion getRegion(float[] angle) {
		int val = 0;
		
		boolean top = !(gridY < fieldHeight - 1) || tiles[gridX][gridY + 1] instanceof SolidTile;
		boolean bottom = !(gridY > 0) || tiles[gridX][gridY - 1] instanceof SolidTile;
		boolean right = !(gridX < fieldWidth - 1) || tiles[gridX + 1][gridY] instanceof SolidTile;
		boolean left = !(gridX > 0) || tiles[gridX - 1][gridY] instanceof SolidTile;
		
		boolean topRight = !(gridX < fieldWidth - 1) || !(gridY < fieldHeight - 1) || tiles[gridX + 1][gridY + 1] instanceof SolidTile;
		boolean bottomRight = !(gridX < fieldWidth - 1) || !(0 < gridY) || tiles[gridX + 1][gridY - 1]  instanceof SolidTile;
		boolean topLeft = !(0 < gridX) || !(gridY < fieldHeight - 1) || tiles[gridX - 1][gridY + 1] instanceof SolidTile;
		boolean bottomLeft = !(gridX > 0) || !(0 < gridY) || tiles[gridX - 1][gridY - 1] instanceof SolidTile;

		// corners
		if (!top && bottom && right && !left) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[0][val];
		}
		if (!top && bottom && !right && left) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[0][val];
		}
		if (top && !bottom && !right && left) {
			angle[0] = -180;
			return LaplacityAssets.DEADLY_REGIONS[0][val];
		}
		if (top && !bottom && right && !left) {
			angle[0] = -270;
			return LaplacityAssets.DEADLY_REGIONS[0][val];
		}
		
		// sides
		if ((!top && bottom && right && left) || (top && !bottom && right && left)) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[1][val];
		}
		if ((top && bottom && !right && left) || (top && bottom && right && !left)){
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[1][val];
		}

		// internal corners
		if (top && bottom && right && left && !topRight && topLeft && bottomLeft && bottomRight) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[2][val];
		}
		if (top && bottom && right && left && topRight && !topLeft && bottomLeft && bottomRight) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[2][val];
		}
		if (top && bottom && right && left && topRight && topLeft && !bottomLeft && bottomRight) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[2][val];
		}
		if (top && bottom && right && left && topRight && topLeft && bottomLeft && !bottomRight) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[2][val];
		}
		
		return null;
		//sc.add(LaplacityAssets.DEADLY_REGIONS[0][val], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
	}
}
