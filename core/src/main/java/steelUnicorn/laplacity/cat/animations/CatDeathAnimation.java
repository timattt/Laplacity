package steelUnicorn.laplacity.cat.animations;

import com.badlogic.gdx.math.Vector3;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.graphics.TilesRenderer;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.particles.CatAnimationManager;
import steelUnicorn.laplacity.particles.CatAnimationManager.CatAnimation;

public class CatDeathAnimation extends CatAnimation {

	public CatDeathAnimation() {
		setDuration(GameProcess.DEAD_ANIMATION_TIME);
	}

	@Override
	public void animationStarted() {
		DeadlyTile.setActiveTexture(true);
		TilesRenderer.requestRepaint();
		CatAnimationManager.setEmoji(5);
	}

	@Override
	public void updateAnimation(Vector3 pos_and_scale, float t) {
		float r = 1f;
		float omega = 100f;
		pos_and_scale.x += r * Math.cos(omega * t);
		pos_and_scale.y += r * Math.sin(omega * t);
	}

	@Override
	public void animationEnded() {
		CatAnimationManager.setRandomEmoji(5);	
		GameProcess.justHitted = true;
		DeadlyTile.setActiveTexture(false);
		TilesRenderer.requestRepaint();
	}

}
