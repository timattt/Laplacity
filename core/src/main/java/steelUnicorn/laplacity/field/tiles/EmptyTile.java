package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.DensityRenderer;

/**
 * Класс пустой клетки поля. Содержит поля плотности. Видимая плотность отображается на поле. Невидимая нет.
 * Невидимая нужна, чтобы частицы добавлять.
 * @author timat
 *
 */
public class EmptyTile {

	// grid
	protected int gridX;
	protected int gridY;
	
	// Field potential
	protected float potential;
	
	// charge density
	protected float chargeDensity = 0;
	protected float invisibleDensity = 0;
	
	// Density change
	private boolean allowDensityChange = true;
	
	// id
	private int id;
	
	public EmptyTile(int gridX, int gridY) {
		super();
		this.gridX = gridX;
		this.gridY = gridY;
		
		LaplacityField.fromGridToWorldCoords(gridX, gridY, TMP1);
		setId(1);
	}

	public void addChargeDensity(float delta) {
		if (allowDensityChange) {
			this.chargeDensity += delta;
			chargeDensity = Math.min(MAX_DENSITY, chargeDensity);
			DensityRenderer.setTileDensity(gridX, gridY, chargeDensity);
		}
	}

	public void addInvisibleDensity(float delta) {
		invisibleDensity += delta;
	}

	protected void drawIntensityArrow() {
		/*
		if (gridX % 1 == 0 && gridY % 1 == 0) {
		float sz = LaplacityField.tileSize;
		TMP1.set((gridX)*sz, (gridY)*sz);
		FieldPotentialCalculator.calculateFieldIntensity(TMP1.x, TMP1.y, LaplacityField.tiles, TMP2);
		TMP2.scl(0.03f);
		TMP1.sub(sz*(- 0.5f), sz*(- 0.5f));
		TMP2.add(TMP1);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1f, 1f, 0, 1f);
		shapeRenderer.line(TMP1, TMP2);
		shapeRenderer.end();

		}*/
	}
	
	public int getGridX() {
		return gridX;
	}

	public int getGridY() {
		return gridY;
	}

	public int getId() {
		return id;
	}

	public float getPotential() {
		return potential;
	}

	public float getChargeDensity() {
		return chargeDensity;
	}

	public float getTotalChargeDensity() {
		return chargeDensity + invisibleDensity;
	}

	public boolean isAllowDensityChange() {
		return allowDensityChange;
	}
	
	protected void setAllowDensityChange(boolean allowDensityChange) {
		this.allowDensityChange = allowDensityChange;
	}

	public void setChargeDensity(float chargeDensity) {
		if (allowDensityChange) {
			this.chargeDensity = chargeDensity;
			DensityRenderer.setTileDensity(gridX, gridY, chargeDensity);
		}
	}

	protected void setId(int id) {
		this.id = id;
	}

	public void setInvisibleDensity(float invisibleDensity) {
		this.invisibleDensity = invisibleDensity;
	}
	
	public void setPotential(float potential) {
		this.potential = potential;
	}
	
	public float getCenterX() {
		return (gridX + 0.5f) * LaplacityField.tileSize;
	}
	
	public float getCenterY() {
		return (gridY + 0.5f) * LaplacityField.tileSize;
	}

}
