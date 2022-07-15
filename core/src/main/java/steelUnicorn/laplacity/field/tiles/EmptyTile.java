package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EmptyTile extends Actor {

	// grid
	protected int gridX;
	protected int gridY;
	
	// Field potential
	protected float potential;
	
	// charge density
	protected float chargeDensity;
	
	public EmptyTile(int gridX, int gridY) {
		super();
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
	
	protected void drawArrow() {
		/*
		float sz = field.getTileSize();
		int w = field.getFieldWidth();
		int h = field.getFieldHeight();
		TMP1.set((gridX)*sz, (gridY)*sz);
		FieldPotentialCalculator.calculateForce(TMP1.x, TMP1.y, field.getTiles(), TMP2);
		TMP2.scl(0.03f);
		TMP1.sub(sz*(w/2 - 0.5f), sz*(h/2 - 0.5f));
		TMP2.add(TMP1);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1f, 1f, 0, 1f);
		shapeRenderer.line(TMP1, TMP2);
		shapeRenderer.end();
		*/
	}

	public void setChargeDensity(float chargeDensity) {
		this.chargeDensity = chargeDensity;
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

	public int getGridX() {
		return gridX;
	}

	public int getGridY() {
		return gridY;
	}
	
	public float getChargeDensity() {
		return chargeDensity;
	}


}