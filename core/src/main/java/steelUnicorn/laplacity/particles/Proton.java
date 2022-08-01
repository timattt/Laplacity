package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class Proton extends ChargedParticle {

	private long lastAnimationUpdate;
	private int currentTextureIndex = 0;
	
	public Proton(float x, float y) {
		super(x, y, PARTICLE_SIZE, PARTICLE_CHARGE);
		pointLight.setDistance(PARTICLE_LIGHT_DISTANCE);
	}

	@Override
	public void draw() {
		if (TimeUtils.millis() - lastAnimationUpdate > PARTICLE_TEXTURES_UPDATE_DELTA) {
			lastAnimationUpdate = TimeUtils.millis();
			currentTextureIndex = (currentTextureIndex + 1) % LaplacityAssets.PARTICLES_REGIONS.length;
		}
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(LaplacityAssets.PARTICLES_REGIONS[currentTextureIndex][1], getX() - PARTICLE_SIZE, getY() - PARTICLE_SIZE, 2 * PARTICLE_SIZE, 2 * PARTICLE_SIZE);
		GameProcess.gameBatch.disableBlending();
	}

}
