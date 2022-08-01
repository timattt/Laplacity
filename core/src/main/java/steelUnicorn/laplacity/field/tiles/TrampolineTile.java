package steelUnicorn.laplacity.field.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.core.LaplacityAssets;

public class TrampolineTile extends SolidTile {

	public TrampolineTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(15);
		setRestitution(2f);
	}

	@Override
	public TextureRegion getRegion(float[] angle) {
		return LaplacityAssets.TRAMPOLINE_REGION;
	}
	
}
