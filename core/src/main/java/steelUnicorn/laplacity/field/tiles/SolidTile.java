package steelUnicorn.laplacity.field.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteCache;

import steelUnicorn.laplacity.field.physics.TilesBodyHandler;

/**
 * Тайл, который есть физическое тело. Структура тел обсчитывается в классе {@link TilesBodyHandler}.
 * Несколько одинаковых тайлов рядом будут образовывать только одно тело.
 * @author timat
 *
 */
public class SolidTile extends EmptyTile {

	// params
	private float restitution;
	
	public SolidTile(int gridX, int gridY) {
		super(gridX, gridY);
		setId(2);
		restitution = 1f;
	}
	
	public void constantDraw(SpriteCache sc) {
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}
	
}