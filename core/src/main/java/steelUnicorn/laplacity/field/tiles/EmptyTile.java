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
	protected float visibleDensity = 0;
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

	public void addVisibleDensity(float delta) {
		if (allowDensityChange) {
			this.visibleDensity += delta;
			visibleDensity = Math.min(MAX_DENSITY, visibleDensity);
			DensityRenderer.setTileDensity(gridX, gridY, visibleDensity);
		}
	}

	public void addInvisibleDensity(float delta) {
		if (allowDensityChange) {
			invisibleDensity += delta;
		}
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

	public float getVisibleDensity() {
		return visibleDensity;
	}

	public float getTotalChargeDensity() {
		return visibleDensity + invisibleDensity;
	}

	public boolean isAllowDensityChange() {
		return allowDensityChange;
	}
	
	protected void setAllowDensityChange(boolean allowDensityChange) {
		this.allowDensityChange = allowDensityChange;
	}

	public void setVisibleDensity(float chargeDensity) {
		if (allowDensityChange) {
			this.visibleDensity = chargeDensity;
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
