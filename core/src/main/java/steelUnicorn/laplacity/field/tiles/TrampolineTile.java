package steelUnicorn.laplacity.field.tiles;

public class TrampolineTile extends SolidTile {

	public TrampolineTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(0f, 0f, 0.4f, 1f);
		setName("Trampoline");
		setAllowDensityChange(false);
		setId(15);
		setRestitution(100f);
	}

}
