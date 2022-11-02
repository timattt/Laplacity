package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.IntRect;
import steelUnicorn.laplacity.field.tiles.EmptyTile;

public class AcceleratorStructure extends FieldStructure {

	private static final int[] codes1 = new int[] {1677721855, 51455};
	
	// dest point
	private float dirX;
	private float dirY;
	
	private float angle;
	
	public AcceleratorStructure(int left, int bottom, Pixmap pm, int initCode) {
		super(left, bottom, pm, codes1);
		IntRect direction = new IntRect();
		
		findSubRect(pm, bounds, 51455, direction);
		
		direction.getCenterInWorldCoords(Globals.TMP1);
		bounds.getCenterInWorldCoords(Globals.TMP2);
		Globals.TMP1.sub(Globals.TMP2);
		Globals.TMP1.nor();
		
		dirX = Globals.TMP1.x;
		dirY = Globals.TMP1.y;
		
		angle = (float) Math.acos(dirX) * MathUtils.radDeg * Math.signum(dirY + 0.001f);
	}

	@Override
	public void updatePhysics(float timeFromStart) {
		EmptyTile tl = LaplacityField.getTileFromWorldCoords(GameProcess.cat.getX(), GameProcess.cat.getY());
		if (tl != null && bounds.containsPoint(tl.getGridX(), tl.getGridY())) {
			GameProcess.cat.getBody().applyForceToCenter(dirX * GameProcess.CELERATION_FACTOR, dirY * GameProcess.CELERATION_FACTOR, true);
		}
	}

	@Override
	public void renderBatched(float timeFromStart) {		
		gameBatch.enableBlending();
		gameBatch.draw(LaplacityAssets.CELERATORS_REGIONS[0], leftBottomX, leftBottomY, worldWidth/2, worldHeight/2, worldWidth, worldHeight, 1, 1, angle);
		gameBatch.disableBlending();
	}

}
