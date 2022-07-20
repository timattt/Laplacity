package steelUnicorn.laplacity.field.tiles;

public class DeadlyTile extends SolidTile {

	public DeadlyTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(1f, 0f, 0f, 1f);
		setName("Deadly");
		setAllowDensityChange(false);
		setId(4);
	}

}
