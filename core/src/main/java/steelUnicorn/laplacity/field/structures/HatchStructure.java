package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.GameProcess;
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

	private long finishTime;
	private boolean mayFinish = false;
	
	@Override
	public void renderBatched(float timeFromStart) {
		if (mayFinish) {
			cat.getBody().setLinearVelocity(0, 0);
		}
		if (mayFinish && TimeUtils.millis() - finishTime > 1000) {
			GameProcess.justFinished = true;
		}
		
		float sz = LaplacityField.tileSize;
		TMP1.x = centerX - cat.getX();
		TMP1.y = centerY - cat.getY();
		
		float len = TMP1.len2();
		
		float r = LaplacityField.tileSize * 15f;
		
		int i = 0;
		
		if (len < r * r) {
			i = (int) (11f * (1f - len / (r * r)));
		}
		
		float R = (bounds.width() * sz / 2);
		
		if (len < R * R && !mayFinish) {
			mayFinish = true;
			finishTime = TimeUtils.millis();
		}
		
		GameProcess.gameBatch.enableBlending();
		gameBatch.draw(LaplacityAssets.HATCH_REGIONS[i % 5][i / 5], bounds.left * sz, bounds.bottom * sz, bounds.width() * sz, bounds.height() * sz);
		GameProcess.gameBatch.disableBlending();
	}

}
