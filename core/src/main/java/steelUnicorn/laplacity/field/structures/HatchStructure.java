package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.cat.animations.CatFinishAnimation;
import steelUnicorn.laplacity.chargedParticles.CatAnimationManager;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;

public class HatchStructure extends FieldStructure {

	private static final int[] codes = new int[] {-2105409281};
	
	public HatchStructure(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, codes);
		
		if (bounds.width() != bounds.height()) {
			throw new RuntimeException("hatch may be square");
		}
		
		Gdx.app.log("new hatch structure", "bounds: " + bounds);
	}

	private CatFinishAnimation anim = null;
	
	@Override
	public void renderBatched(float timeFromStart) {
		if (anim != null && anim.isFinished()) {
			anim = null;
		}
		
		float sz = LaplacityField.tileSize;
		TMP1.x = centerX - cat.getX();
		TMP1.y = centerY - cat.getY();
		
		float len = TMP1.len2();
		
		float R = (bounds.width() * sz / 2);
		
		if (len < R * R && anim == null) {
			anim = (CatFinishAnimation) cat.startAnimation(CatAnimationManager.FINISH);
			anim.setStartAndFinishPos(cat.getX(), cat.getY(), centerX, centerY);
		}
		
		GameProcess.gameBatch.enableBlending();
		gameBatch.draw(LaplacityAssets.HATCH1_REGIONS[1][0], bounds.left * sz, bounds.bottom * sz, bounds.width() * sz, bounds.height() * sz);
		GameProcess.gameBatch.disableBlending();
		
		if (anim == null || !anim.isDrawForeground()) {
			int i = 0;
			
			if (anim != null) {
				i = anim.getAnimIndex();
			}
			
			GameProcess.gameBatch.enableBlending();
			gameBatch.draw(LaplacityAssets.HATCH2_REGIONS[i % 5][i / 5], bounds.left * sz, bounds.bottom * sz, bounds.width() * sz, bounds.height() * sz);
			GameProcess.gameBatch.disableBlending();
		}
	}

	@Override
	public void renderBatchedForeground(float timeFromStart) {
		if (anim == null || !anim.isDrawForeground()) {
			return;
		}
		
		int i = 0;
		float sz = LaplacityField.tileSize;
		
		if (anim != null) {
			i = anim.getAnimIndex();
		}
		
		GameProcess.gameBatch.enableBlending();
		gameBatch.draw(LaplacityAssets.HATCH2_REGIONS[i % 5][i / 5], bounds.left * sz, bounds.bottom * sz, bounds.width() * sz, bounds.height() * sz);
		GameProcess.gameBatch.disableBlending();
	}

}
