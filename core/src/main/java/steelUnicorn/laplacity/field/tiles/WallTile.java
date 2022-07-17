package steelUnicorn.laplacity.field.tiles;

public class WallTile extends FieldTile {

	public WallTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(0f, 0f, 0f, 1f);
		setName("Wall");
		setAllowDensityChange(false);
	}

}
