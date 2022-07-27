package steelUnicorn.laplacity.field.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteCache;

import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;

public class TrampolineTile extends SolidTile {

	public TrampolineTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(15);
		setRestitution(100f);
	}

	@Override
	public void constantDraw(SpriteCache sc) {
		float sz = LaplacityField.tileSize;
/*
		boolean top = !(gridY < fieldHeight - 1) || tiles[gridX][gridY + 1] instanceof SolidTile;
		boolean bottom = !(gridY > 0) || tiles[gridX][gridY - 1] instanceof SolidTile;
		boolean right = !(gridX < fieldWidth - 1) || tiles[gridX + 1][gridY] instanceof SolidTile;
		boolean left = !(gridX > 0) || tiles[gridX - 1][gridY] instanceof SolidTile;
		
		boolean topRight = !(gridX < fieldWidth - 1) || !(gridY < fieldHeight - 1) || tiles[gridX + 1][gridY + 1] instanceof SolidTile;
		boolean bottomRight = !(gridX < fieldWidth - 1) || !(0 < gridY) || tiles[gridX + 1][gridY - 1]  instanceof SolidTile;
		boolean topLeft = !(0 < gridX) || !(gridY < fieldHeight - 1) || tiles[gridX - 1][gridY + 1] instanceof SolidTile;
		boolean bottomLeft = !(gridX > 0) || !(0 < gridY) || tiles[gridX - 1][gridY - 1] instanceof SolidTile;
*/
		sc.add(LaplacityAssets.TRAMPOLINE_REGION, gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
	}
	
}
