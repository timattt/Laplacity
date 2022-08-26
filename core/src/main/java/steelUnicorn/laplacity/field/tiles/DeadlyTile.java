package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.field.LaplacityField.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import steelUnicorn.laplacity.core.LaplacityAssets;

public class DeadlyTile extends SolidTile {

	private static boolean currentTexture = false;
	
	public DeadlyTile(int gridX, int gridY) {
		super(gridX, gridY);
		setAllowDensityChange(false);
		setId(4);
		index = gridX + gridY;
	}

	@Override
	public TextureRegion getRegion(float[] angle) {
		if (currentTexture) {
			return texture1(angle);
		}
		return texture2(angle);
	}

	private TextureRegion texture2(float[] angle) {
		boolean top = tiles[gridX][gridY + 1] instanceof DeadlyTile;
		boolean bottom = tiles[gridX][gridY - 1] instanceof DeadlyTile;
		boolean right = tiles[gridX + 1][gridY] instanceof DeadlyTile;
		boolean left = tiles[gridX - 1][gridY] instanceof DeadlyTile;
		
		boolean topRight = !(gridX < fieldWidth - 1) || !(gridY < fieldHeight - 1) || tiles[gridX + 1][gridY + 1] instanceof DeadlyTile;
		boolean bottomRight = !(gridX < fieldWidth - 1) || !(0 < gridY) || tiles[gridX + 1][gridY - 1]  instanceof DeadlyTile;
		boolean topLeft = !(0 < gridX) || !(gridY < fieldHeight - 1) || tiles[gridX - 1][gridY + 1] instanceof DeadlyTile;
		boolean bottomLeft = !(gridX > 0) || !(0 < gridY) || tiles[gridX - 1][gridY - 1] instanceof DeadlyTile;
		
		// corners
		if (top && bottom && right && left && !topRight && bottomRight && topLeft && bottomLeft) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[6][2];
		}
		if (top && bottom && right && left && topRight && bottomRight && topLeft && !bottomLeft) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[6][2];
		}
		if (top && bottom && right && left && topRight && !bottomRight && topLeft && bottomLeft) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[6][2];
		}
		if (top && bottom && right && left && topRight && bottomRight && !topLeft && bottomLeft) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[6][2];
		}
		//--------
		// wires
		if (top && bottom && !left && !right) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[3][4];//LaplacityAssets.DEADLY_REGIONS[7][1];
		}
		if (!top && !bottom && left && right) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[3][4];//LaplacityAssets.DEADLY_REGIONS[7][1];
		}
		
		// 3
		if (!top && bottom && right && left && bottomLeft && bottomRight) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[3][4];//LaplacityAssets.DEADLY_REGIONS[7][0];
		}
		if (top && !bottom && right && left && topLeft && topRight) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[3][4];//LaplacityAssets.DEADLY_REGIONS[7][0];
		}
		if (top && bottom && !right && left && topLeft && bottomLeft) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[3][4];//LaplacityAssets.DEADLY_REGIONS[7][0];
		}
		if (top && bottom && right && !left && topRight && bottomRight) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[3][4];//LaplacityAssets.DEADLY_REGIONS[7][0];
		}
		//-------
		
		// 1
		if (top && !bottom && !left && !right) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[4][0];
		}
		if (!top && bottom && !left && !right) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[4][0];
		}
		if (!top && !bottom && left && !right) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[4][0];
		}
		if (!top && !bottom && !left && right) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[4][0];
		}
		
		// 2
		if (!top && bottom && right && !left) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[6][2];
		}
		if (!top && bottom && !right && left) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[6][2];
		}
		if (top && !bottom && !right && left) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[6][2];
		}
		if (top && !bottom && right && !left) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[6][2];
		}
		
		// 3
		if (!top && bottom && right && left) {
			if (bottomLeft && bottomRight) {
				angle[0] = 0;
				return null;
			} else {
				angle[0] = 0;
				return LaplacityAssets.DEADLY_REGIONS[4][1];
			}
		}
		if (top && !bottom && right && left) {
			if (topLeft && topRight) {
				angle[0] = 180;
				return null;
			} else {
				angle[0] = 180;
				return LaplacityAssets.DEADLY_REGIONS[4][1];
			}
		}
		if (top && bottom && !right && left) {
			if (topLeft && bottomLeft) {
				angle[0] = -90;
				return null;
			} else {
				angle[0] = -90;
				return LaplacityAssets.DEADLY_REGIONS[4][1];
			}
		}
		if (top && bottom && right && !left) {
			if (topRight && bottomRight) {
				angle[0] = 90;
				return null;
			} else {
				angle[0] = 90;
				return LaplacityAssets.DEADLY_REGIONS[4][1];
			}
		}
	
		// 4
		if (top && bottom && right && left) {
			return null;
		}
		
		return LaplacityAssets.DEADLY_REGIONS[0][0];
	}
	
	private int index = 0;
	
	@Override
	public TextureRegion getAnimatedRegion(float[] angle) {
		boolean top = tiles[gridX][gridY + 1] instanceof DeadlyTile;
		boolean bottom = tiles[gridX][gridY - 1] instanceof DeadlyTile;
		boolean right = tiles[gridX + 1][gridY] instanceof DeadlyTile;
		boolean left = tiles[gridX - 1][gridY] instanceof DeadlyTile;
		
		boolean topRight = !(gridX < fieldWidth - 1) || !(gridY < fieldHeight - 1) || tiles[gridX + 1][gridY + 1] instanceof DeadlyTile;
		boolean bottomRight = !(gridX < fieldWidth - 1) || !(0 < gridY) || tiles[gridX + 1][gridY - 1]  instanceof DeadlyTile;
		boolean topLeft = !(0 < gridX) || !(gridY < fieldHeight - 1) || tiles[gridX - 1][gridY + 1] instanceof DeadlyTile;
		boolean bottomLeft = !(gridX > 0) || !(0 < gridY) || tiles[gridX - 1][gridY - 1] instanceof DeadlyTile;
		index++;
		index %= 3;
		
		//--------
		// wires
		if (top && bottom && !left && !right) {
			if (currentTexture) {return LaplacityAssets.DEADLY_REGIONS[3][4];}
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[7+index][0];
		}
		if (!top && !bottom && left && right) {
			if (currentTexture) {return LaplacityAssets.DEADLY_REGIONS[3][4];}
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[7+index][0];
		}
		
		// 3
		if (!top && bottom && right && left && bottomLeft && bottomRight) {
			if (currentTexture) {return LaplacityAssets.DEADLY_REGIONS[3][4];}
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[7+index][0];
		}
		if (top && !bottom && right && left && topLeft && topRight) {
			if (currentTexture) {return LaplacityAssets.DEADLY_REGIONS[3][4];}
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[7+index][0];
		}
		if (top && bottom && !right && left && topLeft && bottomLeft) {
			if (currentTexture) {return LaplacityAssets.DEADLY_REGIONS[3][4];}
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[7+index][0];
		}
		if (top && bottom && right && !left && topRight && bottomRight) {
			if (currentTexture) {return LaplacityAssets.DEADLY_REGIONS[3][4];}
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[7+index][0];
		}
		//-------
		
		return null;
	}

	private TextureRegion texture1(float[] angle) {
		boolean top = tiles[gridX][gridY + 1] instanceof DeadlyTile;
		boolean bottom = tiles[gridX][gridY - 1] instanceof DeadlyTile;
		boolean right = tiles[gridX + 1][gridY] instanceof DeadlyTile;
		boolean left = tiles[gridX - 1][gridY] instanceof DeadlyTile;
		
		boolean topRight = !(gridX < fieldWidth - 1) || !(gridY < fieldHeight - 1) || tiles[gridX + 1][gridY + 1] instanceof DeadlyTile;
		boolean bottomRight = !(gridX < fieldWidth - 1) || !(0 < gridY) || tiles[gridX + 1][gridY - 1]  instanceof DeadlyTile;
		boolean topLeft = !(0 < gridX) || !(gridY < fieldHeight - 1) || tiles[gridX - 1][gridY + 1] instanceof DeadlyTile;
		boolean bottomLeft = !(gridX > 0) || !(0 < gridY) || tiles[gridX - 1][gridY - 1] instanceof DeadlyTile;
		
		// corners
		if (top && bottom && right && left && !topRight && bottomRight && topLeft && bottomLeft) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[1][3];
		}
		if (top && bottom && right && left && topRight && bottomRight && topLeft && !bottomLeft) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[1][3];
		}
		if (top && bottom && right && left && topRight && !bottomRight && topLeft && bottomLeft) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[1][3];
		}
		if (top && bottom && right && left && topRight && bottomRight && !topLeft && bottomLeft) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[1][3];
		}
		//--------
		// wires
		if (top && bottom && !left && !right) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[7][1];
		}
		if (!top && !bottom && left && right) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[7][1];
		}
		
		// 3
		if (!top && bottom && right && left && bottomLeft && bottomRight) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[7][1];
		}
		if (top && !bottom && right && left && topLeft && topRight) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[7][1];
		}
		if (top && bottom && !right && left && topLeft && bottomLeft) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[7][1];
		}
		if (top && bottom && right && !left && topRight && bottomRight) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[7][1];
		}
		//-------
		
		// 1
		if (top && !bottom && !left && !right) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[2][1];
		}
		if (!top && bottom && !left && !right) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[2][1];
		}
		if (!top && !bottom && left && !right) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[2][1];
		}
		if (!top && !bottom && !left && right) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[2][1];
		}
		
		// 2
		if (!top && bottom && right && !left) {
			angle[0] = 90;
			return LaplacityAssets.DEADLY_REGIONS[1][3];
		}
		if (!top && bottom && !right && left) {
			angle[0] = 0;
			return LaplacityAssets.DEADLY_REGIONS[1][3];
		}
		if (top && !bottom && !right && left) {
			angle[0] = -90;
			return LaplacityAssets.DEADLY_REGIONS[1][3];
		}
		if (top && !bottom && right && !left) {
			angle[0] = 180;
			return LaplacityAssets.DEADLY_REGIONS[1][3];
		}
		
		// 3
		if (!top && bottom && right && left) {
			if (bottomLeft && bottomRight) {
				angle[0] = 0;
				return null;
			} else {
				angle[0] = 0;
				return LaplacityAssets.DEADLY_REGIONS[0][1];
			}
		}
		if (top && !bottom && right && left) {
			if (topLeft && topRight) {
				angle[0] = 180;
				return null;
			} else {
				angle[0] = 180;
				return LaplacityAssets.DEADLY_REGIONS[0][1];
			}
		}
		if (top && bottom && !right && left) {
			if (topLeft && bottomLeft) {
				angle[0] = -90;
				return null;
			} else {
				angle[0] = -90;
				return LaplacityAssets.DEADLY_REGIONS[0][1];
			}
		}
		if (top && bottom && right && !left) {
			if (topRight && bottomRight) {
				angle[0] = 90;
				return null;
			} else {
				angle[0] = 90;
				return LaplacityAssets.DEADLY_REGIONS[0][1];
			}
		}
	
		// 4
		if (top && bottom && right && left) {
			return null;
		}
		
		return LaplacityAssets.DEADLY_REGIONS[0][0];
	}
	
	public static void setActiveTexture(boolean active) {
		currentTexture = active;
	}
}
