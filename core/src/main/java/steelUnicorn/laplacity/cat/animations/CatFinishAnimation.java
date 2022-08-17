package steelUnicorn.laplacity.cat.animations;

import com.badlogic.gdx.math.Vector3;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.particles.CatAnimationManager.CatAnimation;

public class CatFinishAnimation extends CatAnimation {

	private float finishX;
	private float finishY;
	
	public CatFinishAnimation() {
		setDuration(GameProcess.FINISH_ANIMATION_TIME);
	}
	
	public void setFinishPos(float x, float y) {
		finishX = x;
		finishY = y;
	}
	
	@Override
	public void animationStarted() {
		GameProcess.cat.getBody().setLinearVelocity(0, 0);
		GameProcess.gameUI.setPauseButtonEnabled(false);
	}

	@Override
	public void updateAnimation(Vector3 pos_and_scale, float t) {
		pos_and_scale.x = (finishX - pos_and_scale.x) * animationCoef + pos_and_scale.x;
		pos_and_scale.y = (finishY - pos_and_scale.y) * animationCoef + pos_and_scale.y;
		pos_and_scale.z = 0.8f + 0.2f * (1f - animationCoef);
	}

	@Override
	public void animationEnded() {
		GameProcess.gameUI.setPauseButtonEnabled(true);
		GameProcess.cat.setPosition(finishX, finishY);
		GameProcess.justFinished = true;
	}

}
