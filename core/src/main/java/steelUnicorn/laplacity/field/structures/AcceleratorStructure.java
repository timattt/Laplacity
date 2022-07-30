package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Pixmap;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.tiles.EmptyTile;

public class AcceleratorStructure extends FieldStructure {

	private static final int[] codes1 = new int[] {1677721855};
	private static final int[] codes2 = new int[] {6553855};
	
	// sizes
	private float x;
	private float y;
	
	private float width;
	private float height;
	
	// mode
	private boolean accelerating;
	
	public AcceleratorStructure(int left, int bottom, Pixmap pm, int initCode) {
		super(left, bottom, pm, initCode == 6553855 ? codes2 : codes1);
		
		float sz = LaplacityField.tileSize;
		
		x = bounds.left * sz;
		y = bounds.bottom * sz;
		
		width = (bounds.right - bounds.left + 1) * sz;
		height = (bounds.top - bounds.bottom + 1) * sz;
		
		accelerating = initCode == 1677721855;
	}

	@Override
	public void renderBatched(float timeFromStart) {
		EmptyTile tl = LaplacityField.getTileFromWorldCoords(GameProcess.mainParticle.getX(), GameProcess.mainParticle.getY());
		if (tl != null && bounds.containsPoint(tl.getGridX(), tl.getGridY())) {
			GameProcess.mainParticle.addDissipative((accelerating ? GameProcess.DISSIPATIVE_ACCELERATION_FACTOR : GameProcess.DISSIPATIVE_MODERATION_FACTOR));
		}
		
		gameBatch.draw(LaplacityAssets.CELERATORS_REGIONS[accelerating ? 0 : 1], x, y, width, height);
	}

}
