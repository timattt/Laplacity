package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import steelUnicorn.laplacity.field.DensityRenderer;

public class EmptyTile extends Actor {

	// grid
	protected int gridX;
	protected int gridY;
	
	// Field potential
	protected float potential;
	
	// charge density
	protected float chargeDensity;
	
	// Density change
	private boolean allowDensityChange = true;
	
	// id
	private int id;
	
	public EmptyTile(int gridX, int gridY) {
		super();
		this.gridX = gridX;
		this.gridY = gridY;

		registerObject(this);
		
		field.fromGridToWorldCoords(gridX, gridY, TMP1);
		setPosition(TMP1.x, TMP1.y);
		
		setColor(0f, 0f, 0f, 0f);
		setName("Empty");
		setId(1);
	}

	public boolean isAllowDensityChange() {
		return allowDensityChange;
	}

	public void setAllowDensityChange(boolean allowDensityChange) {
		this.allowDensityChange = allowDensityChange;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawArrow();
	}
	
	protected void drawArrow() {
		/*
		if (gridX % 4 == 0 && gridY % 4 == 0) {
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
		}*/
	}

	public void setChargeDensity(float chargeDensity) {
		if (allowDensityChange) {
			this.chargeDensity = chargeDensity;
			DensityRenderer.setTileDensity(gridX, gridY, chargeDensity);
		}
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

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}


}
