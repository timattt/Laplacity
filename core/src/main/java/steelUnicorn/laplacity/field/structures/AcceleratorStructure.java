package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Pixmap;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.IntRect;
import steelUnicorn.laplacity.field.tiles.EmptyTile;

public class AcceleratorStructure extends FieldStructure {

	private static final int[] codes1 = new int[] {1677721855, 51455};
	
	// sizes
	private float x;
	private float y;
	
	private float width;
	private float height;
	
	// dest point
	private float dirX;
	private float dirY;
	
	public AcceleratorStructure(int left, int bottom, Pixmap pm, int initCode) {
		super(left, bottom, pm, codes1);
		IntRect direction = new IntRect();
		
		findSubRect(pm, bounds, 51455, direction);
		
		float sz = LaplacityField.tileSize;
		
		x = bounds.left * sz;
		y = bounds.bottom * sz;
		
		width = bounds.width() * sz;
		height = bounds.height() * sz;
		
		direction.getCenterInWorldCoords(Globals.TMP1);
		bounds.getCenterInWorldCoords(Globals.TMP2);
		
		Globals.TMP1.sub(Globals.TMP2);
		
		Globals.TMP1.nor();
		
		dirX = Globals.TMP1.x;
		dirY = Globals.TMP1.y;
		
		System.out.println(dirX + " " + dirY);
	}

	@Override
	public void updatePhysics(float timeFromStart) {
		EmptyTile tl = LaplacityField.getTileFromWorldCoords(GameProcess.mainParticle.getX(), GameProcess.mainParticle.getY());
		if (tl != null && bounds.containsPoint(tl.getGridX(), tl.getGridY())) {
			GameProcess.mainParticle.getBody().applyForceToCenter(dirX * GameProcess.CELERATION_FACTOR, dirY * GameProcess.CELERATION_FACTOR, true);
		}
	}

	@Override
	public void renderBatched(float timeFromStart) {		
		gameBatch.enableBlending();
		gameBatch.draw(LaplacityAssets.CELERATORS_REGIONS[0], x, y, width, height);
		gameBatch.disableBlending();
	}

}
