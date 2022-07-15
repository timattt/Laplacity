package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class EmptyTile extends FieldTile {

	public EmptyTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(0f, 0f, 0f, 0f);
		setName("Empty");
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float sz = field.getTileSize();
		int w = field.getFieldWidth();
		int h = field.getFieldHeight();
		
		// TODO tmp density draw
		if (chargeDensity > 0.1) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0f, chargeDensity / MAX_DENSITY, chargeDensity / MAX_DENSITY, 1f);
			shapeRenderer.rect((gridX - w / 2) * sz, (gridY - h / 2) * sz, sz, sz);
			shapeRenderer.end();
		}
		
		drawArrow();
	}

}
