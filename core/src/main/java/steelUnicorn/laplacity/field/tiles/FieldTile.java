package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

import steelUnicorn.laplacity.field.LaplacityField;

public class FieldTile extends Actor {

	// grid
	private int gridX;
	private int gridY;
	
	// Field potential
	private float potential;
	
	// charge density
	private float chargeDensity;
	
	public float getChargeDensity() {
		return chargeDensity;
	}

	public FieldTile(int gridX, int gridY, LaplacityField field) {
		super();
		setParent(field);
		this.gridX = gridX;
		this.gridY = gridY;
		setColor(new Color(0.3f, 0.3f, 0.3f, 1));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float sz = field.getTileSize();
		int w = field.getFieldWidth();
		int h = field.getFieldHeight();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(getColor());
		shapeRenderer.rect((gridX - w/2)*sz, (gridY - h/2)*sz, sz, sz);
		shapeRenderer.end();
	}

	@Override
	public void act(float delta) {		
	}

	public float getPotential() {
		return potential;
	}

	public void setPotential(float potential) {
		this.potential = potential;
	}

}
