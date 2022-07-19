package steelUnicorn.laplacity.field.tiles;

public class BarrierTile extends FieldTile {

	public BarrierTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(0f, 0f, 1f, 1f);
		setName("Barrier");
		setAllowDensityChange(false);
		setId(3);
	}

}
