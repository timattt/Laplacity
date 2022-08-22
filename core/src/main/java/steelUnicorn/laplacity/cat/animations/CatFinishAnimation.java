package steelUnicorn.laplacity.cat.animations;

import com.badlogic.gdx.math.Vector3;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.chargedParticles.CatAnimationManager.CatAnimation;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class CatFinishAnimation extends CatAnimation {

	private float finishX;
	private float finishY;
	
	private float startX;
	private float startY;
	
	private int animIndex = 0;
	
	private boolean drawForeground = false;
	
	public CatFinishAnimation() {
		setDuration(GameProcess.FINISH_ANIMATION_TIME);
	}
	
	public void setStartAndFinishPos(float startX, float startY, float endX, float endY) {
		finishX = endX;
		finishY = endY;
		this.startX = startX;
		this.startY = startY;
	}
	
	@Override
	public void animationStarted() {
		GameProcess.cat.getBody().setLinearVelocity(0, 0);
		GameProcess.gameUI.setPauseButtonEnabled(false);
	}

	@Override
	public void updateAnimation(Vector3 pos_and_scale, float t) {
		if (animationCoef < 0.5f) {
			animIndex = (int) (LaplacityAssets.HATCH2_REGIONS.length * LaplacityAssets.HATCH2_REGIONS[0].length * animationCoef * 2f);
			GameProcess.cat.setPosition(startX + animationCoef * 2f * (finishX - startX), startY + animationCoef * 2f * (finishY - startY));
			drawForeground = false;
		} else {
			drawForeground = true;
			animIndex = (int) (LaplacityAssets.HATCH2_REGIONS.length * LaplacityAssets.HATCH2_REGIONS[0].length * Math.min(0.99f, (1f - animationCoef)*2f));
		}
	}

	public boolean isDrawForeground() {
		return drawForeground;
	}

	@Override
	public void animationEnded() {
		GameProcess.gameUI.setPauseButtonEnabled(true);
		GameProcess.cat.setPosition(finishX, finishY);
		GameProcess.justFinished = true;
	}

	public int getAnimIndex() {
		return animIndex;
	}

}
