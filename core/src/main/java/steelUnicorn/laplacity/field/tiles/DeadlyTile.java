package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;

import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;

public class DeadlyTile extends SolidTile {

	public DeadlyTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(1f, 0f, 0f, 1f);
		setName("Deadly");
		setAllowDensityChange(false);
		setId(4);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {}

	@Override
	public void constantDraw(SpriteCache sc) {
		float sz = LaplacityField.tileSize;

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
			sc.add(LaplacityAssets.DEADLY_REGIONS[0][0], gridX * sz, gridY * sz, sz, sz);
			return;
		}
		if (!top && bottom && !right && left) {
			sc.add(LaplacityAssets.DEADLY_REGIONS[0][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -90);
			return;
		}
		if (top && !bottom && !right && left) {
			sc.add(LaplacityAssets.DEADLY_REGIONS[0][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -180);
			return;
		}
		if (top && !bottom && right && !left) {
			sc.add(LaplacityAssets.DEADLY_REGIONS[0][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -270);
			return;
		}
		
		// sides
		if ((!top && bottom && right && left) || (top && !bottom && right && left)) {
			sc.add(LaplacityAssets.DEADLY_REGIONS[1][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
			return;
		}
		if ((top && bottom && !right && left) || (top && bottom && right && !left)){
			sc.add(LaplacityAssets.DEADLY_REGIONS[1][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
			return;
		}

		// internal corners
		if (top && bottom && right && left && !topRight && topLeft && bottomLeft && bottomRight) {
			sc.add(LaplacityAssets.DEADLY_REGIONS[2][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, -90);
			return;
		}
		if (top && bottom && right && left && topRight && !topLeft && bottomLeft && bottomRight) {
			sc.add(LaplacityAssets.DEADLY_REGIONS[2][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
			return;
		}
		if (top && bottom && right && left && topRight && topLeft && !bottomLeft && bottomRight) {
			sc.add(LaplacityAssets.DEADLY_REGIONS[2][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
			return;
		}
		if (top && bottom && right && left && topRight && topLeft && bottomLeft && !bottomRight) {
			sc.add(LaplacityAssets.DEADLY_REGIONS[2][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 180);
			return;
		}
		
		sc.add(LaplacityAssets.DEADLY_REGIONS[1][0], gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
	}
}
