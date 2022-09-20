package steelUnicorn.laplacity.cat.animations;

import com.badlogic.gdx.math.Vector3;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.chargedParticles.CatAnimationManager;
import steelUnicorn.laplacity.chargedParticles.CatAnimationManager.CatAnimation;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.graphics.TilesRenderer;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.particles.ParticlesManager;

public class CatDeathAnimation extends CatAnimation {

	public CatDeathAnimation() {
		setDuration(GameProcess.DEAD_ANIMATION_TIME);
	}

	@Override
	public void animationStarted() {
		DeadlyTile.setActiveTexture(true);
		TilesRenderer.requestRepaint();
		CatAnimationManager.setEmoji(5);
		
		for (int i = 0; i < 20; i++) {
			ParticlesManager.createParticleInRandomCircle(
					GameProcess.cat.getX(),
					GameProcess.cat.getY(),
					GameProcess.CAT_SIZE,
					GameProcess.CAT_SIZE/2,
					GameProcess.CAT_SIZE/2,
					GameProcess.DEAD_ANIMATION_TIME,
					LaplacityAssets.PARTICLES_STARS1_REGIONS,(float)Math.random() * 360).generateRandomColor();
		}
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
