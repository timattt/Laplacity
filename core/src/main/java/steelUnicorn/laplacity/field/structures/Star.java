package steelUnicorn.laplacity.field.structures;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.core.LaplacityAssets;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

public class Star extends FieldStructure {

	private static final int[] codes = new int[] {-757989121};
	
	private long lastAnimationUpdate;
	private int currentTextureIndex = 0;
	
	private boolean collected;
	
	public Star(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, codes);
		currentTextureIndex = (int) (Math.random() * 1000);
	}

	@Override
	public void renderBatched(float timeFromStart) {
		if (TimeUtils.millis() - lastAnimationUpdate > STAR_ROTATION_TIME) {
			lastAnimationUpdate = TimeUtils.millis();
			currentTextureIndex = (currentTextureIndex + 1) % LaplacityAssets.STAR_REGIONS.length;
		}
		if (!collected) {
			gameBatch.enableBlending();
			gameBatch.draw(LaplacityAssets.STAR_REGIONS[currentTextureIndex], centerX - STAR_SIZE, centerY - STAR_SIZE, 2 * STAR_SIZE, 2 * STAR_SIZE);
			gameBatch.disableBlending();
		}
	}

	@Override
	public void updatePhysics(float timeFromStart) {
		TMP1.set(cat.getX(), cat.getY());
		TMP1.sub(centerX, centerY);
		if (TMP1.len2() < (STAR_SIZE + CAT_SIZE) * (STAR_SIZE + CAT_SIZE)) {
			if (!collected) {
				collectStar();
			}
			collected = true;
		}
	}

	@Override
	public void reset() {
		collected = false;
	}

}
