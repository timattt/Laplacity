package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;

public class FieldTile extends EmptyTile {
	
	// Body
	protected Body body;

	public FieldTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(new Color(0.3f, 0.3f, 0.3f, 1));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float sz = field.getTileSize();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(getColor());
		shapeRenderer.rect(gridX*sz, gridY*sz, sz, sz);
		shapeRenderer.end();
		
		drawArrow(); 
	}
	
}