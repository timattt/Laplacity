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
		sc.add(LaplacityAssets.TRAMPOLINE_REGION, gridX * sz, gridY * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
	}
	
}
