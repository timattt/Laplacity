package steelUnicorn.laplacity.field.tiles;

import steelUnicorn.laplacity.field.structures.FieldStructure;

public class StructureTile extends EmptyTile {

	// Parent
	protected FieldStructure parent;
	
	public StructureTile(int gridX, int gridY, FieldStructure parent) {
		super(gridX, gridY);
		this.parent = parent;
		setName("Structure");
		setAllowDensityChange(false);
		setId(10);
	}

}
