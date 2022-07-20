package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import steelUnicorn.laplacity.field.LaplacityField;

/**
 * Тайл, который есть физическое тело. Структура тел обсчитывается в классе {@link TilesBodyHandler}.
 * Несколько одинаковых тайлов рядом будут образовывать только одно тело.
 * @author timat
 *
 */
public class SolidTile extends EmptyTile {

	public SolidTile(int gridX, int gridY) {
		super(gridX, gridY);
		setColor(new Color(0.3f, 0.3f, 0.3f, 1));
		setId(2);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float sz = LaplacityField.tileSize;
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(getColor());
		shapeRenderer.rect(gridX*sz, gridY*sz, sz, sz);
		shapeRenderer.end();
	}
	
}