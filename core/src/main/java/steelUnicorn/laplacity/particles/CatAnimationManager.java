package steelUnicorn.laplacity.particles;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.cat.animations.CatDeathAnimation;
import steelUnicorn.laplacity.cat.animations.CatFinishAnimation;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class CatAnimationManager {

	// ANIMATIONS
	public static final CatAnimation DEATH = new CatDeathAnimation();
	public static final CatAnimation FINISH = new CatFinishAnimation();
	
	// emoji
	private static int currentEmoji = 0;
	
	// Current animation
	private static CatAnimation currentAnimation;
	
	public static void update(Vector3 pos_and_scale) {
		if (currentAnimation != null) {
			float t = (TimeUtils.millis() - currentAnimation.startTime) / 1000f;
			
			if (TimeUtils.millis() - currentAnimation.startTime > currentAnimation.duration) {
				currentAnimation.animationEnded();
				currentAnimation = null;
			} else {
				currentAnimation.animationCoef = 1000f * t / (float) currentAnimation.duration;
				currentAnimation.updateAnimation(pos_and_scale, t);
			}
		}
	}
	
	public static void stopCurrentAnimation() {
		if (currentAnimation != null) {
			currentAnimation.finished = true;
			currentAnimation.animationEnded();
			currentAnimation = null;
		}
	}
	
	public static CatAnimation startAnimation(CatAnimation type) {
		if (currentAnimation != null) {
			currentAnimation.animationEnded();
			currentAnimation.finished = true;
		}
		try {
			currentAnimation = type.getClass().getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		currentAnimation.startTime = TimeUtils.millis();
		currentAnimation.animationStarted();
		
		return currentAnimation;
	}
	
	public static void setRandomEmoji(int... dontUse) {
		boolean contains = false;
		
		do {
			currentEmoji = (int) (Math.random() * LaplacityAssets.CAT_REGIONS.length * LaplacityAssets.CAT_REGIONS[0].length);
			contains = false;
			for (int dont : dontUse) {
				if (dont == currentEmoji) {
					contains = true;
					break;
				}
			}
		} while (contains);
	}
	
	public static boolean isPlayingSomeAnimation() {
		return currentAnimation != null;
	}
	
	public static TextureRegion getTextureRegion() {
		return LaplacityAssets.CAT_REGIONS[currentEmoji % LaplacityAssets.CAT_REGIONS.length][currentEmoji / LaplacityAssets.CAT_REGIONS.length];
	}
	
	public static void setEmoji(int index) {
		currentEmoji = index;
	}
	
	public static class CatAnimation {
		
		protected long startTime;
		protected long duration;
		protected float animationCoef;
		
		private boolean finished = false;
		
		public CatAnimation() {
		}

		public void animationStarted() {	
		}
		
		public void updateAnimation(Vector3 pos_and_scale, float t) {
		}
		
		public void animationEnded() {
		}

		public void setDuration(long duration) {
			this.duration = duration;
		}

		public float getAnimationCoef() {
			return animationCoef;
		}

		public boolean isFinished() {
			return finished;
		}
		
	}
	
}
