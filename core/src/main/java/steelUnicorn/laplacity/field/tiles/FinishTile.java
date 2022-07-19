package steelUnicorn.laplacity.field.tiles;

public class FinishTile extends FieldTile {

	public FinishTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(0f, 1f, 0f, 1f);
		setName("Finish");
		setAllowDensityChange(false);
		setId(5);
	}

}
