package steelUnicorn.laplacity.field.tiles;

import com.badlogic.gdx.graphics.Color;

import steelUnicorn.laplacity.field.LaplacityField;

public class WallFieldTile extends FieldTile {

	public WallFieldTile(int gridX, int gridY, LaplacityField field) {
		super(gridX, gridY, field);
		setColor(new Color(0, 0, 1, 1));
	}

}
