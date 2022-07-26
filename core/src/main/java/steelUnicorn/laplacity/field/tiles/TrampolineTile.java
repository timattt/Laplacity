package steelUnicorn.laplacity.field.tiles;

public class TrampolineTile extends SolidTile {

	public TrampolineTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(15);
		setRestitution(100f);
	}

}
