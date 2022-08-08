package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class Electron extends ChargedParticle {

	private long lastAnimationUpdate;
	private int currentTextureIndex = 0;
	
	public Electron(float x, float y) {
		super(x, y, PARTICLE_SIZE, -PARTICLE_CHARGE, true, Color.RED);
		pointLight.setDistance(PARTICLE_LIGHT_DISTANCE);
	}

	@Override
	public void draw() {
		if (TimeUtils.millis() - lastAnimationUpdate > PARTICLE_TEXTURES_UPDATE_DELTA) {
			lastAnimationUpdate = TimeUtils.millis();
			currentTextureIndex = (currentTextureIndex + 1) % LaplacityAssets.ELECTRON_REGIONS.length;
		}
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(LaplacityAssets.ELECTRON_REGIONS[currentTextureIndex], getX() - PARTICLE_SIZE, getY() - PARTICLE_SIZE, 2 * PARTICLE_SIZE, 2 * PARTICLE_SIZE);
		GameProcess.gameBatch.disableBlending();
	}

}
