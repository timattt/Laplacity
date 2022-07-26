package steelUnicorn.laplacity.field.tiles;

public class FinishTile extends SolidTile {

	public FinishTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(5);
	}

}
